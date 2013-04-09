/*
 * Copyright (c) 2013 monnef.
 */

package monnef.core.utils;

public class TimeUtils {
    public static long getCurrentTimeInSeconds() {
        return System.currentTimeMillis() / 1000;
    }
}
