/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.common;

import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

public class MerchantRecipeListWrapper implements IMerchantRecipeListWrapper {
    private MerchantRecipeList list;

    public MerchantRecipeListWrapper(MerchantRecipeList list) {
        this.list = list;
    }

    @Override
    public void addToListWithCheck(MerchantRecipe recipe) {
        list.addToListWithCheck(recipe);
    }
}
