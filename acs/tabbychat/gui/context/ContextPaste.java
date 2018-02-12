package acs.tabbychat.gui.context;

import java.util.List;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class ContextPaste extends ChatContext
{
    public void onClicked()
    {
        this.getMenu().screen.inputField2.func_146191_b(GuiScreen.getClipboardString());
    }

    public ResourceLocation getDisplayIcon()
    {
        return new ResourceLocation("textures/gui/icons/paste.png");
    }

    public String getDisplayString()
    {
        return "Paste";
    }

    public boolean isPositionValid(int x, int y)
    {
        String clipboard = GuiScreen.getClipboardString();
        return clipboard != null && !clipboard.isEmpty();
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
