package monnef.core.utils

import net.minecraft.util.DamageSource
import net.minecraftforge.event.entity.living.{LivingDeathEvent, LivingEvent, LivingHurtEvent}
import net.minecraft.item.Item
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer

object DamageSourceHelper {
  val DamageSourcePlayer = "player"

  def sourceIsPlayer(source: DamageSource): Boolean = {
    source.damageType == DamageSourcePlayer
  }

  def handlePlayerHurtingMobWithItem[T <: LivingEvent](event: LivingHurtAndDeathEventWrapper[T], item: Item, isValidMob: EntityLivingBase => Boolean, onValidMob: (EntityPlayer, EntityLivingBase, T) => Unit): Boolean = {
    val source = event.source
    val mob = event.entityLiving

    if (DamageSourceHelper.sourceIsPlayer(source)) {
      val player: EntityPlayer = source.getEntity.asInstanceOf[EntityPlayer]
      if (PlayerHelper.playerHasEquipped(player, item)) {
        if (isValidMob(mob)) {
          onValidMob(player, mob, event.event)
          true
        } else false
      } else false
    } else false
  }

  def handleDamageInflictedByToolAndPlayer(event: LivingHurtEvent, item: Item, damage: Int, isValidMob: EntityLivingBase => Boolean, onValidMobPost: (EntityPlayer, EntityLivingBase, LivingHurtEvent) => Unit): Boolean = {
    handlePlayerHurtingMobWithItem(new LivingHurtAndDeathEventWrapper(event),
      item,
      isValidMob,
      (player, mob, event: LivingHurtEvent) => {
        event.ammount += damage
        PlayerHelper.damageCurrentItem(player)
        onValidMobPost(player, mob, event)
      }
    )
  }

  class LivingHurtAndDeathEventWrapper[T <: LivingEvent](val event: T) {
    if (!event.isInstanceOf[LivingHurtEvent] && !event.isInstanceOf[LivingDeathEvent]) throw new RuntimeException("Bad event class")

    val source = event match {
      case d: LivingDeathEvent => d.source
      case h: LivingHurtEvent => h.source
    }

    val entityLiving = event.entityLiving
  }


}
