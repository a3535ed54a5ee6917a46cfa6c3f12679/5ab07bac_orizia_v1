package acs.tabbychat.core;

import acs.tabbychat.util.TabbyChatUtils;
import net.minecraft.client.Minecraft;

public class TabbyChatMod
{
    public static boolean onTickInGui(Minecraft minecraft)
    {
        TabbyChatUtils.chatGuiTick(minecraft);
        return true;
    }
}
