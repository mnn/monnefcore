package monnef.core.utils

import net.minecraft.entity.player.EntityPlayerMP
import cpw.mods.fml.common.FMLCommonHandler

object ServerUtils {
  def getPlayerForUsername(name: String): EntityPlayerMP = FMLCommonHandler.instance().getMinecraftServerInstance.getConfigurationManager.func_152612_a(name)
}
