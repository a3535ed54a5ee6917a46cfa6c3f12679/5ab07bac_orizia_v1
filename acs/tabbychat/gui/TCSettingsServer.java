package acs.tabbychat.gui;

import acs.tabbychat.core.TabbyChat;
import acs.tabbychat.settings.ChannelDelimEnum;
import acs.tabbychat.settings.ColorCodeEnum;
import acs.tabbychat.settings.FormatCodeEnum;
import acs.tabbychat.settings.TCSettingBool;
import acs.tabbychat.settings.TCSettingEnum;
import acs.tabbychat.settings.TCSettingTextBox;
import acs.tabbychat.util.TabbyChatUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import org.apache.commons.lang3.StringUtils;

public class TCSettingsServer extends TCSettingsGUI
{
    private static final int AUTO_CHANNEL_SEARCH_ID = 9201;
    private static final int CHATCHANNEL_DELIMS_ID = 9202;
    private static final int DELIM_COLOR_BOOL_ID = 9203;
    private static final int DELIM_COLOR_ENUM_ID = 9204;
    private static final int DELIM_FORMAT_BOOL_ID = 9205;
    private static final int DELIM_FORMAT_ENUM_ID = 9206;
    private static final int DEFAULT_CHANNELS_ID = 9207;
    private static final int IGNORED_CHANNELS_ID = 9208;
    private static final int AUTO_PM_SEARCH_ID = 9209;
    private static final int REGEX_IGNORE_ID = 9210;
    private static final int PM_TAB_REGEX_TO_ID = 9211;
    private static final int PM_TAB_REGEX_FROM_ID = 9212;
    public static final Pattern SPLIT_PATTERN = Pattern.compile("[ ]?,[ ]?");
    public TCSettingBool autoChannelSearch;
    public TCSettingBool autoPMSearch;
    public TCSettingEnum delimiterChars;
    public TCSettingBool delimColorBool;
    public TCSettingEnum delimColorCode;
    public TCSettingBool delimFormatBool;
    public TCSettingEnum delimFormatCode;
    public TCSettingTextBox defaultChannels;
    public TCSettingTextBox ignoredChannels;
    public TCSettingBool regexIgnoreBool;
    public TCSettingTextBox pmTabRegexToMe;
    public TCSettingTextBox pmTabRegexFromMe;
    public List<String> defaultChanList;
    public Pattern ignoredChanPattern;
    public String serverIP;

    public TCSettingsServer(TabbyChat _tc)
    {
        super(_tc);
        this.propertyPrefix = "settings.server";
        this.autoChannelSearch = new TCSettingBool(Boolean.valueOf(false), "autoChannelSearch", this.propertyPrefix, 9201);
        this.autoPMSearch = new TCSettingBool(Boolean.valueOf(true), "autoPMSearch", this.propertyPrefix, 9209);
        this.delimiterChars = new TCSettingEnum(ChannelDelimEnum.BRACKETS, "delimiterChars", this.propertyPrefix, 9202);
        this.delimColorBool = new TCSettingBool(Boolean.valueOf(false), "delimColorBool", this.propertyPrefix, 9203, FormatCodeEnum.ITALIC);
        this.delimColorCode = new TCSettingEnum(ColorCodeEnum.DEFAULT, "delimColorCode", "", 9204);
        this.delimFormatBool = new TCSettingBool(Boolean.valueOf(false), "delimFormatBool", this.propertyPrefix, 9205, FormatCodeEnum.ITALIC);
        this.delimFormatCode = new TCSettingEnum(FormatCodeEnum.DEFAULT, "delimFormatCode", "", 9206);
        this.defaultChannels = new TCSettingTextBox("", "defaultChannels", this.propertyPrefix, 9207);
        this.ignoredChannels = new TCSettingTextBox("", "ignoredChannels", this.propertyPrefix, 9208);
        this.regexIgnoreBool = new TCSettingBool(Boolean.valueOf(false), "regexIgnoreBool", this.propertyPrefix, 9210);
        this.pmTabRegexToMe = new TCSettingTextBox("", "pmTabRegex.toMe", this.propertyPrefix, 9211);
        this.pmTabRegexFromMe = new TCSettingTextBox("", "pmTabRegex.fromMe", this.propertyPrefix, 9212);
        this.defaultChanList = new ArrayList();
        this.ignoredChanPattern = Pattern.compile("a^");
        this.serverIP = "";
        this.name = I18n.format("settings.server.name", new Object[0]);
        this.settingsFile = new File(TabbyChatUtils.getServerDir(), "settings.cfg");
        this.bgcolor = 1725355587;
        this.defaultChannels.setCharLimit(Integer.MAX_VALUE);
        this.ignoredChannels.setCharLimit(Integer.MAX_VALUE);
        this.pmTabRegexFromMe.setCharLimit(Integer.MAX_VALUE);
        this.pmTabRegexToMe.setCharLimit(Integer.MAX_VALUE);
        this.defineDrawableSettings();
    }

