/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.calendar;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import monnef.core.Reference;

import java.util.EnumSet;

public class CoreTickHandler implements ITickHandler {
    private static EventCalendar coreEventCalendar = new EventCalendar();

    public static EventCalendar getCoreEventCalendar() {
        return coreEventCalendar;
    }

    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData) {
        coreEventCalendar.tick();
    }

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData) {
    }

    @Override
    public EnumSet<TickType> ticks() {
        return EnumSet.of(TickType.CLIENT, TickType.SERVER);
    }

    @Override
    public String getLabel() {
        return Reference.ModId;
    }
}
