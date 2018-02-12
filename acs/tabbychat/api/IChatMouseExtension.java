package acs.tabbychat.api;

import net.minecraft.client.gui.GuiButton;

public interface IChatMouseExtension extends IChatExtension
{
    boolean mouseClicked(int var1, int var2, int var3);

    boolean actionPerformed(GuiButton var1);

    void handleMouseInput();
}
