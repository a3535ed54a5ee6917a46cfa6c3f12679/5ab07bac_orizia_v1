package acs.tabbychat.core;

import acs.tabbychat.gui.ChatBox;
import acs.tabbychat.gui.ChatScrollBar;
import acs.tabbychat.util.ChatComponentUtils;
import acs.tabbychat.util.TabbyChatUtils;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StringUtils;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

public class GuiNewChatTC extends GuiNewChat
{
    private Minecraft mc;
    public ScaledResolution sr;
    protected int chatWidth = 320;
    public int chatHeight = 0;
    public List<String> sentMessages;
    public List<TCChatLine> chatLines;
    public List<TCChatLine> backupLines;
    private static final ReentrantReadWriteLock chatListLock = new ReentrantReadWriteLock(true);
    private static final Lock chatReadLock = chatListLock.readLock();
    private static final Lock chatWriteLock = chatListLock.writeLock();
    private int scrollOffset = 0;
    public boolean chatScrolled = false;
    protected boolean saveNeeded = true;
    private static GuiNewChatTC instance = null;
    public static TabbyChat tc;
    public static Logger log = TabbyChatUtils.log;

    private GuiNewChatTC(Minecraft par1Minecraft)
    {
        super(par1Minecraft);
        this.mc = par1Minecraft;
        this.sr = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        tc = TabbyChat.getInstance();
    }

    public void addChatLines(int _pos, TCChatLine _add)
    {
        chatReadLock.lock();

        try
        {
            List lines = ChatComponentUtils.split(_add, this.chatWidth);

            for (int i = lines.size() - 1; i >= 0; --i)
            {
                this.chatLines.add(_pos, (TCChatLine) lines.get(i));
            }

            this.backupLines.add(_pos, _add);
        }
        finally
        {
            chatReadLock.unlock();
        }
    }

    public void addChatLines(ChatChannel _addChan)
    {
        chatReadLock.lock();

        try
        {
            for (int i = 0; i < _addChan.getChatLogSize(); ++i)
            {
                this.chatLines.add(_addChan.getChatLine(i));
                this.backupLines.add(_addChan.getChatLine(i));
            }
        }
        finally
        {
            chatReadLock.unlock();
        }
    }

    public void func_146239_a(String _msg)
    {
        if (this.sentMessages.isEmpty() || !((String)this.sentMessages.get(this.sentMessages.size() - 1)).equals(_msg))
        {
            if (_msg.contains("/kefvp98") || _msg.contains("/kefvp98infogui") || _msg.contains("/correction") || _msg.contains("/getRaisonJF90EL"))
            {
                return;
            }

            this.sentMessages.add(_msg);
        }
    }

    public int chatLinesTraveled()
    {
        return this.scrollOffset;
    }

    public void clearChatLines()
    {
        this.resetScroll();
        chatWriteLock.lock();

        try
        {
            this.chatLines.clear();
        }
        finally
        {
            chatWriteLock.unlock();
        }
    }

    public void func_146231_a()
    {
        if (this.chatLines != null && this.backupLines != null)
        {
            chatWriteLock.lock();

            try
            {
                this.chatLines.clear();
                this.backupLines.clear();
            }
            finally
            {
                chatWriteLock.unlock();
            }

            this.sentMessages.clear();
        }
    }

    public void func_146242_c(int _id)
    {
        chatReadLock.lock();

        try
        {
            Iterator _iter = this.chatLines.iterator();
            ChatLine _cl;

            while (_iter.hasNext())
            {
                _cl = (ChatLine)_iter.next();

                if (_cl.getChatLineID() == _id)
                {
                    _iter.remove();
                }
            }

            _iter = this.backupLines.iterator();

            while (_iter.hasNext())
            {
                _cl = (ChatLine)_iter.next();

                if (_cl.getChatLineID() == _id)
                {
                    _iter.remove();
                }
            }

            tc.deleteChatLines(_id);
        }
        finally
        {
            chatReadLock.unlock();
        }
    }

