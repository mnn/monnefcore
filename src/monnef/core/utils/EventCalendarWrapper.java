/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.utils;

import monnef.core.calendar.CoreTickHandler;
import monnef.core.calendar.IEventCalendarAction;

public class EventCalendarWrapper {
    public static void addAction(int timeFromNowInTicks, IEventCalendarAction action) {
        CoreTickHandler.getCoreEventCalendar().addRecord(timeFromNowInTicks, action);
    }
}
