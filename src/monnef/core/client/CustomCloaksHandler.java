/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.client;

import com.google.common.base.Joiner;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import monnef.core.MonnefCorePlugin;
import monnef.core.Reference;
import monnef.core.utils.WebHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.TextureObject;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.ForgeSubscribe;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;

import static monnef.core.MonnefCorePlugin.Log;

// partially based on mDiyo's EventCloakRender
public class CustomCloaksHandler {
    private static final String JAFFA_CLOAK_URL_BASE = Reference.URL_JAFFAS + "/skin/cloak/_get.php";
    private static final String JAFFA_CLOAK_URL = JAFFA_CLOAK_URL_BASE + "?name=%s";

    private static HashSet<String> specialNames = new HashSet<String>();
    private ArrayList<AbstractClientPlayer> processedPlayers = new ArrayList<AbstractClientPlayer>();
    private Method getImageMethod;

    public CustomCloaksHandler() {
        ArrayList<String> tmp = new ArrayList<String>();
        WebHelper.getLinesTillFooter(JAFFA_CLOAK_URL_BASE, tmp);
        specialNames.clear();
        specialNames.addAll(tmp);
        Log.printFine("special names: " + Joiner.on(", ").join(specialNames));
        prepareGetImageMethod();
    }

    private void prepareGetImageMethod() {
        //     private static ThreadDownloadImageData getDownloadImage(ResourceLocation par0ResourceLocation, String par1Str, ResourceLocation par2ResourceLocation, IImageBuffer par3IImageBuffer)
        try {
            getImageMethod = AbstractClientPlayer.class.getDeclaredMethod("getDownloadImage", ResourceLocation.class, String.class, ResourceLocation.class, IImageBuffer.class);
        } catch (NoSuchMethodException e) {
            try {
                getImageMethod = AbstractClientPlayer.class.getDeclaredMethod("func_110301_a", ResourceLocation.class, String.class, ResourceLocation.class, IImageBuffer.class);
            } catch (NoSuchMethodException e1) {
                throw new RuntimeException("Unable to get helper method for cape downloading.");
            }
        }
        if (!getImageMethod.isAccessible()) {
            getImageMethod.setAccessible(true);
        }
    }

    public String getCloakUrl(String name) {
        return String.format(JAFFA_CLOAK_URL, name);
    }

    @ForgeSubscribe
    public void onPreRenderSpecials(RenderPlayerEvent.Specials.Pre event) {
        if (Loader.isModLoaded("shadersmod")) {
            return;
        }
        if (event.entityPlayer instanceof AbstractClientPlayer) {
            AbstractClientPlayer player = (AbstractClientPlayer) event.entityPlayer;

            if (!processedPlayers.contains(player)) {
                String playerName = player.username;
                processedPlayers.add(player);

                if (!specialNames.contains(playerName)) {
                    return;
                }

                String cloakURL = getCloakUrl(player.username);
                Log.printFine("got question on a cloak for [" + playerName + "]");

                tryItTCWay(player, cloakURL);
                //tryItMyWay(player, playerName, cloakURL);
                //tryItMyWay2(player, playerName, cloakURL);

                event.renderCape = true;
            }
        }
    }

    private void tryItMyWay(AbstractClientPlayer player, String playerName, String cloakURL) {
        ResourceLocation resLoc = new ResourceLocation("jaffa_cloaks/" + StringUtils.stripControlCodes(playerName));
        ThreadDownloadImageData img;
        try {
            img = (ThreadDownloadImageData) getImageMethod.invoke(null, resLoc, cloakURL, null, null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        // player.downloadImageCape = img
        ObfuscationReflectionHelper.setPrivateValue(AbstractClientPlayer.class, player, img, "downloadImageCape", "field_110315_c");
    }

    private void tryItMyWay2(AbstractClientPlayer player, String playerName, String cloakURL) {
        ResourceLocation resLoc = new ResourceLocation("jaffa_cloaks/" + StringUtils.stripControlCodes(playerName));
        ThreadDownloadImageData obj = new ThreadDownloadImageData(cloakURL, null, null);
        Minecraft.getMinecraft().getTextureManager().loadTexture(null, (TextureObject) obj);
        ObfuscationReflectionHelper.setPrivateValue(AbstractClientPlayer.class, player, obj, "downloadImageCape", "field_110315_c");
    }

    private void tryItTCWay(AbstractClientPlayer player, String cloakURL) {
        //player.getTextureCape().textureUploaded = false;
        ObfuscationReflectionHelper.setPrivateValue(ThreadDownloadImageData.class, player.getTextureCape(), false, "textureUploaded", "field_110559_g");
        new Thread(new CloakThread(player, cloakURL)).start();
    }

    private class CloakThread implements Runnable {
        AbstractClientPlayer player;
        String url;

        public CloakThread(AbstractClientPlayer player, String cloak) {
            this.player = player;
            url = cloak;
        }

        @Override
        public void run() {
            try {
                Image cape = new ImageIcon(new URL(url)).getImage();
                BufferedImage bo = new BufferedImage(cape.getWidth(null), cape.getHeight(null), BufferedImage.TYPE_INT_ARGB);
                bo.getGraphics().drawImage(cape, 0, 0, null);
                if (MonnefCorePlugin.debugEnv) {
                    try {
                        ImageIO.write(bo, "png", new File("./lastCape.png"));
                    } catch (IOException ignored) {
                    }
                }
                //player.getTextureCape().bufferedImage = bo;
                ObfuscationReflectionHelper.setPrivateValue(ThreadDownloadImageData.class, player.getTextureCape(), bo, "bufferedImage", "field_110560_d");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }
}
