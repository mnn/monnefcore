/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.event;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.world.World;

@Event.HasResult
public class LightningGeneratedEvent extends Event {
    public final int x;
    public final int y;
    public final int z;
    public final World world;

    public LightningGeneratedEvent(Object world, int x, int y, int z) {
        super();
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = (World) world;
    }
}
