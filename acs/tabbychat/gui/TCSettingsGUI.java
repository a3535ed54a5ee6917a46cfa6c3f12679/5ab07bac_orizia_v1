package acs.tabbychat.gui;

import acs.tabbychat.core.TabbyChat;
import acs.tabbychat.settings.ITCSetting;
import acs.tabbychat.settings.TCSettingSlider;
import acs.tabbychat.settings.TCSettingTextBox;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;

abstract class TCSettingsGUI extends GuiScreen implements ITCSettingsGUI
{
    protected static TabbyChat tc;

    /** Reference to the Minecraft object. */
    protected Minecraft mc;
    protected int lastOpened;
    protected String name;
    protected String propertyPrefix;
    protected int bgcolor;
    protected int id;
    protected static List<TCSettingsGUI> ScreenList = new ArrayList();
    protected File settingsFile;

    private TCSettingsGUI()
    {
        this.lastOpened = 0;
        this.bgcolor = 1722148836;
        this.id = 9000;
        this.mc = Minecraft.getMinecraft();
        ScreenList.add(this);
    }

    public TCSettingsGUI(TabbyChat _tc)
    {
        this();
        tc = _tc;
    }

    public void actionPerformed(GuiButton button)
    {
        if (button instanceof ITCSetting && ((ITCSetting)button).getType() != "textbox")
        {
            ((ITCSetting)button).actionPerformed();
        }
        else
        {
            Iterator i;
            TCSettingsGUI screen;

            if (button.id == 8901)
            {
                i = ScreenList.iterator();

                while (i.hasNext())
                {
                    screen = (TCSettingsGUI)i.next();
                    screen.storeTempVars();
                    screen.saveSettingsFile();
                }

                tc.reloadSettingsData(true);
                this.mc.displayGuiScreen((GuiScreen)null);

                if (TabbyChat.generalSettings.tabbyChatEnable.getValue().booleanValue())
                {
                    tc.resetDisplayedChat();
                }
            }
            else if (button.id == 8902)
            {
                i = ScreenList.iterator();

                while (i.hasNext())
                {
                    screen = (TCSettingsGUI)i.next();
                    screen.resetTempVars();
                }

                this.mc.displayGuiScreen((GuiScreen)null);

                if (TabbyChat.generalSettings.tabbyChatEnable.getValue().booleanValue())
                {
                    tc.resetDisplayedChat();
                }
            }
            else
            {
                for (int var4 = 0; var4 < ScreenList.size(); ++var4)
                {
                    if (button.id == ((TCSettingsGUI)ScreenList.get(var4)).id)
                    {
                        this.mc.displayGuiScreen((GuiScreen)ScreenList.get(var4));
                    }
                }
            }
        }

        this.validateButtonStates();
    }

