/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.test;

import monnef.core.utils.MathHelper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import scala.Option;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static monnef.core.utils.MathHelper.getIntSquareRootJava;
import static org.junit.Assert.assertArrayEquals;

@RunWith(JUnit4.class)
public class MathHelperTest {
    @Test
    public void TestRange() {
        int[] r = MathHelper.range(0, 2);
        assertArrayEquals(new int[]{0, 1}, r);

        r = MathHelper.range(0, 3, 50);
        assertArrayEquals(new int[]{0, 50, 100}, r);
    }

    @Test
    public void TestIntRoot() {
        MathHelper.IntSquareRoot$.MODULE$.purgeCache(); // from Java only called in tests (it's ugly)

        Option<Integer> r = getIntSquareRootJava(0);
        assertTrue(r.isDefined());
        assertEquals(Integer.valueOf(0), r.get());

        r = getIntSquareRootJava(1);
        assertTrue(r.isDefined());
        assertEquals(Integer.valueOf(1), r.get());

        r = getIntSquareRootJava(2);
        assertTrue(!r.isDefined());

        r = getIntSquareRootJava(3);
        assertTrue(!r.isDefined());

        r = getIntSquareRootJava(4);
        assertTrue(r.isDefined());
        assertEquals(Integer.valueOf(2), r.get());

        r = getIntSquareRootJava(16);
        assertTrue(r.isDefined());
        assertEquals(Integer.valueOf(4), r.get());

        r = getIntSquareRootJava(256);
        assertTrue(r.isDefined());
        assertEquals(Integer.valueOf(16), r.get());

        r = getIntSquareRootJava(257);
        assertTrue(!r.isDefined());
    }
}
