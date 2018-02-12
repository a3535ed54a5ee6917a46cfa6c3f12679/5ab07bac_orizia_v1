package acs.tabbychat.settings;

import java.util.Properties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;

abstract class TCSetting extends GuiButton implements ITCSetting
{
    public int buttonColor;
    protected int labelX;
    public String description;
    protected String type;
    public final String categoryName;
    public final String propertyName;
    protected Object value;
    protected Object tempValue;
    protected Object theDefault;
    protected static Minecraft mc = Minecraft.getMinecraft();

    public TCSetting(Object theSetting, String theProperty, String theCategory, int theID)
    {
        super(theID, 0, 0, "");
        this.buttonColor = -1146755100;
        this.labelX = 0;
        this.value = theSetting;
        this.tempValue = theSetting;
        this.theDefault = theSetting;
        this.propertyName = theProperty;
        this.categoryName = theCategory;
        this.description = I18n.format(theCategory + "." + theProperty.toLowerCase(), new Object[0]);
    }

    public TCSetting(Object theSetting, String theProperty, String theCategory, int theID, FormatCodeEnum theFormat)
    {
        this(theSetting, theProperty, theCategory, theID);
        this.description = theFormat.toCode() + this.description + "\u00a7r";
    }

    public void actionPerformed() {}

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
        this.value = this.theDefault;
        this.tempValue = this.theDefault;
    }

    public void disable()
    {
        this.enabled = false;
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft mc, int cursorX, int cursorY) {}

    public void enable()
    {
        this.enabled = true;
    }

    public boolean enabled()
    {
        return this.enabled;
    }

    public Object getDefault()
    {
        return this.theDefault;
    }

    public String getProperty()
    {
        return this.propertyName;
    }

    public Object getTempValue()
    {
        return this.tempValue;
    }

    public String getType()
    {
        return this.type;
    }

    protected Object getValue()
    {
        return this.value;
    }

    public Boolean hovered(int cursorX, int cursorY)
    {
        return Boolean.valueOf(cursorX >= this.x() && cursorY >= this.y() && cursorX < this.x() + this.width() && cursorY < this.y() + this.height());
    }

    public void loadSelfFromProps(Properties readProps)
    {
        this.setCleanValue(readProps.get(this.propertyName));
    }

    public void mouseClicked(int par1, int par2, int par3) {}

    public void reset()
    {
        this.tempValue = this.value;
    }

    public void resetDescription()
    {
        this.description = this.categoryName.isEmpty() ? "" : I18n.format(this.categoryName + "." + this.propertyName.toLowerCase(), new Object[0]);
    }

    public void save()
    {
        this.value = this.tempValue;
    }

    public void saveSelfToProps(Properties writeProps)
    {
        if (this.value instanceof Enum)
        {
            writeProps.put(this.propertyName, ((Enum)this.value).name());
        }
        else
        {
            writeProps.put(this.propertyName, this.value.toString());
        }
    }

    public void setButtonDims(int wide, int tall)
    {
        this.width(wide);
        this.height(tall);
    }

    public void setButtonLoc(int bx, int by)
    {
        this.x(bx);
        this.y(by);
    }

    public void setLabelLoc(int _x)
    {
        this.labelX = _x;
    }

    public void setTempValue(Object updateVal)
    {
        this.tempValue = updateVal;
    }

    public void setCleanValue(Object updateVal)
    {
        if (updateVal == null)
        {
            this.clear();
        }
        else
        {
            this.value = updateVal;
        }
    }

    public void setValue(Object updateVal)
    {
        this.value = updateVal;
    }
}
