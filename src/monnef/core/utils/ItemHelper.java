/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.utils;

import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemHelper {
    /**
     * Damages item.
     * @param item
     * @param amount
     * @return If item is destroyed.
     */
    public static boolean DamageItem(ItemStack item, int amount) {
        //this.itemDamage > this.getMaxDamage()
        if (item == null) return false;
        if (amount <= 0) return false;

        int newItemDamage = item.getItemDamage() + amount;
        item.setItemDamage(newItemDamage);
        if (newItemDamage > item.getMaxDamage()) {
            return true;
        }

        return false;
    }

    public static void insertStackMultipleTimes(List<ItemStack> list, ItemStack item, int count) {
        for (int i = 0; i < count; i++) {
            list.add(item.copy());
        }
    }
}
