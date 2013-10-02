/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.client;

import cpw.mods.fml.client.FMLClientHandler;
import monnef.core.common.CommonProxy;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {
    @Override
    public void registerClientStuff() {
        CustomCloaksHandler handler = new CustomCloaksHandler();
        MinecraftForge.EVENT_BUS.register(handler);
    }

    @Override
    public World getClientWorld() {
        return FMLClientHandler.instance().getClient().theWorld;
    }
}
