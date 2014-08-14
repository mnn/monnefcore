package monnef.core.asm;

import cpw.mods.fml.common.registry.GameData;
import monnef.core.MonnefCorePlugin;
import monnef.core.api.IItemBlock;
import monnef.core.utils.GameDataAccessor;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class GameDataEvents {
    /**
     * Filters idHint, if needed (custom ItemBlock) removes ID allocation made by block
     *
     * @param item   Item currently being processed.
     * @param idHint ID hint of current item.
     * @return Filtered idHint.
     */
    public static int onRegisterItemPre(GameData gameData, Item item, int idHint) {
        if (item instanceof IItemBlock) {
            IItemBlock itemBlock = (IItemBlock) item;
            if (itemBlock.suppressSpecialItemBlockHandling()) {
                MonnefCorePlugin.Log.printFine(String.format("Item %s suppressed special IItemBlock handling.", item.getUnlocalizedName()));
                return idHint;
            }
            Block block = itemBlock.getBlock();
            idHint = GameDataAccessor.iBlockRegistry(gameData).getId(block);
            if (idHint == -1) {
                throw new RuntimeException("Block must be registered first!");
            }
            MonnefCorePlugin.Log.printFine(String.format("Found matching Block %s for IItemBlock %s at id %d", block, item, idHint));
            GameDataAccessor.freeSlot(gameData, idHint, item);
            if (GameDataAccessor.iItemRegistry(gameData).getObjectById(idHint) != null) {
                throw new RuntimeException("Unable to free used ID by a block for registration of IItemBlock.");
            }
        }
        return idHint;
    }

    /**
     * Checks whether registration of custom ItemBlock was a success.
     *
     * @param item   Item currently being processed.
     * @param itemId Newly registered item ID.
     */
    public static void onRegisterItemPost(GameData gameData, Item item, int itemId, int idHint) {
        if (item instanceof IItemBlock && !((IItemBlock) item).suppressSpecialItemBlockHandling()) {
            if (itemId != idHint) {
                throw new IllegalStateException("Block -> IItemBlock insertion failed (itemId=" + itemId + ", idHint=" + idHint + ").");
            }
            GameDataAccessor.verifyCustomItemBlockName(gameData, (IItemBlock) item);
        }
    }

    public static void test() {
        Item item = null;
        int idHint = 1;
        idHint = onRegisterItemPre(GameDataAccessor.getMain(), item, idHint);
        int itemId = 2;
        onRegisterItemPost(GameDataAccessor.getMain(), item, itemId, idHint);
    }
}
