/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.client;

import monnef.core.utils.ColorHelper;
import net.minecraftforge.client.model.obj.WavefrontObject;

public interface IModelObj {
    void bindTexture();

    void render(float scale);

    void renderWithTexture(float scale);

    void renderPossiblyWithTexture(float scale);

    WavefrontObject getModel();

    void renderWithTint(float scale, ColorHelper.IntColor tint);

    void renderWithTextureAndTint(float scale, ColorHelper.IntColor tint);
}
