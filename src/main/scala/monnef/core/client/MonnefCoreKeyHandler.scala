/*
 * Automatic Assembly Table
 * author: monnef
 */

package monnef.core.client

import KeyboardHelper._
import net.minecraft.client.Minecraft
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent
import cpw.mods.fml.common.eventhandler.SubscribeEvent

class MonnefCoreKeyHandler {
  def getSneakKeyBind = Minecraft.getMinecraft.gameSettings.keyBindSneak

  @SubscribeEvent
  def tick(e: KeyInputEvent) {
    isShiftPressed = getSneakKeyBind.getIsKeyPressed
  }
}
