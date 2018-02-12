package acs.tabbychat.api;

import net.minecraft.client.gui.GuiScreen;

public interface IChatUpdateExtension extends IChatExtension
{
    void initGui(GuiScreen var1);

    void updateScreen();

    void onGuiClosed();
}
