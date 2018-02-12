package acs.tabbychat.core;

import acs.tabbychat.gui.ChatBox;
import acs.tabbychat.gui.ChatButton;
import acs.tabbychat.util.ChatComponentUtils;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.gson.annotations.Expose;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.opengl.GL11;

public class ChatChannel
{
    protected static int nextID = 3600;
    public ChatButton tab;
    private final ReentrantReadWriteLock chatListLock;
    private final Lock chatReadLock;
    private final Lock chatWriteLock;
    public boolean unread;
    protected boolean hasSpam;
    protected int spamCount;
    private File logFile;
    @Expose
    protected int chanID;
    @Expose
    private String title;
    @Expose
    private String alias;
    @Expose
    public boolean active;
    @Expose
    public boolean notificationsOn;
    @Expose
    public boolean hidePrefix;
    @Expose
    public String cmdPrefix;
    @Expose
    private ArrayList<TCChatLine> chatLog;
    private Supplier<List<TCChatLine>> supplier;

    public ChatChannel()
    {
        this.chatListLock = new ReentrantReadWriteLock(true);
        this.chatReadLock = this.chatListLock.readLock();
        this.chatWriteLock = this.chatListLock.writeLock();
        this.unread = false;
        this.hasSpam = false;
        this.spamCount = 1;
        this.chanID = nextID + 1;
        this.active = false;
        this.notificationsOn = false;
        this.hidePrefix = false;
        this.cmdPrefix = "";
        this.supplier = Suppliers.memoizeWithExpiration(new Supplier()
        {
            public List<TCChatLine> get()
            {
                return ChatChannel.this.getSplitChat(true);
            }
        }, 50L, TimeUnit.MILLISECONDS);
        this.chanID = nextID++;
        this.chatLog = new ArrayList();
        this.notificationsOn = TabbyChat.generalSettings.unreadFlashing.getValue().booleanValue();
    }

    public ChatChannel(int _x, int _y, int _w, int _h, String _title)
    {
        this();
        this.tab = new ChatButton(this.chanID, _x, _y, _w, _h, _title);
        this.title = _title;
        this.alias = this.title;
        this.tab.channel = this;
        this.tab.width(TabbyChat.mc.fontRenderer.getStringWidth(this.alias + "<>") + 8);
    }

    public ChatChannel(String _title)
    {
        this(3, 3, Minecraft.getMinecraft().fontRenderer.getStringWidth("<" + _title + ">") + 8, 14, _title);
    }

    public void addChat(TCChatLine newChat, boolean visible)
    {
        this.chatWriteLock.lock();

        try
        {
            this.chatLog.add(0, newChat);
        }
        finally
        {
            this.chatWriteLock.unlock();
        }

        if (!this.title.equals("*") && this.notificationsOn && !visible)
        {
            this.unread = true;
        }
    }

    public void deleteChatLines(int id)
    {
        this.chatReadLock.lock();

        try
        {
            Iterator iter = this.chatLog.iterator();

            while (iter.hasNext())
            {
                TCChatLine line = (TCChatLine)iter.next();

                if (line.getChatLineID() == id)
                {
                    iter.remove();
                }
            }
        }
        finally
        {
            this.chatReadLock.unlock();
        }
    }

    public boolean doesButtonEqual(GuiButton btnObj)
    {
        return this.tab.id == btnObj.id;
    }

    public String getAlias()
    {
        return this.alias;
    }

    public int getButtonEnd()
    {
        return this.tab.x() + this.tab.width();
    }

    public TCChatLine getChatLine(int index)
    {
        TCChatLine retVal = null;
        this.chatReadLock.lock();
        List lines = this.getSplitChat(false);

        try
        {
            retVal = (TCChatLine)lines.get(index);
        }
        finally
        {
            this.chatReadLock.unlock();
        }

        return retVal;
    }

    public int getChatLogSize()
    {
        boolean mySize = false;
        this.chatReadLock.lock();
        int mySize1;

        try
        {
            mySize1 = this.getSplitChat(false).size();
        }
        finally
        {
            this.chatReadLock.unlock();
        }

        return mySize1;
    }

    private List<TCChatLine> getSplitChat(boolean force)
    {
        return !force ? (List)this.supplier.get() : ChatComponentUtils.split((List)this.chatLog, ChatBox.getChatWidth());
    }

