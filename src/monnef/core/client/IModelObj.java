/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.client;

import net.minecraftforge.client.model.obj.WavefrontObject;

public interface IModelObj {
    void bindTexture();

    void render(float scale);

    void renderWithTexture(float scale);

    void renderPossiblyWithTexture(float scale);

    WavefrontObject getModel();
}
