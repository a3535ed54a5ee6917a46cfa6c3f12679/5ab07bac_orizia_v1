package acs.tabbychat.util;

import com.google.gson.annotations.Expose;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class TCChatLineFake extends ChatLine
{
    /** GUI Update Counter value this Line was created at */
    protected int updateCounterCreated = -1;
    @Expose
    protected IChatComponent chatComponent;

    /**
     * int value to refer to existing Chat Lines, can be 0 which means unreferrable
     */
    protected int chatLineID;

    public TCChatLineFake()
    {
        super(-1, new ChatComponentText(""), 0);
    }

    public TCChatLineFake(int _counter, IChatComponent _string, int _id)
    {
        super(_counter, _string, _id);
        this.updateCounterCreated = _counter;

        if (_string == null)
        {
            this.chatComponent = new ChatComponentText("");
        }
        else
        {
            this.chatComponent = _string;
        }

        this.chatLineID = _id;
    }

    @Deprecated
    public IChatComponent func_151461_a()
    {
        return this.getChatComponent();
    }

    public IChatComponent getChatComponent()
    {
        return this.chatComponent;
    }

    public int getUpdatedCounter()
    {
        return this.updateCounterCreated;
    }

    public int getChatLineID()
    {
        return this.chatLineID;
    }
}
