/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.utils;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import monnef.core.calendar.CoreTickHandler;
import monnef.core.calendar.IEventCalendarAction;

import static monnef.core.utils.EventCalendarWrapper.EventCalendarSide.BOTH;

public class EventCalendarWrapper {
    public static void addAction(int timeFromNowInTicks, IEventCalendarAction action) {
        addAction(timeFromNowInTicks, action, BOTH);
    }

    public static void addAction(int timeFromNowInTicks, IEventCalendarAction action, EventCalendarSide side) {
        Side effSide = FMLCommonHandler.instance().getEffectiveSide();
        if (!side.equalSide(effSide)) {
            return;
        }
        CoreTickHandler.getCoreEventCalendar().addRecord(timeFromNowInTicks, action);
    }

    public static enum EventCalendarSide {
        BOTH,
        SERVER,
        CLIENT;

        public boolean equalSide(Side effSide) {
            return this == BOTH || (this == CLIENT && effSide == Side.CLIENT) || (this == SERVER && effSide == Side.SERVER);
        }
    }
}
