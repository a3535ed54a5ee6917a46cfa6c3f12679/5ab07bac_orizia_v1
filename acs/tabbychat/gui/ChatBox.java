package acs.tabbychat.gui;

import acs.tabbychat.core.ChatChannel;
import acs.tabbychat.core.GuiNewChatTC;
import acs.tabbychat.core.TabbyChat;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Iterator;
import java.util.LinkedHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Mouse;

public class ChatBox
{
    public static Rectangle current = new Rectangle(0, -36, 320, 180);
    public static Rectangle desired = new Rectangle(current);
    protected static int absMinX = 0;
    protected static int absMinY = -36;
    protected static int absMinW = 200;
    protected static int absMinH = 24;
    private static int tabHeight = 14;
    protected static int tabTrayHeight = 14;
    private static int chatHeight = 165;
    public static int unfocusedHeight = 160;
    public static boolean dragging = false;
    private static Point dragStart = new Point(0, 0);
    public static boolean resizing = false;
    public static boolean anchoredTop = false;
    public static boolean pinned = false;
    private static GuiNewChatTC gnc = GuiNewChatTC.getInstance();

    public static void addRowToTray()
    {
        float sf = gnc.getScaleSetting();
        int sh = MathHelper.floor_float((float)(gnc.sr.getScaledHeight() + current.y) / sf - (float)current.y);
        tabTrayHeight += tabHeight;

        if (current.height + tabHeight - absMinY > sh)
        {
            current.y = anchoredTop ? -sh + 1 : -sh + 1 + current.height;
            current.height = sh + absMinY - 3;
        }
        else if (!anchoredTop && current.y - current.height - tabHeight - 1 < -sh)
        {
            current.y = -sh + current.height + 1;
            current.height += tabHeight;
        }
        else if (anchoredTop && current.y + current.height + tabHeight > absMinY)
        {
            current.height += tabHeight;
            current.y = absMinY - current.height;
        }
        else
        {
            current.height += tabHeight;
        }
    }

