/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.client;

import cpw.mods.fml.client.FMLClientHandler;
import monnef.core.utils.ColorHelper;
import monnef.core.utils.VectorUtils;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.client.model.obj.WavefrontObject;
import org.lwjgl.opengl.GL11;

public class ModelObj extends ModelBase implements IModelObj {
    private final WavefrontObject model;
    private float rotationYFix;
    private String texture;
    private ResourceLocation textureResource;
    private float scale;
    private Vec3 rotationPoint, rotation;
    private boolean rotationPointEnabled;

    public ModelObj(String resourceName, float rotationYFix, String texture, float defaultScale) {
        this.rotationYFix = rotationYFix;
        this.texture = texture;
        this.scale = defaultScale;
        this.textureResource = new ResourceLocation(texture);
        IModelCustom tmp = AdvancedModelLoader.loadModel(new ResourceLocation(resourceName));
        if (!(tmp instanceof WavefrontObject))
            throw new RuntimeException("Loaded model is not " + WavefrontObject.class.getSimpleName() + ", cannot continue (Forge probably changed implementation).");
        model = (WavefrontObject) tmp;
    }

    @Override
    public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
    }

    @Override
    public void bindTexture() {
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(textureResource);
    }

    @Override
    public void render() {
        renderWithTint(null);
    }

    @Override
    public void renderWithTint(ColorHelper.IntColor tint) {
        GL11.glPushMatrix();
        GL11.glScalef(scale, scale, scale);
        GL11.glRotatef(rotationYFix, 0, 1, 0);
        if (tint != null) {
            GL11.glColor4f(tint.getFloatRed(), tint.getFloatGreen(), tint.getFloatBlue(), 1f);
        }
        if (rotationPointEnabled) {
            GL11.glPushMatrix();
            GL11.glTranslated(-rotationPoint.xCoord, -rotationPoint.yCoord, -rotationPoint.zCoord);
            GL11.glRotated(rotation.xCoord, 1, 0, 0);
            GL11.glRotated(rotation.yCoord, 0, 1, 0);
            GL11.glRotated(rotation.zCoord, 0, 0, 1);
            GL11.glTranslated(rotationPoint.xCoord, rotationPoint.yCoord, rotationPoint.zCoord);
        }
        model.renderAll();
        if (rotationPointEnabled) GL11.glPopMatrix();
        GL11.glPopMatrix();
    }

    @Override
    public void renderWithTexture() {
        renderWithTextureAndTint(null);
    }

    @Override
    public void renderWithTextureAndTint(ColorHelper.IntColor tint) {
        bindTexture();
        renderWithTint(tint);
    }

    @Override
    public void renderPossiblyWithTexture() {
        if (texture != null) renderWithTexture();
        else render();
    }

    @Override
    public WavefrontObject getModel() {
        return model;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    @Override
    public ResourceLocation getTexture() {
        return textureResource;
    }

    @Override
    public void setRotationPoint(float x, float y, float z) {
        rotationPoint = VectorUtils.createVector(x, y, z);
        rotationPointEnabled = true;
    }

    @Override
    public void setRotation(float x, float y, float z) {
        rotation = VectorUtils.createVector(x, y, z);
    }
}
