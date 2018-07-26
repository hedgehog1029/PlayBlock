package com.skcraft.playblock;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Written by @offbeatwitch.
 * Licensed under MIT.
 */
public class SharedRegistry {
	@SubscribeEvent
	public void onBlockRegister(RegistryEvent.Register<Block> event) {
		event.getRegistry().register(SharedRuntime.blockProjector);
	}

	@SubscribeEvent
	public void onItemRegister(RegistryEvent.Register<Item> event) {
		event.getRegistry().register(new ItemBlock(SharedRuntime.blockProjector));
		event.getRegistry().register(SharedRuntime.itemRemote);
	}
}
