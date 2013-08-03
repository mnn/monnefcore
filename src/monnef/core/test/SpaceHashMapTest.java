package monnef.core.test;

import monnef.core.collection.ISpaceMap;
import monnef.core.collection.SpaceHashMap;
import monnef.core.collection.TrivialSpaceHashMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class SpaceHashMapTest {
    private final ISpaceMap<Integer, String> space;

    @Parameters(name = "{0}")
    public static Iterable<ISpaceMap[]> data() {
        return Arrays.asList(new ISpaceMap[][]{
                {new TrivialSpaceHashMap<Integer, String>()},
                {new SpaceHashMap<Integer, String>()}
        });
    }

    public SpaceHashMapTest(ISpaceMap<Integer, String> space) {
        this.space = space;
    }

    @Test
    public void testMap() {
        assertTrue(space.isEmpty());
        assertEquals(space.size(), 0);
        space.put(1, 2, 3, "123");
        assertFalse(space.isEmpty());
        assertEquals(space.size(), 1);
        space.put(4, 5, 6, "456");
        assertEquals(space.size(), 2);
        assertEquals(space.get(4, 5, 6), "456");
        space.put(4, 5, 6, "456+");
        assertEquals(space.size(), 2);
        assertEquals(space.get(4, 5, 6), "456+");
        space.clear();
        assertTrue(space.isEmpty());
        assertEquals(space.size(), 0);

        int expSize = 0;
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 20; y++) {
                for (int z = 0; z < 30; z++) {
                    String value = x + ":" + y + ":" + z;
                    space.put(x, y, z, value);
                    expSize++;
                    assertEquals(expSize, space.size());
                    assertEquals(value, space.get(x, y, z));
                }
            }
        }
    }
}
