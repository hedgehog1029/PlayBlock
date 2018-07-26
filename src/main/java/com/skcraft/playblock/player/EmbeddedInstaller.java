package com.skcraft.playblock.player;

import com.sk89q.task.ProgressListener;
import com.skcraft.playblock.installer.PlayBlockSetup;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.awt.*;

/**
 * Manages installation of libraries from within the game.
 */
public class EmbeddedInstaller implements ProgressListener {

    public static enum State {
        NOT_INSTALLING, INSTALLING, ERROR, RESTART_NEEDED
    };

    private PlayBlockSetup installer;

    private State state = State.NOT_INSTALLING;
    private double lastProgress;
    private String lastMessage = "";

    EmbeddedInstaller() {
    }

    /**
     * Start the installation if an installation already isn't going.
     */
    public void start() {
        if (state == State.RESTART_NEEDED) {
            this.sendMessage("PlayBlock: Please restart your game.");
        } else if (state == State.NOT_INSTALLING || state == State.ERROR) {
            installer = PlayBlockSetup.startEmbedded(this);
            state = State.INSTALLING;

            this.sendMessage("PlayBlock: Now installing the necessary files! Please switch to the installer window outside the game for more information.");
        } else if (installer != null) {
            installer.setState(Frame.NORMAL);
            installer.setVisible(true);
            installer.setLocationRelativeTo(null);

            this.sendMessage("PlayBlock: Please switch to the installer window outside the game for more information.");
        }
    }

    /**
     * Get the state of the installer.
     * 
     * @return state
     */
    public State getState() {
        return state;
    }

    /**
     * Get the message shown on the screen.
     * 
     * @return the message
     */
    public String getStatusMessage() {
        switch (state) {
        case NOT_INSTALLING:
            return "Not installing...";
        case INSTALLING:
            return String.format("%.1f%% installed...", lastProgress * 100);
        case ERROR:
            return "Error during install!";
        case RESTART_NEEDED:
            return "Please restart your game.";
        }

        throw new RuntimeException("Missing state message");
    }

    @Override
    public void progressChange(double progress) {
        this.lastProgress = progress;
    }

    @Override
    public void statusChange(String message) {
        this.lastMessage = message;
    }

    @Override
    public void complete() {
        state = State.RESTART_NEEDED;
        installer = null;

        this.sendMessage("PlayBlock: Please now restart your game so that the installed files can be loaded.");
    }

    @Override
    public void aborted() {
        state = State.NOT_INSTALLING;
        installer = null;

        this.sendMessage("PlayBlock: Installation aborted.");
    }

    @Override
    public void error(Throwable exception) {
        state = State.ERROR;
        installer = null;

        this.sendMessage("PlayBlock: An error occurred during installation. " + "Please switch to the installer window outside the game for more information.");
    }

    public void sendMessage(String text) {
        Minecraft.getMinecraft().player.sendMessage(new TextComponentString(text)
                .setStyle(new Style().setColor(TextFormatting.GOLD)));
    }
}
