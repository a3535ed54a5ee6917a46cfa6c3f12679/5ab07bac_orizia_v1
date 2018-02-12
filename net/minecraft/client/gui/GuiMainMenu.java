package net.minecraft.client.gui;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiLoadingScreen;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.I18n;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import org.apache.commons.io.Charsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.Project;

public class GuiMainMenu extends GuiScreen implements GuiYesNoCallback {

   private static final Logger logger = LogManager.getLogger();
   private static final Random rand = new Random();
   private float updateCounter;
   private String splashText;
   private GuiButton buttonResetDemo;
   private int panoramaTimer;
   private DynamicTexture viewportTexture;
   private final Object field_104025_t = new Object();
   private String field_92025_p;
   private String field_146972_A;
   private String field_104024_v;
   private static final ResourceLocation splashTexts = new ResourceLocation("texts/splashes.txt");
   private static final ResourceLocation minecraftTitleTextures = new ResourceLocation("textures/gui/title/minecraft.png");
   private static final ResourceLocation[] titlePanoramaPaths = new ResourceLocation[]{new ResourceLocation("textures/gui/title/background/panorama_0.png"), new ResourceLocation("textures/gui/title/background/panorama_1.png"), new ResourceLocation("textures/gui/title/background/panorama_2.png"), new ResourceLocation("textures/gui/title/background/panorama_3.png"), new ResourceLocation("textures/gui/title/background/panorama_4.png"), new ResourceLocation("textures/gui/title/background/panorama_5.png")};
   public static final String field_96138_a = "Please click " + EnumChatFormatting.UNDERLINE + "here" + EnumChatFormatting.RESET + " for more information.";
   private int field_92024_r;
   private int field_92023_s;
   private int field_92022_t;
   private int field_92021_u;
   private int field_92020_v;
   private int field_92019_w;
   private ResourceLocation field_110351_G;
   private static final String __OBFID = "CL_00001154";
   private static final ResourceLocation logo = new ResourceLocation("textures/gui/logo1.png");
   private static final ResourceLocation background = new ResourceLocation("textures/gui/background.png");
   private int textPosition = 440;
   private String text = "§5 ORIZIA";


   public GuiMainMenu() {
      this.field_146972_A = field_96138_a;
      this.splashText = "missingno";
      BufferedReader var1 = null;

      try {
         ArrayList var2 = new ArrayList();
         var1 = new BufferedReader(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(splashTexts).getInputStream(), Charsets.UTF_8));

         String var3;
         while((var3 = var1.readLine()) != null) {
            var3 = var3.trim();
            if(!var3.isEmpty()) {
               var2.add(var3);
            }
         }

         if(!var2.isEmpty()) {
            do {
               this.splashText = (String)var2.get(rand.nextInt(var2.size()));
            } while(this.splashText.hashCode() == 125780783);
         }
      } catch (IOException var12) {
         ;
      } finally {
         if(var1 != null) {
            try {
               var1.close();
            } catch (IOException var11) {
               ;
            }
         }

      }

