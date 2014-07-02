/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.utils;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.EntityInteractEvent;

import java.util.HashMap;

public class WolfFoodRegistry {
    public static final int HEALTH_DATAWATCHER = 18;
    public static final int MAXIMAL_HEALTH = 20;
    private static HashMap<Item, WolfFood> food = new HashMap<Item, WolfFood>();

    public static void registerWolfFood(Item item, int healAmount) {
        food.put(item, new WolfFood(healAmount));
    }

    public static boolean isWolfFood(Item item) {
        return food.containsKey(item);
    }

    public static WolfFood getWolfFood(Item item) {
        return food.get(item);
    }

    @SubscribeEvent
    public void onInteract(EntityInteractEvent event) {
        if (event.entityPlayer == null || event.target == null) return;
        if (!(event.target instanceof EntityWolf)) return;
        EntityWolf wolf = (EntityWolf) event.target;
        EntityPlayer player = event.entityPlayer;
        ItemStack hand = event.entityPlayer.getCurrentEquippedItem();
        if (hand == null) return;
        if (isWolfFood(hand.getItem())) {
            if (wolf.getDataWatcher().getWatchableObjectInt(HEALTH_DATAWATCHER) < MAXIMAL_HEALTH) {
                if (!player.capabilities.isCreativeMode) {
                    --hand.stackSize;
                }

                wolf.heal(getWolfFood(hand.getItem()).healAmount);

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
