package net.minecraft.client.gui;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import org.lwjgl.opengl.GL11;

public class GuiLoadingScreen
  extends GuiScreen
{
  private static final ResourceLocation backgroundImage = new ResourceLocation("textures/gui/chargement.png");
  private transient long updateCounter = 0L;
  public String serverName = "§5Orizia";
  public String bfsName = "§7Connexion:";
  public String loading = "§5Connexion en cours...";
  public String serverIp = "164.132.222.44";
  public int serverPort = 25565;
  
  public void initGui()
  {
    this.buttonList.add(new GuiButton(0, this.width - 100, this.height - 30, 60, 20, "Annuler"));
  }
  
  public void drawBack()
  {
    this.mc.getTextureManager().bindTexture(backgroundImage);
    
    Tessellator tessellator = Tessellator.instance;
    
    tessellator.startDrawingQuads();
    GL11.glTexParameteri(3553, 10241, 9729);
    GL11.glTexParameteri(3553, 10240, 9729);
    tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
    
    tessellator.addVertexWithUV(0.0D, 0.0D, this.zLevel, 0.0D, 0.0D);
    tessellator.addVertexWithUV(0.0D, this.height, this.zLevel, 0.0D, 1.0D);
    tessellator.addVertexWithUV(this.width, this.height, this.zLevel, 1.0D, 1.0D);
    tessellator.addVertexWithUV(this.width, 0.0D, this.zLevel, 1.0D, 0.0D);
    tessellator.draw();
  }
  
  public void updateScreen()
  {
    super.updateScreen();
    this.updateCounter += 1L;
    if (this.updateCounter == 100L) {
      this.mc.displayGuiScreen(new GuiConnecting(this, this.mc, "164.132.222.44", 25565));
    }
    if (this.updateCounter >= 101L) {
      this.mc.displayGuiScreen(new GuiMainMenu());
    }
  }
  
  public void drawScreen(int par1, int par2, float par3)
  {
    drawBack();
    
    drawHorizontalLine(0, this.width, this.height - 47, -16777216);
    drawHorizontalLine(0, this.width, this.height - 46, -9211021);
    drawRect(0, this.height - 45, this.width, this.height, -13421773);
    
    drawRect(0, 0, this.width, 12, 1711276032);
    drawRect(0, 0, this.width, 12, 1711276032);
    
    drawRect(this.width / 2 - 101, this.height - 28, this.width / 2 + 101, this.height - 15, 41054848);
    drawRect(this.width / 2 - 100, this.height - 27, this.width / 2 + 100, this.height - 16, 2013243904);
    drawRect(this.width / 2 - 100, this.height - 27, this.width / 2 - 100 + (int)this.updateCounter * 2, this.height - 16, -1996510720);
    drawCenteredString(this.fontRendererObj, this.loading, this.width / 2, this.height - 40, 16777215);
    
    String percent = this.updateCounter + "%";
    drawCenteredString(this.fontRendererObj, percent, this.width / 2, this.height - 10, 16777215);
    
    drawString(this.fontRendererObj, this.bfsName, 10, this.height - 30, -1);
    
    drawString(this.fontRendererObj, this.serverName, 10, this.height - 20, -1);
    
    GL11.glPushMatrix();
    
    float scaleFactor = 1.0F;
    
    translateToStringPos("§5Bonjour §7" + this.mc.getSession().getUsername() + "§5, vous allez rejoindre Orizia.", scaleFactor, this.width / 2, 2);
    
    GL11.glScalef(scaleFactor, scaleFactor, 1.0F);
    drawString(this.fontRendererObj, "§5Bonjour §7" + this.mc.getSession().getUsername() + "§5, vous allez rejoindre Orizia.", 0, 0, 15778560);
    
    GL11.glPopMatrix();
    
    super.drawScreen(par1, par2, par3);
  }
  
  protected void translateToStringPos(String text, float scale, int x, int y)
  {
    float stringWidth = this.fontRendererObj.getStringWidth(text) * scale;
    GL11.glTranslatef(x - stringWidth / 2.0F, y, 0.0F);
  }
  
  public void actionPerformed(GuiButton button)
  {
    if (button.id == 0) {
      this.mc.displayGuiScreen(new GuiMainMenu());
    }
  }
}
