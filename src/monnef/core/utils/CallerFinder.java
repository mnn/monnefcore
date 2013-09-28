/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.utils;

public class CallerFinder {
    private final static MySecurityManager manager = new MySecurityManager();
    public static final int DEPTH_OFFSET = 2;

    public static String getCallerClassName(int callStackDepth) {
        return manager.getCallerClassName(callStackDepth + DEPTH_OFFSET);
    }

    public static String getCallerPackage(int depth) {
        return getCallerClass(depth + 1).getPackage().getName();
    }

    public static String getMyPackage() {
        return getCallerPackage(1);
    }

    public static Class getCallerClass(int depth) {
        try {
            return Class.forName(getCallerClassName(depth + 1));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    static class MySecurityManager extends SecurityManager {
        public String getCallerClassName(int callStackDepth) {
            return getClassContext()[callStackDepth].getName();
        }
    }
}
