/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.test;

import junit.framework.Assert;
import monnef.core.external.javassist.Modifier;
import monnef.core.utils.IntegerCoordinates;
import net.minecraftforge.common.ForgeDirection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.reflect.Constructor;

import static junit.framework.Assert.assertEquals;

@RunWith(JUnit4.class)
public class IntCoordsTest {
    @Test
    public void testBasic() {
        IntegerCoordinates pos = createIntCoords(10, 20, 50);
        assertEquals(10, pos.getX());
        assertEquals(20, pos.getY());
        assertEquals(50, pos.getZ());
    }

    @Test
    public void testShift() {
        IntegerCoordinates pos = createIntCoords(10, 20, 50);
        pos = (IntegerCoordinates) pos.shiftInDirectionBy(ForgeDirection.NORTH, 2);
        assertEquals(10, pos.getX());
        assertEquals(20, pos.getY());
        assertEquals(48, pos.getZ());
    }

    @Test
    public void testStrafe() {
        IntegerCoordinates pos = createIntCoords(10, 20, 50);
        pos = (IntegerCoordinates) pos.strafeInDirection(ForgeDirection.NORTH, 2);
        assertEquals(8, pos.getX());
        assertEquals(20, pos.getY());
        assertEquals(50, pos.getZ());
    }

    private IntegerCoordinates createIntCoords(int x, int y, int z) {
        try {
            Constructor<IntegerCoordinates> constructor = IntegerCoordinates.class.getDeclaredConstructor(int.class, int.class, int.class);
            Assert.assertTrue(Modifier.isPrivate(constructor.getModifiers()));
            constructor.setAccessible(true);
            return constructor.newInstance(x, y, z);
        } catch (Throwable e) {
            throw new RuntimeException("Cannot create the integer coordinates instance.", e);
        }
    }
}
