package acs.tabbychat.gui;

import acs.tabbychat.core.TabbyChat;
import acs.tabbychat.settings.ColorCodeEnum;
import acs.tabbychat.settings.FormatCodeEnum;
import acs.tabbychat.settings.ITCSetting;
import acs.tabbychat.settings.NotificationSoundEnum;
import acs.tabbychat.settings.TCChatFilter;
import acs.tabbychat.settings.TCSettingBool;
import acs.tabbychat.settings.TCSettingEnum;
import acs.tabbychat.settings.TCSettingTextBox;
import acs.tabbychat.util.TabbyChatUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.TreeMap;
import java.util.Map.Entry;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;

public class TCSettingsFilters extends TCSettingsGUI
{
    protected int curFilterId = 0;
    private static final int INVERSE_MATCH_ID = 9301;
    private static final int CASE_SENSE_ID = 9302;
    private static final int HIGHLIGHT_BOOL_ID = 9303;
    private static final int HIGHLIGHT_COLOR_ID = 9304;
    private static final int HIGHLIGHT_FORMAT_ID = 9305;
    private static final int AUDIO_NOTIFICATION_BOOL_ID = 9306;
    private static final int AUDIO_NOTIFICATION_ENUM_ID = 9307;
    private static final int PREV_ID = 9308;
    private static final int NEXT_ID = 9309;
    private static final int FILTER_NAME_ID = 9310;
    private static final int SEND_TO_TAB_BOOL_ID = 9311;
    private static final int SEND_TO_TAB_NAME_ID = 9312;
    private static final int SEND_TO_ALL_TABS_ID = 9313;
    private static final int REMOVE_MATCHES_ID = 9314;
    private static final int EXPRESSION_ID = 9315;
    private static final int ADD_ID = 9316;
    private static final int DEL_ID = 9317;
    public TCSettingBool inverseMatch;
    public TCSettingBool caseSensitive;
    public TCSettingBool highlightBool;
    public TCSettingEnum highlightColor;
    public TCSettingEnum highlightFormat;
    public TCSettingBool audioNotificationBool;
    public TCSettingEnum audioNotificationSound;
    public TCSettingTextBox filterName;
    public TCSettingBool sendToTabBool;
    public TCSettingTextBox sendToTabName;
    public TCSettingBool sendToAllTabs;
    public TCSettingBool removeMatches;
    public TCSettingTextBox expressionString;
    public TreeMap<Integer, TCChatFilter> filterMap;
    protected TreeMap<Integer, TCChatFilter> tempFilterMap;

    public TCSettingsFilters(TabbyChat _tc)
    {
        super(_tc);
        this.propertyPrefix = "settings.filters";
        this.inverseMatch = new TCSettingBool(Boolean.valueOf(false), "inverseMatch", this.propertyPrefix, 9301);
        this.caseSensitive = new TCSettingBool(Boolean.valueOf(false), "caseSensitive", this.propertyPrefix, 9302);
        this.highlightBool = new TCSettingBool(Boolean.valueOf(true), "highlightBool", this.propertyPrefix, 9303);
        this.highlightColor = new TCSettingEnum(ColorCodeEnum.YELLOW, "highlightColor", this.propertyPrefix, 9304, FormatCodeEnum.ITALIC);
        this.highlightFormat = new TCSettingEnum(FormatCodeEnum.BOLD, "highlightFormat", this.propertyPrefix, 9305, FormatCodeEnum.ITALIC);
        this.audioNotificationBool = new TCSettingBool(Boolean.valueOf(false), "audioNotificationBool", this.propertyPrefix, 9306);
        this.audioNotificationSound = new TCSettingEnum(NotificationSoundEnum.ORB, "audioNotificationSound", this.propertyPrefix, 9307, FormatCodeEnum.ITALIC);
        this.filterName = new TCSettingTextBox("New", "filterName", this.propertyPrefix, 9310);
        this.sendToTabBool = new TCSettingBool(Boolean.valueOf(false), "sendToTabBool", this.propertyPrefix, 9311);
        this.sendToTabName = new TCSettingTextBox("", "sendToTabName", this.propertyPrefix, 9312);
        this.sendToAllTabs = new TCSettingBool(Boolean.valueOf(false), "sendToAllTabs", this.propertyPrefix, 9313);
        this.removeMatches = new TCSettingBool(Boolean.valueOf(false), "removeMatches", this.propertyPrefix, 9314);
        this.expressionString = new TCSettingTextBox(".*", "expressionString", this.propertyPrefix, 9315);
        this.filterMap = new TreeMap();
        this.tempFilterMap = new TreeMap();
        this.name = I18n.format("settings.filters.name", new Object[0]);
        this.settingsFile = new File(TabbyChatUtils.getServerDir(), "filters.cfg");
        this.bgcolor = 1713938216;
        this.filterName.setCharLimit(50);
        this.sendToTabName.setCharLimit(20);
        this.expressionString.setCharLimit(Integer.MAX_VALUE);
        this.defineDrawableSettings();
    }

