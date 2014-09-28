/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.collection;

public interface ISpaceMap<C, V> {
    void put(C x, C y, C z, V value);

    boolean contains(C x, C y, C z);

    V get(C x, C y, C z);

    int size();

    boolean isEmpty();

    void clear();
}
