/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.collection;

import java.util.Map;

public class SpaceMap<C, V, I extends Map> implements ISpaceMap<C, V> {
    private Map<C, Map<C, Map<C, V>>> data;
    private IMapFactory factory;

    public SpaceMap(IMapFactory<I> factory) {
        this.factory = factory;
        data = factory.build();
    }

    @Override
    public void put(C x, C y, C z, V value) {
        if (!data.containsKey(x)) data.put(x, factory.build());
        Map<C, Map<C, V>> xSlice = data.get(x);
        if (!xSlice.containsKey(y)) xSlice.put(y, factory.build());
        Map<C, V> xySlice = xSlice.get(y);
        xySlice.put(z, value);
    }

    @Override
    public boolean contains(C x, C y, C z) {
        return get(x, y, z) != null;
    }

    @Override
    public V get(C x, C y, C z) {
        Map<C, Map<C, V>> xSlice = data.get(x);
        if (xSlice == null) return null;
        Map<C, V> xySlice = xSlice.get(y);
        if (xySlice == null) return null;
        return xySlice.get(z);
    }

    @Override
    public int size() {
        int counter = 0;
        for (Map.Entry<C, Map<C, Map<C, V>>> xSlices : data.entrySet()) {
            for (Map.Entry<C, Map<C, V>> xySlice : xSlices.getValue().entrySet()) {
                counter += xySlice.getValue().size();
            }
        }
        return counter;
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
