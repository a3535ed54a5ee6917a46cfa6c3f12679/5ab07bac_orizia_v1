package acs.tabbychat.gui;

import acs.tabbychat.core.GuiNewChatTC;
import java.awt.Point;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Mouse;

public class ChatScrollBar
{
    private static Minecraft mc = Minecraft.getMinecraft();
    private static GuiNewChatTC gnc = GuiNewChatTC.getInstance();
    private static float mouseLoc = 0.0F;
    private static int scrollBarCenter = 0;
    private static int barBottomY = 0;
    private static int barTopY = 0;
    private static int barX = 326;
    private static int barMinY = 0;
    private static int barMaxY = 0;
    private static int lastY = 0;
    protected static int barHeight = 5;
    protected static int barWidth = 5;
    private static boolean scrolling = false;

    public static void handleMouse()
    {
        Point cursor = ChatBox.scaleMouseCoords(Mouse.getEventX(), Mouse.getEventY());

        if (Mouse.getEventButton() == 0 && Mouse.isButtonDown(0))
        {
            int offsetX = barX + ChatBox.current.x;
            int offsetY = ChatBox.current.y;

            if (cursor.x - offsetX > 0 && cursor.x - offsetX <= barWidth && cursor.y <= barMaxY + offsetY && cursor.y >= barMinY + offsetY)
            {
                scrolling = true;
            }
            else
            {
                scrolling = false;
            }
        }
        else if (!Mouse.isButtonDown(0))
        {
            scrolling = false;
        }

        if (Math.abs(cursor.y - lastY) > 1 && scrolling)
        {
            scrollBarMouseDrag(cursor.y);
        }
    }

    private static void update()
    {
        barHeight = MathHelper.floor_float(5.0F * gnc.getScaleSetting());
        barWidth = MathHelper.floor_float(5.0F * gnc.getScaleSetting());
        barX = ChatBox.current.width - barWidth - 2;
        barBottomY = 0;

        if (ChatBox.anchoredTop)
        {
            barBottomY = ChatBox.current.height - ChatBox.tabTrayHeight;
            barTopY = 0;
        }
        else
        {
            barBottomY = 0;
            barTopY = -ChatBox.current.height + ChatBox.tabTrayHeight;
        }

        barMaxY = barBottomY - barHeight / 2 - 1;
        barMinY = barTopY + barHeight / 2 + 1;

        if (!ChatBox.anchoredTop)
        {
            scrollBarCenter = Math.round(mouseLoc * (float)barMinY + (1.0F - mouseLoc) * (float)barMaxY);
        }
        else
        {
            scrollBarCenter = Math.round(mouseLoc * (float)barMaxY + (1.0F - mouseLoc) * (float)barMinY);
        }
    }

    public static void drawScrollBar()
    {
        update();
        int minX = barX + 1;
        int maxlines = gnc.getHeightSetting() / 9;
        float chatOpacity = mc.gameSettings.chatOpacity * 0.9F + 0.1F;
        int currentOpacity = (int)(180.0F * chatOpacity);
        Gui.drawRect(barX, barTopY, barX + barWidth + 2, barBottomY, currentOpacity << 24);

        if (gnc.GetChatSize() > maxlines)
        {
            Gui.drawRect(minX, scrollBarCenter - barHeight / 2, minX + barWidth, scrollBarCenter + barHeight / 2, 16777215 + (currentOpacity / 2 << 24));
            Gui.drawRect(minX + 1, scrollBarCenter - barHeight / 2 - 1, minX + barWidth - 1, scrollBarCenter + barHeight / 2 + 1, 16777215 + (currentOpacity / 2 << 24));
        }
    }

    public static void scrollBarMouseWheel()
    {
        update();
        int maxlines = gnc.getHeightSetting() / 9;
        int blines = gnc.GetChatSize();

        if (blines > maxlines)
        {
            mouseLoc = (float)gnc.chatLinesTraveled() / (float)(blines - maxlines);
        }
        else
        {
            mouseLoc = 0.0F;
        }

        if (!ChatBox.anchoredTop)
        {
            scrollBarCenter = Math.round(mouseLoc * (float)barMinY + (1.0F - mouseLoc) * (float)barMaxY);
        }
        else
        {
            scrollBarCenter = Math.round(mouseLoc * (float)barMaxY + (1.0F - mouseLoc) * (float)barMinY);
        }
    }

    public static void scrollBarMouseDrag(int _absY)
    {
        int maxlines = gnc.getHeightSetting() / 9;
        int blines = gnc.GetChatSize();

        if (blines <= maxlines)
        {
            mouseLoc = 0.0F;
        }
        else
        {
            int adjBarMin = barMinY + ChatBox.current.y;
            int adjBarMax = barMaxY + ChatBox.current.y;

            if (_absY < adjBarMin)
            {
                mouseLoc = ChatBox.anchoredTop ? 0.0F : 1.0F;
            }
            else if (_absY > adjBarMax)
            {
                mouseLoc = ChatBox.anchoredTop ? 1.0F : 0.0F;
            }
            else if (!ChatBox.anchoredTop)
            {
                mouseLoc = Math.abs((float)(adjBarMax - _absY)) / (float)(adjBarMax - adjBarMin);
            }
            else
            {
                mouseLoc = Math.abs((float)(adjBarMin - _absY)) / (float)(adjBarMax - adjBarMin);
            }

            float moveInc = 1.0F / (float)(blines - maxlines);
            int moveLines = (int)(mouseLoc / moveInc);

            if (moveLines > blines - maxlines)
            {
                moveLines = blines - maxlines;
            }

            gnc.setVisChatLines(moveLines);
            mouseLoc = moveInc * (float)moveLines;

            if (!ChatBox.anchoredTop)
            {
                scrollBarCenter = Math.round(mouseLoc * (float)(barMinY - barMaxY) + (float)barMaxY);
            }
            else
            {
                scrollBarCenter = Math.round(mouseLoc * (float)(barMaxY - barMinY) + (float)barMinY);
            }

            lastY = _absY;
        }
    }

    public static void setOffset(int _x, int _y)
    {
        int maxlines = gnc.getHeightSetting() / 9;
        int clines = gnc.GetChatSize() < maxlines ? gnc.GetChatSize() : maxlines;
        barX = 324 + _x;
        barMinY = mc.currentScreen.height - ((clines - 1) * 9 + 8) - 35 + _y;
        barTopY = barMinY + barHeight / 2 + _y;
        barMaxY = mc.currentScreen.height - 45 + _y;
        barBottomY = barMaxY - barHeight / 2 + _y;
    }
}