    public void actionPerformed(GuiButton button)
    {
        this.storeTempFilter();

        switch (button.id)
        {
            case 9308:
                if (this.tempFilterMap.size() > 0 && !this.displayPreviousFilter())
                {
                    this.curFilterId = ((Integer)this.tempFilterMap.lastKey()).intValue();
                    this.displayCurrentFilter();
                }

                break;

            case 9309:
                if (this.tempFilterMap.size() > 0 && !this.displayNextFilter())
                {
                    this.curFilterId = ((Integer)this.tempFilterMap.firstKey()).intValue();
                    this.displayCurrentFilter();
                }

            case 9310:
            case 9311:
            case 9312:
            case 9313:
            case 9314:
            case 9315:
            default:
                break;

            case 9316:
                if (this.tempFilterMap.size() == 0)
                {
                    this.curFilterId = 1;
                }
                else
                {
                    this.curFilterId = ((Integer)this.tempFilterMap.lastKey()).intValue() + 1;
                }

                this.tempFilterMap.put(Integer.valueOf(this.curFilterId), new TCChatFilter("New" + this.curFilterId));
                this.displayCurrentFilter();
                break;

            case 9317:
                this.tempFilterMap.remove(Integer.valueOf(this.curFilterId));

                if (!this.displayNextFilter())
                {
                    this.displayPreviousFilter();
                }
        }

        super.actionPerformed(button);
    }

    private void clearDisplay()
    {
        Iterator var1 = this.buttonList.iterator();

        while (var1.hasNext())
        {
            Object drawable = var1.next();

            if (drawable instanceof ITCSetting)
            {
                ((ITCSetting)drawable).clear();
            }
        }
    }

    public void defineDrawableSettings()
    {
        this.buttonList.add(this.filterName);
        this.buttonList.add(this.sendToTabBool);
        this.buttonList.add(this.sendToAllTabs);
        this.buttonList.add(this.sendToTabName);
        this.buttonList.add(this.removeMatches);
        this.buttonList.add(this.highlightBool);
        this.buttonList.add(this.highlightColor);
        this.buttonList.add(this.highlightFormat);
        this.buttonList.add(this.audioNotificationBool);
        this.buttonList.add(this.audioNotificationSound);
        this.buttonList.add(this.inverseMatch);
        this.buttonList.add(this.caseSensitive);
        this.buttonList.add(this.expressionString);
    }

    private boolean displayCurrentFilter()
    {
        if (!this.tempFilterMap.containsKey(Integer.valueOf(this.curFilterId)))
        {
            this.clearDisplay();
            return false;
        }
        else
        {
            Properties displayMe = ((TCChatFilter)this.tempFilterMap.get(Integer.valueOf(this.curFilterId))).getProperties();
            Iterator var2 = this.buttonList.iterator();

            while (var2.hasNext())
            {
                Object drawable = var2.next();

                if (drawable instanceof ITCSetting)
                {
                    ITCSetting tcDrawable = (ITCSetting)drawable;

                    if (tcDrawable.getType().equals("enum"))
                    {
                        ((TCSettingEnum)tcDrawable).setTempValueFromProps(displayMe);
                    }
                    else
                    {
                        tcDrawable.setTempValue(displayMe.get(tcDrawable.getProperty()));
                    }
                }
            }

            return true;
        }
    }

