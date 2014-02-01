/*
 * Automatic Assembly Table
 * author: monnef
 */

package monnef.core.client

import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler
import java.util
import cpw.mods.fml.common.TickType
import net.minecraft.client.settings.{GameSettings, KeyBinding}

import KeyboardHelper._
import net.minecraft.client.Minecraft

class MonnefCoreKeyHandler extends KeyHandler(Array(Minecraft.getMinecraft.gameSettings.keyBindSneak), Array(false)) {
  def getSneakKeyCode = Minecraft.getMinecraft.gameSettings.keyBindSneak.keyCode

  def keyDown(types: util.EnumSet[TickType], kb: KeyBinding, tickEnd: Boolean, isRepeat: Boolean) {
    if (kb.keyCode == getSneakKeyCode) isShiftPressed = true
  }

  def keyUp(types: util.EnumSet[TickType], kb: KeyBinding, tickEnd: Boolean) {
    if (kb.keyCode == getSneakKeyCode) isShiftPressed = false
  }

  def ticks(): util.EnumSet[TickType] = util.EnumSet.of(TickType.CLIENT)

  def getLabel: String = this.getClass.getSimpleName
}