    public static void drawChatBoxBorder(Gui overlay, boolean chatOpen, int opacity)
    {
        int borderColor = 0 + (2 * opacity / 3 << 24);
        int trayColor = 0 + (opacity / 3 << 24);
        int highlightColor = 16777120 + (2 * opacity / 3 << 24);
        int handleColor = resizeHovered() ? highlightColor : borderColor;
        int pinColor = pinHovered() ? highlightColor : borderColor;

        if (chatOpen)
        {
            if (!anchoredTop)
            {
                Gui.drawRect(-1, -current.height - 1, current.width + 1, -current.height, borderColor);
                Gui.drawRect(-1, -current.height, 0, 0, borderColor);
                Gui.drawRect(current.width, -current.height, current.width + 1, 0, borderColor);
                Gui.drawRect(-1, 0, current.width + 1, 1, borderColor);
                Gui.drawRect(0, -current.height + tabTrayHeight, current.width, -current.height + tabTrayHeight + 1, borderColor);
                Gui.drawRect(0, -current.height, current.width, -current.height + tabTrayHeight, trayColor);
                Gui.drawRect(0, -current.height + tabTrayHeight + 1, current.width - ChatScrollBar.barWidth - 2, -chatHeight, opacity / 2 << 24);
                Gui.drawRect(current.width - 7, -current.height + 2, current.width - 2, -current.height + 3, handleColor);
                Gui.drawRect(current.width - 3, -current.height + 3, current.width - 2, -current.height + 7, handleColor);
                Gui.drawRect(current.width - 14, -current.height + 2, current.width - 9, -current.height + 7, pinColor);

                if (pinned)
                {
                    Gui.drawRect(current.width - 13, -current.height + 3, current.width - 10, -current.height + 6, highlightColor);
                }
            }
            else
            {
                Gui.drawRect(-1, -1, current.width + 1, 0, borderColor);
                Gui.drawRect(-1, 0, 0, current.height, borderColor);
                Gui.drawRect(current.width, 0, current.width + 1, current.height, borderColor);
                Gui.drawRect(-1, current.height, current.width + 1, current.height + 1, borderColor);
                Gui.drawRect(0, current.height - tabTrayHeight, current.width, current.height - tabTrayHeight - 1, borderColor);
                Gui.drawRect(0, current.height, current.width, current.height - tabTrayHeight, trayColor);
                Gui.drawRect(0, current.height - tabTrayHeight - chatHeight - 1, current.width - ChatScrollBar.barWidth - 2, 0, opacity / 2 << 24);
                Gui.drawRect(current.width - 7, current.height - 2, current.width - 2, current.height - 3, handleColor);
                Gui.drawRect(current.width - 3, current.height - 3, current.width - 2, current.height - 7, handleColor);
                Gui.drawRect(current.width - 14, current.height - 2, current.width - 9, current.height - 7, pinColor);

                if (pinned)
                {
                    Gui.drawRect(current.width - 13, current.height - 3, current.width - 10, current.height - 6, highlightColor);
                }
            }
        }
        else if (unfocusedHeight > 0)
        {
            if (!anchoredTop)
            {
                Gui.drawRect(-1, -unfocusedHeight - 1, current.width + 1, -unfocusedHeight, borderColor);
                Gui.drawRect(-1, -unfocusedHeight, 0, 0, borderColor);
                Gui.drawRect(current.width, -unfocusedHeight, current.width + 1, 0, borderColor);
                Gui.drawRect(-1, 0, current.width + 1, 1, borderColor);
            }
            else
            {
                int y = getChatHeight() - unfocusedHeight;
                Gui.drawRect(-1, y + unfocusedHeight, current.width + 1, y + unfocusedHeight + 1, borderColor);
                Gui.drawRect(-1, y + unfocusedHeight, 0, y, borderColor);
                Gui.drawRect(current.width, y + unfocusedHeight, current.width + 1, y, borderColor);
                Gui.drawRect(-1, y, current.width + 1, y - 1, borderColor);
            }
        }
    }

    public static void enforceScreenBoundary(Rectangle newBounds)
    {
        float scaleSetting = gnc.getScaleSetting();
        int scaledWidth = Math.round((float)(gnc.sr.getScaledWidth() - current.x) / scaleSetting + (float)current.x);
        int scaledHeight = Math.round((float)(gnc.sr.getScaledHeight() + current.y) / scaleSetting - (float)current.y);
        current.setBounds(newBounds);

        if (gnc.sr.getScaledHeight() < -current.y)
        {
            scaledHeight = gnc.sr.getScaledHeight();
        }

        if (gnc.sr.getScaledWidth() < current.x)
        {
            scaledWidth = gnc.sr.getScaledWidth();
        }

        if (current.height < absMinH)
        {
            current.height = absMinH;
        }

        if (current.width < absMinW)
        {
            current.width = absMinW;
        }

        if (current.height > scaledHeight - 2)
        {
            current.height = scaledHeight - 2;

            if (anchoredTop)
            {
                current.y = -scaledHeight + 1;
            }
            else
            {
                current.y = -scaledHeight + current.height + 1;
            }
        }

        if (current.width > scaledWidth - 2)
        {
            current.width = scaledWidth - 2;
            current.x = 0;
        }

        if (current.x < absMinX + 1)
        {
            current.x = absMinX + 1;
        }

        if (current.x + current.width + 1 > scaledWidth)
        {
            if (resizing)
            {
                current.width = scaledWidth - current.x - 1;
            }
            else
            {
                current.x = scaledWidth - current.width - 1;
            }
        }

        if (anchoredTop)
        {
            if (current.y - 1 < -scaledHeight)
            {
                current.y = -scaledHeight + 1;
            }
            else if (current.y + current.height + 1 > absMinY)
            {
                if (resizing)
                {
                    current.height = absMinY - current.y - 1;
                }
                else
                {
                    current.y = absMinY - current.height - 1;
                }
            }
        }
        else if (current.y + 1 > absMinY)
        {
            current.y = absMinY - 1;
        }
        else if (current.y - current.height - 1 < -scaledHeight)
        {
            if (resizing)
            {
                current.height = scaledHeight + current.y - 1;
            }
            else
            {
                current.y = current.height + 1 - scaledHeight;
            }
        }
    }

