package com.skcraft.playblock.projector;

import com.skcraft.playblock.media.MediaResolver;
import com.skcraft.playblock.player.MediaPlayer;
import com.skcraft.playblock.util.DoubleThresholdRange;
import com.skcraft.playblock.util.StringUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

/**
 * The GUI for the projector.
 */
@SideOnly(Side.CLIENT)
public class GuiProjector extends GuiScreen {

    public static final int ID = 0;
    private static final int defaultTextColor = 14737632; // Hardcoded, from the
                                                          // text box;
    private static final int xSize = 247;
    private static final int ySize = 165;

    private TileEntityProjector tile;
    private GuiTextField uriField, heightField, widthField, triggerRangeField, fadeRangeField;
    private GuiButton applyButton;
    private GuiButton clearUriButton;
    private GuiButton toggleQueueButton;

    private float projectorWidth, projectorHeight, triggerRange, fadeRange;
    private String uri;

    public GuiProjector(TileEntityProjector tileEntity) {
        tile = tileEntity;
        MediaPlayer mediaPlayer = tileEntity.getMediaPlayer();
        DoubleThresholdRange range = tileEntity.getRange();
        uri = mediaPlayer.getUri();
        projectorWidth = mediaPlayer.getWidth();
        projectorHeight = mediaPlayer.getHeight();
        triggerRange = range.getTriggerRange();
        fadeRange = range.getFadeRange();
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @Override
    public void initGui() {
        this.buttonList.clear();
        Keyboard.enableRepeatEvents(true);
        int left = (width - xSize) / 2;
        int top = (height - ySize) / 2;

        this.buttonList.add(applyButton = new GuiButton(0, left + 160, top + 125, 80, 20, StringUtils.translate("gui.done")));
        this.buttonList.add(clearUriButton = new GuiButton(1, left + 220, top + 14, 17, 20, "X"));
        this.buttonList.add(toggleQueueButton = new GuiButton(2, left + 10, top + 100, 80, 20, this.getQueueButtonString()));
        
        uriField = new GuiTextField(2, this.fontRenderer, left + 60, top + 17, 157, this.fontRenderer.FONT_HEIGHT + 5);
        initTextField(uriField, 100, uri);

        heightField = new GuiTextField(3, this.fontRenderer, left + 130, top + 37, 50, this.fontRenderer.FONT_HEIGHT + 5);
        initTextField(heightField, 10, Float.toString(projectorHeight));

        widthField = new GuiTextField(4, this.fontRenderer, left + 60, top + 37, 50, this.fontRenderer.FONT_HEIGHT + 5);
        initTextField(widthField, 10, Float.toString(projectorWidth));

        triggerRangeField = new GuiTextField(5, this.fontRenderer, left + 60, top + 57, 50, this.fontRenderer.FONT_HEIGHT + 5);
        initTextField(triggerRangeField, 10, Float.toString(triggerRange));

        fadeRangeField = new GuiTextField(6, this.fontRenderer, left + 60, top + 77, 50, this.fontRenderer.FONT_HEIGHT + 5);
        initTextField(fadeRangeField, 10, Float.toString(fadeRange));
    }

    /**
     * Prepare a text field for entry.
     * 
     * @param field
     *            the field
     * @param length
     *            the maximum length of the string
     * @param text
     *            the initial text
     */
    private void initTextField(GuiTextField field, int length, String text) {
        field.setVisible(true);
        field.setMaxStringLength(length);
        field.setEnableBackgroundDrawing(true);
        field.setCanLoseFocus(true);
        field.setFocused(false);
        field.setText(text);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void actionPerformed(GuiButton button) {
        if (button.id == applyButton.id) {
            tile.getOptions().sendUpdate(uri, projectorWidth, projectorHeight, triggerRange, fadeRange);

            this.mc.displayGuiScreen((GuiScreen) null);
            this.mc.setIngameFocus();
        } else if (button.id == clearUriButton.id) {
            uriField.setText("");
            uriField.setFocused(true);
            uri = uriField.getText();
        } else if (button.id == toggleQueueButton.id) {
            MediaPlayer player = tile.getMediaPlayer();

            boolean state = player.inQueueMode();
            player.setQueueMode(!state);

            toggleQueueButton.displayString = this.getQueueButtonString();
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float par3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(new ResourceLocation("playblock:textures/gui/projector_bg.png"));
        int left = (width - xSize) / 2;
        int top = (height - ySize) / 2;
        drawTexturedModalRect(left, top, 0, 0, xSize, ySize);
        uriField.drawTextBox();
        heightField.drawTextBox();
        widthField.drawTextBox();
        triggerRangeField.drawTextBox();
        fadeRangeField.drawTextBox();

        fontRenderer.drawString(StringUtils.translate("options.url"), left + 10, top + 20, 0xff999999);
        fontRenderer.drawString(StringUtils.translate("options.screenSize"), left + 10, top + 40, 0xff999999);
        fontRenderer.drawString("x", left + 117, top + 40, 0xff999999);
        fontRenderer.drawString(StringUtils.translate("options.turnOn"), left + 10, top + 60, 0xff999999);
        fontRenderer.drawString(StringUtils.translate("options.blocksAway"), left + 117, top + 60, 0xff999999);
        fontRenderer.drawString(StringUtils.translate("options.turnOff"), left + 10, top + 80, 0xff999999);
        fontRenderer.drawString(StringUtils.translate("options.blocksAway"), left + 117, top + 80, 0xff999999);
        fontRenderer.drawString("TEST VERSION - skcraft.com", left + 10, top + 132, 0xffffffff);

        super.drawScreen(mouseX, mouseY, par3);
    }

    @Override
    protected void mouseClicked(int x, int y, int buttonClicked) throws IOException {
        super.mouseClicked(x, y, buttonClicked);
        uriField.mouseClicked(x, y, buttonClicked);
        heightField.mouseClicked(x, y, buttonClicked);
        widthField.mouseClicked(x, y, buttonClicked);
        triggerRangeField.mouseClicked(x, y, buttonClicked);
        fadeRangeField.mouseClicked(x, y, buttonClicked);
    }

    @Override
    protected void keyTyped(char key, int par2) throws IOException {
        super.keyTyped(key, par2);

        if (uriField.isFocused()) {
            uriField.textboxKeyTyped(key, par2);
            uri = uriField.getText();

            if (MediaResolver.canPlayUri(MediaResolver.cleanUri(uri))) {
                uriField.setTextColor(defaultTextColor);
            } else {
                uriField.setTextColor(0xffff0000);
            }
        }

        if (Character.isDigit(key) || par2 == 14 || par2 == 52 || par2 == 199 || par2 == 203 || par2 == 205 || par2 == 207 || par2 == 211) {
            if (heightField.isFocused()) {
                heightField.textboxKeyTyped(key, par2);
                if (heightField.getText().length() != 0) {
                    try {
                        projectorHeight = Float.parseFloat(heightField.getText());
                    } catch (NumberFormatException e) {
                    }
                }
            } else if (widthField.isFocused()) {
                widthField.textboxKeyTyped(key, par2);
                if (widthField.getText().length() != 0) {
                    try {
                        projectorWidth = Float.parseFloat(widthField.getText());
                    } catch (NumberFormatException e) {
                    }
                }
            } else if (triggerRangeField.isFocused()) {
                triggerRangeField.textboxKeyTyped(key, par2);
                if (triggerRangeField.getText().length() != 0) {
                    try {
                        triggerRange = Float.parseFloat(triggerRangeField.getText());
                    } catch (NumberFormatException e) {
                    }
                }
            } else if (fadeRangeField.isFocused()) {
                fadeRangeField.textboxKeyTyped(key, par2);
                if (fadeRangeField.getText().length() != 0) {
                    try {
                        fadeRange = Float.parseFloat(fadeRangeField.getText());
                    } catch (NumberFormatException e) {
                    }
                }
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public String getQueueButtonString() {
        String key = this.tile.getMediaPlayer().inQueueMode() ? "on" : "off";

        return StringUtils.translate("gui.queue." + key);
    }
}
