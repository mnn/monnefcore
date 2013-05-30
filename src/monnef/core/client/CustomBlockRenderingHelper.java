/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.client;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;

public class CustomBlockRenderingHelper {

    // heavily based on RenderBlocks
    public static void Render(Block block, int meta, float colorMultiplier, RenderBlocks renderer) {
        boolean isGrass = block.blockID == Block.grass.blockID;
        Tessellator tess = Tessellator.instance;

        block.setBlockBoundsForItemRender();
        renderer.setRenderBoundsFromBlock(block);
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        tess.startDrawingQuads();
        tess.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, meta));
        tess.draw();

        if (isGrass && renderer.useInventoryTint) {
            int color = block.getRenderColor(meta);
            float red = (float) (color >> 16 & 255) / 255.0F;
            float green = (float) (color >> 8 & 255) / 255.0F;
            float blue = (float) (color & 255) / 255.0F;
            GL11.glColor4f(red * colorMultiplier, green * colorMultiplier, blue * colorMultiplier, 1.0F);
        }

        tess.startDrawingQuads();
        tess.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 1, meta));
        tess.draw();

        if (isGrass && renderer.useInventoryTint) {
            GL11.glColor4f(colorMultiplier, colorMultiplier, colorMultiplier, 1.0F);
        }

        tess.startDrawingQuads();
        tess.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 2, meta));
        tess.draw();
        tess.startDrawingQuads();
        tess.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 3, meta));
        tess.draw();
        tess.startDrawingQuads();
        tess.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 4, meta));
        tess.draw();
        tess.startDrawingQuads();
        tess.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 5, meta));
        tess.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }

    public static void doRendering(RenderBlocks renderer, Block block) {
        doRendering(renderer, block, 0, 0, 0, true, 0, 1);
    }

    public static void doRendering(RenderBlocks renderer, Block block, int x, int y, int z) {
        doRendering(renderer, block, x, y, z, false, 0, 1);
    }

    public static void doRendering(RenderBlocks renderer, Block block, int x, int y, int z, boolean renderingInventory, int metaForInventory, int coloringForInventory) {
        if (renderingInventory) {
            CustomBlockRenderingHelper.Render(block, metaForInventory, coloringForInventory, renderer);
        } else {
            renderer.renderStandardBlock(block, x, y, z);
        }
    }
}
