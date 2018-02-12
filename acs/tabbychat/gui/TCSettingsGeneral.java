package acs.tabbychat.gui;

import acs.tabbychat.core.TabbyChat;
import acs.tabbychat.settings.ColorCodeEnum;
import acs.tabbychat.settings.FormatCodeEnum;
import acs.tabbychat.settings.TCSettingBool;
import acs.tabbychat.settings.TCSettingEnum;
import acs.tabbychat.settings.TimeStampEnum;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Properties;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;

public class TCSettingsGeneral extends TCSettingsGUI
{
    public SimpleDateFormat timeStamp = new SimpleDateFormat();
    private static final int TABBYCHAT_ENABLE_ID = 9101;
    private static final int SAVE_CHATLOG_ID = 9102;
    private static final int TIMESTAMP_ENABLE_ID = 9103;
    private static final int TIMESTAMP_STYLE_ID = 9104;
    private static final int GROUP_SPAM_ID = 9105;
    private static final int UNREAD_FLASHING_ID = 9106;
    private static final int TIMESTAMP_COLOR_ID = 9107;
    private static final int UPDATE_CHECK_ENABLE = 9109;
    private static final int SPLIT_CHATLOG = 9110;
    public TCSettingBool tabbyChatEnable;
    public TCSettingBool saveChatLog;
    public TCSettingBool timeStampEnable;
    public TCSettingEnum timeStampStyle;
    public TCSettingEnum timeStampColor;
    public TCSettingBool groupSpam;
    public TCSettingBool unreadFlashing;
    public TCSettingBool updateCheckEnable;
    public TCSettingBool splitChatLog;

    public TCSettingsGeneral(TabbyChat _tc)
    {
        super(_tc);
        this.propertyPrefix = "settings.general";
        this.tabbyChatEnable = new TCSettingBool(Boolean.valueOf(true), "tabbyChatEnable", this.propertyPrefix, 9101);
        this.saveChatLog = new TCSettingBool(Boolean.valueOf(false), "saveChatLog", this.propertyPrefix, 9102);
        this.timeStampEnable = new TCSettingBool(Boolean.valueOf(false), "timeStampEnable", this.propertyPrefix, 9103);
        this.timeStampStyle = new TCSettingEnum(TimeStampEnum.MILITARY, "timeStampStyle", this.propertyPrefix, 9104, FormatCodeEnum.ITALIC);
        this.timeStampColor = new TCSettingEnum(ColorCodeEnum.DEFAULT, "timeStampColor", this.propertyPrefix, 9107, FormatCodeEnum.ITALIC);
        this.groupSpam = new TCSettingBool(Boolean.valueOf(false), "groupSpam", this.propertyPrefix, 9105);
        this.unreadFlashing = new TCSettingBool(Boolean.valueOf(true), "unreadFlashing", this.propertyPrefix, 9106);
        this.updateCheckEnable = new TCSettingBool(Boolean.valueOf(true), "updateCheckEnable", this.propertyPrefix, 9109);
        this.splitChatLog = new TCSettingBool(Boolean.valueOf(false), "splitChatLog", this.propertyPrefix, 9110);
        this.name = I18n.format("settings.general.name", new Object[0]);
        this.settingsFile = new File(tabbyChatDir, "general.cfg");
        this.bgcolor = 1715962558;
        this.defineDrawableSettings();
    }

    public void actionPerformed(GuiButton button)
    {
        switch (button.id)
        {
            case 9101:
                if (tc.enabled())
                {
                    tc.disable();
                }
                else
                {
                    tc.enable();
                }

            default:
                super.actionPerformed(button);
        }
    }

    private void applyTimestampPattern()
    {
        if (((ColorCodeEnum)this.timeStampColor.getValue()).toCode().length() > 0)
        {
            StringBuilder tsPattern = new StringBuilder();
            tsPattern.append("\'").append(((ColorCodeEnum)this.timeStampColor.getValue()).toCode()).append("\'");
            tsPattern.append(((TimeStampEnum)this.timeStampStyle.getValue()).toCode());
            tsPattern.append("\'\u00a7r\'");
            this.timeStamp.applyPattern(tsPattern.toString());
        }
        else
        {
            this.timeStamp.applyPattern(((TimeStampEnum)this.timeStampStyle.getValue()).toCode());
        }
    }

    public void defineDrawableSettings()
    {
        this.buttonList.add(this.tabbyChatEnable);
        this.buttonList.add(this.saveChatLog);
        this.buttonList.add(this.timeStampEnable);
        this.buttonList.add(this.timeStampStyle);
        this.buttonList.add(this.timeStampColor);
        this.buttonList.add(this.groupSpam);
        this.buttonList.add(this.unreadFlashing);
        this.buttonList.add(this.updateCheckEnable);
        this.buttonList.add(this.splitChatLog);
    }

    public void initDrawableSettings()
    {
        int effRight = (this.width + 300) / 2;
        int col1x = (this.width - 300) / 2 + 55;
        int col2x = this.width / 2 + 25;
        int buttonColor = (this.bgcolor & 16777215) + -16777216;
        this.tabbyChatEnable.setButtonLoc(col1x, this.rowY(1));
        this.tabbyChatEnable.setLabelLoc(col1x + 19);
        this.tabbyChatEnable.buttonColor = buttonColor;
        this.saveChatLog.setButtonLoc(col1x, this.rowY(2));
        this.saveChatLog.setLabelLoc(col1x + 19);
        this.saveChatLog.buttonColor = buttonColor;
        this.splitChatLog.setButtonLoc(col2x, this.rowY(2));
        this.splitChatLog.setLabelLoc(col2x + 19);
        this.splitChatLog.buttonColor = buttonColor;
        this.timeStampEnable.setButtonLoc(col1x, this.rowY(3));
        this.timeStampEnable.setLabelLoc(col1x + 19);
        this.timeStampEnable.buttonColor = buttonColor;
        this.timeStampStyle.setButtonDims(80, 11);
        this.timeStampStyle.setButtonLoc(effRight - 80, this.rowY(4));
        this.timeStampStyle.setLabelLoc(this.timeStampStyle.x() - 10 - this.mc.fontRenderer.getStringWidth(this.timeStampStyle.description));
        this.timeStampColor.setButtonDims(80, 11);
        this.timeStampColor.setButtonLoc(effRight - 80, this.rowY(5));
        this.timeStampColor.setLabelLoc(this.timeStampColor.x() - 10 - this.mc.fontRenderer.getStringWidth(this.timeStampColor.description));
        this.groupSpam.setButtonLoc(col1x, this.rowY(6));
        this.groupSpam.setLabelLoc(col1x + 19);
        this.groupSpam.buttonColor = buttonColor;
        this.unreadFlashing.setButtonLoc(col1x, this.rowY(7));
        this.unreadFlashing.setLabelLoc(col1x + 19);
        this.unreadFlashing.buttonColor = buttonColor;
        this.updateCheckEnable.setButtonLoc(col1x, this.rowY(8));
        this.updateCheckEnable.setLabelLoc(col1x + 19);
        this.updateCheckEnable.buttonColor = buttonColor;
    }

    public Properties loadSettingsFile()
    {
        super.loadSettingsFile();
        this.applyTimestampPattern();
        return null;
    }

    public void storeTempVars()
    {
        super.storeTempVars();
        this.applyTimestampPattern();
    }

    public void validateButtonStates()
    {
        this.timeStampColor.enabled = this.timeStampEnable.getTempValue().booleanValue();
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
}
