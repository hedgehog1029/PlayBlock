package com.skcraft.playblock.util;

import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides a very simple way to check who may have access to a block,
 * depending on right-click mechanics of the block being clicked.
 *
 * I'm leaving the original text here for posterity because it's too funny
 *
 * Privates a very simple way to see who may have access to a block, depending
 * on the right click mechanic on blocks being blocked.
 */
public class AccessList {

    private static final int MAX_ACCESS_TIME = 1000 * 60 * 15;

    private Map<String, Long> allowed = new HashMap<String, Long>();

    public void allow(String name) {
        long now = System.currentTimeMillis();

        // Removed old entries
        allowed.entrySet().removeIf(entry -> now - entry.getValue() > MAX_ACCESS_TIME);

        allowed.put(name, now);
    }

    public void allow(EntityPlayer player) {
        allow(player.getName());
    }

    public boolean checkAndForget(String name) {
        long now = System.currentTimeMillis();
        Long since = allowed.remove(name);

        if (since == null) {
            return false;
        }

        if (now - since > MAX_ACCESS_TIME) {
            return false;
        }

        return true;
    }

    public boolean checkAndForget(EntityPlayer player) {
        return checkAndForget(player.getName());
    }

}
