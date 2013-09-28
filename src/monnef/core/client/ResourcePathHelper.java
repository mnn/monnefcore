/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.client;

import net.minecraft.util.ResourceLocation;

public class ResourcePathHelper {
    public enum ResourceTextureType {
        ITEM("textures/items"),
        BLOCK("textures/blocks"),
        GUI("textures/gui"),
        ENTITY("textures/entities"),
        TILE("textures/tiles"),;

        private String path;

        ResourceTextureType(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }

    public static String assemble(String fileName, String modId, ResourceTextureType type) {
        return modId + ":" + type.getPath() + "/" + fileName;
    }

    public static ResourceLocation assembleAndCreate(String fileName, String modId, ResourceTextureType type) {
        return new ResourceLocation(assemble(fileName, modId, type));
    }
}
