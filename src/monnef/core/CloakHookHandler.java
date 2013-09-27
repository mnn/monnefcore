/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;

import java.util.ArrayList;

public class CloakHookHandler {
    private static ArrayList<ICloakHandler> listeners = new ArrayList<ICloakHandler>();

    @SideOnly(Side.CLIENT)
    public static boolean handleUpdateCloak(Entity entity) {
        // TODO: remove and use rendering event?
        /*
        if (!(entity instanceof EntityPlayer)) return false;
        EntityPlayer player = (EntityPlayer) entity;
        String old = player.cloakUrl;
        if (old == null) old = "";

        for (ICloakHandler handler : listeners) {
            handler.handleCloak(player, player.getEntityName());
        }
        boolean skipRest = !old.equals(player.cloakUrl);
        // not nice, but better than being blocked by thermal expansion...
        if (skipRest) {
            TextureManager re = FMLClientHandler.instance().getClient().renderEngine;
            if (entity.skinUrl != null) {
                re.obtainImageData(entity.skinUrl, new ImageBufferDownload());
            }

            if (entity.cloakUrl != null) {
                re.obtainImageData(entity.cloakUrl, new ImageBufferDownload());
            }
        }
        return skipRest;
        */
        return false;
    }

    public static void registerCloakHandler(ICloakHandler handler) {
        listeners.add(handler);
    }
}
