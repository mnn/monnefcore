/*
 * Jaffas and more!
 * author: monnef
 */
package monnef.core.client

import cpw.mods.fml.client.FMLClientHandler
import monnef.core.MonnefCorePlugin
import monnef.core.utils.{GameDataHelper, ColorEnum, GuiHelper, PlayerHelper}
import net.minecraft.block.Block
import net.minecraft.client.gui.GuiButton
import net.minecraft.init.Blocks
import net.minecraft.inventory.Container
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import cpw.mods.fml.common.registry.GameData
import scala.collection.JavaConversions

object GuiExporter {
  final val BUTTON_TAKE_SHOT: Int = 0
  final val BUTTON_TAKE_SHOTS: Int = 1
  final val BUTTON_PROCESS_ALL: Int = 2
}

class GuiExporter(container: Container) extends GuiContainerMonnefCore(container) {

  import GuiExporter._

  protected override def drawGuiContainerBackgroundLayer(f: Float, i: Int, j: Int) {
    GuiHelper.drawRect(x, y, xSize, ySize, ColorEnum.MAGENTA.getInt)
  }

  def addButtonToList(button: GuiButton) {
    buttonList.asInstanceOf[java.util.List[GuiButton]].add(button)
  }

  override def initGui() {
    super.initGui()
    addButtonToList(new GuiButton(BUTTON_TAKE_SHOT, x + 10, y + 32, 100, 20, "Take shot"))
    addButtonToList(new GuiButton(BUTTON_TAKE_SHOTS, x + 10, y + 52, 100, 20, "Take shots"))
    addButtonToList(new GuiButton(BUTTON_PROCESS_ALL, x + 10, y + 72, 100, 20, "Process all"))
  }

  protected override def actionPerformed(button: GuiButton) {
    def queueBlock(b: Block) { ExporterTickHandler.scheduleTask(new ItemStack(b), Item.getItemFromBlock(b)) }

    button.id match {
      case BUTTON_TAKE_SHOT =>
        queueBlock(Blocks.chest)

      case BUTTON_TAKE_SHOTS =>
        queueBlock(Blocks.lapis_block)
        queueBlock(Blocks.hopper)
        queueBlock(Blocks.dispenser)

      case BUTTON_PROCESS_ALL =>
        val itemsToProcess: Seq[Item] = GameDataHelper.extractAllItems().filter(_ != null).toSeq
        val tmpList = new java.util.ArrayList[ItemStack]()
        val queued = itemsToProcess.map {
          item => {
            tmpList.clear()
            item.getSubItems(item, null, tmpList)
            JavaConversions.asScalaBuffer(tmpList).map { stack =>
              if (stack != null) {
                ExporterTickHandler.scheduleTask(stack.copy(), item)
                1
              } else {
                MonnefCorePlugin.Log.printWarning("Possible badly coded block: " + item.getUnlocalizedName)
                0
              }
            }.sum
          }
        }.sum
        PlayerHelper.addMessage(FMLClientHandler.instance.getClient.thePlayer, "Queued " + queued + " items to process.")
    }
  }
}