package acs.tabbychat.settings;

import java.util.Properties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;

public class TCSettingTextBox extends TCSetting implements ITCSetting
{
    protected GuiTextField textBox;
    protected int charLimit = 32;

    public TCSettingTextBox(Object theSetting, String theProperty, String theCategory, int theID)
    {
        super(theSetting, theProperty, theCategory, theID);
        this.type = "textbox";
        this.width(50);
        this.height(11);
        this.textBox = new GuiTextField(mc.fontRenderer, 0, 0, this.width(), this.height());
        this.textBox.setText((String)this.value);
    }

    public void clear()
    {
        super.clear();
        this.textBox.setText((String)this.theDefault);
    }

    public void disable()
    {
        super.disable();
        this.textBox.func_146184_c(false);
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft par1, int cursorX, int cursorY)
    {
        int labelColor = this.enabled ? 16777215 : 6710886;
        this.textBox.drawTextBox();
        this.drawCenteredString(mc.fontRenderer, this.description, this.labelX + mc.fontRenderer.getStringWidth(this.description) / 2, this.y() + (this.height() - 6) / 2, labelColor);
    }

    public void enable()
    {
        super.enable();
        this.textBox.func_146184_c(true);
    }

    public void func_146184_c(boolean val)
    {
        this.enabled = val;
        this.textBox.func_146184_c(val);
    }

    public String getTempValue()
    {
        return this.textBox.getText().trim();
    }

    public String getValue()
    {
        return (String)this.value;
    }

    public void keyTyped(char par1, int par2)
    {
        this.textBox.textboxKeyTyped(par1, par2);
    }

    public void mouseClicked(int par1, int par2, int par3)
    {
        this.textBox.mouseClicked(par1, par2, par3);
    }

    private void reassignField()
    {
        String tmp = this.textBox.getText();
        this.textBox = new GuiTextField(mc.fontRenderer, this.x(), this.y() + 1, this.width(), this.height() + 1);
        this.textBox.func_146203_f(this.charLimit);
        this.textBox.setText(tmp);
    }

    public void reset()
    {
        if (this.value == null)
        {
            this.value = "";
        }

        this.textBox.setText((String)this.value);
    }

    public void save()
    {
        this.value = this.textBox.getText().trim();
    }

    public void setButtonDims(int wide, int tall)
    {
        super.setButtonDims(wide, tall);
        this.reassignField();
    }

    public void setButtonLoc(int bx, int by)
    {
        super.setButtonLoc(bx, by);
        this.reassignField();
    }

    public void setCharLimit(int newLimit)
    {
        this.charLimit = newLimit;
        this.textBox.func_146203_f(newLimit);
    }

    public void setDefault(Object newDefault)
    {
        this.theDefault = newDefault;
    }

    public void setTempValue(Object theVal)
    {
        this.textBox.setText((String)theVal);
    }

    public void setValue(Object var1)
    {
        super.setValue(var1);
    }

    public void setCleanValue(Object var1)
    {
        super.setCleanValue(var1);
    }

    public void setLabelLoc(int var1)
    {
        super.setLabelLoc(var1);
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