    public void defineDrawableSettings() {}

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int x, int y, float f)
    {
        int effLeft = (this.width - 300) / 2;
        int absLeft = effLeft - 4;
        int effTop = (this.height - 180) / 2;
        int absTop = effTop - 4;
        drawRect(absLeft, absTop, absLeft + 300 + 8, absTop + 180 + 8, -2013265920);
        drawRect(absLeft + 45, absTop, absLeft + 46, absTop + 180, 1728053247);
        int i;

        for (i = 0; i < ScreenList.size(); ++i)
        {
            if (ScreenList.get(i) == this)
            {
                int tabDist = Math.max(this.mc.fontRenderer.getStringWidth(((TCSettingsGUI)ScreenList.get(i)).name) + 4 - 40, 25);
                int curWidth;

                if (0 <= this.lastOpened && this.lastOpened <= 5)
                {
                    curWidth = 45 + this.lastOpened * tabDist / 5;
                    ++this.lastOpened;
                }
                else
                {
                    curWidth = tabDist + 45;
                }

                drawRect(absLeft - curWidth + 45, effTop + 30 * i, absLeft + 45, effTop + 30 * i + 20, ((TCSettingsGUI)ScreenList.get(i)).bgcolor);
                this.drawString(this.mc.fontRenderer, this.mc.fontRenderer.trimStringToWidth(((TCSettingsGUI)ScreenList.get(i)).name, curWidth - 5), effLeft - curWidth + 45, effTop + 6 + 30 * i, 16777215);
            }
            else
            {
                drawRect(absLeft, effTop + 30 * i, absLeft + 45, effTop + 30 * i + 20, ((TCSettingsGUI)ScreenList.get(i)).bgcolor);
            }
        }

        for (i = 0; i < this.buttonList.size(); ++i)
        {
            ((GuiButton)this.buttonList.get(i)).drawButton(this.mc, x, y);
        }
    }

    /**
     * Handles mouse input.
     */
    public void handleMouseInput()
    {
        super.handleMouseInput();

        for (int i = 0; i < this.buttonList.size(); ++i)
        {
            if (this.buttonList.get(i) instanceof ITCSetting)
            {
                ITCSetting tmp = (ITCSetting)this.buttonList.get(i);

                if (tmp.getType() == "slider")
                {
                    ((TCSettingSlider)tmp).handleMouseInput();
                }
            }
        }
    }

    public void initDrawableSettings() {}

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        int effLeft = (this.width - 300) / 2;
        int effTop = (this.height - 180) / 2;
        this.lastOpened = 0;
        int effRight = (this.width + 300) / 2;
        byte bW = 40;
        byte bH = 14;
        PrefsButton savePrefs = new PrefsButton(8901, effRight + 10, (this.height + 180) / 2 - bH, bW, bH, I18n.format("settings.save", new Object[0]));
        this.buttonList.add(savePrefs);
        PrefsButton cancelPrefs = new PrefsButton(8902, effRight + 10, (this.height + 180) / 2 - 2 * bH - 2, bW, bH, I18n.format("settings.cancel", new Object[0]));
        this.buttonList.add(cancelPrefs);

        for (int i = 0; i < ScreenList.size(); ++i)
        {
            ((TCSettingsGUI)ScreenList.get(i)).id = 9000 + i;
            ((TCSettingsGUI)ScreenList.get(i)).name = I18n.format(((TCSettingsGUI)ScreenList.get(i)).propertyPrefix + ".name", new Object[0]);

            if (ScreenList.get(i) != this)
            {
                this.buttonList.add(new PrefsButton(((TCSettingsGUI)ScreenList.get(i)).id, effLeft, effTop + 30 * i, 45, 20, this.mc.fontRenderer.trimStringToWidth(((TCSettingsGUI)ScreenList.get(i)).name, 35) + "..."));
                ((PrefsButton)this.buttonList.get(this.buttonList.size() - 1)).bgcolor = 0;
            }
        }

        this.defineDrawableSettings();
        this.initDrawableSettings();
        this.validateButtonStates();
        Iterator var10 = this.buttonList.iterator();

        while (var10.hasNext())
        {
            Object drawable = var10.next();

            if (drawable instanceof ITCSetting)
            {
                ((ITCSetting)drawable).resetDescription();
            }
        }
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    public void keyTyped(char par1, int par2)
    {
        for (int i = 0; i < this.buttonList.size(); ++i)
        {
            if (ITCSetting.class.isInstance(this.buttonList.get(i)))
            {
                ITCSetting tmp = (ITCSetting)this.buttonList.get(i);

                if (tmp.getType() == "textbox")
                {
                    ((TCSettingTextBox)tmp).keyTyped(par1, par2);
                }
            }
        }

        super.keyTyped(par1, par2);
    }

    public Properties loadSettingsFile()
    {
        Properties settingsTable = new Properties();

        if (this.settingsFile == null)
        {
            return settingsTable;
        }
        else if (!this.settingsFile.exists())
        {
            return settingsTable;
        }
        else
        {
            FileInputStream fInStream = null;
            BufferedInputStream bInStream = null;

            try
            {
                fInStream = new FileInputStream(this.settingsFile);
                bInStream = new BufferedInputStream(fInStream);
                settingsTable.load(bInStream);
            }
            catch (Exception var13)
            {
                TabbyChat.printException("Error while reading settings from file \'" + this.settingsFile + "\'", var13);
            }
            finally
            {
                try
                {
                    if (bInStream != null)
                    {
                        bInStream.close();
                    }

                    if (fInStream != null)
                    {
                        fInStream.close();
                    }
                }
                catch (Exception var12)
                {
                    ;
                }
            }

            Iterator e = this.buttonList.iterator();

            while (e.hasNext())
            {
                Object drawable = e.next();

                if (drawable instanceof ITCSetting)
                {
                    ((ITCSetting)drawable).loadSelfFromProps(settingsTable);
                }
            }

            this.resetTempVars();
            return settingsTable;
        }
    }

    /**
     * Called when the mouse is clicked.
     */
    public void mouseClicked(int par1, int par2, int par3)
    {
        for (int i = 0; i < this.buttonList.size(); ++i)
        {
            if (this.buttonList.get(i) instanceof ITCSetting)
            {
                ITCSetting tmp = (ITCSetting)this.buttonList.get(i);

                if (tmp.getType() == "textbox" || tmp.getType() == "enum" || tmp.getType() == "slider")
                {
                    tmp.mouseClicked(par1, par2, par3);
                }
            }
        }

        super.mouseClicked(par1, par2, par3);
    }

    public void resetTempVars()
    {
        Iterator var1 = this.buttonList.iterator();

        while (var1.hasNext())
        {
            Object drawable = var1.next();

            if (drawable instanceof ITCSetting)
            {
                ((ITCSetting)drawable).reset();
            }
        }
    }

    public int rowY(int rowNum)
    {
        return (this.height - 180) / 2 + (rowNum - 1) * 18;
    }

    public void saveSettingsFile(Properties settingsTable)
    {
        if (this.settingsFile != null)
        {
            if (!this.settingsFile.getParentFile().exists())
            {
                this.settingsFile.getParentFile().mkdirs();
            }

            Iterator fOutStream = this.buttonList.iterator();

            while (fOutStream.hasNext())
            {
                Object bOutStream = fOutStream.next();

                if (bOutStream instanceof ITCSetting)
                {
                    ((ITCSetting)bOutStream).saveSelfToProps(settingsTable);
                }
            }

            FileOutputStream fOutStream1 = null;
            BufferedOutputStream bOutStream1 = null;

            try
            {
                fOutStream1 = new FileOutputStream(this.settingsFile);
                bOutStream1 = new BufferedOutputStream(fOutStream1);
                settingsTable.store(bOutStream1, this.propertyPrefix);
            }
            catch (Exception var13)
            {
                TabbyChat.printException("Error while writing settings to file \'" + this.settingsFile + "\'", var13);
            }
            finally
            {
                try
                {
                    if (bOutStream1 != null)
                    {
                        bOutStream1.close();
                    }

                    if (fOutStream1 != null)
                    {
                        fOutStream1.close();
                    }
                }
                catch (Exception var12)
                {
                    ;
                }
            }
        }
    }

    public void saveSettingsFile()
    {
        this.saveSettingsFile(new Properties());
    }

    public void storeTempVars()
    {
        Iterator var1 = this.buttonList.iterator();

        while (var1.hasNext())
        {
            Object drawable = var1.next();

            if (drawable instanceof ITCSetting)
            {
                ((ITCSetting)drawable).save();
            }
        }
    }

    public void validateButtonStates() {}
}
