package net.minecraft.client.gui.inventory;

import org.lwjgl.opengl.GL11;

import fr.shey.wiki.CodexGUI;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiCodex extends GuiContainer
{
    private static final ResourceLocation field_147019_u = new ResourceLocation("textures/gui/container/codex.png");
    
    public static int pageNumber;
    
    private static CodexGUI codexGUI;
    
    public static GuiButton beforeButton;
    
    public static GuiButton nextButton;
    
    public GuiCodex(EntityPlayer p_i46397_1_)
    {
        super(codexGUI = new CodexGUI(p_i46397_1_));
    }
    
    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
    	super.initGui();
    	pageNumber = 1;
        this.buttonList.clear();
        
        beforeButton = new GuiButton(1, this.width / 2 - 78, this.height / 2 - 50, 12, 20, I18n.format("<", new Object[0]));
		this.buttonList.add(beforeButton);
		
		nextButton = new GuiButton(2, this.width / 2 + 64, this.height / 2 - 50, 12, 20, I18n.format(">", new Object[0]));
		this.buttonList.add(nextButton);
		
		if(pageNumber == 1)
			beforeButton.enabled = false;
		else
			beforeButton.enabled = true;
		
    }
    
    protected void func_146979_b(int p_146979_1_, int p_146979_2_)
    {
        this.fontRendererObj.drawString(I18n.format("Codex des crafts", new Object[0]), 28, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("Page n° " + getPageNumber(), new Object[0]), 110, 70, 4210752);
    }

    protected void func_146976_a(float p_146976_1_, int p_146976_2_, int p_146976_3_)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(field_147019_u);
        int var4 = (this.width - this.field_146999_f) / 2;
        int var5 = (this.height - this.field_147000_g) / 2;
        this.drawTexturedModalRect(var4, var5, 0, 0, this.field_146999_f, this.field_147000_g);
        codexGUI.updatePage();
    }
    
	protected void actionPerformed(GuiButton p_146284_1_) 
	{
		if (p_146284_1_.id == 1) 
		{
			if(getPageNumber() > 1)
			{
				setPageNumber(getPageNumber() - 1);
				codexGUI.updatePage();
			}
		}

		if (p_146284_1_.id == 2) 
		{	
			beforeButton.enabled = true;
			setPageNumber(getPageNumber() + 1);
			codexGUI.updatePage();
		}
		
	}
	
	protected void keyTyped(char p_73869_1_, int p_73869_2_) 
	{
		if(p_73869_2_ == 1)
			this.mc.thePlayer.closeScreen();
		else
			return;
	}
    
    public void setPageNumber(int number)
    {
    	pageNumber = number;
    }
    
    public int getPageNumber()
    {
    	return pageNumber;
    }

}