    private boolean displayNextFilter()
    {
        Entry next = this.tempFilterMap.higherEntry(Integer.valueOf(this.curFilterId));

        if (next == null)
        {
            this.clearDisplay();
            return false;
        }
        else
        {
            Properties displayMe = ((TCChatFilter)next.getValue()).getProperties();
            Iterator var3 = this.buttonList.iterator();

            while (var3.hasNext())
            {
                Object drawable = var3.next();

                if (drawable instanceof ITCSetting)
                {
                    ITCSetting tcDrawable = (ITCSetting)drawable;

                    if (tcDrawable.getType().equals("enum"))
                    {
                        ((TCSettingEnum)tcDrawable).setTempValueFromProps(displayMe);
                    }
                    else
                    {
                        tcDrawable.setTempValue(displayMe.get(tcDrawable.getProperty()));
                    }
                }
            }

            this.curFilterId = ((Integer)next.getKey()).intValue();
            return true;
        }
    }

    private boolean displayPreviousFilter()
    {
        Entry next = this.tempFilterMap.lowerEntry(Integer.valueOf(this.curFilterId));

        if (next == null)
        {
            this.clearDisplay();
            return false;
        }
        else
        {
            Properties displayMe = ((TCChatFilter)next.getValue()).getProperties();
            Iterator var3 = this.buttonList.iterator();

            while (var3.hasNext())
            {
                Object drawable = var3.next();

                if (drawable instanceof ITCSetting)
                {
                    ITCSetting tcDrawable = (ITCSetting)drawable;

                    if (tcDrawable.getType().equals("enum"))
                    {
                        ((TCSettingEnum)tcDrawable).setTempValueFromProps(displayMe);
                    }
                    else
                    {
                        tcDrawable.setTempValue(displayMe.get(tcDrawable.getProperty()));
                    }
                }
            }

            this.curFilterId = ((Integer)next.getKey()).intValue();
            return true;
        }
    }

