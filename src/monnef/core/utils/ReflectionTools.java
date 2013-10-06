/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.utils;

import java.lang.annotation.Annotation;

public class ReflectionTools {
    public static <T extends Annotation> T findAnnotation(Class<T> annotation, Class<?> clazz) {
        Class<?> curr = clazz;
        while (!curr.equals(Object.class)) {
            T a = curr.getAnnotation(annotation);
            if (a != null) return a;
            curr = curr.getSuperclass();
        }
        return null;
    }
}
