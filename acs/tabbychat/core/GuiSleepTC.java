package acs.tabbychat.core;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.play.client.C0BPacketEntityAction;

public class GuiSleepTC extends GuiChatTC
{
    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        super.initGui();
        GuiButton leaveBed = new GuiButton(1, this.width / 2 - 100, this.height - 40, I18n.format("multiplayer.stopSleeping", new Object[0]));
        this.buttonList.add(leaveBed);
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    public void keyTyped(char c, int code)
    {
        switch (code)
        {
            case 1:
                this.playerWakeUp();
                break;

            case 28:
            case 156:
                this.sendChat(true);

            default:
                super.keyTyped(c, code);
        }
    }

    public void actionPerformed(GuiButton button)
    {
        if (button.id == 1)
        {
            this.playerWakeUp();
        }
        else
        {
            super.actionPerformed(button);
        }
    }

    private void playerWakeUp()
    {
        NetHandlerPlayClient var1 = this.mc.thePlayer.sendQueue;
        var1.addToSendQueue(new C0BPacketEntityAction(this.mc.thePlayer, 3));
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        if (!this.mc.thePlayer.isPlayerSleeping())
        {
            this.mc.displayGuiScreen((GuiScreen)null);
        }

        super.updateScreen();
    }
}
