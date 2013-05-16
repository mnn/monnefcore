/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.calendar;

import java.util.PriorityQueue;

public class EventCalendar {
    private PriorityQueue<CalendarRecord> events = new PriorityQueue<CalendarRecord>();
    private int currentTime = 0;
    private static final long MAX_TIME = Integer.MAX_VALUE;

    public void tick() {
        while (!events.isEmpty() && events.peek().activationTime == currentTime) {
            CalendarRecord e = events.poll();
            e.action.execute();
        }

        currentTime++;
        checkTimeOverflow(0);
    }

    private void checkTimeOverflow(int shift) {
        if (currentTime + shift >= MAX_TIME) {
            rearrangeEvents();
        }
    }

    public void addRecord(int runInNTicks, IEventCalendarAction action) {
        checkTimeOverflow(runInNTicks);

        int startTime = runInNTicks + currentTime;
        events.add(new CalendarRecord(startTime, action));
    }

    private void rearrangeEvents() {
        // If I'm computing correctly, then this will only occur if server/client is running
        // longer than 3.4 years. Not really a priority...
        throw new RuntimeException("Not implemented yet.");
    }

    private class CalendarRecord implements Comparable<CalendarRecord> {
        public int activationTime;
        public IEventCalendarAction action;

        private CalendarRecord(int activationTime, IEventCalendarAction action) {
            this.activationTime = activationTime;
            this.action = action;
        }

        @Override
        public int compareTo(CalendarRecord o) {
            if (activationTime < o.activationTime) return -1;
            if (activationTime > o.activationTime) return 1;
            return 0;
        }
    }
}
