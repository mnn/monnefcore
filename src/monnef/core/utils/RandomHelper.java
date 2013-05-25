/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.utils;

import java.util.Random;

public class RandomHelper {
    public static Random rand = new Random();

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

    public static float generateRandomFromSymmetricInterval(float len) {
        return generateRandomFromInterval(-len, len);
    }

    public static int generateRandomFromInterval(int min, int max) {
        int size = max - min;
        if (size < 0) throw new IllegalArgumentException();
        return rand.nextInt(size + 1) + min;
    }

    public static int generateRandomFromSymmetricInterval(int len) {
        return generateRandomFromInterval(-len, len);
    }

    public static boolean rollPercentBooleanDice(int percentsOfSuccess) {
        return rand.nextFloat() < percentsOfSuccess / 100f;
    }
}