    public static int getChatHeight()
    {
        return current.height - tabTrayHeight - 1;
    }

    public static int getChatWidth()
    {
        return gnc.func_146241_e() ? current.width - ChatScrollBar.barWidth - 2 : current.width;
    }

    public static int getMinChatWidth()
    {
        return current.width - ChatScrollBar.barWidth - 2;
    }

    public static int getUnfocusedHeight()
    {
        return unfocusedHeight;
    }

    public static void handleMouseDrag(int _curX, int _curY)
    {
        if (dragging)
        {
            Point click = scaleMouseCoords(_curX, _curY, true);

            if (Math.abs(click.x - dragStart.x) >= 3 || Math.abs(click.y - dragStart.y) >= 3)
            {
                float scaleSetting = gnc.getScaleSetting();
                int scaledHeight = Math.round((float)(gnc.sr.getScaledHeight() + current.y) / scaleSetting - (float)current.y);
                desired.x = current.x + click.x - dragStart.x;
                desired.y = current.y + click.y - dragStart.y;

                if (desired.y - current.height < -scaledHeight + 1 && !anchoredTop)
                {
                    anchoredTop = true;
                    desired.y -= current.height;
                }
                else if (desired.y + current.height + 1 > absMinY && anchoredTop)
                {
                    anchoredTop = false;
                    desired.y += current.height;
                }

                desired.setSize(current.width, current.height);
                enforceScreenBoundary(desired);
                dragStart = click;
            }
        }
    }

    public static void handleMouseResize(int _curX, int _curY)
    {
        if (resizing)
        {
            Point click = scaleMouseCoords(_curX, _curY, true);

            if (Math.abs(click.x - dragStart.x) >= 3 || Math.abs(click.y - dragStart.y) >= 3)
            {
                desired.width = current.width + click.x - dragStart.x;
                desired.x = current.x;
                desired.y = current.y;

                if (!anchoredTop)
                {
                    desired.height = current.height - click.y + dragStart.y;
                }
                else
                {
                    desired.height = current.height + click.y - dragStart.y;
                }

                enforceScreenBoundary(desired);
                GuiNewChatTC.getInstance().func_146245_b();
                dragStart = click;
            }
        }
    }

    public static boolean pinHovered()
    {
        Point cursor = scaleMouseCoords(Mouse.getX(), Mouse.getY());

        if (cursor == null)
        {
            return false;
        }
        else
        {
            int rX = current.x + current.width - 15;
            int rY;

            if (anchoredTop)
            {
                rY = current.y + current.height - 8;
            }
            else
            {
                rY = current.y - current.height;
            }

            return cursor.x > rX && cursor.x < rX + 6 && cursor.y > rY && cursor.y < rY + 8;
        }
    }

    public static boolean resizeHovered()
    {
        Point cursor = scaleMouseCoords(Mouse.getX(), Mouse.getY());

        if (cursor == null)
        {
            return false;
        }
        else
        {
            int rX = current.x + current.width - 8;
            int rY;

            if (anchoredTop)
            {
                rY = current.y + current.height - 8;
            }
            else
            {
                rY = current.y - current.height;
            }

            return cursor.x > rX && cursor.x < rX + 8 && cursor.y > rY && cursor.y < rY + 8;
        }
    }

    public static void setChatSize(int height)
    {
        chatHeight = height;
    }

    public static void setUnfocusedHeight(int uHeight)
    {
        unfocusedHeight = Math.min(uHeight, (int)(TabbyChat.advancedSettings.chatBoxUnfocHeight.getValue().floatValue() * (float)getChatHeight() / 100.0F));
    }

