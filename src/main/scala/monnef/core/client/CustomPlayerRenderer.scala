package monnef.core.client

import net.minecraft.client.model.{ModelRenderer, ModelBiped}
import net.minecraftforge.client.event.RenderPlayerEvent
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import monnef.core.mod.MonnefCoreNormalMod
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.client.FMLClientHandler
import monnef.core.client.ResourcePathHelper.ResourceTextureType
import net.minecraft.util.ResourceLocation
import monnef.core.Reference
import org.lwjgl.opengl.GL11

class CustomPlayerRenderer {

  import CustomPlayerRenderer._

  val texture = new ResourceLocation(ResourcePathHelper.assemble("sash_1.png", Reference.ModName, ResourceTextureType.ENTITY))

  val modelSmallOver = new CustomModelBiped(.51f)
  val modelBigOver = new CustomModelBiped(1.01f)

  class CustomModelBiped(_size: Float) extends ModelBiped(_size, 0, TEXTURE_WIDTH, TEXTURE_HEIGHT) {
    isChild = false

    def disablePart(part: ModelRenderer) { part.showModel = false }

    Seq(bipedHead, bipedHeadwear, bipedCloak, bipedEars, bipedLeftArm, bipedLeftLeg, bipedRightArm, bipedRightLeg).foreach(disablePart)
  }

  @SubscribeEvent
  def onPostRenderSpecials(event: RenderPlayerEvent.Specials.Post) {
    val sashNumber = MonnefCoreNormalMod.sashRegistry.getSashNumber(event.entityPlayer.getUniqueID)
    if (sashNumber != 0) {
      // setRotationAngles(partial tick, arm, arm max, headRotY, headRotX, ???, entity)
      // render(entity, partial tick, arm, arm max, headRotY, headRotX, ?scale?)
      GL11.glPushMatrix()
      FMLClientHandler.instance.getClient.renderEngine.bindTexture(texture)
      val player = event.entityPlayer
      val playerModel = event.renderer.modelBipedMain
      val modelToRender = if (player.getCurrentArmor(2) == null) modelSmallOver else modelBigOver
      modelToRender.aimedBow = playerModel.aimedBow
      modelToRender.isSneak = playerModel.isSneak
      modelToRender.heldItemRight = playerModel.heldItemRight
      modelToRender.heldItemLeft = playerModel.heldItemLeft
      modelToRender.onGround = playerModel.onGround
      GL11.glColor4f(1, 1, 1, 1)
      modelToRender.render(player, event.partialRenderTick, 0, 0, 0, 0, MODEL_SCALE)
      GL11.glPopMatrix()
    }
  }
}

object CustomPlayerRenderer {
  final val TEXTURE_WIDTH = 64
  final val TEXTURE_HEIGHT = 32
  final val MODEL_SCALE = 1f / 16
}