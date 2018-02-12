package acs.tabbychat.gui.context;

import java.util.List;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ResourceLocation;

public class ContextCut extends ChatContext
{
    public void onClicked()
    {
        GuiTextField chat = this.getMenu().screen.inputField2;
        GuiScreen.setClipboardString(chat.func_146207_c());
        String text = chat.getText().replace(chat.func_146207_c(), "");
        chat.setText(text);
    }

    public String getDisplayString()
    {
        return "Cut";
    }

    public ResourceLocation getDisplayIcon()
    {
        return new ResourceLocation("textures/gui/icons/cut.png");
    }

    public boolean isPositionValid(int x, int y)
    {
        GuiTextField text = this.getMenu().screen.inputField2;
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
