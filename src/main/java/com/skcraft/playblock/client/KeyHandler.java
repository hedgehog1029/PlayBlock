package com.skcraft.playblock.client;

import com.skcraft.playblock.GuiHandler;
import com.skcraft.playblock.PlayBlock;
import com.skcraft.playblock.util.StringUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import org.lwjgl.input.Keyboard;

/**
 * Handles key presses.
 */
public class KeyHandler {

    private static Minecraft mc = Minecraft.getMinecraft();
    private static KeyBinding keyOptions = new KeyBinding(StringUtils.translate("options"), Keyboard.KEY_F4, "PlayBlock");

    public KeyHandler() {
        ClientRegistry.registerKeyBinding(keyOptions);
    }

    @SubscribeEvent
    public void onKey(KeyInputEvent evt) {
        if (keyOptions.isPressed() && mc.player != null && mc.currentScreen == null) {
            mc.player.openGui(PlayBlock.instance, GuiHandler.OPTIONS, mc.world, 0, 0, 0);
        }
    }

}