    public int getID()
    {
        return this.chanID;
    }

    public String getDisplayTitle()
    {
        return this.active ? "[" + this.alias + "]" : (this.unread ? "<" + this.alias + ">" : this.alias);
    }

    public String getTitle()
    {
        return this.title;
    }

    public File getLogFile()
    {
        return this.logFile;
    }

    public void setLogFile(File file)
    {
        this.logFile = file;
    }

    public void setButtonObj(ChatButton btnObj)
    {
        this.tab = btnObj;
        this.tab.channel = this;
    }

    public void setAlias(String _alias)
    {
        this.alias = _alias;
        this.tab.width(TabbyChat.mc.fontRenderer.getStringWidth(_alias + "<>") + 8);
    }

    public String toString()
    {
        return this.getDisplayTitle();
    }

    public void clear()
    {
        this.chatWriteLock.lock();

        try
        {
            this.chatLog.clear();
        }
        finally
        {
            this.chatWriteLock.unlock();
        }

        this.tab = null;
    }

    public void setButtonLoc(int _x, int _y)
    {
        this.tab.x(_x);
        this.tab.y(_y);
    }

    protected void setChatLogLine(int ind, TCChatLine newLine)
    {
        this.chatWriteLock.lock();

        try
        {
            if (ind < this.chatLog.size())
            {
                this.chatLog.set(ind, newLine);
            }
            else
            {
                this.chatLog.add(newLine);
            }
        }
        finally
        {
            this.chatWriteLock.unlock();
        }

        GuiNewChatTC.getInstance().func_146245_b();
    }

    public void removeChatLine(int pos)
    {
        this.chatWriteLock.lock();

        try
        {
            if (pos < this.chatLog.size() && pos >= 0)
            {
                this.chatLog.remove(pos);
            }
        }
        finally
        {
            this.chatWriteLock.unlock();
        }

        GuiNewChatTC.getInstance().func_146245_b();
    }

    public void trimLog()
    {
        TabbyChat tc = GuiNewChatTC.tc;

        if (tc != null && tc.serverDataLock.availablePermits() >= 1)
        {
            int maxChats = tc.enabled() ? Integer.parseInt(TabbyChat.advancedSettings.chatScrollHistory.getValue()) : 100;
            this.chatWriteLock.lock();

            try
            {
                while (this.chatLog.size() > maxChats)
                {
                    this.chatLog.remove(this.chatLog.size() - 1);
                }
            }
            finally
            {
                this.chatWriteLock.unlock();
            }
        }
    }

    public void unreadNotify(Gui _gui, int _opacity)
    {
        Minecraft mc = Minecraft.getMinecraft();
        GuiNewChatTC gnc = GuiNewChatTC.getInstance();
        int tabY = this.tab.y() - gnc.sr.getScaledHeight() - ChatBox.current.y;
        tabY = ChatBox.anchoredTop ? tabY - ChatBox.getChatHeight() + ChatBox.getUnfocusedHeight() : tabY + ChatBox.getChatHeight() - ChatBox.getUnfocusedHeight() + 1;
        Gui.drawRect(this.tab.x(), tabY, this.tab.x() + this.tab.width(), tabY + this.tab.height(), 7471104 + (_opacity / 2 << 24));
        GL11.glEnable(GL11.GL_BLEND);
        mc.ingameGUI.getChatGUI().drawCenteredString(mc.fontRenderer, this.getDisplayTitle(), this.tab.x() + this.tab.width() / 2, tabY + 4, 16711680 + (_opacity << 24));
    }

    protected void importOldChat(ChatChannel oldChan)
    {
        if (oldChan != null && !oldChan.chatLog.isEmpty())
        {
            this.chatWriteLock.lock();

            try
            {
                Iterator var2 = oldChan.chatLog.iterator();

                while (var2.hasNext())
                {
                    TCChatLine oldChat = (TCChatLine)var2.next();

                    if (oldChat != null && !oldChat.statusMsg)
                    {
                        this.chatLog.add(oldChat);
                    }
                }
            }
            finally
            {
                this.chatWriteLock.unlock();
            }

            this.trimLog();
        }
    }
}
