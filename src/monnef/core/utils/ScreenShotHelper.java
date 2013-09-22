/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.utils;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.IntBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

// based on Mojang's helper
public class ScreenShotHelper {
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
    private static IntBuffer intBuff;
    private static int[] arrayBuff;

    public static String saveScreenShot(File path, int left, int top, int width, int height) {
        return saveScreenShot(path, null, left, top, width, height);
    }

    public static String saveScreenShot(File path, String fileName, int left, int top, int width, int height) {
        try {
            File screenshotDir = new File(path, "monnefCoreExporter");
            screenshotDir.mkdir();
            int pixelsCount = width * height;

            if (intBuff == null || intBuff.capacity() < pixelsCount) {
                intBuff = BufferUtils.createIntBuffer(pixelsCount);
                arrayBuff = new int[pixelsCount];
            }

            GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
            intBuff.clear();
            GL11.glReadPixels(left, top, width, height, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, intBuff);
            intBuff.get(arrayBuff);
            func_74289_a(arrayBuff, width, height);
            BufferedImage bufferedimage = new BufferedImage(width, height, 1);
            bufferedimage.setRGB(0, 0, width, height, arrayBuff, 0, width);
            File outFile;

            if (fileName == null) {
                outFile = generateName(screenshotDir);
            } else {
                outFile = new File(screenshotDir, fileName);
            }

            ImageIO.write(bufferedimage, "png", outFile);
            return "Saved screenshot as " + outFile.getName();
        } catch (Exception exception) {
            exception.printStackTrace();
            return "Failed to save: " + exception;
        }
    }

    private static File generateName(File par0File) {
        String s = dateFormat.format(new Date());
        int counter = 1;

        while (true) {
            File newFile = new File(par0File, s + (counter == 1 ? "" : "_" + counter) + ".png");

            if (!newFile.exists()) {
                return newFile;
            }

            counter++;
        }
    }

    private static void func_74289_a(int[] par0ArrayOfInteger, int par1, int par2) {
        int[] aint1 = new int[par1];
        int k = par2 / 2;

        for (int l = 0; l < k; ++l) {
            System.arraycopy(par0ArrayOfInteger, l * par1, aint1, 0, par1);
            System.arraycopy(par0ArrayOfInteger, (par2 - 1 - l) * par1, par0ArrayOfInteger, l * par1, par1);
            System.arraycopy(aint1, 0, par0ArrayOfInteger, (par2 - 1 - l) * par1, par1);
        }
    }
}