      this.updateCounter = rand.nextFloat();
      this.field_92025_p = "";
      if(!GLContext.getCapabilities().OpenGL20 && !OpenGlHelper.func_153193_b()) {
         this.field_92025_p = I18n.format("title.oldgl1", new Object[0]);
         this.field_146972_A = I18n.format("title.oldgl2", new Object[0]);
         this.field_104024_v = "https://help.mojang.com/customer/portal/articles/325948?ref=game";
      }

   }

   public void updateScreen() {
      ++this.panoramaTimer;
      if(this.textPosition < 0 - this.mc.fontRenderer.getStringWidth(this.text)) {
         this.textPosition = this.width / 2 + 200;
      }

      this.textPosition -= 3;
   }

   public void onGuiClosed() {}

   public boolean doesGuiPauseGame() {
      return false;
   }

   protected void keyTyped(char p_73869_1_, int p_73869_2_) {}

   public void initGui() {
      this.drawLogo();
      this.viewportTexture = new DynamicTexture(256, 256);
      this.field_110351_G = this.mc.getTextureManager().getDynamicTextureLocation("background", this.viewportTexture);
      Calendar var1 = Calendar.getInstance();
      var1.setTime(new Date());
      if(var1.get(2) + 1 == 11 && var1.get(5) == 9) {
         this.splashText = "Happy birthday, ez!";
      } else if(var1.get(2) + 1 == 6 && var1.get(5) == 1) {
         this.splashText = "Happy birthday, Notch!";
      } else if(var1.get(2) + 1 == 12 && var1.get(5) == 24) {
         this.splashText = "Merry X-mas!";
      } else if(var1.get(2) + 1 == 1 && var1.get(5) == 1) {
         this.splashText = "Happy new year!";
      } else if(var1.get(2) + 1 == 10 && var1.get(5) == 31) {
         this.splashText = "OOoooOOOoooo! Spooky!";
      }

      boolean var2 = true;
      int var3 = this.height / 4 + 48;
      if(this.mc.isDemo()) {
         this.addDemoButtons(var3, 24);
      } else {
         this.addSingleplayerMultiplayerButtons(var3, 24);
      }

      this.buttonList.add(new GuiButton(0, this.width / 2 - 200, var3 + 38 + 12, 98, 20, I18n.format("§c§oOptions...", new Object[0])));
      this.buttonList.add(new GuiButton(4, this.width / 2 + 105, var3 + 36 + 12, 98, 20, I18n.format("§7§oQuitter AverFight", new Object[0])));
      Object var4 = this.field_104025_t;
      Object var5 = this.field_104025_t;
      synchronized(this.field_104025_t) {
         this.field_92023_s = this.fontRendererObj.getStringWidth(this.field_92025_p);
         this.field_92024_r = this.fontRendererObj.getStringWidth(this.field_146972_A);
         int var51 = Math.max(this.field_92023_s, this.field_92024_r);
         this.field_92022_t = (this.width - var51) / 2;
         this.field_92021_u = ((GuiButton)this.buttonList.get(0)).field_146129_i - 24;
         this.field_92020_v = this.field_92022_t + var51;
         this.field_92019_w = this.field_92021_u + 24;
      }
   }

   private void addSingleplayerMultiplayerButtons(int p_73969_1_, int p_73969_2_) {
      this.buttonList.add(new GuiButton(1, this.width / 2 - 100, p_73969_1_ + 60, 200, 20, I18n.format("§6§oJouez en solo", new Object[0])));
      this.buttonList.add(new GuiButton(11, this.width / 2 - 100, p_73969_1_ + 35, 200, 20, I18n.format("§4§oJouez sur AverFight", new Object[0])));
   }

   private void addDemoButtons(int p_73972_1_, int p_73972_2_) {
      this.buttonList.add(new GuiButton(11, this.width / 2 - 100, p_73972_1_, I18n.format("menu.playdemo", new Object[0])));
      this.buttonList.add(this.buttonResetDemo = new GuiButton(12, this.width / 2 - 100, p_73972_1_ + p_73972_2_ * 1, I18n.format("menu.resetdemo", new Object[0])));
      ISaveFormat var3 = this.mc.getSaveLoader();
      WorldInfo var4 = var3.getWorldInfo("Demo_World");
      if(var4 == null) {
         this.buttonResetDemo.enabled = false;
      }

   }

   protected void actionPerformed(GuiButton p_146284_1_) {
      if(p_146284_1_.id == 0) {
         this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
      }

      if(p_146284_1_.id == 5) {
         URI var2 = URI.create("ts3server://ts.averfight.fr");

         try {
            Desktop.getDesktop().browse(var2);
         } catch (IOException var5) {
            var5.printStackTrace();
         }
      }

      if(p_146284_1_.id == 1) {
         this.mc.displayGuiScreen(new GuiSelectWorld(this));
      }

      int var10000 = p_146284_1_.id;
      if(p_146284_1_.id == 4) {
         this.mc.shutdown();
      }

      if(p_146284_1_.id == 11) {
         this.mc.displayGuiScreen(new GuiLoadingScreen());
      }

      if(p_146284_1_.id == 12) {
         ISaveFormat var21 = this.mc.getSaveLoader();
         WorldInfo var3 = var21.getWorldInfo("Demo_World");
         if(var3 != null) {
            GuiYesNo var4 = GuiSelectWorld.func_152129_a(this, var3.getWorldName(), 12);
            this.mc.displayGuiScreen(var4);
         }
      }

   }

   private void func_140005_i() {
      RealmsBridge var1 = new RealmsBridge();
      var1.switchToRealms(this);
   }

   public void confirmClicked(boolean p_73878_1_, int p_73878_2_) {
      if(p_73878_1_ && p_73878_2_ == 12) {
         ISaveFormat var52 = this.mc.getSaveLoader();
         var52.flushCache();
         var52.deleteWorldDirectory("Demo_World");
         this.mc.displayGuiScreen(this);
      } else if(p_73878_2_ == 13) {
         if(p_73878_1_) {
            try {
               Class var5 = Class.forName("java.awt.Desktop");
               Object var4 = var5.getMethod("getDesktop", new Class[0]).invoke((Object)null, new Object[0]);
               var5.getMethod("browse", new Class[]{URI.class}).invoke(var4, new Object[]{new URI(this.field_104024_v)});
            } catch (Throwable var51) {
               logger.error("Couldn\'t open link", var51);
            }
         }

         this.mc.displayGuiScreen(this);
      }

   }

   public static String readFile(String url) {
      String text = "0";

      try {
         URL e = new URL(url);
         BufferedReader in = new BufferedReader(new InputStreamReader(e.openStream()));
         text = in.readLine();
      } catch (Exception var4) {
         System.out.println(var4);
      }

      return text;
   }

   public void drawTextureWithOptionalSize(int x, int y, int u, int v, int width, int height, int uSize, int vSize) {
      float scaledX = 1.0F / (float)uSize;
      float scaledY = 1.0F / (float)vSize;
      Tessellator tessellator = Tessellator.instance;
      tessellator.startDrawingQuads();
      tessellator.addVertexWithUV((double)(x + 0), (double)(y + height), (double)this.zLevel, (double)((float)(u + 0) * scaledX), (double)((float)(v + height) * scaledY));
      tessellator.addVertexWithUV((double)(x + width), (double)(y + height), (double)this.zLevel, (double)((float)(u + width) * scaledX), (double)((float)(v + height) * scaledY));
      tessellator.addVertexWithUV((double)(x + width), (double)(y + 0), (double)this.zLevel, (double)((float)(u + width) * scaledX), (double)((float)(v + 0) * scaledY));
      tessellator.addVertexWithUV((double)(x + 0), (double)(y + 0), (double)this.zLevel, (double)((float)(u + 0) * scaledX), (double)((float)(v + 0) * scaledY));
      tessellator.draw();
   }

   public void drawLogo() {
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      this.mc.getTextureManager().bindTexture(logo);
      this.drawTextureWithOptionalSize(this.width / 2 - 50, this.height / 4 - 50, 0, 0, 100, 100, 100, 100);
   }

   public void drawBackground() {
      GL11.glDisable(2896);
      GL11.glDisable(2912);
      Tessellator var2 = Tessellator.instance;
      GL11.glBindTexture(3553, 0);
      this.mc.getTextureManager().bindTexture(background);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      var2.startDrawingQuads();
      var2.addVertexWithUV(0.0D, (double)this.height, 0.0D, 0.0D, 1.0D);
      var2.addVertexWithUV((double)this.width, (double)this.height, 0.0D, 1.0D, 1.0D);
      var2.addVertexWithUV((double)this.width, 0.0D, 0.0D, 1.0D, 0.0D);
      var2.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
      var2.draw();
   }

   private void drawPanorama(int p_73970_1_, int p_73970_2_, float p_73970_3_) {
      Tessellator var4 = Tessellator.instance;
      GL11.glMatrixMode(5889);
      GL11.glPushMatrix();
      GL11.glLoadIdentity();
      Project.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
      GL11.glMatrixMode(5888);
      GL11.glPushMatrix();
      GL11.glLoadIdentity();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
      GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
      GL11.glEnable(3042);
      GL11.glDisable(3008);
      GL11.glDisable(2884);
      GL11.glDepthMask(false);
      OpenGlHelper.glBlendFunc(770, 771, 1, 0);
      byte var5 = 8;

      for(int var6 = 0; var6 < var5 * var5; ++var6) {
         GL11.glPushMatrix();
         float var7 = ((float)(var6 % var5) / (float)var5 - 0.5F) / 64.0F;
         float var8 = ((float)(var6 / var5) / (float)var5 - 0.5F) / 64.0F;
         float var9 = 0.0F;
         GL11.glTranslatef(var7, var8, var9);
         GL11.glRotatef(MathHelper.sin(((float)this.panoramaTimer + p_73970_3_) / 400.0F) * 25.0F + 20.0F, 1.0F, 0.0F, 0.0F);
         GL11.glRotatef(-((float)this.panoramaTimer + p_73970_3_) * 0.1F, 0.0F, 1.0F, 0.0F);

         for(int var10 = 0; var10 < 6; ++var10) {
            GL11.glPushMatrix();
            if(var10 == 1) {
               GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
            }

            if(var10 == 2) {
               GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            }

            if(var10 == 3) {
               GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
            }

            if(var10 == 4) {
               GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
            }

            if(var10 == 5) {
               GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
            }

            this.mc.getTextureManager().bindTexture(titlePanoramaPaths[var10]);
            var4.startDrawingQuads();
            var4.setColorRGBA_I(16777215, 255 / (var6 + 1));
            float var11 = 0.0F;
            var4.addVertexWithUV(-1.0D, -1.0D, 1.0D, (double)(0.0F + var11), (double)(0.0F + var11));
            var4.addVertexWithUV(1.0D, -1.0D, 1.0D, (double)(1.0F - var11), (double)(0.0F + var11));
            var4.addVertexWithUV(1.0D, 1.0D, 1.0D, (double)(1.0F - var11), (double)(1.0F - var11));
            var4.addVertexWithUV(-1.0D, 1.0D, 1.0D, (double)(0.0F + var11), (double)(1.0F - var11));
            var4.draw();
            GL11.glPopMatrix();
         }

         GL11.glPopMatrix();
         GL11.glColorMask(true, true, true, false);
      }

      var4.setTranslation(0.0D, 0.0D, 0.0D);
      GL11.glColorMask(true, true, true, true);
      GL11.glMatrixMode(5889);
      GL11.glPopMatrix();
      GL11.glMatrixMode(5888);
      GL11.glPopMatrix();
      GL11.glDepthMask(true);
      GL11.glEnable(2884);
      GL11.glEnable(2929);
   }

   private void rotateAndBlurSkybox(float p_73968_1_) {
      this.mc.getTextureManager().bindTexture(this.field_110351_G);
      GL11.glTexParameteri(3553, 10241, 9729);
      GL11.glTexParameteri(3553, 10240, 9729);
      GL11.glCopyTexSubImage2D(3553, 0, 0, 0, 0, 0, 256, 256);
      GL11.glEnable(3042);
      OpenGlHelper.glBlendFunc(770, 771, 1, 0);
      GL11.glColorMask(true, true, true, false);
      Tessellator var2 = Tessellator.instance;
      var2.startDrawingQuads();
      GL11.glDisable(3008);
      byte var3 = 3;

      for(int var4 = 0; var4 < var3; ++var4) {
         var2.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F / (float)(var4 + 1));
         int var5 = this.width;
         int var6 = this.height;
         float var7 = (float)(var4 - var3 / 2) / 256.0F;
         var2.addVertexWithUV((double)var5, (double)var6, (double)this.zLevel, (double)(0.0F + var7), 1.0D);
         var2.addVertexWithUV((double)var5, 0.0D, (double)this.zLevel, (double)(1.0F + var7), 1.0D);
         var2.addVertexWithUV(0.0D, 0.0D, (double)this.zLevel, (double)(1.0F + var7), 0.0D);
         var2.addVertexWithUV(0.0D, (double)var6, (double)this.zLevel, (double)(0.0F + var7), 0.0D);
      }

      var2.draw();
      GL11.glEnable(3008);
      GL11.glColorMask(true, true, true, true);
   }

   private void renderSkybox(int p_73971_1_, int p_73971_2_, float p_73971_3_) {
      this.mc.getFramebuffer().unbindFramebuffer();
      GL11.glViewport(0, 0, 256, 256);
      this.drawPanorama(p_73971_1_, p_73971_2_, p_73971_3_);
      this.rotateAndBlurSkybox(p_73971_3_);
      this.rotateAndBlurSkybox(p_73971_3_);
      this.rotateAndBlurSkybox(p_73971_3_);
      this.rotateAndBlurSkybox(p_73971_3_);
      this.rotateAndBlurSkybox(p_73971_3_);
      this.rotateAndBlurSkybox(p_73971_3_);
      this.rotateAndBlurSkybox(p_73971_3_);
      this.mc.getFramebuffer().bindFramebuffer(true);
      GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
      Tessellator var4 = Tessellator.instance;
      var4.startDrawingQuads();
      float var5 = this.width > this.height?120.0F / (float)this.width:120.0F / (float)this.height;
      float var6 = (float)this.height * var5 / 256.0F;
      float var7 = (float)this.width * var5 / 256.0F;
      var4.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
      int var8 = this.width;
      int var9 = this.height;
      var4.addVertexWithUV(0.0D, (double)var9, (double)this.zLevel, (double)(0.5F - var6), (double)(0.5F + var7));
      var4.addVertexWithUV((double)var8, (double)var9, (double)this.zLevel, (double)(0.5F - var6), (double)(0.5F - var7));
      var4.addVertexWithUV((double)var8, 0.0D, (double)this.zLevel, (double)(0.5F + var6), (double)(0.5F - var7));
      var4.addVertexWithUV(0.0D, 0.0D, (double)this.zLevel, (double)(0.5F + var6), (double)(0.5F + var7));
      var4.draw();
   }

   public void drawScreen(int par1, int par2, float par3) {
      this.drawBackground();
      this.drawLogo();
      this.drawGradientRect(0, 0, this.width, 15, 2130706433, 2130706433);
      this.drawString(this.fontRendererObj, this.text, this.textPosition, 4, 12128795);
      GL11.glPushMatrix();
      GL11.glTranslatef((float)(this.width / 2 + 90), 70.0F, 0.0F);
      GL11.glRotatef(-20.0F, 0.0F, 0.0F, 1.0F);
      float var8 = 1.8F - MathHelper.abs(MathHelper.sin((float)(Minecraft.getSystemTime() % 1000L) / 1000.0F * 3.1415927F * 2.0F) * 0.1F);
      var8 = var8 * 80.0F / (float)(this.fontRendererObj.getStringWidth(this.splashText) + 32);
      GL11.glScalef(var8, var8, var8);
      GL11.glPopMatrix();
      boolean var1 = true;
      super.drawScreen(par1, par2, par3);
   }

   protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_) {
      super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
      Object var4 = this.field_104025_t;
      Object var5 = this.field_104025_t;
      synchronized(this.field_104025_t) {
         if(this.field_92025_p.length() > 0 && p_73864_1_ >= this.field_92022_t && p_73864_1_ <= this.field_92020_v && p_73864_2_ >= this.field_92021_u && p_73864_2_ <= this.field_92019_w) {
            GuiConfirmOpenLink var51 = new GuiConfirmOpenLink(this, this.field_104024_v, 13, true);
            var51.func_146358_g();
            this.mc.displayGuiScreen(var51);
         }

      }
   }
}
