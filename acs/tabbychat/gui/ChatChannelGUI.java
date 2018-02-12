package acs.tabbychat.gui;

import acs.tabbychat.core.ChatChannel;
import acs.tabbychat.core.GuiNewChatTC;
import acs.tabbychat.core.TabbyChat;
import acs.tabbychat.settings.ITCSetting;
import acs.tabbychat.settings.TCSettingBool;
import acs.tabbychat.settings.TCSettingTextBox;
import acs.tabbychat.util.TabbyChatUtils;
import java.util.Iterator;
import java.util.LinkedHashMap;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;

public class ChatChannelGUI extends GuiScreen
{
    protected ChatChannel channel;
    public final int displayWidth = 255;
    public final int displayHeight = 120;
    private String title;
    private int position;
    private TabbyChat tc;
    private static final int SAVE_ID = 8981;
    private static final int CANCEL_ID = 8982;
    private static final int NOTIFICATIONS_ON_ID = 8983;
    private static final int ALIAS_ID = 8984;
    private static final int CMD_PREFIX_ID = 8985;
    private static final int PREV_ID = 8986;
    private static final int NEXT_ID = 8987;
    private static final int HIDE_PREFIX = 8988;
    private TCSettingBool hidePrefix = new TCSettingBool(Boolean.valueOf(false), "hidePrefix", "settings.channel", 8988);
    private TCSettingBool notificationsOn = new TCSettingBool(Boolean.valueOf(false), "notificationsOn", "settings.channel", 8983);
    private TCSettingTextBox alias = new TCSettingTextBox("", "alias", "settings.channel", 8984);
    private TCSettingTextBox cmdPrefix = new TCSettingTextBox("", "cmdPrefix", "settings.channel", 8985);

    public ChatChannelGUI(ChatChannel _c)
    {
        this.tc = GuiNewChatTC.tc;
        this.channel = _c;
        this.hidePrefix.setValue(Boolean.valueOf(_c.hidePrefix));
        this.notificationsOn.setValue(Boolean.valueOf(_c.notificationsOn));
        this.alias.setCharLimit(20);
        this.alias.setValue(_c.getAlias());
        this.cmdPrefix.setCharLimit(100);
        this.cmdPrefix.setValue(_c.cmdPrefix);
        this.resetTempVars();
        this.title = _c.getTitle();
    }

