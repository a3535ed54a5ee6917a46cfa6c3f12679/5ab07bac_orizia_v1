package fr.shey.exoniaradio;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.lwjgl.opengl.GL11;

public class ExoniaSlider extends GuiButton
{
	  public static float field_146134_p = getVolume();
	  public boolean field_146135_o;
	  private String field_146133_q;
	  private final float field_146132_r;
	  private final float field_146131_s;
	  public static ExoniaRadio playerRadio;

	  public ExoniaSlider(int p_i45016_1_, int p_i45016_2_, int p_i45016_3_, String p_i45016_4_)
	  {
	    this(p_i45016_1_, p_i45016_2_, p_i45016_3_, p_i45016_4_, 0.0F, 1.0F);
	  }

    public ExoniaSlider(int p_i45017_1_, int p_i45017_2_, int p_i45017_3_, String p_i45017_4_, float p_i45017_5_, float p_i45017_6_)
    {
        super(p_i45017_1_, p_i45017_2_, p_i45017_3_, 200, 20, "");
        this.field_146133_q = p_i45017_4_;
        this.field_146132_r = p_i45017_5_;
        this.field_146131_s = p_i45017_6_;
        Minecraft var7 = Minecraft.getMinecraft();
        this.displayString = ("OriziaRadio : " + Math.round(field_146134_p * 100.0F) + "%");
    }

    public int getHoverState(boolean p_146114_1_)
    {
        return 0;
    }

    /**
     * Fired when the mouse button is dragged. Equivalent of MouseListener.mouseDragged(MouseEvent e).
     */
    protected void mouseDragged(Minecraft p_146119_1_, int p_146119_2_, int p_146119_3_)
    {
        if (this.field_146125_m)
        {
          if (this.field_146135_o)
          {
            field_146134_p = (float)(p_146119_2_ - (this.field_146128_h + 4)) / (float)(this.field_146120_f - 8);
            if (field_146134_p < 0.0F) 
            {
              field_146134_p = 0.0F;
            }
            if (field_146134_p > 1.0F) {
              field_146134_p = 1.0F;
            }
              playerRadio.setVolume(field_146134_p);
            
            float var4 = field_146134_p;
            
            this.displayString = ("OriziaRadio : " + Math.round(field_146134_p * 100.0F) + "%");
          }
          GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
          drawTexturedModalRect(this.field_146128_h + (int)(field_146134_p * (float) (this.field_146120_f - 8)), field_146129_i, 0, 66, 4, 20);
          drawTexturedModalRect(this.field_146128_h + (int)(field_146134_p * (float) (this.field_146120_f - 8)) + 4, field_146129_i, 196, 66, 4, 20);
        }
    }
    
    
    public static float getVolume()
    {
        float volume = 0;
    	try 
    	{
    			FileReader fr = new FileReader(new File("furyOptions.txt"));
    	        BufferedReader br = new BufferedReader(fr);
    	        String line;

    	        
    	        while ((line = br.readLine()) != null) 
    	        {
    	        	if(line.contains("radioVolume="))
    	        	{
    	        		String keyString = line.substring(line.lastIndexOf("=") + 1);
    	        		volume = Float.valueOf(keyString);
    	        		br.close();
    	                return volume;
    	        	}
    	        }
    	        
    	        br.close();
    	        

    	}
    	catch(Exception e)
    	{
            volume = 1.0F;	
    	}
		return volume;
    }

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     */
    public boolean mousePressed(Minecraft p_146116_1_, int p_146116_2_, int p_146116_3_)
    {
      if (super.mousePressed(p_146116_1_, p_146116_2_, p_146116_3_))
      {
        field_146134_p = (float)(p_146116_2_ - (this.field_146128_h + 4)) / (float)(this.field_146120_f - 8);
        if (field_146134_p < 0.0F)
        {
          field_146134_p = 0.0F;
          ExoniaRadio.setVolume(-150);
        }
        if (field_146134_p > 1.0F) {
          field_146134_p = 1.0F;
        }
          ExoniaRadio.setVolume(field_146134_p);
        
        this.displayString = this.field_146133_q;
        this.field_146135_o = true;
        return true;
      }
      return false;
    }

    /**
     * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
     */
    public void mouseReleased(int p_146118_1_, int p_146118_2_)
    {
        this.field_146135_o = false;
    }

}
