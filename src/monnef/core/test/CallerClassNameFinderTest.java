/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.test;

import monnef.core.test.innerPackage.CallerFinderWrapper;
import monnef.core.utils.CallerFinder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static junit.framework.Assert.assertEquals;

@RunWith(JUnit4.class)
public class CallerClassNameFinderTest {
    @Test
    public void testMyClass() {
        String myClass = CallerFinder.getCallerClassName(0); // <- me
        assertEquals("monnef.core.test.CallerClassNameFinderTest", myClass);
    }

    @Test
    public void testMyPackage() {
        String myPackage = CallerFinder.getMyPackage();
        assertEquals("monnef.core.test", myPackage);
    }

    @Test
    public void testMyClassDeeper() {
        String myClass = CallerFinderWrapper.getCallerClassName(0 + 1); // <- me
        assertEquals(getClass().getName(), myClass);
    }

    @Test
    public void testWrapperClass() {
        String clazz = CallerFinderWrapper.getCallerClassName(0); // lowest
        assertEquals(CallerFinderWrapper.class.getName(), clazz);

        Class clazzz = CallerFinderWrapper.getCallerClass(0);
        assertEquals(CallerFinderWrapper.class, clazzz);
    }

    @Test
    public void testWrapperPackage() {
        String pack = CallerFinderWrapper.getCallerPackage(0); // lowest
        assertEquals("monnef.core.test.innerPackage", pack);
    }
}
