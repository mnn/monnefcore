/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.api;

import net.minecraft.block.Block;

public interface IItemBlock {
    void setSubNames(String[] newNames);

    Block getBlock();
}
