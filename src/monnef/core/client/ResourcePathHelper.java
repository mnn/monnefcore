/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.client;

import monnef.core.MonnefCorePlugin;
import net.minecraft.util.ResourceLocation;

public class ResourcePathHelper {
    public enum ResourceTextureType {
        ITEM("textures/items"),
        BLOCK("textures/blocks"),
        GUI("textures/gui"),
        ENTITY("textures/entities"),
        TILE("textures/tiles"),
        ARMOR("textures/armor");

        private String path;

        ResourceTextureType(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }

    public static String assemble(String fileName, String modId, ResourceTextureType type) {
        if (MonnefCorePlugin.debugEnv && fileName.startsWith("/")) {
            MonnefCorePlugin.Log.printWarning("Redundant slash symbol: '" + fileName + "'.");
        }
        return modId + ":" + type.getPath() + "/" + fileName;
    }

    public static ResourceLocation assembleAndCreate(String fileName, String modId, ResourceTextureType type) {
        return new ResourceLocation(assemble(fileName, modId, type));
    }
}
