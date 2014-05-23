package monnef.core.utils;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.MinecraftForge;

import static monnef.core.MonnefCorePlugin.Log;

public class BreakableIronMaterial {
    private static boolean initialized = false;

    public static final Material breakableIronMaterial = new Material(MapColor.ironColor);

    public static void onPostLoad() {
        if (initialized) throw new RuntimeException("re-initialization!");
        initialized = true;

        int marked = 0;
        for (int i = 0; i < Block.blocksList.length; i++) {
            Block b = Block.blocksList[i];
            if (b != null && b.blockMaterial == breakableIronMaterial) {
                MinecraftForge.setBlockHarvestLevel(b, "pickaxe", 0);
                marked++;
            }
        }
        Log.printFine("Registered " + marked + " blocks as mine-able by pickaxe.");
        if (marked <= 0) {
            Log.printWarning("No block registered as mine-able by pickaxe, possible error!");
        }
    }
}
