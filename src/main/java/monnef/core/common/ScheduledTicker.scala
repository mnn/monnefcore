/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.common

import cpw.mods.fml.common.gameevent.TickEvent
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.gameevent.TickEvent.Phase
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.relauncher.Side

/**
 * Simplified reimplementation of IScheduledTickHandler from FML
 */
abstract class ScheduledTicker {
  private var ticksClient = 0
  private var ticksServer = 0

  def onClientTickStart() {}

  def onClientTickEnd() {}

  def onServerTickStart() {}

  def onServerTickEnd() {}

  def nextTickSpacing(side: Side): Int

  @SubscribeEvent def onClientTick(evt: TickEvent.ClientTickEvent) {
    if (evt.phase == Phase.START) ticksClient -= 1

    if (ticksClient <= 0) {
      if (evt.phase == Phase.START) onClientTickStart()
      else if (evt.phase == Phase.END) onClientTickEnd()
      ticksClient = nextTickSpacing(Side.CLIENT)
    }
  }

  @SubscribeEvent def onServerTick(evt: TickEvent.ServerTickEvent) {
    if (evt.phase == Phase.START) ticksServer -= 1

    if (ticksServer <= 0) {
      if (evt.phase == Phase.START) onServerTickStart()
      else if (evt.phase == Phase.END) onServerTickEnd()
      ticksServer = nextTickSpacing(Side.SERVER)
    }
  }

  def register(): ScheduledTicker = {
    FMLCommonHandler.instance.bus.register(this)
    this
  }

  def unregister(): ScheduledTicker = {
    FMLCommonHandler.instance.bus.unregister(this)
    this
  }
}
