package monnef.core.client

import net.minecraft.client.model.ModelBiped
import net.minecraftforge.client.event.RenderPlayerEvent
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import monnef.core.mod.MonnefCoreNormalMod

class CustomPlayerRenderer {
  val modelOver = new ModelBiped(1.0F)

  @SubscribeEvent
  def onPostRenderSpecials(event: RenderPlayerEvent.Specials.Post) {
    val sashNumber = MonnefCoreNormalMod.sashRegistry.getSashNumber(event.entityPlayer.getUniqueID)
    if (sashNumber != 0) {
      // setRotationAngles(partial tick, arm, arm max, headRotY, headRotX, ???, entity)
      // render(entity, partial tick, arm, arm max, headRotY, headRotX, ?scale?)
      modelOver.render(event.entity, event.partialRenderTick, 0, 0, 0, 0, 1)
    }
  }
}
