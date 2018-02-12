package acs.tabbychat.settings;

import acs.tabbychat.core.TabbyChat;
import acs.tabbychat.util.TabbyChatUtils;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import org.apache.commons.lang3.ArrayUtils;

public class TCChatFilter
{
    public boolean inverseMatch;
    public boolean caseSensitive;
    public boolean highlightBool;
    public boolean audioNotificationBool;
    public boolean sendToTabBool;
    public boolean sendToAllTabs;
    public boolean removeMatches;
    public ColorCodeEnum highlightColor;
    public FormatCodeEnum highlightFormat;
    public NotificationSoundEnum audioNotificationSound;
    public String sendToTabName;
    public String expressionString;
    public Pattern expressionPattern;
    public String filterName;
    private int[] lastMatch;
    private String tabName;

    public TCChatFilter(String name)
    {
        this.inverseMatch = false;
        this.caseSensitive = false;
        this.highlightBool = true;
        this.audioNotificationBool = false;
        this.sendToTabBool = false;
        this.sendToAllTabs = false;
        this.removeMatches = false;
        this.highlightColor = ColorCodeEnum.YELLOW;
        this.highlightFormat = FormatCodeEnum.BOLD;
        this.audioNotificationSound = NotificationSoundEnum.ORB;
        this.highlightBool = false;
        this.sendToTabName = "";
        this.expressionString = ".*";
        this.expressionPattern = Pattern.compile(this.expressionString);
        this.tabName = null;
        this.filterName = name;
    }

    public TCChatFilter(TCChatFilter orig)
    {
        this(orig.filterName);
        this.copyFrom(orig);
    }

    public boolean applyFilterToDirtyChat(IChatComponent input)
    {
        Matcher findFilterMatches = this.expressionPattern.matcher(EnumChatFormatting.getTextWithoutFormattingCodes(input.getUnformattedText()));
        boolean foundMatch = false;
        ArrayList list = Lists.newArrayList();

        while (findFilterMatches.find())
        {
            foundMatch = true;

            if (!this.highlightBool)
            {
                break;
            }

            list.add(Integer.valueOf(findFilterMatches.start()));
            list.add(Integer.valueOf(findFilterMatches.end()));
        }

        this.lastMatch = ArrayUtils.toPrimitive((Integer[])list.toArray(new Integer[list.size()]));

        if (this.sendToTabBool && !this.sendToAllTabs)
        {
            if (this.inverseMatch)
            {
                this.tabName = this.sendToTabName;
            }
            else if (this.sendToTabName.startsWith("%"))
            {
                int group = TabbyChatUtils.parseInteger(this.sendToTabName.substring(1));

                if (foundMatch && group >= 0 && findFilterMatches.groupCount() >= group)
                {
                    this.tabName = findFilterMatches.group(group);

                    if (this.tabName == null)
                    {
                        this.tabName = this.filterName;
                    }
                }
                else
                {
                    this.tabName = this.filterName;
                }
            }
            else
            {
                this.tabName = this.sendToTabName;
            }
        }
        else
        {
            this.tabName = null;
        }

        return !foundMatch && this.inverseMatch || foundMatch && !this.inverseMatch;
    }

    public void audioNotification()
    {
        Minecraft.getMinecraft().thePlayer.playSound(this.audioNotificationSound.file(), 1.0F, 1.0F);
    }

    public void compilePattern()
    {
        try
        {
            if (this.caseSensitive)
            {
                this.expressionPattern = Pattern.compile(this.expressionString);
            }
            else
            {
                this.expressionPattern = Pattern.compile(this.expressionString, 2);
            }
        }
        catch (PatternSyntaxException var2)
        {
            TabbyChat.printMessageToChat("Invalid expression entered for filter \'" + this.filterName + "\', resetting to default.");
            this.expressionString = ".*";
            this.expressionPattern = Pattern.compile(this.expressionString);
        }
    }

    public void compilePattern(String newExpression)
    {
        this.expressionString = newExpression;
        this.compilePattern();
    }

    public void copyFrom(TCChatFilter orig)
    {
        this.filterName = orig.filterName;
        this.inverseMatch = orig.inverseMatch;
        this.caseSensitive = orig.caseSensitive;
        this.highlightBool = orig.highlightBool;
        this.audioNotificationBool = orig.audioNotificationBool;
        this.sendToTabBool = orig.sendToTabBool;
        this.sendToAllTabs = orig.sendToAllTabs;
        this.removeMatches = orig.removeMatches;
        this.highlightColor = orig.highlightColor;
        this.highlightFormat = orig.highlightFormat;
        this.audioNotificationSound = orig.audioNotificationSound;
        this.sendToTabName = orig.sendToTabName;
        this.expressionString = orig.expressionString;
        this.compilePattern();
    }

    public int[] getLastMatch()
    {
        int[] tmp = this.lastMatch;
        this.lastMatch = null;
        return tmp;
    }

    public Properties getProperties()
    {
        Properties myProps = new Properties();
        myProps.put("filterName", this.filterName);
        myProps.put("inverseMatch", Boolean.valueOf(this.inverseMatch));
        myProps.put("caseSensitive", Boolean.valueOf(this.caseSensitive));
        myProps.put("highlightBool", Boolean.valueOf(this.highlightBool));
        myProps.put("audioNotificationBool", Boolean.valueOf(this.audioNotificationBool));
        myProps.put("sendToTabBool", Boolean.valueOf(this.sendToTabBool));
        myProps.put("sendToAllTabs", Boolean.valueOf(this.sendToAllTabs));
        myProps.put("removeMatches", Boolean.valueOf(this.removeMatches));
        myProps.put("highlightColor", this.highlightColor.name());
        myProps.put("highlightFormat", this.highlightFormat.name());
        myProps.put("audioNotificationSound", this.audioNotificationSound.name());
        myProps.put("sendToTabName", this.sendToTabName);
        myProps.put("expressionString", this.expressionString);
        return myProps;
    }

    public String getTabName()
    {
        String tmp = this.tabName;
        this.tabName = null;
        return tmp;
    }
}
