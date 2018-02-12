package acs.tabbychat.gui.context;

import acs.tabbychat.core.GuiChatTC;
import acs.tabbychat.gui.ChatBox;
import com.google.common.collect.Lists;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

public class ChatContextMenu extends Gui
{
    private static List<ChatContext> registered = Lists.newArrayList();
    private Minecraft mc = Minecraft.getMinecraft();
    private ScaledResolution sr;
    protected List<ChatContext> items;
    public ChatContext parent;
    public GuiChatTC screen;
    public int xPos;
    public int yPos;
    public int width;
    public int height;

    public ChatContextMenu(GuiChatTC chat, int x, int y)
    {
        this.items = registered;
        this.screen = chat;
        this.setup(chat, x, y);
    }

    ChatContextMenu(ChatContext parent, int x, int y, List<ChatContext> items)
    {
        this.parent = parent;
        this.items = items;
        this.screen = parent.getMenu().screen;
        this.setup(this.screen, x, y);
    }

    private void setup(GuiChatTC chat, int x, int y)
    {
        this.sr = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        this.xPos = x;
        this.yPos = y;
        this.width = 100;

        if (x > this.sr.getScaledWidth() - this.width)
        {
            if (this.parent == null)
            {
                this.xPos -= this.width;
            }
            else
            {
                this.xPos -= this.width * 2;
            }
        }

        ArrayList visible = Lists.newArrayList();
        Iterator i = this.items.iterator();

        while (i.hasNext())
        {
            ChatContext item = (ChatContext)i.next();
            item.menu = this;
            item.enabled = item.isPositionValid(this.xPos, this.yPos);

            if (item.enabled || item.getDisabledBehavior() != ChatContext.Behavior.HIDE)
            {
                visible.add(item);
            }
        }

        this.height = visible.size() * 15;

        if (this.yPos + this.height > this.sr.getScaledHeight())
        {
            this.yPos -= this.height;

            if (this.parent != null)
            {
                this.yPos += 15;
            }
        }

        if (this.yPos < 0)
        {
            this.yPos = 0;
        }

        int var8 = 0;

        for (Iterator var9 = visible.iterator(); var9.hasNext(); ++var8)
        {
            ChatContext item1 = (ChatContext)var9.next();
            item1.id = var8;
            item1.field_146128_h = this.xPos;
            item1.field_146129_i = this.yPos + var8 * 15;
        }
    }

    public void drawMenu(int x, int y)
    {
        Point scaled = ChatBox.scaleMouseCoords(x, y, true);
        Iterator var4 = this.items.iterator();

        while (var4.hasNext())
        {
            ChatContext item = (ChatContext)var4.next();

            if (item.enabled || item.getDisabledBehavior() != ChatContext.Behavior.HIDE)
            {
                item.drawButton(this.mc, scaled.x, scaled.y);
            }
        }
    }

    public boolean mouseClicked(int mouseX, int mouseY)
    {
        Iterator var3 = this.items.iterator();
        ChatContext item;

        do
        {
            if (!var3.hasNext())
            {
                return false;
            }

            item = (ChatContext)var3.next();
        }
        while (!item.enabled || !item.isHoveredWithChildren(mouseX, mouseY));

        return item.mouseClicked(mouseX, mouseY);
    }

    public void buttonClicked(ChatContext item)
    {
        item.onClicked();
    }

    public static void addContext(ChatContext item)
    {
        registered.add(item);
    }

    public boolean isCursorOver(int x, int y)
    {
        boolean children = false;
        Iterator var4 = this.items.iterator();

        while (var4.hasNext())
        {
            ChatContext cont = (ChatContext)var4.next();

            if (cont.isHoveredWithChildren(x, y) && cont.children != null)
            {
                children = cont.children.isCursorOver(x, y);
            }

            if (children)
            {
                break;
            }
        }

        return x > this.xPos && x < this.xPos + this.width && y > this.yPos && y < this.yPos + this.height || children;
    }

    public static void insertContextAtPos(int pos, ChatContext item)
    {
        registered.add(pos, item);
    }

    public static void removeContext(ChatContext item)
    {
        registered.remove(item);
    }

    public static void removeContext(int pos)
    {
        registered.remove(pos);
    }

    public static List<ChatContext> getRegisteredMenus()
    {
        return registered;
    }
}
