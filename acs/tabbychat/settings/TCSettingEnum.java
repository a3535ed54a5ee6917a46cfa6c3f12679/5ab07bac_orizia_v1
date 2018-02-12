package acs.tabbychat.settings;

import acs.tabbychat.util.TabbyChatUtils;
import java.util.Properties;
import net.minecraft.client.Minecraft;

public class TCSettingEnum extends TCSetting implements ITCSetting
{
    public TCSettingEnum(Object theSetting, String theProperty, String theCategory, int theID)
    {
        super(theSetting, theProperty, theCategory, theID);
        this.type = "enum";
        this.width(30);
        this.height(11);
    }

    public TCSettingEnum(Object theSetting, String theProperty, String theCategory, int theID, FormatCodeEnum theFormat)
    {
        super(theSetting, theProperty, theCategory, theID, theFormat);
        this.type = "enum";
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft par1, int cursorX, int cursorY)
    {
        int centerX = this.x() + this.width() / 2;
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
        drawRect(this.x() + 1, this.y(), this.x() + this.width() - 1, this.y() + 1, fgcolor);
        drawRect(this.x() + 1, this.y() + this.height() - 1, this.x() + this.width() - 1, this.y() + this.height(), fgcolor);
        drawRect(this.x(), this.y() + 1, this.x() + 1, this.y() + this.height() - 1, fgcolor);
        drawRect(this.x() + this.width() - 1, this.y() + 1, this.x() + this.width(), this.y() + this.height() - 1, fgcolor);
        drawRect(this.x() + 1, this.y() + 1, this.x() + this.width() - 1, this.y() + this.height() - 1, -16777216);
        this.drawCenteredString(mc.fontRenderer, this.tempValue.toString(), centerX, this.y() + 2, labelColor);
        this.drawCenteredString(mc.fontRenderer, this.description, this.labelX + mc.fontRenderer.getStringWidth(this.description) / 2, this.y() + (this.height() - 6) / 2, labelColor);
    }

    public Enum<?> getTempValue()
    {
        return (Enum)this.tempValue;
    }

    public Enum<?> getValue()
    {
        return (Enum)this.value;
    }

    public void loadSelfFromProps(Properties readProps)
    {
        String found = (String)readProps.get(this.propertyName);

        if (found == null)
        {
            this.clear();
        }
        else
        {
            if (this.propertyName.contains("Color"))
            {
                this.value = TabbyChatUtils.parseColor(found);
            }
            else if (this.propertyName.contains("Format"))
            {
                this.value = TabbyChatUtils.parseFormat(found);
            }
            else if (this.propertyName.contains("Sound"))
            {
                this.value = TabbyChatUtils.parseSound(found);
            }
            else if (this.propertyName.contains("delim"))
            {
                this.value = TabbyChatUtils.parseDelimiters(found);
            }
            else if (this.propertyName.contains("Stamp"))
            {
                this.value = TabbyChatUtils.parseTimestamp(found);
            }
        }
    }

    public void mouseClicked(int par1, int par2, int par3)
    {
        if (this.hovered(par1, par2).booleanValue() && this.enabled)
        {
            if (par3 == 1)
            {
                this.previous();
            }
            else if (par3 == 0)
            {
                this.next();
            }
        }
    }

    public void next()
    {
        Enum eCast = (Enum)this.tempValue;
        Enum[] E = (Enum[])eCast.getClass().getEnumConstants();
        Enum tmp;

        if (eCast.ordinal() == E.length - 1)
        {
            tmp = Enum.valueOf(eCast.getClass(), E[0].name());
        }
        else
        {
            tmp = Enum.valueOf(eCast.getClass(), E[eCast.ordinal() + 1].name());
        }

        this.tempValue = tmp;
    }

    public void previous()
    {
        Enum eCast = (Enum)this.tempValue;
        Enum[] E = (Enum[])eCast.getClass().getEnumConstants();

        if (eCast.ordinal() == 0)
        {
            this.tempValue = Enum.valueOf(eCast.getClass(), E[E.length - 1].name());
        }
        else
        {
            this.tempValue = Enum.valueOf(eCast.getClass(), E[eCast.ordinal() - 1].name());
        }
    }

    public void setTempValueFromProps(Properties readProps)
    {
        String found = (String)readProps.get(this.propertyName);

        if (found == null)
        {
            this.tempValue = this.theDefault;
        }
        else
        {
            if (this.propertyName.contains("Color"))
            {
                this.tempValue = TabbyChatUtils.parseColor(found);
            }
            else if (this.propertyName.contains("Format"))
            {
                this.tempValue = TabbyChatUtils.parseFormat(found);
            }
            else if (this.propertyName.contains("Sound"))
            {
                this.tempValue = TabbyChatUtils.parseSound(found);
            }
            else if (this.propertyName.contains("delim"))
            {
                this.tempValue = TabbyChatUtils.parseDelimiters(found);
            }
            else if (this.propertyName.contains("Stamp"))
            {
                this.tempValue = TabbyChatUtils.parseTimestamp(found);
            }
        }
    }

    public void setValue(Object var1)
    {
        super.setValue(var1);
    }

    public void setCleanValue(Object var1)
    {
        super.setCleanValue(var1);
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

    public void actionPerformed()
    {
        super.actionPerformed();
    }
}
