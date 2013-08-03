/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.collection;

public class SpaceCoordinate<T> {
    public final T x, y, z;

    public SpaceCoordinate(T x, T y, T z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SpaceCoordinate that = (SpaceCoordinate) o;

        if (!x.equals(that.x)) return false;
        if (!y.equals(that.y)) return false;
        if (!z.equals(that.z)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = x.hashCode();
        result = 31 * result + y.hashCode();
        result = 31 * result + z.hashCode();
        return result;
    }
}
