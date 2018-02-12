package acs.tabbychat.settings;

import java.util.Properties;
import net.minecraft.client.Minecraft;

public class TCSettingBool extends TCSetting implements ITCSetting
{
    public TCSettingBool(Object theSetting, String theProperty, String theCategory, int theID)
    {
        super(theSetting, theProperty, theCategory, theID);
        this.type = "bool";
        this.width(9);
        this.height(9);
    }

    public TCSettingBool(Object theSetting, String theProperty, String theCategory, int theID, FormatCodeEnum theFormat)
    {
        super(theSetting, theProperty, theCategory, theID, theFormat);
        this.type = "bool";
        this.width(9);
        this.height(9);
    }

    public void actionPerformed()
    {
        this.toggle();
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft par1, int cursorX, int cursorY)
    {
        int centerX = this.x() + this.width() / 2;
        int centerY = this.y() + this.height() / 2;
        byte tmpWidth = 9;
        byte tmpHeight = 9;
        int tmpX = centerX - 4;
        int tmpY = centerY - 4;
        int fgcolor = -1717526368;

        if (!this.enabled)
        {
            fgcolor = 1721802912;
        }
        else if (this.hovered(cursorX, cursorY).booleanValue())
        {
            fgcolor = -1711276128;
        }

        int labelColor = this.enabled ? 16777215 : 6710886;
        drawRect(tmpX + 1, tmpY, tmpX + tmpWidth - 1, tmpY + 1, fgcolor);
        drawRect(tmpX + 1, tmpY + tmpHeight - 1, tmpX + tmpWidth - 1, tmpY + tmpHeight, fgcolor);
        drawRect(tmpX, tmpY + 1, tmpX + 1, tmpY + tmpHeight - 1, fgcolor);
        drawRect(tmpX + tmpWidth - 1, tmpY + 1, tmpX + tmpWidth, tmpY + tmpHeight - 1, fgcolor);
        drawRect(tmpX + 1, tmpY + 1, tmpX + tmpWidth - 1, tmpY + tmpHeight - 1, -16777216);

        if (((Boolean)this.tempValue).booleanValue())
        {
            drawRect(centerX - 2, centerY, centerX - 1, centerY + 1, this.buttonColor);
            drawRect(centerX - 1, centerY + 1, centerX, centerY + 2, this.buttonColor);
            drawRect(centerX, centerY + 2, centerX + 1, centerY + 3, this.buttonColor);
            drawRect(centerX + 1, centerY + 2, centerX + 2, centerY, this.buttonColor);
            drawRect(centerX + 2, centerY, centerX + 3, centerY - 2, this.buttonColor);
            drawRect(centerX + 3, centerY - 2, centerX + 4, centerY - 4, this.buttonColor);
        }

        this.drawCenteredString(mc.fontRenderer, this.description, this.labelX + mc.fontRenderer.getStringWidth(this.description) / 2, this.y() + (this.height() - 6) / 2, labelColor);
    }

    public Boolean getTempValue()
    {
        return (Boolean)this.tempValue;
    }

    public Boolean getValue()
    {
        return (Boolean)this.value;
    }

    public void setCleanValue(Object _input)
    {
        if (_input == null)
        {
            this.clear();
        }
        else
        {
            this.value = Boolean.valueOf(Boolean.parseBoolean(_input.toString()));
        }
    }

    public void toggle()
    {
        this.tempValue = Boolean.valueOf(!((Boolean)this.tempValue).booleanValue());
    }

    public void setValue(Object var1)
    {
        super.setValue(var1);
    }

    public void setTempValue(Object var1)
    {
        super.setTempValue(var1);
    }

    public void setLabelLoc(int var1)
    {
        super.setLabelLoc(var1);
    }

    public void setButtonLoc(int var1, int var2)
    {
        super.setButtonLoc(var1, var2);
    }

    public void setButtonDims(int var1, int var2)
    {
        super.setButtonDims(var1, var2);
    }

    public void saveSelfToProps(Properties var1)
    {
        super.saveSelfToProps(var1);
    }

    public void save()
    {
        super.save();
    }

    public void resetDescription()
    {
        super.resetDescription();
    }

    public void reset()
    {
        super.reset();
    }

    public void mouseClicked(int var1, int var2, int var3)
    {
        super.mouseClicked(var1, var2, var3);
    }

    public void loadSelfFromProps(Properties var1)
    {
        super.loadSelfFromProps(var1);
    }

    public Boolean hovered(int var1, int var2)
    {
        return super.hovered(var1, var2);
    }

    public String getType()
    {
        return super.getType();
    }

    public String getProperty()
    {
        return super.getProperty();
    }

    public Object getDefault()
    {
        return super.getDefault();
    }

    public boolean enabled()
    {
        return super.enabled();
    }

    public void enable()
    {
        super.enable();
    }

    public void disable()
    {
        super.disable();
    }

    public void clear()
    {
        super.clear();
    }

    public void y(int var1)
    {
        super.y(var1);
    }

    public int y()
    {
        return super.y();
    }

    public void x(int var1)
    {
        super.x(var1);
    }

    public int x()
    {
        return super.x();
    }

    public void height(int var1)
    {
        super.height(var1);
    }

    public int height()
    {
        return super.height();
    }

    public void width(int var1)
    {
        super.width(var1);
    }

    public int width()
    {
        return super.width();
    }
}
