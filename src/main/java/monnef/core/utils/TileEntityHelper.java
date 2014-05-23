/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.utils;

import net.minecraft.tileentity.TileEntity;

public class TileEntityHelper {
    public static String getFormattedCoordinates(TileEntity tile) {
        StringBuilder s = new StringBuilder();
        s.append(tile.xCoord);
        s.append("x");
        s.append(tile.yCoord);
        s.append("x");
        s.append(tile.zCoord);
        return s.toString();
    }
}
