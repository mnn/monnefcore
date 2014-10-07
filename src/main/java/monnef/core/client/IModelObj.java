/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.client;

import monnef.core.utils.ColorHelper;
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
}