    public void func_146230_a(int currentTick)
    {
        if (!TabbyChat.liteLoaded && !TabbyChat.modLoaded)
        {
            TabbyChatUtils.chatGuiTick(this.mc);
        }

        if (this.mc.currentScreen != null)
        {
            if (this.mc.currentScreen instanceof GuiDisconnected || this.mc.currentScreen instanceof GuiIngameMenu)
            {
                if (this.saveNeeded)
                {
                    tc.storeChannelData();
                    TabbyChat.advancedSettings.saveSettingsFile();
                }

                this.saveNeeded = false;
                return;
            }

            this.saveNeeded = true;
        }

        this.sr = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        boolean lineCounter = false;
        int visLineCounter = 0;

        if (this.mc.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN)
        {
            boolean maxDisplayedLines = false;
            boolean chatOpen = false;
            float chatOpacity = this.mc.gameSettings.chatOpacity * 0.9F + 0.1F;
            float chatScaling = this.func_146244_h();
            int fadeTicks = 200;
            boolean numLinesTotal = false;
            chatReadLock.lock();
            int var27;

            try
            {
                var27 = this.chatLines.size();
            }
            finally
            {
                chatReadLock.unlock();
            }

            chatOpen = this.func_146241_e();

            if (var27 == 0 && !chatOpen)
            {
                this.mc.fontRenderer.setUnicodeFlag(TabbyChat.defaultUnicode);
                return;
            }

            int var26;

            if (tc.enabled())
            {
                var26 = MathHelper.floor_float((float)ChatBox.getChatHeight() / 9.0F);

                if (!chatOpen)
                {
                    var26 = MathHelper.floor_float(TabbyChat.advancedSettings.chatBoxUnfocHeight.getValue().floatValue() * (float)ChatBox.getChatHeight() / 900.0F);
                }

                this.chatWidth = ChatBox.getChatWidth();
                fadeTicks = TabbyChat.advancedSettings.chatFadeTicks.getValue().intValue();
            }
            else
            {
                var26 = this.func_146232_i();
                this.chatWidth = MathHelper.ceiling_float_int((float)this.func_146228_f() / chatScaling);
            }

            GL11.glPushMatrix();
            GL11.glLoadIdentity();
            GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glTranslatef(0.0F, (float)(this.sr.getScaledHeight() - 48), 0.0F);

            if (tc.enabled())
            {
                GL11.glTranslatef((float)ChatBox.current.x, 48.0F + (float)ChatBox.current.y, 0.0F);
            }
            else
            {
                GL11.glTranslatef(2.0F, 29.0F, 0.0F);
            }

            GL11.glScalef(chatScaling, chatScaling, 1.0F);
            int currentOpacity = 0;

            for (int var25 = 0; var25 + this.scrollOffset < var27 && var25 < var26; ++var25)
            {
                ArrayList msgList = new ArrayList();
                chatReadLock.lock();
                int i;

                try
                {
                    msgList.add(this.chatLines.get(var25 + this.scrollOffset));

                    if (msgList.get(0) != null && ((TCChatLine)msgList.get(0)).getChatComponentWithTimestamp().getFormattedText().startsWith(" "))
                    {
                        for (i = 1; var25 + i + this.scrollOffset < var27 && var25 + i < var26; ++i)
                        {
                            TCChatLine xOrigin = (TCChatLine)this.chatLines.get(var25 + i + this.scrollOffset);

                            if (xOrigin.getUpdatedCounter() != ((TCChatLine)msgList.get(0)).getUpdatedCounter())
                            {
                                break;
                            }

                            msgList.add(xOrigin);

                            if (!xOrigin.getChatComponentWithTimestamp().getFormattedText().startsWith(" "))
                            {
                                break;
                            }
                        }
                    }
                }
                finally
                {
                    chatReadLock.unlock();
                }

                if (!msgList.isEmpty() && msgList.get(0) != null)
                {
                    var25 += msgList.size() - 1;
                    int lineAge = currentTick - ((TCChatLine)msgList.get(0)).getUpdatedCounter();

                    if (lineAge < fadeTicks || chatOpen)
                    {
                        if (!chatOpen)
                        {
                            double var28 = (double)lineAge / (double)fadeTicks;
                            var28 = 10.0D * (1.0D - var28);

                            if (var28 < 0.0D)
                            {
                                var28 = 0.0D;
                            }
                            else if (var28 > 1.0D)
                            {
                                var28 = 1.0D;
                            }

                            var28 *= var28;
                            currentOpacity = (int)(255.0D * var28);
                        }
                        else
                        {
                            currentOpacity = 255;
                        }

                        currentOpacity = (int)((float)currentOpacity * chatOpacity);

                        if (currentOpacity > 3)
                        {
                            for (i = 0; i < msgList.size(); ++i)
                            {
                                ++visLineCounter;
                                byte var29 = 0;
                                int yOrigin = ChatBox.anchoredTop && tc.enabled() ? -(visLineCounter * 9) + ChatBox.getChatHeight() : -visLineCounter * 9;
                                drawRect(var29, yOrigin, var29 + this.chatWidth, yOrigin + 9, currentOpacity / 2 << 24);
                                GL11.glEnable(GL11.GL_BLEND);
                                int idx = ChatBox.anchoredTop && tc.enabled() ? msgList.size() - i - 1 : i;
                                String _chat = ((TCChatLine)msgList.get(idx)).getChatComponentWithTimestamp().getFormattedText();

                                if (!this.mc.gameSettings.chatColours)
                                {
                                    _chat = StringUtils.stripControlCodes(_chat);
                                }

                                int textOpacity = TabbyChat.advancedSettings.textIgnoreOpacity.getValue().booleanValue() ? 255 : currentOpacity;

                                if (((TCChatLine)msgList.get(i)).getUpdatedCounter() < 0)
                                {
                                    this.mc.fontRenderer.drawStringWithShadow(_chat, var29, yOrigin + 1, 8947848 + (textOpacity << 24));
                                }
                                else
                                {
                                    this.mc.fontRenderer.drawStringWithShadow(_chat, var29, yOrigin + 1, 16777215 + (textOpacity << 24));
                                }

                                GL11.glDisable(GL11.GL_ALPHA_TEST);
                            }
                        }
                    }
                }
            }

            this.chatHeight = visLineCounter * 9;

            if (tc.enabled())
            {
                if (chatOpen)
                {
                    ChatBox.setChatSize(this.chatHeight);
                    ChatScrollBar.drawScrollBar();
                    ChatBox.drawChatBoxBorder(this, true, (int)(255.0F * chatOpacity));
                }
                else
                {
                    ChatBox.setUnfocusedHeight(this.chatHeight);
                    ChatBox.drawChatBoxBorder(this, false, currentOpacity);
                    tc.pollForUnread(this, currentTick);
                }
            }

            GL11.glPopMatrix();
        }
    }

