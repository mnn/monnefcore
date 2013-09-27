/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.client;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderItemInAir extends Render {
    private Icon icon;
    private final Item item;

    public RenderItemInAir(Item item) {
        super();
        this.item = item;
    }

    public void tryLoadIcon() {
        icon = item.getIconFromDamage(0);
    }

    @Override
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
        if (icon == null) tryLoadIcon();

        GL11.glPushMatrix();

        GL11.glTranslatef((float) par2, (float) par4, (float) par6);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        Tessellator var10 = Tessellator.instance;

        //texture.bindTexture(0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 1); // 1 for items

        this.doRender(var10);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);

        GL11.glPopMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return null;
    }

    private void doRender(Tessellator par1Tessellator) {
        float f = icon.getMinU();
        float f1 = icon.getMaxU();
        float f2 = icon.getMinV();
        float f3 = icon.getMaxV();
        float f4 = 1.0F;
        float f5 = 0.5F;
        float f6 = 0.25F;
        GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        par1Tessellator.startDrawingQuads();
        par1Tessellator.setNormal(0.0F, 1.0F, 0.0F);
        par1Tessellator.addVertexWithUV((double) (0.0F - f5), (double) (0.0F - f6), 0.0D, (double) f, (double) f3);
        par1Tessellator.addVertexWithUV((double) (f4 - f5), (double) (0.0F - f6), 0.0D, (double) f1, (double) f3);
        par1Tessellator.addVertexWithUV((double) (f4 - f5), (double) (f4 - f6), 0.0D, (double) f1, (double) f2);
        par1Tessellator.addVertexWithUV((double) (0.0F - f5), (double) (f4 - f6), 0.0D, (double) f, (double) f2);
        par1Tessellator.draw();
    }
}