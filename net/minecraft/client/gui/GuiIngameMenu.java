package net.minecraft.client.gui;

import java.awt.Desktop;
import java.net.URI;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import org.lwjgl.opengl.GL11;

public class GuiIngameMenu
  extends GuiScreen
{
  private int field_146445_a;
  private int field_146444_f;
  private static final String __OBFID = "CL_00000703";
  private static final ResourceLocation head = new ResourceLocation("textures/gui/head.png");
  
  public void initGui()
  {
    this.field_146445_a = 0;
    this.buttonList.clear();
    byte var1 = -16;
    boolean var2 = true;
    this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 160 + var1, I18n.format(EnumChatFormatting.DARK_GRAY + "Quitter", new Object[0])));
    if (!this.mc.isIntegratedServerRunning()) {
      ((GuiButton)this.buttonList.get(0)).displayString = I18n.format("Quitter", new Object[0]);
    }
    this.buttonList.add(new GuiButton(4, this.width / 2 - 100, this.height / 4 + 130 + var1, I18n.format(EnumChatFormatting.DARK_GRAY + "Revenir en jeu", new Object[0])));
    
    this.buttonList.add(new GuiButton(6, this.width / 2 - 200, this.height / 4 + 66 + var1, 98, 20, I18n.format(EnumChatFormatting.DARK_PURPLE + "Statistiques.", new Object[0])));
    
    this.buttonList.add(new GuiButton(3, this.width / 2 + 102, this.height / 4 + 66 + var1, 98, 20, I18n.format(EnumChatFormatting.DARK_PURPLE + "Boutique", new Object[0])));
    
    this.buttonList.add(new GuiButton(0, this.width / 2 - 200, this.height / 4 + 96 + var1, 98, 20, I18n.format(EnumChatFormatting.DARK_PURPLE + "Options...", new Object[0])));
    
    this.buttonList.add(new GuiButton(7, this.width / 2 + 102, this.height / 4 + 96 + var1, 98, 20, I18n.format(EnumChatFormatting.DARK_PURPLE + "Teamspeak", new Object[0])));
    
    this.buttonList.add(new GuiButton(5, this.width / 2 - 200, this.height / 4 + 36 + var1, 98, 20, I18n.format(EnumChatFormatting.DARK_PURPLE + "Achivements", new Object[0])));
    this.buttonList.add(new GuiButton(2, this.width / 2 + 102, this.height / 4 + 36 + var1, 98, 20, I18n.format(EnumChatFormatting.DARK_PURPLE + "Site", new Object[0])));
  }
  
  protected void actionPerformed(GuiButton p_146284_1_)
  {
    switch (p_146284_1_.id)
    {
    case 0: 
      this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
      break;
    case 1: 
      p_146284_1_.enabled = false;
      this.mc.theWorld.sendQuittingDisconnectingPacket();
      this.mc.loadWorld(null);
      this.mc.displayGuiScreen(new GuiMainMenu());
      break;
    case 2: 
      try
      {
        URI var2 = new URI("https://orizia-mc.fr");
        if (Desktop.isDesktopSupported()) {
          Desktop.getDesktop().browse(var2);
        }
      }
      catch (Exception var7)
      {
        var7.printStackTrace();
      }
    case 3: 
      try
      {
        URI var2 = new URI("https://orizia-mc.fr/shop/");
        if (Desktop.isDesktopSupported()) {
          Desktop.getDesktop().browse(var2);
        }
      }
      catch (Exception var7)
      {
        var7.printStackTrace();
      }
    case 8: 
    case 9: 
    default: 
      break;
    case 4: 
      this.mc.displayGuiScreen(null);
      this.mc.setIngameFocus();
      break;
    case 5: 
      this.mc.displayGuiScreen(new GuiAchievements(this, this.mc.thePlayer.func_146107_m()));
      break;
    case 6: 
      this.mc.displayGuiScreen(new GuiStats(this, this.mc.thePlayer.func_146107_m()));
      break;
    case 7: 
      try
      {
        URI var2 = new URI("ts3server://ts.orizia-mc.fr?nickname=" + this.mc.getSession().getUsername());
        if (Desktop.isDesktopSupported()) {
          Desktop.getDesktop().browse(var2);
        }
      }
      catch (Exception var7)
      {
        var7.printStackTrace();
      }
    case 10: 
      try
      {
        URI var2 = new URI("https://orizia-mc.fr/" + this.mc.getSession().getUsername());
        if (Desktop.isDesktopSupported()) {
          Desktop.getDesktop().browse(var2);
        }
      }
      catch (Exception var7)
      {
        var7.printStackTrace();
      }
    }
  }
  
  public static void func_147046_a(int p_147046_0_, int p_147046_1_, int p_147046_2_, float p_147046_3_, float p_147046_4_, EntityLivingBase p_147046_5_)
  {
    GL11.glEnable(2903);
    GL11.glPushMatrix();
    GL11.glTranslatef(p_147046_0_, p_147046_1_, 50.0F);
    GL11.glScalef(-p_147046_2_, p_147046_2_, p_147046_2_);
    GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
    float var6 = p_147046_5_.renderYawOffset;
    float var7 = p_147046_5_.rotationYaw;
    float var8 = p_147046_5_.rotationPitch;
    float var9 = p_147046_5_.prevRotationYawHead;
    float var10 = p_147046_5_.rotationYawHead;
    GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
    RenderHelper.enableStandardItemLighting();
    GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
    GL11.glRotatef(-(float)Math.atan(p_147046_4_ / 40.0F) * 20.0F, 1.0F, 0.0F, 0.0F);
    p_147046_5_.renderYawOffset = ((float)Math.atan(p_147046_3_ / 40.0F) * 20.0F);
    p_147046_5_.rotationYaw = ((float)Math.atan(p_147046_3_ / 40.0F) * 40.0F);
    p_147046_5_.rotationPitch = (-(float)Math.atan(p_147046_4_ / 40.0F) * 20.0F);
    p_147046_5_.rotationYawHead = p_147046_5_.rotationYaw;
    p_147046_5_.prevRotationYawHead = p_147046_5_.rotationYaw;
    GL11.glTranslatef(0.0F, p_147046_5_.yOffset, 0.0F);
    RenderManager.instance.playerViewY = 180.0F;
    RenderManager.instance.func_147940_a(p_147046_5_, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
    p_147046_5_.renderYawOffset = var6;
    p_147046_5_.rotationYaw = var7;
    p_147046_5_.rotationPitch = var8;
    p_147046_5_.prevRotationYawHead = var9;
    p_147046_5_.rotationYawHead = var10;
    GL11.glPopMatrix();
    RenderHelper.disableStandardItemLighting();
    GL11.glDisable(32826);
    OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
    GL11.glDisable(3553);
    OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
  }
  
  public void drawTextureWithOptionalSize(int x, int y, int u, int v, int width, int height, int uSize, int vSize)
  {
    float scaledX = 1.0F / uSize;
    float scaledY = 1.0F / vSize;
    Tessellator tessellator = Tessellator.instance;
    tessellator.startDrawingQuads();
    tessellator.addVertexWithUV(x + 0, y + height, this.zLevel, (u + 0) * scaledX, (v + height) * scaledY);
    tessellator.addVertexWithUV(x + width, y + height, this.zLevel, (u + width) * scaledX, (v + height) * scaledY);
    tessellator.addVertexWithUV(x + width, y + 0, this.zLevel, (u + width) * scaledX, (v + 0) * scaledY);
    tessellator.addVertexWithUV(x + 0, y + 0, this.zLevel, (u + 0) * scaledX, (v + 0) * scaledY);
    tessellator.draw();
  }
  
  public void updateScreen()
  {
    super.updateScreen();
    this.field_146444_f += 1;
  }
  
  public void drawScreen(int par1, int par2, float par3)
  {
    drawDefaultBackground();
    func_147046_a(this.width / 2, this.height / 2, 35, (this.width - 176) / 2 + this.height / 8 - par1, (this.height - 166) / 2 + 75 - par2, this.mc.thePlayer);
    
    drawCenteredString(this.fontRendererObj, EnumChatFormatting.GOLD + "§cBonjour §7" + this.mc.getSession().getUsername() + " §c, que souhaitez-vous faire ?", this.width / 2, 40, 16777215);
    
    super.drawScreen(par1, par2, par3);
  }
  
  public int getXPNeededFor(int level)
  {
    if (level == 0) {
      return 0;
    }
    if (level == 1) {
      return 35;
    }
    if (level == 2) {
      return 40;
    }
    if (level == 3) {
      return 46;
    }
    if (level == 4) {
      return 53;
    }
    if (level == 5) {
      return 61;
    }
    if (level == 6) {
      return 71;
    }
    if (level == 7) {
      return 82;
    }
    if (level == 8) {
      return 95;
    }
    if (level == 9) {
      return 110;
    }
    if (level == 10) {
      return 128;
    }
    if (level == 11) {
      return 149;
    }
    if (level == 12) {
      return 173;
    }
    if (level == 13) {
      return 201;
    }
    if (level == 14) {
      return 234;
    }
    if (level == 15) {
      return 273;
    }
    if (level == 16) {
      return 318;
    }
    if (level == 17) {
      return 371;
    }
    if (level == 18) {
      return 432;
    }
    if (level == 19) {
      return 504;
    }
    if (level == 20) {
      return 588;
    }
    if (level == 21) {
      return 686;
    }
    if (level == 22) {
      return 800;
    }
    if (level == 23) {
      return 933;
    }
    if (level == 24) {
      return 1088;
    }
    if (level == 25) {
      return 1269;
    }
    if (level == 26) {
      return 1480;
    }
    if (level == 27) {
      return 1726;
    }
    if (level == 28) {
      return 2013;
    }
    if (level == 29) {
      return 2348;
    }
    if (level == 30) {
      return 2739;
    }
    if (level == 31) {
      return 3195;
    }
    if (level == 32) {
      return 3727;
    }
    if (level == 33) {
      return 4348;
    }
    if (level == 34) {
      return 5072;
    }
    if (level == 35) {
      return 5917;
    }
    if (level == 36) {
      return 6903;
    }
    if (level == 37) {
      return 8053;
    }
    if (level == 38) {
      return 9395;
    }
    if (level == 39) {
      return 10960;
    }
    if (level == 40) {
      return 11786;
    }
    if (level == 41) {
      return 12917;
    }
    if (level == 42) {
      return 14403;
    }
    if (level == 43) {
      return 17303;
    }
    if (level == 44) {
      return 20686;
    }
    if (level == 45) {
      return 23633;
    }
    if (level == 46) {
      return 27238;
    }
    if (level == 47) {
      return 31611;
    }
    if (level == 48) {
      return 43879;
    }
    if (level == 49) {
      return 47192;
    }
    if (level == 50) {
      return 52724;
    }
    if (level == 51) {
      return 58678;
    }
    if (level == 52) {
      return 64291;
    }
    if (level == 53) {
      return 71839;
    }
    if (level == 54) {
      return 79645;
    }
    if (level == 55) {
      return 87085;
    }
    if (level == 56) {
      return 99599;
    }
    if (level == 57) {
      return 110698;
    }
    if (level == 58) {
      return 120981;
    }
    if (level == 59) {
      return 130144;
    }
    if (level == 60) {
      return 141001;
    }
    if (level == 61) {
      return 152501;
    }
    if (level == 62) {
      return 164751;
    }
    if (level == 63) {
      return 176042;
    }
    if (level == 64) {
      return 188882;
    }
    if (level == 65) {
      return 200029;
    }
    if (level == 66) {
      return 224533;
    }
    if (level == 67) {
      return 248788;
    }
    if (level == 68) {
      return 272586;
    }
    if (level == 69) {
      return 294183;
    }
    if (level == 70) {
      return 315380;
    }
    if (level == 71) {
      return 338610;
    }
    if (level == 72) {
      return 358045;
    }
    if (level == 73) {
      return 379719;
    }
    if (level == 74) {
      return 399672;
    }
    if (level == 75) {
      return 429117;
    }
    if (level == 76) {
      return 454636;
    }
    if (level == 77) {
      return 486408;
    }
    if (level == 78) {
      return 510476;
    }
    if (level == 79) {
      return 535055;
    }
    if (level == 80) {
      return 572897;
    }
    if (level == 81) {
      return 600713;
    }
    if (level == 82) {
      return 630665;
    }
    if (level == 83) {
      return 670942;
    }
    if (level == 84) {
      return 700432;
    }
    if (level == 85) {
      return 725846;
    }
    if (level == 86) {
      return 752654;
    }
    if (level == 87) {
      return 783645;
    }
    if (level == 88) {
      return 805975;
    }
    if (level == 89) {
      return 824456;
    }
    if (level == 90) {
      return 853032;
    }
    if (level == 91) {
      return 876870;
    }
    if (level == 92) {
      return 899848;
    }
    if (level == 93) {
      return 925851;
    }
    if (level == 94) {
      return 945261;
    }
    if (level == 95) {
      return 960446;
    }
    if (level == 96) {
      return 984316;
    }
    if (level == 97) {
      return 83682363;
    }
    if (level == 98) {
      return 97629423;
    }
    if (level == 99) {
      return 1000000;
    }
    if (level == 100) {
      return 1000000000;
    }
    return 0;
  }
  
  public int getXPNeededForFac(int level)
  {
    if (level == 1) {
      return 100;
    }
    if (level == 2) {
      return 111;
    }
    if (level == 3) {
      return 123;
    }
    if (level == 4) {
      return 136;
    }
    if (level == 5) {
      return 150;
    }
    if (level == 6) {
      return 166;
    }
    if (level == 7) {
      return 184;
    }
    if (level == 8) {
      return 204;
    }
    if (level == 9) {
      return 226;
    }
    if (level == 10) {
      return 250;
    }
    if (level == 11) {
      return 277;
    }
    if (level == 12) {
      return 307;
    }
    if (level == 13) {
      return 340;
    }
    if (level == 14) {
      return 377;
    }
    if (level == 15) {
      return 418;
    }
    if (level == 16) {
      return 463;
    }
    if (level == 17) {
      return 513;
    }
    if (level == 18) {
      return 569;
    }
    if (level == 19) {
      return 631;
    }
    if (level == 20) {
      return 700;
    }
    if (level == 21) {
      return 777;
    }
    if (level == 22) {
      return 862;
    }
    if (level == 23) {
      return 956;
    }
    if (level == 24) {
      return 1061;
    }
    if (level == 25) {
      return 1177;
    }
    if (level == 26) {
      return 1306;
    }
    if (level == 27) {
      return 1449;
    }
    if (level == 28) {
      return 1608;
    }
    if (level == 29) {
      return 1784;
    }
    if (level == 30) {
      return 1980;
    }
    if (level == 31) {
      return 2197;
    }
    if (level == 32) {
      return 2438;
    }
    if (level == 33) {
      return 2706;
    }
    if (level == 34) {
      return 3003;
    }
    if (level == 35) {
      return 3333;
    }
    if (level == 36) {
      return 3699;
    }
    if (level == 37) {
      return 4105;
    }
    if (level == 38) {
      return 4556;
    }
    if (level == 39) {
      return 5057;
    }
    if (level == 40) {
      return 5613;
    }
    if (level == 41) {
      return 6230;
    }
    if (level == 42) {
      return 6915;
    }
    if (level == 43) {
      return 7675;
    }
    if (level == 44) {
      return 8519;
    }
    if (level == 45) {
      return 9456;
    }
    if (level == 46) {
      return 10496;
    }
    if (level == 47) {
      return 11650;
    }
    if (level == 48) {
      return 12931;
    }
    if (level == 49) {
      return 14353;
    }
    if (level == 50) {
      return 15931;
    }
    if (level == 51) {
      return 17683;
    }
    if (level == 52) {
      return 19628;
    }
    if (level == 53) {
      return 21787;
    }
    if (level == 54) {
      return 24183;
    }
    if (level == 55) {
      return 26843;
    }
    if (level == 56) {
      return 29795;
    }
    if (level == 57) {
      return 33072;
    }
    if (level == 58) {
      return 36709;
    }
    if (level == 59) {
      return 40746;
    }
    if (level == 60) {
      return 45228;
    }
    if (level == 61) {
      return 50203;
    }
    if (level == 62) {
      return 55725;
    }
    if (level == 63) {
      return 61854;
    }
    if (level == 64) {
      return 68657;
    }
    if (level == 65) {
      return 76209;
    }
    if (level == 66) {
      return 84591;
    }
    if (level == 67) {
      return 93896;
    }
    if (level == 68) {
      return 104224;
    }
    if (level == 69) {
      return 115688;
    }
    if (level == 70) {
      return 128413;
    }
    if (level == 71) {
      return 142538;
    }
    if (level == 72) {
      return 158217;
    }
    if (level == 73) {
      return 175620;
    }
    if (level == 74) {
      return 194938;
    }
    if (level == 75) {
      return 216381;
    }
    if (level == 76) {
      return 240182;
    }
    if (level == 77) {
      return 266602;
    }
    if (level == 78) {
      return 295928;
    }
    if (level == 79) {
      return 328480;
    }
    if (level == 80) {
      return 364612;
    }
    if (level == 81) {
      return 404719;
    }
    if (level == 82) {
      return 449238;
    }
    if (level == 83) {
      return 498654;
    }
    if (level == 84) {
      return 553505;
    }
    if (level == 85) {
      return 614390;
    }
    if (level == 86) {
      return 681972;
    }
    if (level == 87) {
      return 756988;
    }
    if (level == 88) {
      return 840256;
    }
    if (level == 89) {
      return 932684;
    }
    if (level == 90) {
      return 1035279;
    }
    if (level == 91) {
      return 1149159;
    }
    if (level == 92) {
      return 1275566;
    }
    if (level == 93) {
      return 1415878;
    }
    if (level == 94) {
      return 1571624;
    }
    if (level == 95) {
      return 1744502;
    }
    if (level == 96) {
      return 1936397;
    }
    if (level == 97) {
      return 2149400;
    }
    if (level == 98) {
      return 2385834;
    }
    if (level == 99) {
      return 2648275;
    }
    if (level == 100) {
      return 2939585;
    }
    return 0;
  }
}
