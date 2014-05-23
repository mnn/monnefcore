/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.power;

import monnef.core.power.IPipeWrench;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.HashSet;

public class WrenchHelper {
    private static HashSet<Integer> wrenchIDs = new HashSet<Integer>();

    public static void registerWrenchItem(int itemId){
        wrenchIDs.add(itemId);
    }

    public static boolean isPipeWrenchOrCompatible(Item item) {
        if (item instanceof IPipeWrench) return true;
        if(wrenchIDs.contains(item.itemID)) return true;
        return false;
    }

    public static boolean isHoldingWrench(EntityPlayer player) {
        ItemStack hand = player.getHeldItem();
        if (hand != null && isPipeWrenchOrCompatible(hand.getItem())) {
            return true;
        }
        return false;
    }
}
