/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.client;

import monnef.core.utils.ColorHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.obj.WavefrontObject;

public interface IModelObj {
    void bindTexture();

    void render();

    void renderWithTexture();

    void renderPossiblyWithTexture();

    WavefrontObject getModel();

    void renderWithTint(ColorHelper.IntColor tint);

    void renderWithTextureAndTint(ColorHelper.IntColor tint);

    void setScale(float scale);

    float getScale();

    ResourceLocation getTexture();

    void setRotationPoint(float x, float y, float z);

    void setRotation(float x, float y, float z);
}
