/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.utils;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import monnef.core.MonnefCorePlugin;
import net.minecraft.client.Minecraft;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.stats.StatList;

import java.lang.reflect.Field;
import java.util.Map;

public class AchievementsHelper {
    public static void removeAchievementFromStatsFile(StatBase achievement) {
        if (FMLCommonHandler.instance().getEffectiveSide() != Side.CLIENT) {
            return;
        }

        Minecraft mc = FMLClientHandler.instance().getClient();
        Map mapA = ReflectionHelper.getPrivateValue(StatFileWriter.class, mc.statFileWriter, 0);
        Map mapB = ReflectionHelper.getPrivateValue(StatFileWriter.class, mc.statFileWriter, 1);
        mapA.remove(achievement);
        mapB.remove(achievement);
    }

    public static void removeAchievementFromStatList(Integer statId) {
        Field oss = ReflectionHelper.findField(StatList.class, "oneShotStats", "field_75942_a");
        try {
            Map m = (Map) oss.get(null);
            if (m.containsKey(statId)) {
                MonnefCorePlugin.Log.printFine(String.format("Removed stat with id=%d, name=%s.", statId, ((StatBase) (m.get(statId))).getName()));
                m.remove(statId);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to remove achievement from stats list.");
        }
    }
}
