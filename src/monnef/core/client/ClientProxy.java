/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.client;

import cpw.mods.fml.common.registry.GameRegistry;
import monnef.core.common.CommonProxy;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {
    @Override
    public void registerClientStuff() {
        CustomCloaksHandler handler = new CustomCloaksHandler();
        MinecraftForge.EVENT_BUS.register(handler);
    }
}
