package fr.shey.exoniaradio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

public class RadioGui extends GuiScreen
{
    private int field_146445_a;
    private int field_146444_f;
    
    public boolean isPlaying;
    public String btnLabel;
    
    public GuiButton radioBtn;
    
    public ExoniaSlider slider;

    private GuiScreen lastGui;
    
    private ExoniaRadio radio;
    
    public RadioGui(GuiScreen gui)
    {
        this.lastGui = gui;
    }


    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
    	radio = new ExoniaRadio(0.1F);
    	
    	this.field_146445_a = 0;
        this.buttonList.clear();
        byte var1 = -16;
        boolean var2 = true;
        
        slider = new ExoniaSlider(301, this.width / 2 + 5, this.height / 4 + 30 + var1, "FuryRadio", 200.0F, 20.0F);
        
        this.buttonList.add(slider);
        
    	if(radio.isPlaying())
    		btnLabel = "§aRadio: ON";
    	else
    	{
    		btnLabel = "§cRadio: OFF";
        	slider.displayString = "Radio eteinte..";
        	slider.enabled = false;
        	slider.field_146134_p = 0;
    	}
        
        radioBtn = new GuiButton(300, this.width / 2 - 205, this.height / 4 + 30 + -16, I18n.format(btnLabel, new Object[0]));
        this.buttonList.add(radioBtn);
        
        this.buttonList.add(new GuiButton(305, this.width / 2 - 100, this.height / 4 + 150 + var1, I18n.format("gui.done", new Object[0])));
    
    }

    protected void actionPerformed(GuiButton p_146284_1_)
    {
        switch (p_146284_1_.id)
        {
            case 305:
            	this.mc.displayGuiScreen(lastGui);
            	saveVolume(slider.field_146134_p);
                break;
            case 300:
            	
            	if(!radio.isPlaying())
            	{
            		btnLabel = "Radio: ON";
                	radioBtn.displayString = "Radio: ON";
                	slider.enabled = true;
                	slider.displayString = "§aOrizia : " + Math.round(slider.getVolume() * 100.0F) + "%";
                	slider.field_146134_p = slider.getVolume();
                	radio.startPlayer();
                    radio.setVolume(slider.getVolume());
            	}
            	else
            	{
            		System.out.println(slider.getVolume());
            		saveVolume(slider.getVolume());
            		radio.setVolume(-1500);
            		isPlaying = false;
            		btnLabel = "§cRadio: OFF";
                	radioBtn.displayString = "§cRadio: OFF";
                	slider.displayString = "§cRadio eteinte..";
                	slider.enabled = false;
                    radio.stop();  
            	}
            		
            	this.updateScreen();
            	break;
        }
    }
    
    public void saveVolume(float vol)
    {
    	try 
    	{
    		PrintStream fileStream = new PrintStream(new File("radioOptions.txt"));
    	    BufferedReader br = new BufferedReader(new FileReader(new File("radioOptions.txt")));
    	    
    	    String line;
    	    
    	    while ((line = br.readLine()) != null) 
    	    {
    	    	if(line.contains("radioVolume="))
    	        {
    	    		line.replace(line.substring(line.lastIndexOf("=")), vol + "");
    	        }
    	        	
    	    }
    	    
        	fileStream.println("radioVolume=" + vol);
    		fileStream.close();
    		br.close();
    	} 
    	
    	catch(IOException e) {}
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();
        
        ++this.field_146444_f;
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, I18n.format("§aVous êtes actuellement sur Orizia", new Object[0]), this.width / 2, 40, 16777215);
        
        if(radio != null && radio.isPlaying())
        {
        	btnLabel = "§aRadio: ON";
        }
        else
        {
        	btnLabel = "§cRadio: OFF";
        }
        
        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
    }
}
