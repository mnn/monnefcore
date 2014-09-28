/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.calendar;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class CoreTickHandler {
    private static EventCalendar coreEventCalendar = new EventCalendar();

    public static EventCalendar getCoreEventCalendar() {
        return coreEventCalendar;
    }

    public void onTick() {
        coreEventCalendar.tick();
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent evt) {
        onTick();
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent evt) {
        onTick();
    }
}
