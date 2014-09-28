/*
 * Jaffas and more!
 * author: monnef
 */

package monnef.core.api;

import net.minecraft.block.Block;

public interface IItemBlock {
    void setSubNames(String[] newNames);

    Block getBlock();

    // Used when Item implements IItemBlock but does not want special ItemBlock handling
    // (like tree seeds - they wound otherwise override tree saplings).
    boolean suppressSpecialItemBlockHandling();
}