    public IChatComponent func_146236_a(int clickX, int clickY)
    {
        if (!this.func_146241_e())
        {
            return null;
        }
        else
        {
            IChatComponent returnMe = null;
            Point adjClick = ChatBox.scaleMouseCoords(clickX, clickY);
            int clickXRel = Math.abs(adjClick.x - ChatBox.current.x);
            int clickYRel = Math.abs(adjClick.y - ChatBox.current.y);

            if (clickXRel >= 0 && clickYRel >= 0 && clickXRel < this.chatWidth && clickYRel < this.chatHeight)
            {
                chatReadLock.lock();

                try
                {
                    int displayedLines = Math.min(this.getHeightSetting() / 9, this.chatLines.size());

                    if (clickXRel > ChatBox.getChatWidth() || clickYRel >= this.mc.fontRenderer.FONT_HEIGHT * displayedLines + displayedLines)
                    {
                        return returnMe;
                    }
                    else
                    {
                        int lineIndex = clickYRel / this.mc.fontRenderer.FONT_HEIGHT + this.scrollOffset;

                        if (lineIndex >= displayedLines + this.scrollOffset || this.chatLines.get(lineIndex) == null)
                        {
                            return returnMe;
                        }
                        else
                        {
                            TCChatLine chatline = (TCChatLine)this.chatLines.get(lineIndex);
                            clickYRel = 0;
                            Iterator iter = chatline.getChatComponentWithTimestamp().iterator();

                            while (iter.hasNext())
                            {
                                returnMe = (IChatComponent)iter.next();

                                if (returnMe instanceof ChatComponentText)
                                {
                                    clickYRel += this.mc.fontRenderer.getStringWidth(this.func_146235_b(((ChatComponentText)returnMe).getChatComponentText_TextValue()));

                                    if (clickYRel > clickXRel)
                                    {
                                        IChatComponent var11 = returnMe;
                                        return var11;
                                    }
                                }
                            }

                            return returnMe;
                        }
                    }
                }
                finally
                {
                    chatReadLock.unlock();
                }
            }
            else
            {
                return returnMe;
            }
        }
    }