    public void actionPerformed(GuiButton _button)
    {
        switch (_button.id)
        {
            case 8981:
                this.channel.notificationsOn = this.notificationsOn.getTempValue().booleanValue();
                this.channel.setAlias(this.alias.getTempValue().trim());
                this.channel.cmdPrefix = this.cmdPrefix.getTempValue().trim();
                this.channel.hidePrefix = this.hidePrefix.getTempValue().booleanValue();
                this.tc.storeChannelData();

            case 8982:
                this.mc.displayGuiScreen((GuiScreen)null);
                break;

            case 8983:
                this.notificationsOn.actionPerformed();

            case 8984:
            case 8985:
            default:
                break;

            case 8986:
                if (this.position <= 2)
                {
                    return;
                }

                LinkedHashMap newMap = TabbyChatUtils.swapChannels(this.tc.channelMap, this.position - 2, this.position - 1);
                this.tc.channelMap.clear();
                this.tc.channelMap = newMap;
                --this.position;
                break;

            case 8987:
                if (this.position >= this.tc.channelMap.size())
                {
                    return;
                }

                LinkedHashMap newMap2 = TabbyChatUtils.swapChannels(this.tc.channelMap, this.position - 1, this.position);
                this.tc.channelMap.clear();
                this.tc.channelMap = newMap2;
                ++this.position;
                break;

            case 8988:
                this.hidePrefix.actionPerformed();
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int _x, int _y, float _f)
    {
        int var10000 = this.width;
        this.getClass();
        int leftX = (var10000 - 255) / 2;
        var10000 = this.height;
        this.getClass();
        int topY = (var10000 - 120) / 2;
        this.getClass();
        int rightX = leftX + 255;
        this.getClass();
        int var10002 = leftX + 255;
        this.getClass();
        drawRect(leftX, topY, var10002, topY + 120, -2013265920);
        int var10001 = topY + 14;
        this.getClass();
        drawRect(leftX, var10001, leftX + 255, topY + 15, -1996488705);
        this.drawString(this.mc.fontRenderer, this.title, leftX + 3, topY + 3, 11184810);
        this.drawString(this.mc.fontRenderer, Integer.toString(this.position), rightX - 34, topY + 22, 16777215);
        this.drawString(this.mc.fontRenderer, I18n.format("settings.channel.position", new Object[0]), rightX - 55 - this.mc.fontRenderer.getStringWidth(I18n.format("settings.channel.position", new Object[0])), topY + 22, 16777215);
        this.drawString(this.mc.fontRenderer, I18n.format("settings.channel.of", new Object[0]) + " " + this.tc.channelMap.size(), rightX - 34, topY + 35, 16777215);

        for (int i = 0; i < this.buttonList.size(); ++i)
        {
            ((GuiButton)this.buttonList.get(i)).drawButton(this.mc, _x, _y);
        }
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        int var10000 = this.width;
        this.getClass();
        int leftX = (var10000 - 255) / 2;
        var10000 = this.height;
        this.getClass();
        int topY = (var10000 - 120) / 2;
        this.getClass();
        int rightX = leftX + 255;
        this.getClass();
        int botY = topY + 120;
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        PrefsButton savePrefs = new PrefsButton(8981, rightX - 45, botY - 19, 40, 14, I18n.format("settings.save", new Object[0]));
        this.buttonList.add(savePrefs);
        PrefsButton cancelPrefs = new PrefsButton(8982, rightX - 90, botY - 19, 40, 14, I18n.format("settings.cancel", new Object[0]));
        this.buttonList.add(cancelPrefs);
        PrefsButton nextButton = new PrefsButton(8987, rightX - 20, topY + 20, 15, 14, ">>");
        this.buttonList.add(nextButton);
        PrefsButton prevButton = new PrefsButton(8986, rightX - 50, topY + 20, 15, 14, "<<");
        this.buttonList.add(prevButton);
        this.alias.setLabelLoc(leftX + 15);
        this.alias.setButtonLoc(leftX + 20 + this.mc.fontRenderer.getStringWidth(this.alias.description), topY + 20);
        this.alias.setButtonDims(70, 11);
        this.buttonList.add(this.alias);
        this.notificationsOn.setButtonLoc(leftX + 15, topY + 40);
        this.notificationsOn.setLabelLoc(leftX + 34);
        this.buttonList.add(this.notificationsOn);
        this.cmdPrefix.setLabelLoc(leftX + 15);
        this.cmdPrefix.setButtonLoc(leftX + 20 + this.mc.fontRenderer.getStringWidth(this.cmdPrefix.description), topY + 57);
        this.cmdPrefix.setButtonDims(100, 11);
        this.buttonList.add(this.cmdPrefix);
        this.hidePrefix.setButtonLoc(leftX + 15, topY + 78);
        this.hidePrefix.setLabelLoc(leftX + 34);
        this.buttonList.add(this.hidePrefix);
        this.position = 1;

        for (Iterator _chanPtr = this.tc.channelMap.keySet().iterator(); _chanPtr.hasNext() && !this.channel.getTitle().equals(_chanPtr.next()); ++this.position)
        {
            ;
        }

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
    protected void keyTyped(char par1, int par2)
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

    /**
     * Called when the mouse is clicked.
     */
    public void mouseClicked(int par1, int par2, int par3)
    {
        for (int i = 0; i < this.buttonList.size(); ++i)
        {
            if (ITCSetting.class.isInstance(this.buttonList.get(i)))
            {
                ITCSetting tmp = (ITCSetting)this.buttonList.get(i);

                if (tmp.getType() == "textbox")
                {
                    tmp.mouseClicked(par1, par2, par3);
                }
            }
        }

        super.mouseClicked(par1, par2, par3);
    }

    public void resetTempVars()
    {
        this.hidePrefix.reset();
        this.notificationsOn.reset();
        this.alias.reset();
        this.cmdPrefix.reset();
    }
}