    public void initDrawableSettings()
    {
        int effRight = (this.width + 300) / 2;
        int col1x = (this.width - 300) / 2 + 55;
        int buttonColor = (this.bgcolor & 16777215) + -16777216;
        PrefsButton newButton = new PrefsButton(9316, col1x, (this.height + 180) / 2 - 14, 45, 14, I18n.format("settings.new", new Object[0]));
        PrefsButton delButton = new PrefsButton(9317, col1x + 50, (this.height + 180) / 2 - 14, 45, 14, I18n.format("settings.delete", new Object[0]));
        newButton.bgcolor = this.bgcolor;
        delButton.bgcolor = this.bgcolor;
        this.buttonList.add(newButton);
        this.buttonList.add(delButton);
        this.filterName.setButtonDims(100, 11);
        this.filterName.setLabelLoc(col1x);
        this.filterName.setButtonLoc(col1x + 33 + this.mc.fontRenderer.getStringWidth(this.filterName.description), this.rowY(1));
        PrefsButton prevButton = new PrefsButton(9308, this.filterName.x() - 23, this.rowY(1), 20, 14, "<<");
        PrefsButton nextButton = new PrefsButton(9309, this.filterName.x() + 103, this.rowY(1), 20, 14, ">>");
        this.buttonList.add(prevButton);
        this.buttonList.add(nextButton);
        this.sendToTabBool.setButtonLoc(col1x, this.rowY(2));
        this.sendToTabBool.setLabelLoc(col1x + 19);
        this.sendToTabBool.buttonColor = buttonColor;
        this.sendToAllTabs.setButtonLoc(col1x + 20, this.rowY(3));
        this.sendToAllTabs.setLabelLoc(col1x + 39);
        this.sendToAllTabs.buttonColor = buttonColor;
        this.sendToTabName.setLabelLoc(effRight - this.mc.fontRenderer.getStringWidth(this.sendToTabName.description) - 55);
        this.sendToTabName.setButtonLoc(effRight - 50, this.rowY(3));
        this.sendToTabName.setButtonDims(50, 11);
        this.removeMatches.setButtonLoc(col1x, this.rowY(4));
        this.removeMatches.setLabelLoc(col1x + 19);
        this.removeMatches.buttonColor = buttonColor;
        this.highlightBool.setButtonLoc(col1x, this.rowY(5));
        this.highlightBool.setLabelLoc(col1x + 19);
        this.highlightBool.buttonColor = buttonColor;
        this.highlightColor.setButtonDims(70, 11);
        this.highlightColor.setButtonLoc(col1x + 15 + this.mc.fontRenderer.getStringWidth(this.highlightColor.description), this.rowY(6));
        this.highlightColor.setLabelLoc(col1x + 10);
        this.highlightFormat.setButtonDims(60, 11);
        this.highlightFormat.setButtonLoc(effRight - 60, this.rowY(6));
        this.highlightFormat.setLabelLoc(this.highlightFormat.x() - 5 - this.mc.fontRenderer.getStringWidth(this.highlightFormat.description));
        this.audioNotificationBool.setButtonLoc(col1x, this.rowY(7));
        this.audioNotificationBool.setLabelLoc(col1x + 19);
        this.audioNotificationBool.buttonColor = buttonColor;
        this.audioNotificationSound.setButtonDims(60, 11);
        this.audioNotificationSound.setButtonLoc(effRight - 60, this.rowY(7));
        this.audioNotificationSound.setLabelLoc(this.audioNotificationSound.x() - 5 - this.mc.fontRenderer.getStringWidth(this.audioNotificationSound.description));
        this.inverseMatch.setButtonLoc(col1x, this.rowY(8));
        this.inverseMatch.setLabelLoc(col1x + 19);
        this.inverseMatch.buttonColor = buttonColor;
        this.caseSensitive.setLabelLoc(effRight - this.mc.fontRenderer.getStringWidth(this.caseSensitive.description));
        this.caseSensitive.setButtonLoc(effRight - this.mc.fontRenderer.getStringWidth(this.caseSensitive.description) - 19, this.rowY(8));
        this.caseSensitive.buttonColor = buttonColor;
        this.expressionString.setLabelLoc(col1x);
        this.expressionString.setButtonLoc(col1x + 5 + this.mc.fontRenderer.getStringWidth(this.expressionString.description), this.rowY(9));
        this.expressionString.setButtonDims(effRight - this.expressionString.x(), 11);
        this.resetTempVars();
        this.displayCurrentFilter();
    }

    public Properties loadSettingsFile()
    {
        this.filterMap.clear();

        if (this.settingsFile == null)
        {
            return null;
        }
        else
        {
            Properties settingsTable = super.loadSettingsFile();
            int loadId = 1;

            for (String loadName = settingsTable.getProperty(loadId + ".filterName"); loadName != null; loadName = settingsTable.getProperty(loadId + ".filterName"))
            {
                TCChatFilter loaded = new TCChatFilter(loadName);
                loaded.inverseMatch = Boolean.parseBoolean(settingsTable.getProperty(loadId + ".inverseMatch"));
                loaded.caseSensitive = Boolean.parseBoolean(settingsTable.getProperty(loadId + ".caseSensitive"));
                loaded.highlightBool = Boolean.parseBoolean(settingsTable.getProperty(loadId + ".highlightBool"));
                loaded.highlightColor = ColorCodeEnum.cleanValueOf(settingsTable.getProperty(loadId + ".highlightColor"));
                loaded.highlightFormat = FormatCodeEnum.cleanValueOf(settingsTable.getProperty(loadId + ".highlightFormat"));
                loaded.audioNotificationBool = Boolean.parseBoolean(settingsTable.getProperty(loadId + ".audioNotificationBool"));
                loaded.audioNotificationSound = TabbyChatUtils.parseSound(settingsTable.getProperty(loadId + ".audioNotificationSound"));
                loaded.sendToTabBool = Boolean.parseBoolean(settingsTable.getProperty(loadId + ".sendToTabBool"));
                loaded.sendToTabName = TabbyChatUtils.parseString(settingsTable.getProperty(loadId + ".sendToTabName"));
                loaded.sendToAllTabs = Boolean.parseBoolean(settingsTable.getProperty(loadId + ".sendToAllTabs"));
                loaded.removeMatches = Boolean.parseBoolean(settingsTable.getProperty(loadId + ".removeMatches"));
                loaded.compilePattern(TabbyChatUtils.parseString(settingsTable.getProperty(loadId + ".expressionString")));
                this.filterMap.put(Integer.valueOf(loadId), loaded);
                ++loadId;
            }

            this.resetTempVars();
            return null;
        }
    }

