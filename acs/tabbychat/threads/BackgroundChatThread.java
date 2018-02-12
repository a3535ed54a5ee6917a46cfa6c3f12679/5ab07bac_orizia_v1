package acs.tabbychat.threads;

import acs.tabbychat.core.TabbyChat;
import acs.tabbychat.util.TabbyChatUtils;
import java.lang.reflect.Method;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatAllowedCharacters;

public class BackgroundChatThread extends Thread
{
    String sendChat = "";
    String knownPrefix = null;

    public BackgroundChatThread(String _send)
    {
        this.sendChat = _send;
    }

    public BackgroundChatThread(String _send, String _prefix)
    {
        this.sendChat = _send;
        this.knownPrefix = _prefix;
    }

    public synchronized void run()
    {
        Minecraft mc = Minecraft.getMinecraft();
        mc.ingameGUI.getChatGUI().func_146239_a(this.sendChat);
        String cmdPrefix = "";
        String[] toSplit;
        byte start;

        if (this.knownPrefix != null && this.sendChat.startsWith(this.knownPrefix))
        {
            cmdPrefix = this.knownPrefix.trim() + " ";
            this.sendChat = this.sendChat.substring(this.knownPrefix.length()).trim();
            toSplit = this.sendChat.split(" ");
            start = 0;
        }
        else
        {
            toSplit = this.sendChat.split(" ");
            start = 0;

            if (toSplit.length > 0 && toSplit[0].startsWith("/"))
            {
                if (toSplit[0].startsWith("/msg"))
                {
                    cmdPrefix = toSplit[0] + " " + toSplit[1] + " ";
                    start = 2;
                }
                else if (!toSplit[0].trim().equals("/"))
                {
                    cmdPrefix = toSplit[0] + " ";
                    start = 1;
                }
            }
        }

        int suffix = cmdPrefix.length();
        StringBuilder sendPart = new StringBuilder(119);

        for (int message = start; message < toSplit.length; ++message)
        {
            if (sendPart.length() + toSplit[message].length() + suffix > 100)
            {
                mc.thePlayer.sendChatMessage(cmdPrefix + sendPart.toString().trim());

                try
                {
                    Thread.sleep((long)Integer.parseInt(TabbyChat.advancedSettings.multiChatDelay.getValue()));
                }
                catch (InterruptedException var14)
                {
                    var14.printStackTrace();
                }

                sendPart = new StringBuilder(119);

                if (toSplit[message].startsWith("/"))
                {
                    sendPart.append("_");
                }
            }

            sendPart.append(toSplit[message] + " ");
        }

        if (sendPart.length() > 0 || cmdPrefix.length() > 0)
        {
            String var15 = cmdPrefix + sendPart.toString().trim();
            var15 = ChatAllowedCharacters.filerAllowedCharacters(var15);

            if (TabbyChat.forgePresent)
            {
                try
                {
                    Class e = Class.forName("net.minecraftforge.client.ClientCommandHandler");
                    Method exeCmd;

                    try
                    {
                        exeCmd = e.getMethod("executeCommand", new Class[] {ICommandSender.class, String.class});
                    }
                    catch (NoSuchMethodException var12)
                    {
                        exeCmd = e.getMethod("executeCommand", new Class[] {ICommandSender.class, String.class});
                    }

                    Object instance = e.getField("instance").get((Object)null);
                    int value = ((Integer)exeCmd.invoke(instance, new Object[] {mc.thePlayer, var15})).intValue();

                    if (value == 1)
                    {
                        return;
                    }
                }
                catch (Exception var13)
                {
                    TabbyChatUtils.log.warn("Oops, something went wrong while checking the message for Client Commands");
                    var13.printStackTrace();
                }
            }

            mc.thePlayer.sendChatMessage(var15);
        }
    }
}
