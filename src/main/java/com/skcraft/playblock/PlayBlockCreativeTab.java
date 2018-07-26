package com.skcraft.playblock;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class PlayBlockCreativeTab extends CreativeTabs {

    public static final PlayBlockCreativeTab tab = new PlayBlockCreativeTab();

    public PlayBlockCreativeTab() {
        super("tabPlayBlock");
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(SharedRuntime.itemRemote);
    }
}
