package com.skcraft.playblock.client;

import com.skcraft.playblock.SharedRuntime;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Written by @offbeatwitch.
 * Licensed under MIT.
 */
public class ClientModelRegistry {
    @SubscribeEvent
    public void onModelRegister(ModelRegistryEvent event) {
        registerItemModel(SharedRuntime.itemRemote);
        registerItemModel(SharedRuntime.itemProjector);
    }

    public void registerItemModel(Item item) {
        ModelResourceLocation modelResource = new ModelResourceLocation(item.getRegistryName(), "inventory");

        ModelLoader.setCustomModelResourceLocation(item, 0, modelResource);
    }
}
