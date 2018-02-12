package acs.tabbychat.gui.context;

import acs.tabbychat.core.GuiChatTC;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ResourceLocation;

public class ContextCopy extends ChatContext
{
    public void onClicked()
    {
        GuiScreen screen = Minecraft.getMinecraft().currentScreen;

        if (screen instanceof GuiChatTC)
        {
            GuiScreen.setClipboardString(((GuiChatTC)screen).inputField2.func_146207_c());
        }
    }

    public String getDisplayString()
    {
        return "Copy";
    }

    public ResourceLocation getDisplayIcon()
    {
        return new ResourceLocation("textures/gui/icons/copy.png");
    }

    public boolean isPositionValid(int x, int y)
    {
        GuiTextField text = this.menu.screen.inputField2;
        return text != null && !text.func_146207_c().isEmpty();
    }

    public ChatContext.Behavior getDisabledBehavior()
    {
        return ChatContext.Behavior.GRAY;
    }

    public List<ChatContext> getChildren()
    {
        return null;
    }
}
