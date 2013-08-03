/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.collection;

import java.util.HashMap;

public class SpaceHashMap<C, V> extends SpaceMap<C, V, HashMap> {
    public SpaceHashMap() {
        super(new HashMapFactory());
    }

    private static class HashMapFactory implements IMapFactory<HashMap> {
        @Override
        public HashMap build() {
            return new HashMap();
        }
    }
}
