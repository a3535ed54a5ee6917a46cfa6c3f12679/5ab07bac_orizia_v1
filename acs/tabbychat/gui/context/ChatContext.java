package acs.tabbychat.gui.context;

import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public abstract class ChatContext extends GuiButton
{
    ChatContextMenu menu;
    public ChatContextMenu children;
    protected boolean enabled;

    public ChatContext()
    {
        super(0, 0, 0, 100, 15, (String)null);
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft mc, int x, int y)
    {
        if (this.field_146125_m)
        {
            if (this.getChildren() != null)
            {
                this.children = new ChatContextMenu(this, this.field_146128_h + this.field_146120_f, this.field_146129_i, this.getChildren());
            }

            this.displayString = this.getDisplayString();

            if (this.field_146125_m)
            {
                Gui.drawRect(this.field_146128_h + 1, this.field_146129_i + 1, this.field_146128_h + this.field_146120_f - 1, this.field_146129_i + this.field_146121_g - 1, this.getBackgroundColor(this.isHovered(x, y)));
                this.drawBorders();

                if (this.getDisplayIcon() != null)
                {
                    this.drawIcon();
                }

                this.drawString(mc.fontRenderer, this.displayString, this.field_146128_h + 18, this.field_146129_i + 4, this.getStringColor());

                if (this.getChildren() != null)
                {
                    int length = mc.fontRenderer.getCharWidth('>');
                    this.drawString(mc.fontRenderer, ">", this.field_146128_h + this.field_146120_f - length, this.field_146129_i + 4, this.getStringColor());
                    Iterator var5 = this.children.items.iterator();

                    while (var5.hasNext())
                    {
                        ChatContext chat = (ChatContext)var5.next();

                        if (this.isHoveredWithChildren(x, y))
                        {
                            chat.field_146125_m = true;
                        }
                        else
                        {
                            chat.field_146125_m = false;
                        }
                    }

                    this.children.drawMenu(x, y);
                }
            }
        }
    }

    protected boolean isHovered(int x, int y)
    {
        return x >= this.field_146128_h && x <= this.field_146128_h + this.field_146120_f && y >= this.field_146129_i && y <= this.field_146129_i + this.field_146121_g;
    }

    protected boolean isHoveredWithChildren(int x, int y)
    {
        boolean hovered = this.isHovered(x, y);

        if (!hovered && this.getChildren() != null)
        {
            Iterator var4 = this.children.items.iterator();

            while (var4.hasNext())
            {
                ChatContext item = (ChatContext)var4.next();

                if (item.field_146125_m)
                {
                    hovered = item.isHoveredWithChildren(x, y);
                }

                if (hovered)
                {
                    break;
                }
            }
        }

        return hovered;
    }

    protected void drawIcon()
    {
        int x1 = this.field_146128_h + 4;
        int y1 = this.field_146129_i + 3;
        int x2 = x1 + 9;
        int y2 = y1 + 9;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(this.getDisplayIcon());
        Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();
        tess.addVertexWithUV((double)x1, (double)y1, (double)this.zLevel, 0.0D, 0.0D);
        tess.addVertexWithUV((double)x1, (double)y2, (double)this.zLevel, 0.0D, 1.0D);
        tess.addVertexWithUV((double)x2, (double)y2, (double)this.zLevel, 1.0D, 1.0D);
        tess.addVertexWithUV((double)x2, (double)y1, (double)this.zLevel, 1.0D, 0.0D);
        tess.draw();
    }

    protected void drawBorders()
    {
        Gui.drawRect(this.field_146128_h, this.field_146129_i, this.field_146128_h + this.field_146120_f, this.field_146129_i + 1, -16777215);
        Gui.drawRect(this.field_146128_h, this.field_146129_i, this.field_146128_h + 1, this.field_146129_i + this.field_146121_g, -16777215);
        Gui.drawRect(this.field_146128_h, this.field_146129_i + this.field_146121_g, this.field_146128_h + this.field_146120_f, this.field_146129_i + this.field_146121_g - 1, -16777215);
        Gui.drawRect(this.field_146128_h + this.field_146120_f, this.field_146129_i, this.field_146128_h + this.field_146120_f - 1, this.field_146129_i + this.field_146121_g, -16777215);
        Gui.drawRect(this.field_146128_h + this.field_146121_g, this.field_146129_i, this.field_146128_h + this.field_146121_g + 1, this.field_146129_i + this.field_146121_g, -16777215);
    }

    private int getStringColor()
    {
        return !this.enabled && this.getDisabledBehavior() == ChatContext.Behavior.GRAY ? 10066329 : 15658734;
    }

    private int getBackgroundColor(boolean hovered)
    {
        return hovered ? -2145049307 : Integer.MIN_VALUE;
    }

    protected boolean mouseClicked(int x, int y)
    {
        if (this.getChildren() == null)
        {
            this.onClicked();
            return true;
        }
        else
        {
            return this.children.mouseClicked(x, y);
        }
    }

    public abstract void onClicked();

    public abstract String getDisplayString();

    public abstract ResourceLocation getDisplayIcon();

    public abstract List<ChatContext> getChildren();

    public abstract boolean isPositionValid(int var1, int var2);

    public abstract ChatContext.Behavior getDisabledBehavior();

    public ChatContextMenu getMenu()
    {
        return this.menu;
    }

    public static enum Behavior
    {
        HIDE("HIDE", 0),
        GRAY("GRAY", 1);

        private static final ChatContext.Behavior[] $VALUES = new ChatContext.Behavior[]{HIDE, GRAY};

        private Behavior(String var1, int var2) {}
    }
}
