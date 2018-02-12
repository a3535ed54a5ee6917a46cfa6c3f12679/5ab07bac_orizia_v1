package net.minecraft.client.gui;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;

import fr.shey.exoniaradio.RadioGui;

public class ExoniaHUD 
{
	
	public GuiIngame gui;
	
	public String claim = "";
	
	public boolean isTagged = false;
	
	public boolean isRadio = false;
	
	public ExoniaHUD(GuiIngame ig)
	{
		gui = ig;
	}
	
	public void drawHUD(int width, int height)
	{
			int hour = LocalDateTime.now().getHour();
			int min = LocalDateTime.now().getMinute();
			
			String spacer = "";
			
			if(min == 0 || min == 1 || min == 2 || min == 3 || min == 4 || min == 5 || min == 6 || min == 7 || min == 8 || min == 9)
			{
				spacer = "0";
			}
			else
			{
				spacer = "";
			}
			//LINES
		    gui.drawHorizontalLine(width - 106, width - 5, 4, 0xff737373);
		    gui.drawHorizontalLine(width - 106, width - 5, 50, 0xff737373);
		    gui.drawVerticalLine(width - 106, 4, 51, 0xff737373);
		    gui.drawVerticalLine(width - 5, 4, 51, 0xff737373);
		        
		    //RECT
		    gui.drawRect(width - 105, 5, width - 5, 50, 0x66000000);
		    
		    //TEXT
			gui.drawCenteredString(Minecraft.getMinecraft().fontRenderer, EnumChatFormatting.DARK_GRAY + "[ " + EnumChatFormatting.DARK_AQUA + hour + ":" + spacer + min + EnumChatFormatting.DARK_GRAY + " ]", width - 55, 8, 16777215);
			gui.drawCenteredString(Minecraft.getMinecraft().fontRenderer, EnumChatFormatting.DARK_GRAY + "> Claim: " + claim, width - 56, 21, 16777215);
			gui.drawCenteredString(Minecraft.getMinecraft().fontRenderer, EnumChatFormatting.DARK_GRAY + "> Combat: " + EnumChatFormatting.DARK_AQUA + isTagged(), width - 60, 35, 16777215); 
	//		gui.drawCenteredString(Minecraft.getMinecraft().fontRenderer, EnumChatFormatting.DARK_PURPLE + "> Radio: " + EnumChatFormatting.RED + isRadio(), width - 56, 48, 16777215); 
	}
	
	public void setClaimName(String name)
	{
		claim = name;
	}
	
	public void setTagged(boolean tag)
	{
		isTagged = tag;
	}

	public String isTagged()
	{
		if(isTagged)
			return "OUI";
		else
			return "NON";
	}
		
}
