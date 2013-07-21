/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;

public class CloakHookHandler {
    private static ArrayList<ICloakHandler> listeners = new ArrayList<ICloakHandler>();

    @SideOnly(Side.CLIENT)
    public static boolean handleUpdateCloak(Entity entity) {
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
            RenderEngine re = FMLClientHandler.instance().getClient().renderEngine;
            if (entity.skinUrl != null) {
                re.obtainImageData(entity.skinUrl, new ImageBufferDownload());
            }

            if (entity.cloakUrl != null) {
                re.obtainImageData(entity.cloakUrl, new ImageBufferDownload());
            }
        }
        return skipRest;
    }

    public static void registerCloakHandler(ICloakHandler handler) {
        listeners.add(handler);
    }
}
