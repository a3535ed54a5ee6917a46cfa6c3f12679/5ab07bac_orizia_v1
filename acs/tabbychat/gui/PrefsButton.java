package acs.tabbychat.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;

public class PrefsButton extends GuiButton
{
    protected int bgcolor = -587202560;
    protected boolean hasControlCodes = false;
    protected String type;

    public PrefsButton()
    {
        super(9999, 0, 0, 1, 1, "");
    }

    public PrefsButton(int _id, int _x, int _y, int _w, int _h, String _title)
    {
        super(_id, _x, _y, _w, _h, _title);
    }

    public PrefsButton(int _id, int _x, int _y, int _w, int _h, String _title, int _bgcolor)
    {
        super(_id, _x, _y, _w, _h, _title);
        this.bgcolor = _bgcolor;
    }

    protected void title(String newtitle)
    {
        this.displayString = newtitle;
    }

    protected String title()
    {
        return this.displayString;
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

    protected int adjustWidthForControlCodes()
    {
        String cleaned = this.displayString.replaceAll("(?i)\u00a7[0-9A-FK-OR]", "");
        boolean bold = this.displayString.replaceAll("(?i)\u00a7L", "").length() != this.displayString.length();
        int badWidth = Minecraft.getMinecraft().fontRenderer.getStringWidth(this.displayString);
        int goodWidth = Minecraft.getMinecraft().fontRenderer.getStringWidth(cleaned);

        if (bold)
        {
            goodWidth += cleaned.length();
        }

        return badWidth > goodWidth ? badWidth - goodWidth : 0;
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft mc, int cursorX, int cursorY)
    {
        if (this.field_146125_m)
        {
            FontRenderer fr = mc.fontRenderer;
            drawRect(this.x(), this.y(), this.x() + this.width(), this.y() + this.height(), this.bgcolor);
            boolean hovered = cursorX >= this.x() && cursorY >= this.y() && cursorX < this.x() + this.width() && cursorY < this.y() + this.height();

            if (this.bgcolor == -587202560 || this.bgcolor == -1717986919)
            {
                drawRect(this.x() - 1, this.y() - 1, this.x(), this.y() + this.height(), -1061109568);
                drawRect(this.x() - 1, this.y() - 1, this.x() + this.width() + 1, this.y(), -1061109568);
                drawRect(this.x() - 1, this.y() + this.height(), this.x() + this.width() + 1, this.y() + this.height() + 1, 1886417008);
                drawRect(this.x() + this.width(), this.y() - 1, this.x() + this.width() + 1, this.y() + this.height() + 1, 1886417008);
            }

            int var7 = 10526880;

            if (!this.enabled)
            {
                var7 = -6250336;
            }
            else if (hovered)
            {
                var7 = 16777120;
            }

            if (this.hasControlCodes)
            {
                int offset = this.adjustWidthForControlCodes();
                this.drawCenteredString(fr, this.displayString, this.x() + (this.width() + offset) / 2, this.y() + (this.height() - 8) / 2, var7);
            }
            else
            {
                this.drawCenteredString(fr, this.displayString, this.x() + this.width() / 2, this.y() + (this.height() - 8) / 2, var7);
            }
        }
    }
}
