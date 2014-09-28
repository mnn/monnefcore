/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.client;

import net.minecraft.client.renderer.IImageBuffer;

import java.awt.*;
import java.awt.image.BufferedImage;

// based on DevCapesImageBufferDownload from DeveloperCapes
public class CapeImageBuffer implements IImageBuffer {
    @Override
    public BufferedImage parseUserSkin(BufferedImage buffImage) {
        if (buffImage == null) {
            return null;
        } else {
            int width = (buffImage.getWidth(null) <= 64) ? 64 : (buffImage.getWidth(null));
            int height = (buffImage.getHeight(null) <= 32) ? 32 : (buffImage.getHeight(null));

            BufferedImage capeImage = new BufferedImage(width, height, 2);

            Graphics gHelper = capeImage.getGraphics();
            gHelper.drawImage(buffImage, 0, 0, null);
            gHelper.dispose();

            return capeImage;
        }
    }
}
