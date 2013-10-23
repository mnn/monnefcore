/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.common;

import monnef.core.MonnefCorePlugin;
import net.minecraft.world.World;

public class CommonProxy {
    public void registerClientStuff() {
        MonnefCorePlugin.Log.printDebug("rCS on server");
    }

    public World getClientWorld() {
        return null;
    }

    public void registerContainers() {
        ContainerRegistry.fillRegistrationsFromAnnotations(false);
    }

    public int getCommonRarity() {
        return 0;
    }

    public int getEpicRarity() {
        return 0;
    }

    public int getUncommonRarity() {
        return 0;
    }
}
