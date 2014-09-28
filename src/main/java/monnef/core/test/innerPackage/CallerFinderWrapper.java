/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.test.innerPackage;

import monnef.core.utils.CallerFinder;

public class CallerFinderWrapper {
    public static String getCallerClassName(int depth) {
        return CallerFinder.getCallerClassName(depth);
    }

    public static Class getCallerClass(int depth) {
        return CallerFinder.getCallerClass(depth);
    }

    public static String getCallerPackage(int depth) {
        return CallerFinder.getCallerPackage(depth);
    }
}