    public static void startDragging(int atX, int atY)
    {
        dragging = true;
        resizing = false;
        dragStart = scaleMouseCoords(atX, atY, true);
    }

    public static void startResizing(int atX, int atY)
    {
        dragging = false;
        resizing = true;
        dragStart = scaleMouseCoords(atX, atY, true);
    }

    public static boolean tabTrayHovered(int mx, int my)
    {
        boolean chatOpen = gnc.func_146241_e();
        GuiScreen theScreen = TabbyChat.mc.currentScreen;

        if (chatOpen && theScreen != null)
        {
            Point click = scaleMouseCoords(mx, my);
            return !anchoredTop ? click.x > current.x && click.x < current.x + current.width && click.y > current.y - current.height && click.y < current.y - current.height + tabTrayHeight : click.x > current.x && click.x < current.x + current.width && click.y > current.y + current.height - tabTrayHeight && click.y < current.y + current.height;
        }
        else
        {
            return false;
        }
    }

    public static Point scaleMouseCoords(int _x, int _y)
    {
        return scaleMouseCoords(_x, _y, false);
    }

    public static Point scaleMouseCoords(int _x, int _y, boolean forGuiScreen)
    {
        Minecraft mc = Minecraft.getMinecraft();
        GuiScreen theScreen = mc.currentScreen;

        if (theScreen == null)
        {
            return null;
        }
        else
        {
            _x = _x * theScreen.width / mc.displayWidth;
            float chatScale = gnc.getScaleSetting();
            _x = Math.round((float)(_x - current.x) / chatScale) + current.x;

            if (!forGuiScreen)
            {
                _y = -_y * theScreen.height / mc.displayHeight;
                _y = Math.round((float)(_y - current.y) / chatScale) + current.y;
            }
            else
            {
                _y = _y * theScreen.height / mc.displayHeight;
                _y = Math.round((float)(_y + current.y) / chatScale) - current.y;
                _y = theScreen.height - _y;
            }

            return new Point(_x, _y);
        }
    }

    public static void updateTabs(LinkedHashMap<String, ChatChannel> chanObjs)
    {
        boolean tabWidth = false;
        int tabX = current.x;
        int tabY = gnc.sr.getScaledHeight() + current.y + (anchoredTop ? current.height - tabTrayHeight : -current.height);
        int tabDx = 0;
        int rows = 0;
        int moveY = tabTrayHeight - tabHeight;
        tabTrayHeight = tabHeight;
        current.height -= moveY;
        int var11;

        for (Iterator var7 = chanObjs.values().iterator(); var7.hasNext(); tabDx += var11 + 1)
        {
            ChatChannel chan = (ChatChannel)var7.next();
            var11 = TabbyChat.mc.fontRenderer.getStringWidth(chan.getAlias() + "<>") + 8;

            if (tabDx + var11 > current.width - 6 && var11 < current.width - 6)
            {
                ++rows;

                if (tabHeight * (rows + 1) > tabTrayHeight)
                {
                    addRowToTray();
                }

                tabDx = 0;

                if (!anchoredTop)
                {
                    Iterator var9 = chanObjs.values().iterator();

                    while (var9.hasNext())
                    {
                        ChatChannel chan2 = (ChatChannel)var9.next();

                        if (chan2 == chan)
                        {
                            break;
                        }

                        chan2.tab.y(chan2.tab.y() + tabHeight);
                    }
                }
            }

            if (chan.tab == null)
            {
                chan.setButtonObj(new ChatButton(chan.getID(), tabX + tabDx, gnc.sr.getScaledHeight() + current.y, var11, tabHeight, chan.getDisplayTitle()));
            }
            else
            {
                chan.tab.id = chan.getID();
                chan.tab.x(tabX + tabDx);
                chan.tab.y(tabY);

                if (anchoredTop)
                {
                    chan.tab.y(chan.tab.y() + tabHeight * rows);
                }

                chan.tab.width(var11);
                chan.tab.height(tabHeight);
                chan.tab.displayString = chan.getDisplayTitle();
            }
        }
    }
}