    /**
     * Called when the mouse is clicked.
     */
    public void mouseClicked(int par1, int par2, int par3)
    {
        if (this.audioNotificationSound.hovered(par1, par2).booleanValue())
        {
            this.audioNotificationSound.mouseClicked(par1, par2, par3);
            this.mc.thePlayer.playSound(((NotificationSoundEnum)this.audioNotificationSound.getTempValue()).file(), 1.0F, 1.0F);
        }
        else
        {
            super.mouseClicked(par1, par2, par3);
        }
    }

    public void resetTempVars()
    {
        this.tempFilterMap.clear();
        Entry realFilter = this.filterMap.firstEntry();

        if (realFilter != null)
        {
            this.curFilterId = ((Integer)realFilter.getKey()).intValue();
        }

        while (realFilter != null)
        {
            this.tempFilterMap.put((Integer) realFilter.getKey(), new TCChatFilter((TCChatFilter)realFilter.getValue()));
            realFilter = this.filterMap.higherEntry((Integer) realFilter.getKey());
        }
    }

    public void saveSettingsFile()
    {
        Properties settingsTable = new Properties();
        int saveId = 1;

        for (Entry saveFilter = this.filterMap.firstEntry(); saveFilter != null; saveFilter = this.filterMap.higherEntry((Integer) saveFilter.getKey()))
        {
            settingsTable.put(saveId + ".filterName", ((TCChatFilter)saveFilter.getValue()).filterName);
            settingsTable.put(saveId + ".inverseMatch", Boolean.toString(((TCChatFilter)saveFilter.getValue()).inverseMatch));
            settingsTable.put(saveId + ".caseSensitive", Boolean.toString(((TCChatFilter)saveFilter.getValue()).caseSensitive));
            settingsTable.put(saveId + ".highlightBool", Boolean.toString(((TCChatFilter)saveFilter.getValue()).highlightBool));
            settingsTable.put(saveId + ".audioNotificationBool", Boolean.toString(((TCChatFilter)saveFilter.getValue()).audioNotificationBool));
            settingsTable.put(saveId + ".sendToTabBool", Boolean.toString(((TCChatFilter)saveFilter.getValue()).sendToTabBool));
            settingsTable.put(saveId + ".sendToAllTabs", Boolean.toString(((TCChatFilter)saveFilter.getValue()).sendToAllTabs));
            settingsTable.put(saveId + ".removeMatches", Boolean.toString(((TCChatFilter)saveFilter.getValue()).removeMatches));
            settingsTable.put(saveId + ".highlightColor", ((TCChatFilter)saveFilter.getValue()).highlightColor.name());
            settingsTable.put(saveId + ".highlightFormat", ((TCChatFilter)saveFilter.getValue()).highlightFormat.name());
            settingsTable.put(saveId + ".audioNotificationSound", ((TCChatFilter)saveFilter.getValue()).audioNotificationSound.name());
            settingsTable.put(saveId + ".sendToTabName", ((TCChatFilter)saveFilter.getValue()).sendToTabName);
            settingsTable.put(saveId + ".expressionString", ((TCChatFilter)saveFilter.getValue()).expressionString);
            ++saveId;
        }

        ArrayList tmpList = new ArrayList(this.buttonList);
        this.buttonList.clear();
        super.saveSettingsFile(settingsTable);
        this.buttonList = tmpList;
    }

