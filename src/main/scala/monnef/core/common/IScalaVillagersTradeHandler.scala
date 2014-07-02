package monnef.core.common

import net.minecraft.entity.passive.EntityVillager
import java.util.Random

trait IScalaVillagersTradeHandler {
  def manipulateTradesForVillager(villager: EntityVillager, recipeList: IMerchantRecipeListWrapper, random: Random)
}
