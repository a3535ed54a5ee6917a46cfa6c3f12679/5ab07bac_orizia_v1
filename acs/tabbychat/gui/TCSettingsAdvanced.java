package acs.tabbychat.gui;

import acs.tabbychat.core.TabbyChat;
import acs.tabbychat.settings.TCSettingBool;
import acs.tabbychat.settings.TCSettingSlider;
import acs.tabbychat.settings.TCSettingTextBox;
import acs.tabbychat.util.TabbyChatUtils;
import java.io.File;
import java.util.Properties;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;

public class TCSettingsAdvanced extends TCSettingsGUI
{
    private static final int CHAT_SCROLL_HISTORY_ID = 9401;
    private static final int MAXLENGTH_CHANNEL_NAME_ID = 9402;
    private static final int MULTICHAT_DELAY_ID = 9403;
    private static final int CHATBOX_UNFOC_HEIGHT_ID = 9406;
    private static final int CHAT_FADE_TICKS_ID = 9408;
    private static final int TEXT_IGNORE_OPACITY_ID = 9410;
    private static final int CONVERT_UNICODE_TEXT_ID = 9411;
    public TCSettingTextBox chatScrollHistory;
    public TCSettingTextBox maxLengthChannelName;
    public TCSettingTextBox multiChatDelay;
    public TCSettingSlider chatBoxUnfocHeight;
    public TCSettingSlider chatFadeTicks;
    public TCSettingBool textIgnoreOpacity;
    public TCSettingBool convertUnicodeText;

    public TCSettingsAdvanced(TabbyChat _tc)
    {
        super(_tc);
        this.propertyPrefix = "settings.advanced";
        this.chatScrollHistory = new TCSettingTextBox("100", "chatScrollHistory", this.propertyPrefix, 9401);
        this.maxLengthChannelName = new TCSettingTextBox("10", "maxLengthChannelName", this.propertyPrefix, 9402);
        this.multiChatDelay = new TCSettingTextBox("500", "multiChatDelay", this.propertyPrefix, 9403);
        this.chatBoxUnfocHeight = new TCSettingSlider(Float.valueOf(50.0F), "chatBoxUnfocHeight", this.propertyPrefix, 9406, 20.0F, 100.0F);
        this.chatFadeTicks = new TCSettingSlider(Float.valueOf(200.0F), "chatFadeTicks", this.propertyPrefix, 9408, 10.0F, 2000.0F);
        this.textIgnoreOpacity = new TCSettingBool(Boolean.valueOf(false), "textignoreopacity", this.propertyPrefix, 9410);
        this.convertUnicodeText = new TCSettingBool(Boolean.valueOf(false), "convertunicodetext", this.propertyPrefix, 9411);
        this.name = I18n.format("settings.advanced.name", new Object[0]);
        this.settingsFile = new File(tabbyChatDir, "advanced.cfg");
        this.bgcolor = 1719676564;
        this.chatScrollHistory.setCharLimit(3);
        this.maxLengthChannelName.setCharLimit(2);
        this.multiChatDelay.setCharLimit(4);
        this.defineDrawableSettings();
    }

    public void defineDrawableSettings()
    {
        this.buttonList.add(this.chatScrollHistory);
        this.buttonList.add(this.maxLengthChannelName);
        this.buttonList.add(this.multiChatDelay);
        this.buttonList.add(this.chatBoxUnfocHeight);
        this.buttonList.add(this.chatFadeTicks);
        this.buttonList.add(this.textIgnoreOpacity);
        this.buttonList.add(this.convertUnicodeText);
    }

