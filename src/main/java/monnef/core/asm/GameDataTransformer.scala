package monnef.core.asm

import org.objectweb.asm.{ClassWriter, ClassReader}
import org.objectweb.asm.tree._
import scala.collection.JavaConverters._
import monnef.core.MonnefCorePlugin
import org.objectweb.asm.Opcodes._
import scala.Some
import monnef.core.MonnefCorePlugin.Log

object GameDataTransformer {
  private[this] final val LOG_PREFIX = "[GDH] "
  private[this] val logger: AdapterLogger = new AdapterLogger(LOG_PREFIX)
  private[this] final val methodName = "registerItem"

  private[this] var verbose: Boolean = false

  private[this] case class VariableIDs(var item: Option[Int], var idHint: Option[Int], var itemId: Option[Int], var id: Option[Int])

  private def debugLog(msg: String) {
    if (verbose) logger.print(msg)
  }

  def transform(bytes: Array[Byte], verbose: Boolean): Array[Byte] = {
    debugLog("entering transform")
    this.verbose = verbose
    val cr = new ClassReader(bytes)
    val classNode = new ClassNode()
    cr.accept(classNode, 0)

    processClass(classNode)

    val cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES)
    classNode.accept(cw)
    debugLog("leaving transform")
    cw.toByteArray
  }

  private[this] def processClass(node: ClassNode) = {
    debugLog("processing class")
    node.methods.asScala.find { m =>
      methodName.equals(m.name) && "(Lnet/minecraft/item/Item;Ljava/lang/String;I)I".equals(m.desc)
    } match {
      case None => crash(s"Method '$methodName' not found.")
      case Some(method) => processMethod(method)
    }
    CoreTransformer.gameDataHookApplied = true
  }

  private[this] def processMethod(method: MethodNode) {
    debugLog("processing method")
    val ids = VariableIDs(None, None, None, None)
    val instructions = method.instructions.toArray.toSeq
    val instructionsWithIndex = instructions.zipWithIndex

    val freeSlotMethodClassIndex = processFreeSlotCall(instructions, ids)
    processIdHintAssign(instructions, ids, freeSlotMethodClassIndex)

    val (targetSectionStartIndex, targetSectionEndIndex) = locateTargetCall(instructionsWithIndex, instructions, ids)
    val targetSectionFirstInstruction = instructions(targetSectionStartIndex)
    val targetSectionLastInstruction = instructions(targetSectionEndIndex)
    val preHook = generatePreHook(ids)
    val postHook = generatePostHook(ids)

    method.instructions.insertBefore(targetSectionFirstInstruction, preHook)
    method.instructions.insert(targetSectionLastInstruction, postHook)

    logger.print("GameData hooks inserted.")
    debugLog(s"Insertion info: target section = $targetSectionStartIndex - $targetSectionEndIndex")
  }

  private[this] def processFreeSlotCall(instructions: Seq[AbstractInsnNode], ids: VariableIDs): Int = {
    val methodNameFreeSlot = "freeSlot"
    var methodIndex = -1

    for {
      idx <- 0 until instructions.size
      i = instructions(idx)
    } {
      if (i.getOpcode == INVOKESPECIAL && i.asInstanceOf[MethodInsnNode].name.equals(methodNameFreeSlot)) {
        if (methodIndex != -1) {
          crash(s"Multiple matches for '$methodNameFreeSlot' call.")
        }
        methodIndex = idx
      }
    }

    if (methodIndex == -1) {
      crash(s"Not found method call'$methodNameFreeSlot'.")
    } else {
      implicit val instructionsImplicit = instructions
      ids.item = Some(extractVariableId(ALOAD, methodIndex - 1))
      ids.id = Some(extractVariableId(ILOAD, methodIndex - 2))
    }

    methodIndex
  }

  def processIdHintAssign(instructions: Seq[AbstractInsnNode], ids: VariableIDs, methodIndex: Int) {
    var loadIdIndex = -1
    for {
      idx <- methodIndex until instructions.size
      i = instructions(idx)
    } {
      if (i.getOpcode == ILOAD && i.asInstanceOf[VarInsnNode].`var` == ids.id.get) {
        if (loadIdIndex != -1) logger.printError("Multiple assign points, something will break.")
        loadIdIndex = idx
      }
    }

    if (loadIdIndex == -1) {
      crash("Assign point not found.")
    } else {
      implicit val instructionsImplicit = instructions
      ids.idHint = Some(extractVariableId(ISTORE, loadIdIndex + 1))
    }
  }

  private[this] def locateTargetCall(instructions: Seq[(AbstractInsnNode, Int)], instructionsSeq: Seq[AbstractInsnNode], ids: VariableIDs): (Int, Int) = {
    val itemRegistryName = "iItemRegistry"
    val addMethodName = "add"
    val lengthFromGetFieldToMethodCall = 6

    val startIdx = instructions.find { case (i, idx) =>
      i.getOpcode == GETFIELD && i.asInstanceOf[FieldInsnNode].name.equals(itemRegistryName) &&
        idx + lengthFromGetFieldToMethodCall < instructions.size && {
        val invoke = instructions(idx + lengthFromGetFieldToMethodCall)
        invoke._1.getOpcode == INVOKEVIRTUAL && invoke._1.asInstanceOf[MethodInsnNode].name.equals(addMethodName)
      }
    } match {
      case Some((i, idx)) => idx - 1
      case None => crash(s"Unable to locate target call.")
    }

    val stopIdx = startIdx + 8
    if (instructions(stopIdx)._1.getOpcode != ISTORE) crash(s"No return value store after calling method '$addMethodName'.")
    ids.itemId = Some(extractVariableId(ISTORE, stopIdx)(instructionsSeq))

    (startIdx, stopIdx)
  }

  private[this] def extractVariableId(expectedOpCode: Int, index: Int)(implicit instructions: Seq[AbstractInsnNode]): Int = {
    val i = instructions(index)
    if (i.getOpcode != expectedOpCode) crash("Unexpected op code value.")
    i.asInstanceOf[VarInsnNode].`var`
  }

  private[this] def generatePreHook(ids: VariableIDs): InsnList = {
    logger.log("Generating preHook")
    val r = new InsnList
    r.add(new VarInsnNode(ALOAD, 0))
    r.add(new VarInsnNode(ALOAD, ids.item.get))
    r.add(new VarInsnNode(ILOAD, ids.idHint.get))
    // public static int onRegisterItemPre(Item item, int idHint) {
    val signature = "(Lcpw/mods/fml/common/registry/GameData;L" + SrgNames.getSlashedName(SrgNames.C_ITEM.getTranslatedName) + ";I)I"
    r.add(new MethodInsnNode(INVOKESTATIC, "monnef/core/asm/GameDataEvents", "onRegisterItemPre", signature))
    r.add(new VarInsnNode(ISTORE, ids.idHint.get))
    r
  }

  private[this] def generatePostHook(ids: VariableIDs): InsnList = {
    logger.log("Generating postHook")
    val r = new InsnList
    r.add(new VarInsnNode(ALOAD, 0))
    r.add(new VarInsnNode(ALOAD, ids.item.get))
    r.add(new VarInsnNode(ILOAD, ids.itemId.get))
    r.add(new VarInsnNode(ILOAD, ids.idHint.get))
    // public static void onRegisterItemPost(GameData gameData, Item item, int itemId, int idHint) {
    val signature = "(Lcpw/mods/fml/common/registry/GameData;L" + SrgNames.getSlashedName(SrgNames.C_ITEM.getTranslatedName) + ";II)V"
    r.add(new MethodInsnNode(INVOKESTATIC, "monnef/core/asm/GameDataEvents", "onRegisterItemPost", signature))
    r
  }

  private[this] def crash(msg: String): Nothing = {
    throw new RuntimeException(s"$LOG_PREFIX $msg")
  }
}
