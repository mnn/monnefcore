/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.utils;

import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderSpider;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.Field;

public class RenderUtils {
    public static void renderStaticTileEntityInWorld(IBlockAccess world, int x, int y, int z, RenderBlocks blocksRenderer, TileEntity tile, TileEntitySpecialRenderer tileRenderer) {
        GL11.glPushMatrix();

        // code based on TileEntityRenderer
        int var3 = world.getLightBrightnessForSkyBlocks(x, y, z, 0);
        int var4 = var3 % 65536;
        int var5 = var3 / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) var4 / 1.0F, (float) var5 / 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        renderTileEntity(x & 15, y & 15, z & 15, tile, tileRenderer);

        GL11.glPopMatrix();
    }

    public static void renderTileEntity(double x, double y, double z, TileEntity tile, TileEntitySpecialRenderer tileRenderer) {
        tileRenderer.renderTileEntityAt(tile, x, y, z, 0f);
    }

    public static void RenderStaticTileEntityOld(IBlockAccess world, int x, int y, int z, RenderBlocks blocksRenderer, TileEntity tile) {
        GL11.glPushMatrix();

        // code based on TileEntityRenderer
        int var3 = world.getLightBrightnessForSkyBlocks(x, y, z, 0);
        int var4 = var3 % 65536;
        int var5 = var3 / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) var4 / 1.0F, (float) var5 / 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        TileEntitySpecialRenderer renderer = TileEntityRendererDispatcher.instance.getSpecialRenderer(tile);
        renderer.renderTileEntityAt(tile, x & 15, y & 15, z & 15, 0f);

        GL11.glPopMatrix();
    }

    public static void setShadowSizeInRenderer(RenderSpider renderer, float shadowSize) {
        Field f = ReflectionHelper.findField(Render.class, "shadowSize", "field_76989_e");
        try {
            f.setFloat(renderer, shadowSize);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void rotate(ForgeDirection rotation) {
        switch (rotation) {
            case NORTH:
                break;

            case SOUTH:
                GL11.glRotatef(180, 0, 1, 0);
                break;

            case WEST:
                GL11.glRotatef(90, 0, 1, 0);
                break;

            case EAST:
                GL11.glRotatef(-90, 0, 1, 0);
                break;

            case UP:
                GL11.glRotatef(-90, 1, 0, 0);
                break;

            case DOWN:
                GL11.glRotatef(90, 1, 0, 0);
                break;
        }
    }

    public static void glRotateAroundPoint(float amount, float cx, float cy, float cz, float px, float py, float pz) {
        GL11.glTranslatef(px, py, pz);
        GL11.glRotatef(amount, cx, cy, cz);
        GL11.glTranslatef(-px, -py, -pz);
    }

    public static void glRenderLine(float red, float green, float blue, float width, float x1, float y1, float z1, float x2, float y2, float z2) {
        GL11.glColor3f(red, green, blue);
        GL11.glLineWidth(width);
        GL11.glBegin(GL11.GL_LINE_STRIP);
        GL11.glVertex3f(x1, y1, z1);
        GL11.glVertex3f(x2, y2, z2);
        GL11.glEnd();
    }
}