    private void storeTempFilter()
    {
        if (this.tempFilterMap.containsKey(Integer.valueOf(this.curFilterId)))
        {
            TCChatFilter storeMe = (TCChatFilter)this.tempFilterMap.get(Integer.valueOf(this.curFilterId));
            storeMe.filterName = this.filterName.getTempValue();
            storeMe.inverseMatch = this.inverseMatch.getTempValue().booleanValue();
            storeMe.caseSensitive = this.caseSensitive.getTempValue().booleanValue();
            storeMe.highlightBool = this.highlightBool.getTempValue().booleanValue();
            storeMe.highlightColor = ColorCodeEnum.valueOf(this.highlightColor.getTempValue().name());
            storeMe.highlightFormat = FormatCodeEnum.valueOf(this.highlightFormat.getTempValue().name());
            storeMe.audioNotificationBool = this.audioNotificationBool.getTempValue().booleanValue();
            storeMe.audioNotificationSound = NotificationSoundEnum.valueOf(this.audioNotificationSound.getTempValue().name());
            storeMe.sendToTabBool = this.sendToTabBool.getTempValue().booleanValue();
            storeMe.sendToAllTabs = this.sendToAllTabs.getTempValue().booleanValue();
            storeMe.sendToTabName = this.sendToTabName.getTempValue();
            storeMe.removeMatches = this.removeMatches.getTempValue().booleanValue();
            storeMe.expressionString = this.expressionString.getTempValue();
        }
    }

    public void storeTempVars()
    {
        this.filterMap.clear();

        for (Entry tempFilter = this.tempFilterMap.firstEntry(); tempFilter != null; tempFilter = this.tempFilterMap.higherEntry((Integer) tempFilter.getKey()))
        {
            this.filterMap.put((Integer) tempFilter.getKey(), new TCChatFilter((TCChatFilter)tempFilter.getValue()));
        }
    }

    public void updateForServer()
    {
        this.settingsFile = new File(TabbyChatUtils.getServerDir(), "filters.cfg");
    }

    public void validateButtonStates()
    {
        this.inverseMatch.enabled = !this.highlightBool.getTempValue().booleanValue();
        this.caseSensitive.enabled = true;
        this.highlightBool.enabled = !this.removeMatches.getTempValue().booleanValue() && !this.inverseMatch.getTempValue().booleanValue();
        this.audioNotificationBool.enabled = !this.removeMatches.getTempValue().booleanValue();
        this.removeMatches.enabled = !this.sendToTabBool.getTempValue().booleanValue() && !this.highlightBool.getTempValue().booleanValue() && !this.audioNotificationBool.getTempValue().booleanValue();
        this.sendToTabBool.enabled = !this.removeMatches.getTempValue().booleanValue();
        this.highlightColor.enabled = this.highlightBool.getTempValue().booleanValue();
        this.highlightFormat.enabled = this.highlightBool.getTempValue().booleanValue();
        this.audioNotificationSound.enabled = this.audioNotificationBool.getTempValue().booleanValue();
        this.sendToAllTabs.enabled = this.sendToTabBool.getTempValue().booleanValue();

        for (int i = 0; i < this.buttonList.size(); ++i)
        {
            if (ITCSetting.class.isInstance(this.buttonList.get(i)))
            {
                ITCSetting tmp = (ITCSetting)this.buttonList.get(i);

                if (this.tempFilterMap.size() == 0)
                {
                    tmp.disable();
                }
                else if (tmp.getType() == "textbox")
                {
                    tmp.enable();
                }
                else if (tmp.getType() == "bool")
                {
                    ((TCSettingBool)tmp).setTempValue(Boolean.valueOf(((TCSettingBool)tmp).getTempValue().booleanValue() && tmp.enabled()));
                }
            }
        }

        this.sendToTabName.func_146184_c(this.sendToTabBool.getTempValue().booleanValue() && !this.sendToAllTabs.getTempValue().booleanValue());
    }

    public void saveSettingsFile(Properties var1)
    {
        super.saveSettingsFile(var1);
    }

    public int rowY(int var1)
    {
        return super.rowY(var1);
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
