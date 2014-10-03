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

class CustomPlayerRenderer {

  import CustomPlayerRenderer._

  val texture = new ResourceLocation(ResourcePathHelper.assemble("sash_1.png", Reference.ModName, ResourceTextureType.ENTITY))

  val modelOver = new ModelBiped(1.0F, 0, TEXTURE_WIDTH, TEXTURE_HEIGHT) {
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
      FMLClientHandler.instance.getClient.renderEngine.bindTexture(texture)
      val player = event.entityPlayer
      modelOver.render(player, event.partialRenderTick, 0, 0, 0, 0, 1f / 16)
    }
  }
}

object CustomPlayerRenderer {
  final val TEXTURE_WIDTH = 64
  final val TEXTURE_HEIGHT = 32
}