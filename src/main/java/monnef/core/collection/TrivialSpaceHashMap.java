/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.collection;

import java.util.HashMap;

public class TrivialSpaceHashMap<C, V> implements ISpaceMap<C, V> {
    private HashMap<SpaceCoordinate<C>, V> data = new HashMap<SpaceCoordinate<C>, V>();

    @Override
    public void put(C x, C y, C z, V value) {
        data.put(new SpaceCoordinate<C>(x, y, z), value);
    }

    @Override
    public boolean contains(C x, C y, C z) {
        return get(x, y, z) != null;
    }

    @Override
    public V get(C x, C y, C z) {
        return data.get(new SpaceCoordinate<C>(x, y, z));
    }

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public void clear() {
        data.clear();
    }
}
