/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.utils;

import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.List;

public class EntityHelper {

    public static final float MAX_UP_MOTION = 0.5f;

    public static boolean animalIsAdult(EntityLiving animal) {
        return animal instanceof EntityAnimal ? ((EntityAnimal) animal).getGrowingAge() >= 0 : true;
    }

    public static String formatCoordinates(Entity e) {
        StringBuilder s = new StringBuilder();
        s.append(e.posX);
        s.append(", ");
        s.append(e.posY);
        s.append(", ");
        s.append(e.posZ);
        return s.toString();
    }

    public static void pushEntitiesBack(World w, Vec3 hitVec, float force, float radius, float damage, float yBoost, EntityLiving thrower) {
        AxisAlignedBB area = AxisAlignedBB.getAABBPool().getAABB(hitVec.xCoord, hitVec.yCoord, hitVec.zCoord, hitVec.xCoord, hitVec.yCoord, hitVec.zCoord).expand(radius, radius, radius);
        List entities = w.getEntitiesWithinAABB(Entity.class, area);
        for (Entity entity : (List<Entity>) entities) {
            double dist = entity.getDistance(hitVec.xCoord, hitVec.yCoord, hitVec.zCoord) / radius;
            if (dist <= 1) {
                Vec3 distFromExpl = w.getWorldVec3Pool().getVecFromPool(entity.posX - hitVec.xCoord, entity.posY + entity.getEyeHeight() - hitVec.yCoord, entity.posZ - hitVec.zCoord);
                double len = distFromExpl.lengthVector();
                if (len == 0) {
                    continue;
                }
                distFromExpl = VectorUtils.divideVector(w.getWorldVec3Pool(), distFromExpl, len); // normalization
                double reduction = entity.boundingBox != null ? w.getBlockDensity(hitVec, entity.boundingBox) : 1;
                double dmg = (1 - dist) * damage * reduction;
                DamageSource dmgSrc = thrower != null
                        ? (thrower instanceof EntityPlayer ? DamageSource.causePlayerDamage((EntityPlayer) thrower) : DamageSource.causeMobDamage(thrower))
                        : DamageSource.generic;
                entity.attackEntityFrom(dmgSrc, (int) (dmg + 0.5));
                entity.motionX += distFromExpl.xCoord * force;
                entity.motionY += distFromExpl.yCoord * force + yBoost;
                entity.motionZ += distFromExpl.zCoord * force;
                if (entity.motionY > MAX_UP_MOTION) entity.motionY = MAX_UP_MOTION;
            }
        }
    }

    public static void setTrackingRange(Class<? extends Entity> aClass, int range) {
        EntityRegistry.EntityRegistration record = EntityRegistry.instance().lookupModSpawn(aClass, false);
        ReflectionHelper.setPrivateValue(EntityRegistry.EntityRegistration.class, record, range, "trackingRange");
    }
}