    public void defineDrawableSettings()
    {
        this.buttonList.add(this.autoChannelSearch);
        this.buttonList.add(this.autoPMSearch);
        this.buttonList.add(this.delimiterChars);
        this.buttonList.add(this.delimColorBool);
        this.buttonList.add(this.delimColorCode);
        this.buttonList.add(this.delimFormatBool);
        this.buttonList.add(this.delimFormatCode);
        this.buttonList.add(this.defaultChannels);
        this.buttonList.add(this.ignoredChannels);
        this.buttonList.add(this.regexIgnoreBool);
        this.buttonList.add(this.pmTabRegexToMe);
        this.buttonList.add(this.pmTabRegexFromMe);
    }

    public void initDrawableSettings()
    {
        int effRight = (this.width + 300) / 2;
        int col1x = (this.width - 300) / 2 + 55;
        int buttonColor = (this.bgcolor & 16777215) + -16777216;
        this.autoChannelSearch.setButtonLoc(col1x, this.rowY(1));
        this.autoChannelSearch.setLabelLoc(col1x + 19);
        this.autoChannelSearch.buttonColor = buttonColor;
        this.autoPMSearch.setButtonLoc(col1x, this.rowY(2));
        this.autoPMSearch.setLabelLoc(col1x + 19);
        this.autoPMSearch.buttonColor = buttonColor;
        this.delimiterChars.setLabelLoc(col1x);
        this.delimiterChars.setButtonLoc(col1x + 20 + this.mc.fontRenderer.getStringWidth(this.delimiterChars.description), this.rowY(3));
        this.delimiterChars.setButtonDims(80, 11);
        this.delimColorBool.setButtonLoc(col1x + 20, this.rowY(4));
        this.delimColorBool.setLabelLoc(col1x + 49);
        this.delimColorBool.buttonColor = buttonColor;
        this.delimColorCode.setButtonLoc(effRight - 70, this.rowY(4));
        this.delimColorCode.setButtonDims(70, 11);
        this.delimFormatBool.setButtonLoc(col1x + 20, this.rowY(5));
        this.delimFormatBool.setLabelLoc(col1x + 39);
        this.delimFormatBool.buttonColor = buttonColor;
        this.delimFormatCode.setButtonLoc(this.delimColorCode.x(), this.rowY(5));
        this.delimFormatCode.setButtonDims(70, 11);
        this.defaultChannels.setLabelLoc(col1x);
        this.defaultChannels.setButtonLoc(effRight - 149, this.rowY(6));
        this.defaultChannels.setButtonDims(149, 11);
        this.ignoredChannels.setLabelLoc(col1x);
        this.ignoredChannels.setButtonLoc(effRight - 149, this.rowY(7));
        this.ignoredChannels.setButtonDims(149, 11);
        this.regexIgnoreBool.setButtonLoc(col1x + 5 + this.mc.fontRenderer.getStringWidth(this.ignoredChannels.description), this.rowY(8));
        this.regexIgnoreBool.setLabelLoc(col1x + 5 + this.mc.fontRenderer.getStringWidth(this.ignoredChannels.description) + 19);
        this.regexIgnoreBool.buttonColor = buttonColor;
        this.pmTabRegexToMe.setLabelLoc(col1x);
        this.pmTabRegexToMe.setButtonLoc(effRight - 149, this.rowY(9));
        this.pmTabRegexToMe.setButtonDims(149, 11);
        this.pmTabRegexFromMe.setLabelLoc(col1x);
        this.pmTabRegexFromMe.setButtonLoc(effRight - 149, this.rowY(10));
        this.pmTabRegexFromMe.setButtonDims(149, 11);
    }

    public Properties loadSettingsFile()
    {
        if (this.settingsFile != null)
        {
            super.loadSettingsFile();
            this.parseChannelsFromInput();
        }

        return null;
    }

    public void storeTempVars()
    {
        super.storeTempVars();
        this.parseChannelsFromInput();
    }

    private void parseChannelsFromInput()
    {
        this.defaultChanList = Arrays.asList(SPLIT_PATTERN.split(this.defaultChannels.getValue()));
        String[] splitChannels = SPLIT_PATTERN.split(this.ignoredChannels.getValue());

        if (!this.regexIgnoreBool.getValue().booleanValue())
        {
            for (int i = 0; i < splitChannels.length; ++i)
            {
                splitChannels[i] = Pattern.quote(splitChannels[i]);
            }
        }

        this.ignoredChanPattern = Pattern.compile("^(" + StringUtils.join(splitChannels, "|") + ")$");
    }

    public void updateForServer()
    {
        this.serverIP = TabbyChatUtils.getServerIp();
        this.settingsFile = new File(TabbyChatUtils.getServerDir(), "settings.cfg");
    }

    public void validateButtonStates()
    {
        this.delimColorBool.enabled = this.autoChannelSearch.getTempValue().booleanValue();
        this.delimFormatBool.enabled = this.autoChannelSearch.getTempValue().booleanValue();
        this.delimColorCode.enabled = this.delimColorBool.getTempValue().booleanValue() && this.autoChannelSearch.getTempValue().booleanValue();
        this.delimFormatCode.enabled = this.delimFormatBool.getTempValue().booleanValue() && this.autoChannelSearch.getTempValue().booleanValue();
        this.delimiterChars.enabled = this.autoChannelSearch.getTempValue().booleanValue();
    }

    public void saveSettingsFile()
    {
        super.saveSettingsFile();
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
