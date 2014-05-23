/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.common;

import net.minecraft.village.MerchantRecipe;

public interface IMerchantRecipeListWrapper {
    void addToListWithCheck(MerchantRecipe recipe);
}
