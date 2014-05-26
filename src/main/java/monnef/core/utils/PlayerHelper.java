/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.utils;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.ArrayList;
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
        if (item == null || item.stackSize <= 0) return;
        InventoryPlayer inv = player.inventory;
        if (inv.getFirstEmptyStack() == -1) {
            if (!world.isRemote) {
                EntityItem entity = new EntityItem(world, player.posX, player.posY + 0.5, player.posZ, item.copy());
                entity.delayBeforeCanPickup = 10;
                world.spawnEntityInWorld(entity);
            }
        } else {
            inv.addItemStackToInventory(item.copy());
        }
    }

    public static boolean playerHasEquipped(EntityPlayer player, Item item) {
        if (player == null) return false;
        ItemStack equippedItem = player.getCurrentEquippedItem();
        if (equippedItem == null) return false;
        return equippedItem.getItem() == item;
    }

    public static Vec3 getPlayersHeadPositionVector(EntityPlayer entity) {
        return entity.worldObj.getWorldVec3Pool().getVecFromPool(entity.posX, calculatePlayerPositionY(entity), entity.posZ);
    }

    public static AxisAlignedBB addCoord(AxisAlignedBB input, Vec3 vector) {
        return input.addCoord(vector.xCoord, vector.yCoord, vector.zCoord);
    }

    public static Vec3 calculatePlayerVectorMultiplied(EntityPlayer player, Vec3 vector, double distance) {
        return VectorUtils.multiplyVector(player.worldObj.getWorldVec3Pool(), vector.normalize(), distance);
    }

    public static MovingObjectPosition rayTraceBlock(EntityPlayer entity, double distance, Vec3 look) {
        Vec3 pos = getPlayersHeadPositionVector(entity);
        Vec3 target = pos.addVector(look.xCoord * distance, look.yCoord * distance, look.zCoord * distance);
        return entity.worldObj.rayTraceBlocks(pos, target);
    }

    public static final float U = 1 / 16f;

    public static double calculatePlayerPositionY(EntityPlayer entity) {
        return entity.posY + 1.62D - entity.yOffset + (entity.isSneaking() ? -2 * U : 0);
    }

    public static EntityHitResult rayTraceEntity(EntityPlayer player, double distance, Vec3 look) {
        Vec3 playerPosition = getPlayersHeadPositionVector(player);

        Entity entityHit;
        Vec3 shift = calculatePlayerVectorMultiplied(player, look, distance);
        AxisAlignedBB searchArea = addCoord(player.boundingBox, shift).expand(1.0D, 1.0D, 1.0D);
        List entitiesInArea = player.worldObj.getEntitiesWithinAABBExcludingEntity(player, searchArea);
        double bestDistance = 0;
        float testBorder = 0; // 0.3F;
        EntityHitResult res = null;
        Vec3 endPointOfTestVector = VectorUtils.addVector(playerPosition, shift);

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

    public static void addMessage(ICommandSender target, String msg) {
        ChatComponentText component = new ChatComponentText(msg);
        target.addChatMessage(component);
    }

    // only server side
    public static List<EntityPlayerMP> getPlayersWithOpenedContainerAround(EntityPlayerMP centerPlayer, double radius, Class<? extends Container> containerClass) {
        ArrayList<EntityPlayerMP> res = new ArrayList<EntityPlayerMP>();
        AxisAlignedBB box = AxisAlignedBB.getBoundingBox(centerPlayer.posX - radius, centerPlayer.posY - radius, centerPlayer.posZ - radius, centerPlayer.posX + radius, centerPlayer.posY + radius, centerPlayer.posZ + radius);
        List tmp = centerPlayer.worldObj.getEntitiesWithinAABBExcludingEntity(centerPlayer, box);
        for (Object item : tmp) {
            if (item instanceof EntityPlayerMP) {
                EntityPlayerMP player = (EntityPlayerMP) item;
                if (player.openContainer != null && containerClass.isAssignableFrom(player.openContainer.getClass())) {
                    res.add(player);
                }
            }
        }
        return res;
    }
}