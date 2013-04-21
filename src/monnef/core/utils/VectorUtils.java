/*
 * Copyright (c) 2013 monnef.
 */

package monnef.core.utils;

import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3Pool;

public class VectorUtils {
    public static Vec3 getEntityPositionVector(Entity entity) {
        return entity.worldObj.getWorldVec3Pool().getVecFromPool(entity.posX, entity.posY, entity.posZ);
    }

    public static Vec3 addVector(Vec3 input, Vec3 toAdd) {
        return input.addVector(toAdd.xCoord, toAdd.yCoord, toAdd.zCoord);
    }

    public static Vec3 multiplyVector(Vec3Pool pool, Vec3 input, double number) {
        return divideVector(pool, input, 1 / number);
    }

    public static Vec3 divideVector(Vec3Pool pool, Vec3 input, double number) {
        return pool.getVecFromPool(input.xCoord / number, input.yCoord / number, input.zCoord / number);
    }

    public static Vec3 shiftedVectorInDirection(Vec3Pool pool, Vec3 start, Vec3 direction, double distance) {
        return addVector(start, multiplyVector(pool, direction.normalize(), distance));
    }
}
