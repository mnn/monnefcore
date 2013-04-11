/*
 * Copyright (c) 2013 monnef.
 */

package monnef.core.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3Pool;
import net.minecraft.world.World;

import java.util.List;

public class PlayerHelper {
    public static void damageCurrentItem(EntityPlayer player) {
        if (player.worldObj.isRemote) return;
        ItemStack handItem = player.getCurrentEquippedItem();
        if (handItem == null) return;
        handItem.damageItem(1, player);
    }

    public static void giveItemToPlayer(EntityPlayer player, ItemStack item) {
        World world = player.worldObj;
        if (item == null || item.stackSize <= 0 || world.isRemote) return;
        Entity entity = new EntityItem(world, player.posX, player.posY + 0.5, player.posZ, item.copy());
        world.spawnEntityInWorld(entity);
    }

    public static boolean PlayerHasEquipped(EntityPlayer player, int itemId) {
        if (player == null) return false;
        ItemStack equippedItem = player.getCurrentEquippedItem();
        if (equippedItem == null) return false;
        return equippedItem.itemID == itemId;
    }

    public static Vec3 getEntityPositionVector(Entity entity) {
        return entity.worldObj.getWorldVec3Pool().getVecFromPool(entity.posX, entity.posY, entity.posZ);
    }

    public static Vec3 getPlayersHeadPositionVector(EntityPlayer entity) {
        return entity.worldObj.getWorldVec3Pool().getVecFromPool(entity.posX, calculatePlayerPositionY(entity), entity.posZ);
    }


    public static AxisAlignedBB addCoord(AxisAlignedBB input, Vec3 vector) {
        return input.addCoord(vector.xCoord, vector.yCoord, vector.zCoord);
    }

    public static Vec3 calculatePlayerLookMultiplied(EntityPlayer player, double distance) {
        return PlayerHelper.multiplyVector(player.worldObj.getWorldVec3Pool(), player.getLookVec(), distance);
    }

    public static Vec3 addVector(Vec3 input, Vec3 toAdd) {
        return input.addVector(toAdd.xCoord, toAdd.yCoord, toAdd.zCoord);
    }

    public static MovingObjectPosition rayTraceBlock(EntityPlayer entity, double distance) {
/**
 Vec3 vec3 = this.getPosition(par3);
 Vec3 vec31 = this.getLook(par3);
 Vec3 vec32 = vec3.addVector(vec31.xCoord * par1, vec31.yCoord * par1, vec31.zCoord * par1);
 */

        Vec3 pos = getPlayersHeadPositionVector(entity);
        Vec3 look = entity.getLookVec();
        Vec3 target = pos.addVector(look.xCoord * distance, look.yCoord * distance, look.zCoord * distance);
        return entity.worldObj.rayTraceBlocks(pos, target);
    }

    public static double calculatePlayerPositionY(EntityPlayer entity) {
        return entity.posY + 1.62D - entity.yOffset - (entity.isSneaking() ? 1 : 0);
    }

    public static EntityHitResult rayTraceEntity(EntityPlayer player, double distance) {
        Vec3 playerPosition = getPlayersHeadPositionVector(player);
        Vec3 playerLook = player.getLookVec();

        Entity entityHit = null;
        Vec3 shift = calculatePlayerLookMultiplied(player, distance);
        AxisAlignedBB searchArea = addCoord(player.boundingBox, shift).expand(1.0D, 1.0D, 1.0D);
        List entitiesInArea = player.worldObj.getEntitiesWithinAABBExcludingEntity(player, searchArea);
        double bestDistance = 0;
        float testBorder = 0; // 0.3F;
        EntityHitResult res = null;
        Vec3 endPointOfTestVector = addVector(playerPosition, shift);

        for (int i = 0; i < entitiesInArea.size(); ++i) {
            Entity entityInArea = (Entity) entitiesInArea.get(i);

            if (entityInArea.canBeCollidedWith() && entityInArea != player) {
                AxisAlignedBB testEntityBox = entityInArea.boundingBox.expand((double) testBorder, (double) testBorder, (double) testBorder);
                MovingObjectPosition interception = testEntityBox.calculateIntercept(playerPosition, endPointOfTestVector);

                if (interception != null) {
                    double distanceFromHitEntity = playerPosition.distanceTo(interception.hitVec);

                    if (distanceFromHitEntity <= distance && (distanceFromHitEntity < bestDistance || bestDistance == 0.0D)) {
                        entityHit = entityInArea;
                        bestDistance = distanceFromHitEntity;
                        res = new EntityHitResult(interception.hitVec, entityHit, distanceFromHitEntity);
                    }
                }
            }
        }

        return res;
    }

    public static class EntityHitResult {
        public final Vec3 hitVector;
        public final Entity entity;
        public final double distance;

        public EntityHitResult(Vec3 hitVector, Entity entity, double distance) {
            this.hitVector = hitVector;
            this.entity = entity;
            this.distance = distance;
        }
    }

    public static Vec3 multiplyVector(Vec3Pool pool, Vec3 input, double number) {
        return pool.getVecFromPool(input.xCoord * number, input.yCoord * number, input.zCoord * number);
    }
}