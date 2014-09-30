package monnef.core.client

import net.minecraft.client.model.ModelBiped
import net.minecraftforge.client.event.RenderPlayerEvent
import cpw.mods.fml.common.eventhandler.SubscribeEvent

class CustomPlayerRenderer {
  val modelOver = new ModelBiped(1.0F)

  @SubscribeEvent
  def onPostRenderSpecials(event: RenderPlayerEvent.Specials.Post) {
    // setRotationAngles(partial tick, arm, arm max, headRotY, headRotX, ???, entity)
    // render(entity, partial tick, arm, arm max, headRotY, headRotX, ?scale?)
    modelOver.render(null, event.partialRenderTick, 0, 0, 0, 0, 1)
  }
}
