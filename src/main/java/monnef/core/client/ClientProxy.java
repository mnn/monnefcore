/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.client;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import monnef.core.common.CommonProxy;
import monnef.core.common.ContainerRegistry;
import monnef.core.mod.MonnefCoreNormalMod;
import net.minecraft.item.EnumRarity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {
    @Override
    public void registerClientStuff() {
        CustomCloaksHandler handler = new CustomCloaksHandler();
        MinecraftForge.EVENT_BUS.register(handler);
        MonnefCoreNormalMod.renderID = RenderingRegistry.getNextAvailableRenderId();

        MinecraftForge.EVENT_BUS.register(new MonnefCoreKeyHandler());
    }

    @Override
    public World getClientWorld() {
        return FMLClientHandler.instance().getClient().theWorld;
    }

    @Override
    public void registerContainers() {
        super.registerContainers();
        ContainerRegistry.fillRegistrationsFromAnnotations(true);
    }

    @Override
    public int getCommonRarity() {
        return EnumRarity.common.ordinal();
    }

    @Override
    public int getEpicRarity() {
        return EnumRarity.epic.ordinal();
    }

    @Override
    public int getUncommonRarity() {
        return EnumRarity.uncommon.ordinal();
    }
}
