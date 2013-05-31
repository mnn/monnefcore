/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.utils;

public class ClassHelper {
    public static boolean isClassPresent(String fullName) {
        try {
            Class c = Class.forName(fullName);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