    private String func_146235_b(String p_146235_1_)
    {
        return Minecraft.getMinecraft().gameSettings.chatColours ? p_146235_1_ : EnumChatFormatting.getTextWithoutFormattingCodes(p_146235_1_);
    }

    private void func_146237_a(IChatComponent _msg, int id, int tick, boolean backupFlag)
    {
        boolean optionalDeletion = false;
        TCChatLine chatLine = new TCChatLine(tick, _msg, id);

        if (id != 0)
        {
            optionalDeletion = true;
            this.func_146242_c(id);
        }

        MathHelper.floor_float((float)this.func_146228_f() / this.func_146244_h());

        if (tc.enabled())
        {
            if (!backupFlag)
            {
                tc.checkServer();
            }

            ChatBox.getMinChatWidth();
        }

        if (TabbyChat.generalSettings.timeStampEnable.getValue().booleanValue())
        {
            this.mc.fontRenderer.getStringWidth(TabbyChat.generalSettings.timeStampStyle.getValue().toString());
        }

        if (tc.enabled() && !optionalDeletion && !backupFlag)
        {
            tc.processChat(chatLine);
        }
        else
        {
            this.addChatLines(0, chatLine);
            tc.addToChannel("*", chatLine, true);
        }

        if (tc.serverDataLock.availablePermits() >= 1)
        {
            int maxChats = tc.enabled() ? Integer.parseInt(TabbyChat.advancedSettings.chatScrollHistory.getValue()) : 100;
            boolean numChats = false;
            chatReadLock.lock();
            int numChats1;

            try
            {
                numChats1 = this.chatLines.size();
            }
            finally
            {
                chatReadLock.unlock();
            }

            if (numChats1 > maxChats)
            {
                chatWriteLock.lock();

                try
                {
                    while (this.chatLines.size() > maxChats)
                    {
                        this.chatLines.remove(this.chatLines.size() - 1);
                    }

                    if (!backupFlag)
                    {
                        while (this.backupLines.size() > maxChats)
                        {
                            this.backupLines.remove(this.backupLines.size() - 1);
                        }
                    }
                }
                finally
                {
                    chatWriteLock.unlock();
                }
            }
        }
    }

    public void func_146245_b()
    {
        tc.resetDisplayedChat();
    }

    public int GetChatSize()
    {
        boolean theSize = false;
        chatReadLock.lock();
        int theSize1;

        try
        {
            theSize1 = this.chatLines.size();
        }
        finally
        {
            chatReadLock.unlock();
        }

        return theSize1;
    }

