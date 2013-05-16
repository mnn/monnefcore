/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.utils;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;

import java.util.ArrayList;
import java.util.List;

public class BiomeHelper {
    public static BiomeGenBase[] compileListOrAsArray(BiomeDictionary.Type... types) {
        return compileListOr(types).toArray(new BiomeGenBase[]{});
    }

    public static List<BiomeGenBase> compileListOr(BiomeDictionary.Type... types) {
        // a bit costly solution to not duplicate code
        // (but it's called only once at a start, so let's have more maintainable code)
        BiomeDictionary.Type[][] tmp = new BiomeDictionary.Type[types.length][1];
        for (int i = 0; i < types.length; i++) {
            tmp[i][0] = types[i];
        }
        return compileList(tmp);
    }

    public static List<BiomeGenBase> compileListAnd(BiomeDictionary.Type... types) {
        if (types.length == 0) throw new RuntimeException("missing types parameter");
        ArrayList<BiomeGenBase> res = new ArrayList<BiomeGenBase>();

        BiomeGenBase[] biomes = BiomeDictionary.getBiomesForType(types[0]);
        for (BiomeGenBase biome : biomes) {
            boolean failed = false;
            BiomeDictionary.Type[] currTypes = BiomeDictionary.getTypesForBiome(biome);
            for (int i = 1; i < currTypes.length; i++) {
                if (!BiomeDictionary.isBiomeOfType(biome, currTypes[i])) {
                    failed = true;
                    break;
                }
            }

            if (!failed && !res.contains(biome)) {
                res.add(biome);
            }
        }

        return res;
    }

    // or (and ...) or (and ...) or ...
    public static List<BiomeGenBase> compileList(BiomeDictionary.Type[]... types) {
        if (types.length == 0) throw new RuntimeException("missing types parameter");

        ArrayList<BiomeGenBase> res = new ArrayList<BiomeGenBase>();
        for (int i = 0; i < types.length; i++) {
            List<BiomeGenBase> tmp = compileListAnd(types[i]);
            for (BiomeGenBase b : tmp) {
                if (!res.contains(b)) res.add(b);
            }
        }

        return res;
    }

    public static BiomeGenBase[] compileListAsArray(BiomeDictionary.Type[]... types) {
        return compileList(types).toArray(new BiomeGenBase[]{});
    }
}
