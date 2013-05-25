/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.utils;

import java.util.ArrayList;

public class Interval {
    private final int min;
    private final int max;

    public Interval(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public static Interval[] fromArray(int... items) {
        if (items.length <= 0) {
            throw new RuntimeException("Incorrect count of parameters.");
        }
        if (items.length % 2 == 1) {
            throw new RuntimeException("Odd count of parameters.");
        }

        ArrayList<Interval> tmp = new ArrayList<Interval>();
        for (int i = 0; i < items.length; i += 2) {
            tmp.add(new Interval(items[i], items[i + 1]));
        }
        return tmp.toArray(new Interval[]{});
    }

    public int getRandom() {
        return RandomHelper.generateRandomFromInterval(getMin(), getMax());
    }
}
