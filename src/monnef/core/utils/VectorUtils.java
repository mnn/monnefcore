/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.utils;

import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;

public class VectorUtils {
    public static Vec3 getEntityPositionVector(Entity entity) {
        return createVector(entity.posX, entity.posY, entity.posZ);
    }

    public static Vec3 addVector(Vec3 input, Vec3 toAdd) {
        return input.addVector(toAdd.xCoord, toAdd.yCoord, toAdd.zCoord);
    }

    public static Vec3 multiplyVector(Vec3 input, double number) {
        return divideVector(input, 1 / number);
    }

    public static Vec3 divideVector(Vec3 input, double number) {
        return createVector(input.xCoord / number, input.yCoord / number, input.zCoord / number);
    }

    public static Vec3 shiftedVectorInDirection(Vec3 start, Vec3 direction, double distance) {
        return addVector(start, multiplyVector(direction.normalize(), distance));
    }

    public static Vec3 createVector(double x, double y, double z) {
        return Vec3.createVectorHelper(x, y, z);
    }
}
