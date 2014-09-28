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
        ARMOR("textures/armor"),
        MODELS("models"),
        SOUND(""),;

        private String path;

        ResourceTextureType(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }

    public static String assemble(String fileName, ResourceTextureType type) {
        return assemble(fileName, PackageToModIdRegistry.searchModIdFromCurrentPackage(1), type);
    }

    public static String assemble(String fileName, String modId, ResourceTextureType type) {
        if (MonnefCorePlugin.debugEnv && fileName.startsWith("/")) {
            MonnefCorePlugin.Log.printWarning("Redundant slash symbol: '" + fileName + "'.");
        }
        StringBuilder sb = new StringBuilder(modId);
        sb.append(":");
        sb.append(type.getPath());
        if (type.getPath().length() > 0) sb.append("/");
        sb.append(fileName);
        return sb.toString();
    }

    public static ResourceLocation assembleAndCreate(String fileName, ResourceTextureType type) {
        return assembleAndCreate(fileName, PackageToModIdRegistry.searchModIdFromCurrentPackage(1), type);
    }

    public static ResourceLocation assembleAndCreate(String fileName, String modId, ResourceTextureType type) {
        return new ResourceLocation(assemble(fileName, modId, type));
    }
}
