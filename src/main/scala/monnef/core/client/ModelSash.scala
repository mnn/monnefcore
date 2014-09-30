package monnef.core.client

import net.minecraft.client.model.{ModelRenderer, ModelBase}

class ModelSash(outerBorderSizer: Float = 0, headOffset: Float = 0, textureWidth: Int = 64, textureHeight: Int = 32) extends ModelBase {
  var bipedBody = new ModelRenderer(this, 16, 16)
  bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, outerBorderSizer)
  bipedBody.setRotationPoint(0.0F, 0.0F + headOffset, 0.0F)
}