    public void initDrawableSettings()
    {
        int col1x = (this.width - 300) / 2 + 55;
        int buttonColor = (this.bgcolor & 16777215) + -16777216;
        this.chatScrollHistory.setLabelLoc(col1x);
        this.chatScrollHistory.setButtonLoc(col1x + 5 + this.mc.fontRenderer.getStringWidth(this.chatScrollHistory.description), this.rowY(1));
        this.chatScrollHistory.setButtonDims(30, 11);
        this.maxLengthChannelName.setLabelLoc(col1x);
        this.maxLengthChannelName.setButtonLoc(col1x + 5 + this.mc.fontRenderer.getStringWidth(this.maxLengthChannelName.description), this.rowY(2));
        this.maxLengthChannelName.setButtonDims(20, 11);
        this.multiChatDelay.setLabelLoc(col1x);
        this.multiChatDelay.setButtonLoc(col1x + 5 + this.mc.fontRenderer.getStringWidth(this.multiChatDelay.description), this.rowY(3));
        this.multiChatDelay.setButtonDims(40, 11);
        this.chatBoxUnfocHeight.setLabelLoc(col1x);
        this.chatBoxUnfocHeight.setButtonLoc(col1x + 5 + this.mc.fontRenderer.getStringWidth(this.chatBoxUnfocHeight.description), this.rowY(4));
        this.chatBoxUnfocHeight.buttonColor = buttonColor;
        this.chatFadeTicks.setLabelLoc(col1x);
        this.chatFadeTicks.setButtonLoc(col1x + 5 + this.mc.fontRenderer.getStringWidth(this.chatFadeTicks.description), this.rowY(5));
        this.chatFadeTicks.buttonColor = buttonColor;
        this.chatFadeTicks.units = "";
        this.textIgnoreOpacity.setButtonLoc(col1x, this.rowY(6));
        this.textIgnoreOpacity.setLabelLoc(col1x + 19);
        this.textIgnoreOpacity.buttonColor = buttonColor;
        this.convertUnicodeText.setButtonLoc(col1x, this.rowY(7));
        this.convertUnicodeText.setLabelLoc(col1x + 19);
        this.convertUnicodeText.buttonColor = buttonColor;
    }

    public Properties loadSettingsFile()
    {
        Properties result = super.loadSettingsFile();
        ChatBox.current.x = TabbyChatUtils.parseInteger(result.getProperty("chatbox.x"), ChatBox.absMinX, 10000, ChatBox.absMinX).intValue();
        ChatBox.current.y = TabbyChatUtils.parseInteger(result.getProperty("chatbox.y"), -10000, ChatBox.absMinY, ChatBox.absMinY).intValue();
        ChatBox.current.width = TabbyChatUtils.parseInteger(result.getProperty("chatbox.width"), ChatBox.absMinW, 10000, 320).intValue();
        ChatBox.current.height = TabbyChatUtils.parseInteger(result.getProperty("chatbox.height"), ChatBox.absMinH, 10000, 180).intValue();
        ChatBox.anchoredTop = Boolean.parseBoolean(result.getProperty("chatbox.anchoredtop"));
        ChatBox.pinned = Boolean.parseBoolean(result.getProperty("pinchatinterface"));
        return null;
    }

    public void saveSettingsFile()
    {
        Properties settingsTable = new Properties();
        settingsTable.put("chatbox.x", Integer.toString(ChatBox.current.x));
        settingsTable.put("chatbox.y", Integer.toString(ChatBox.current.y));
        settingsTable.put("chatbox.width", Integer.toString(ChatBox.current.width));
        settingsTable.put("chatbox.height", Integer.toString(ChatBox.current.height));
        settingsTable.put("chatbox.anchoredtop", Boolean.toString(ChatBox.anchoredTop));
        settingsTable.put("pinchatinterface", Boolean.toString(ChatBox.pinned));
        super.saveSettingsFile(settingsTable);
    }

    public void validateButtonStates()
    {
        super.validateButtonStates();
    }

    public void storeTempVars()
    {
        super.storeTempVars();
    }

    public void saveSettingsFile(Properties var1)
    {
        super.saveSettingsFile(var1);
    }

    public int rowY(int var1)
    {
        return super.rowY(var1);
    }

    public void resetTempVars()
    {
        super.resetTempVars();
    }

    /**
     * Called when the mouse is clicked.
     */
    public void mouseClicked(int var1, int var2, int var3)
    {
        super.mouseClicked(var1, var2, var3);
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    public void keyTyped(char var1, int var2)
    {
        super.keyTyped(var1, var2);
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        super.initGui();
    }

    /**
     * Handles mouse input.
     */
    public void handleMouseInput()
    {
        super.handleMouseInput();
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int var1, int var2, float var3)
    {
        super.drawScreen(var1, var2, var3);
    }

    public void actionPerformed(GuiButton var1)
    {
        super.actionPerformed(var1);
    }
}
