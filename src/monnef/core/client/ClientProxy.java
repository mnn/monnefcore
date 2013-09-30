/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.client;

import monnef.core.common.CommonProxy;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {
    @Override
    public void registerClientStuff() {
        MinecraftForge.EVENT_BUS.register(new CustomCloaksHandler());
    }
}
