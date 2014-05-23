/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.utils;

import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.EntityInteractEvent;

import java.util.HashMap;

public class WolfFoodRegistry {
    public static final int HEALTH_DATAWATCHER = 18;
    public static final int MAXIMAL_HEALTH = 20;
    private static HashMap<Integer, WolfFood> food = new HashMap<Integer, WolfFood>();

    public static void registerWolfFood(Item item, int healAmount) {
        food.put(item.itemID, new WolfFood(healAmount));
    }

    public static boolean isWolfFood(int itemId) {
        return food.containsKey(itemId);
    }

    public static WolfFood getWolfFood(int itemId) {
        return food.get(itemId);
    }

    @ForgeSubscribe
    public void onInteract(EntityInteractEvent event) {
        if (event.entityPlayer == null || event.target == null) return;
        if (!(event.target instanceof EntityWolf)) return;
        EntityWolf wolf = (EntityWolf) event.target;
        EntityPlayer player = event.entityPlayer;
        ItemStack hand = event.entityPlayer.getCurrentEquippedItem();
        if (hand == null) return;
        if (isWolfFood(hand.itemID)) {
            if (wolf.getDataWatcher().getWatchableObjectInt(HEALTH_DATAWATCHER) < MAXIMAL_HEALTH) {
                if (!player.capabilities.isCreativeMode) {
                    --hand.stackSize;
                }

                wolf.heal(getWolfFood(hand.itemID).healAmount);

                if (hand.stackSize <= 0) {
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
                }
            }
        }
    }

    private static class WolfFood {
        public int healAmount;

        public WolfFood(int healAmount) {
            this.healAmount = healAmount;
        }
    }
}
