package acs.tabbychat.settings;

import acs.tabbychat.util.TabbyChatUtils;
import java.util.Properties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class TCSettingSlider extends TCSetting implements ITCSetting
{
    protected float minValue;
    protected float maxValue;
    protected float sliderValue;
    private int sliderX;
    private int buttonOffColor;
    public String units;
    private boolean dragging;

    private TCSettingSlider(Object theSetting, String theProperty, String theCategory, int theID)
    {
        super(theSetting, theProperty, theCategory, theID);
        this.buttonOffColor = 1157627903;
        this.units = "%";
        this.dragging = false;
        this.type = "slider";
        this.width(100);
        this.height(11);
        this.sliderValue = (((Float)this.tempValue).floatValue() - this.minValue) / (this.maxValue - this.minValue);
    }

    public TCSettingSlider(Float theSetting, String theProperty, String theCategory, int theID, float minVal, float maxVal)
    {
        this(theSetting, theProperty, theCategory, theID);
        this.minValue = minVal;
        this.maxValue = maxVal;
        this.sliderValue = (((Float)this.tempValue).floatValue() - this.minValue) / (this.maxValue - this.minValue);
    }

    public void clear()
    {
        super.clear();
        this.sliderValue = (((Float)this.tempValue).floatValue() - this.minValue) / (this.maxValue - this.minValue);
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft par1, int cursorX, int cursorY)
    {
        int fgcolor = -1717526368;

        if (!this.enabled)
        {
            fgcolor = 1721802912;
        }
        else if (this.hovered(cursorX, cursorY).booleanValue())
        {
            fgcolor = -1711276128;

            if (this.dragging)
            {
                this.sliderX = cursorX - 1;
                this.sliderValue = (float)(this.sliderX - (this.x() + 1)) / (float)(this.width() - 5);

                if (this.sliderValue < 0.0F)
                {
                    this.sliderValue = 0.0F;
                }
                else if (this.sliderValue > 1.0F)
                {
                    this.sliderValue = 1.0F;
                }
            }
        }

        int labelColor = this.enabled ? 16777215 : 6710886;
        int buttonColor = this.enabled ? this.buttonColor : this.buttonOffColor;
        Gui.drawRect(this.x(), this.y() + 1, this.x() + 1, this.y() + this.height() - 1, fgcolor);
        Gui.drawRect(this.x() + 1, this.y(), this.x() + this.width() - 1, this.y() + 1, fgcolor);
        Gui.drawRect(this.x() + 1, this.y() + this.height() - 1, this.x() + this.width() - 1, this.y() + this.height(), fgcolor);
        Gui.drawRect(this.x() + this.width() - 1, this.y() + 1, this.x() + this.width(), this.y() + this.height() - 1, fgcolor);
        Gui.drawRect(this.x() + 1, this.y() + 1, this.x() + this.width() - 1, this.y() + this.height() - 1, -16777216);
        this.sliderX = Math.round(this.sliderValue * (float)(this.width() - 5)) + this.x() + 1;
        Gui.drawRect(this.sliderX, this.y() + 1, this.sliderX + 1, this.y() + 2, buttonColor & -1996488705);
        Gui.drawRect(this.sliderX + 1, this.y() + 1, this.sliderX + 2, this.y() + 2, buttonColor);
        Gui.drawRect(this.sliderX + 2, this.y() + 1, this.sliderX + 3, this.y() + 2, buttonColor & -1996488705);
        Gui.drawRect(this.sliderX, this.y() + 2, this.sliderX + 1, this.y() + this.height() - 2, buttonColor);
        Gui.drawRect(this.sliderX + 1, this.y() + 2, this.sliderX + 2, this.y() + this.height() - 2, buttonColor & -1996488705);
        Gui.drawRect(this.sliderX + 2, this.y() + 2, this.sliderX + 3, this.y() + this.height() - 2, buttonColor);
        Gui.drawRect(this.sliderX, this.y() + this.height() - 2, this.sliderX + 1, this.y() + this.height() - 1, buttonColor & -1996488705);
        Gui.drawRect(this.sliderX + 1, this.y() + this.height() - 2, this.sliderX + 2, this.y() + this.height() - 1, buttonColor);
        Gui.drawRect(this.sliderX + 2, this.y() + this.height() - 2, this.sliderX + 3, this.y() + this.height() - 1, buttonColor & -1996488705);
        boolean valCenter = false;
        int valCenter1;

        if (this.sliderValue < 0.5F)
        {
            valCenter1 = Math.round(0.7F * (float)this.width());
        }
        else
        {
            valCenter1 = Math.round(0.2F * (float)this.width());
        }

        String valLabel = Integer.toString(Math.round(this.sliderValue * (this.maxValue - this.minValue) + this.minValue)) + this.units;
        this.drawCenteredString(mc.fontRenderer, valLabel, valCenter1 + this.x(), this.y() + 2, buttonColor);
        this.drawCenteredString(mc.fontRenderer, this.description, this.labelX + mc.fontRenderer.getStringWidth(this.description) / 2, this.y() + (this.height() - 6) / 2, labelColor);
    }

    public Float getTempValue()
    {
        this.tempValue = Float.valueOf(this.sliderValue * (this.maxValue - this.minValue) + this.minValue);
        return (Float)this.tempValue;
    }

    public Float getValue()
    {
        return (Float)this.value;
    }

    public void handleMouseInput()
    {
        if (mc.currentScreen != null)
        {
            int mX = Mouse.getEventX() * mc.currentScreen.width / mc.displayWidth;
            int mY = mc.currentScreen.height - Mouse.getEventY() * mc.currentScreen.height / mc.displayHeight - 1;

            if (this.hovered(mX, mY).booleanValue())
            {
                int var1 = Mouse.getEventDWheel();

                if (var1 != 0)
                {
                    if (var1 > 1)
                    {
                        var1 = 3;
                    }

                    if (var1 < -1)
                    {
                        var1 = -3;
                    }

                    if (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54))
                    {
                        var1 *= -7;
                    }
                }

                this.sliderValue += (float)var1 / 100.0F;

                if (this.sliderValue < 0.0F)
                {
                    this.sliderValue = 0.0F;
                }
                else if (this.sliderValue > 1.0F)
                {
                    this.sliderValue = 1.0F;
                }

                this.tempValue = Float.valueOf(this.sliderValue * (this.maxValue - this.minValue) + this.minValue);
            }
        }
    }

    public void mouseClicked(int par1, int par2, int par3)
    {
        if (par3 == 0 && this.hovered(par1, par2).booleanValue() && this.enabled)
        {
            this.sliderX = par1 - 1;
            this.sliderValue = (float)(this.sliderX - (this.x() + 1)) / (float)(this.width() - 5);

            if (this.sliderValue < 0.0F)
            {
                this.sliderValue = 0.0F;
            }
            else if (this.sliderValue > 1.0F)
            {
                this.sliderValue = 1.0F;
            }

            if (!this.dragging)
            {
                this.dragging = true;
            }
        }
    }

    /**
     * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
     */
    public void mouseReleased(int par1, int par2)
    {
        this.dragging = false;
    }

    public void reset()
    {
        super.reset();
        this.sliderValue = (((Float)this.tempValue).floatValue() - this.minValue) / (this.maxValue - this.minValue);
    }

    public void save()
    {
        this.tempValue = Float.valueOf(this.sliderValue * (this.maxValue - this.minValue) + this.minValue);
        super.save();
    }

    public void setCleanValue(Object updateVal)
    {
        if (updateVal == null)
        {
            this.clear();
        }
        else
        {
            this.value = TabbyChatUtils.median(this.minValue, this.maxValue, Float.valueOf((String)updateVal).floatValue());
        }
    }

    public void setRange(float theMin, float theMax)
    {
        this.minValue = theMin;
        this.maxValue = theMax;
        this.sliderValue = (((Float)this.tempValue).floatValue() - this.minValue) / (this.maxValue - this.minValue);
    }

    public void setTempValue(Object theVal)
    {
        super.setTempValue(theVal);
        this.sliderValue = (((Float)this.tempValue).floatValue() - this.minValue) / (this.maxValue - this.minValue);
    }

    public void setValue(Object var1)
    {
        super.setValue(var1);
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

    public void resetDescription()
    {
        super.resetDescription();
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
