/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.event;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class EventFactory {
    public static boolean onLightningGenerated(World world, int x, int y, int z) {
        LightningGeneratedEvent event = new LightningGeneratedEvent(world, x, y, z);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getResult() != Event.Result.DENY;
    }
}
