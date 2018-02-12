package acs.tabbychat.gui;

import acs.tabbychat.core.ChatChannel;
import acs.tabbychat.core.GuiNewChatTC;
import acs.tabbychat.core.TabbyChat;
import java.awt.Rectangle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class ChatButton extends GuiButton
{
    public ChatChannel channel;

    public ChatButton()
    {
        super(9999, 0, 0, 1, 1, "");
    }

    public ChatButton(int _id, int _x, int _y, int _w, int _h, String _title)
    {
        super(_id, _x, _y, _w, _h, _title);
    }

    public int width()
    {
        return this.field_146120_f;
    }

    public void width(int _w)
    {
        this.field_146120_f = _w;
    }

    public int height()
    {
        return this.field_146121_g;
    }

    public void height(int _h)
    {
        this.field_146121_g = _h;
    }

    public int x()
    {
        return this.field_146128_h;
    }

    public void x(int _x)
    {
        this.field_146128_h = _x;
    }

    public int y()
    {
        return this.field_146129_i;
    }

    public void y(int _y)
    {
        this.field_146129_i = _y;
    }

    public void clear()
    {
        this.channel = null;
    }

    private static Rectangle translateButtonDims(Rectangle unscaled)
    {
        float scaleSetting = GuiNewChatTC.getInstance().getScaleSetting();
        int adjX = Math.round((float)(unscaled.x - ChatBox.current.x) * scaleSetting + (float)ChatBox.current.x);
        int adjY = Math.round((float)(TabbyChat.mc.currentScreen.height - unscaled.y + ChatBox.current.y) * (1.0F - scaleSetting)) + unscaled.y;
        int adjW = Math.round((float)unscaled.width * scaleSetting);
        int adjH = Math.round((float)unscaled.height * scaleSetting);
        return new Rectangle(adjX, adjY, adjW, adjH);
    }

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     */
    public boolean mousePressed(Minecraft mc, int par2, int par3)
    {
        Rectangle cursor = translateButtonDims(new Rectangle(this.x(), this.y(), this.width(), this.height()));
        return this.enabled && this.field_146125_m && par2 >= cursor.x && par3 >= cursor.y && par2 < cursor.x + cursor.width && par3 < cursor.y + cursor.height;
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft mc, int cursorX, int cursorY)
    {
        if (this.field_146125_m)
        {
            FontRenderer fr = mc.fontRenderer;
            float _mult = mc.gameSettings.chatOpacity * 0.9F + 0.1F;
            int _opacity = (int)(255.0F * _mult);
            int textOpacity = TabbyChat.advancedSettings.textIgnoreOpacity.getValue().booleanValue() ? 255 : _opacity;
            Rectangle cursor = translateButtonDims(new Rectangle(this.x(), this.y(), this.width(), this.height()));
            boolean hovered = cursorX >= cursor.x && cursorY >= cursor.y && cursorX < cursor.x + cursor.width && cursorY < cursor.y + cursor.height;
            int var7 = 10526880;
            int var8 = 0;

            if (!this.enabled)
            {
                var7 = -6250336;
            }
            else if (hovered)
            {
                var7 = 16777120;
                var8 = 8355922;
            }
            else if (this.channel.active)
            {
                var7 = 10872804;
                var8 = 5995643;
            }
            else if (this.channel.unread)
            {
                var7 = 16711680;
                var8 = 7471104;
            }

            drawRect(this.x(), this.y(), this.x() + this.width(), this.y() + this.height(), var8 + (_opacity / 2 << 24));
            GL11.glEnable(GL11.GL_BLEND);

            if (hovered && Keyboard.isKeyDown(42))
            {
                String special = this.channel.getTitle().equalsIgnoreCase("*") ? "\u2398" : "\u26a0";
                this.drawCenteredString(fr, special, this.x() + this.width() / 2, this.y() + (this.height() - 8) / 2, var7 + (textOpacity << 24));
            }
            else
            {
                this.drawCenteredString(fr, this.displayString, this.x() + this.width() / 2, this.y() + (this.height() - 8) / 2, var7 + (textOpacity << 24));
            }
        }
    }
}