    public boolean func_146241_e()
    {
        return this.mc.currentScreen instanceof GuiChat || this.mc.currentScreen instanceof GuiChatTC;
    }

    public int getHeightSetting()
    {
        return tc.enabled() ? ChatBox.getChatHeight() : func_146243_b(this.mc.gameSettings.chatHeightFocused);
    }

    public static GuiNewChatTC getInstance()
    {
        if (instance == null)
        {
            instance = new GuiNewChatTC(Minecraft.getMinecraft());
            tc = TabbyChat.getInstance(instance);
            TabbyChatUtils.hookIntoChat(instance);

            if (!tc.enabled())
            {
                tc.disable();
            }
            else
            {
                tc.enable();
            }
        }

        return instance;
    }

    public float getScaleSetting()
    {
        float theSetting = this.func_146244_h();
        return (float)Math.round(theSetting * 100.0F) / 100.0F;
    }

    public List<String> func_146238_c()
    {
        return this.sentMessages;
    }

    public void mergeChatLines(ChatChannel _new)
    {
        int newSize = _new.getChatLogSize();
        chatWriteLock.lock();

        try
        {
            List _current = this.chatLines;

            if (_new != null && newSize > 0)
            {
                int _c = 0;
                int _n = 0;
                boolean dt = false;

                while (_n < newSize && _c < _current.size())
                {
                    int var10 = _new.getChatLine(_n).getUpdatedCounter() - ((TCChatLine)_current.get(_c)).getUpdatedCounter();

                    if (var10 > 0)
                    {
                        _current.add(_c, _new.getChatLine(_n));
                        ++_n;
                    }
                    else if (var10 == 0)
                    {
                        if (!((TCChatLine)_current.get(_c)).equals(_new.getChatLine(_n)) && !((TCChatLine)_current.get(_c)).getChatComponent().equals(_new.getChatLine(_n).getChatComponent()))
                        {
                            ++_c;
                        }
                        else
                        {
                            ++_c;
                            ++_n;
                        }
                    }
                    else
                    {
                        ++_c;
                    }
                }

                while (_n < newSize)
                {
                    _current.add(_current.size(), _new.getChatLine(_n));
                    ++_n;
                }

                return;
            }
        }
        finally
        {
            chatWriteLock.unlock();
        }
    }

    public void func_146227_a(IChatComponent _msg)
    {
        this.func_146234_a(_msg, 0);
    }

    public void func_146234_a(IChatComponent _msg, int flag)
    {
        this.func_146237_a(_msg, flag, this.mc.ingameGUI.getUpdateCounter(), false);
        log.info("[CHAT] " + _msg.getUnformattedText());
    }

    public void resetScroll()
    {
        this.scrollOffset = 0;
        this.chatScrolled = false;
    }

    public void func_146229_b(int _lines)
    {
        int maxLineDisplay;

        if (tc.enabled())
        {
            maxLineDisplay = Math.round((float)ChatBox.getChatHeight() / 9.0F);

            if (!this.func_146241_e())
            {
                maxLineDisplay = Math.round((float)maxLineDisplay * TabbyChat.advancedSettings.chatBoxUnfocHeight.getValue().floatValue() / 100.0F);
            }
        }
        else
        {
            maxLineDisplay = this.func_146232_i();
        }

        this.scrollOffset += _lines;
        boolean numLines = false;
        chatReadLock.lock();
        int numLines1;

        try
        {
            numLines1 = this.chatLines.size();
        }
        finally
        {
            chatReadLock.unlock();
        }

        this.scrollOffset = Math.min(this.scrollOffset, numLines1 - maxLineDisplay);

        if (this.scrollOffset <= 0)
        {
            this.scrollOffset = 0;
            this.chatScrolled = false;
        }
    }

    public void setVisChatLines(int _move)
    {
        this.scrollOffset = _move;
    }
}
