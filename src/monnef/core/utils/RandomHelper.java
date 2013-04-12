/*
 * Copyright (c) 2013 monnef.
 */

package monnef.core.utils;

import java.util.Random;

public class RandomHelper {
    private static Random rand = new Random();

    public static float generateRandomFromInterval(float min, float max) {
        float size = max - min;
        if (size < 0) throw new IllegalArgumentException();
        return rand.nextFloat() * size + min;
    }

    public static float[] randomPositionInCircle(float radius) {
        double angle = rand.nextDouble();
        double x = radius * Math.cos(angle);
        double y = radius * Math.sin(angle);
        return new float[]{(float) x, (float) y};
    }

}
