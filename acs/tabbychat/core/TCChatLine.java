package acs.tabbychat.core;

import acs.tabbychat.util.TCChatLineFake;
import com.google.gson.annotations.Expose;
import java.util.Date;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class TCChatLine extends TCChatLineFake
{
    @Expose
    protected boolean statusMsg;
    @Expose
    public Date timeStamp;

    public TCChatLine(int _counter, IChatComponent _string, int _id)
    {
        super(_counter, _string, _id);
        this.statusMsg = false;
    }

    public TCChatLine(ChatLine _cl)
    {
        super(_cl.getUpdatedCounter(), _cl.func_151461_a(), _cl.getChatLineID());
        this.statusMsg = false;

        if (_cl instanceof TCChatLine)
        {
            this.timeStamp = ((TCChatLine)_cl).timeStamp;
            this.statusMsg = ((TCChatLine)_cl).statusMsg;
        }
    }

    public TCChatLine(int _counter, IChatComponent _string, int _id, boolean _stat)
    {
        this(_counter, _string, _id);
        this.statusMsg = _stat;
    }

    protected void setChatLineString(IChatComponent newLine)
    {
        this.chatComponent = newLine;
    }

    public IChatComponent getTimeStamp()
    {
        String format = TabbyChat.generalSettings.timeStamp.format(this.timeStamp);
        return new ChatComponentText(format + " ");
    }

    public IChatComponent getChatComponentWithTimestamp()
    {
        IChatComponent result = this.getChatComponent();

        if (TabbyChat.generalSettings.timeStampEnable.getValue().booleanValue() && this.timeStamp != null)
        {
            result = this.getTimeStamp().appendSibling(result);
        }

        return result;
    }
}
