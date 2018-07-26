package com.skcraft.playblock.projector;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

/**
 * Written by @offbeatwitch.
 * Licensed under MIT.
 */
public class ModItemBlock extends ItemBlock {
    public ModItemBlock(Block block) {
        super(block);
        setRegistryName(block.getRegistryName());
    }
}
