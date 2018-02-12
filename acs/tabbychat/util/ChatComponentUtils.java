package acs.tabbychat.util;

import acs.tabbychat.core.TCChatLine;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ChatComponentStyle;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class ChatComponentUtils
{
    private static FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;

    public static IChatComponent replaceText(IChatComponent chat, String regex, String replacement)
    {
        List iter = chat.getSiblings();
        ChatComponentText newChat = new ChatComponentText("");
        Iterator var5 = iter.iterator();

        while (var5.hasNext())
        {
            IChatComponent next = (IChatComponent)var5.next();
            ChatComponentText comp = new ChatComponentText(next.getUnformattedText().replaceAll(regex, replacement));
            comp.setChatStyle(next.getChatStyle().createShallowCopy());
            newChat.appendSibling(comp);
        }

        return newChat;
    }

    public static String formatString(String text, boolean force)
    {
        return !force && !Minecraft.getMinecraft().gameSettings.chatColours ? EnumChatFormatting.getTextWithoutFormattingCodes(text) : text;
    }

    public static List<TCChatLine> split(List<TCChatLine> lines, int width)
    {
        ArrayList list = Lists.newArrayList();
        Iterator var3 = lines.iterator();

        while (var3.hasNext())
        {
            TCChatLine line = (TCChatLine)var3.next();
            list.addAll(split(line, width));
        }

        return list;
    }

    public static List<TCChatLine> split(TCChatLine line, int width)
    {
        ArrayList list = Lists.newArrayList();
        List ichat = split(line.getChatComponentWithTimestamp(), width);
        Iterator var4 = ichat.iterator();

        while (var4.hasNext())
        {
            IChatComponent chat = (IChatComponent)var4.next();
            list.add(0, new TCChatLine(line.getUpdatedCounter(), chat, line.getChatLineID()));
        }

        return list;
    }

    public static List<IChatComponent> split(IChatComponent chat, int limit)
    {
        int j = 0;
        ChatComponentText chatcomponenttext = new ChatComponentText("");
        ArrayList arraylist = Lists.newArrayList();
        ArrayList arraylist1 = Lists.newArrayList(chat);

        for (int k = 0; k < arraylist1.size(); ++k)
        {
            if (chatcomponenttext == null)
            {
                chatcomponenttext = new ChatComponentText(" ");
            }

            IChatComponent ichatcomponent1 = (IChatComponent)arraylist1.get(k);
            String s = ichatcomponent1.getUnformattedTextForChat();

            if (s == null)
            {
                s = "";
            }

            boolean flag2 = false;
            String s1;

            if (s.contains("\n"))
            {
                int s4 = s.indexOf(10);
                s1 = s.substring(s4 + 1);
                s = s.substring(0, s4 + 1);
                ChatComponentText j1 = new ChatComponentText(s1);
                j1.setChatStyle(ichatcomponent1.getChatStyle().createShallowCopy());
                arraylist1.add(k + 1, j1);
                flag2 = true;
            }

            String var18 = formatString(ichatcomponent1.getChatStyle().getFormattingCode() + s, true);
            s1 = var18.endsWith("\n") ? var18.substring(0, var18.length() - 1) : var18;
            int var19 = fontRenderer.getStringWidth(s1);
            ChatComponentText chatcomponenttext2 = new ChatComponentText(s1);
            chatcomponenttext2.setChatStyle(ichatcomponent1.getChatStyle().createShallowCopy());

            if (j + var19 > limit)
            {
                String s2 = fontRenderer.trimStringToWidth(var18, limit - j, false);
                String s3 = s2.length() < var18.length() ? var18.substring(s2.length()) : null;

                if (s3 != null && s3.length() > 0)
                {
                    int i1 = s2.lastIndexOf(" ");

                    if (i1 >= 0 && fontRenderer.getStringWidth(var18.substring(0, i1)) > 0)
                    {
                        s2 = var18.substring(0, i1);
                        ++i1;
                        s3 = var18.substring(i1);
                    }
                    else if (j > 0 && !var18.contains(" "))
                    {
                        s2 = "";
                        s3 = var18;
                    }

                    ChatComponentText chatcomponenttext3 = new ChatComponentText(s3);
                    chatcomponenttext3.setChatStyle(ichatcomponent1.getChatStyle().createShallowCopy());
                    arraylist1.add(k + 1, chatcomponenttext3);
                }

                var19 = fontRenderer.getStringWidth(s2);
                chatcomponenttext2 = new ChatComponentText(s2);
                chatcomponenttext2.setChatStyle(ichatcomponent1.getChatStyle().createShallowCopy());
                flag2 = true;
            }

            if (j + var19 <= limit)
            {
                j += var19;
                chatcomponenttext.appendSibling(chatcomponenttext2);
            }
            else
            {
                flag2 = true;
            }

            if (flag2)
            {
                arraylist.add(chatcomponenttext);
                j = 0;
                chatcomponenttext = null;
            }
        }

        arraylist.add(chatcomponenttext);
        return arraylist;
    }

    public static IChatComponent formattedStringToChat(String chat)
    {
        ChatComponentText newChat = new ChatComponentText("");
        String[] parts = chat.split("\u00a7");
        boolean first = true;
        String[] var4 = parts;
        int var5 = parts.length;

        for (int var6 = 0; var6 < var5; ++var6)
        {
            String part = var4[var6];

            if (first)
            {
                first = false;
                newChat.appendText(part);
            }
            else
            {
                IChatComponent last = (IChatComponent)newChat.getSiblings().get(newChat.getSiblings().size() - 1);
                EnumChatFormatting format = null;
                EnumChatFormatting[] chat1 = EnumChatFormatting.values();
                int var11 = chat1.length;

                for (int var12 = 0; var12 < var11; ++var12)
                {
                    EnumChatFormatting formats = chat1[var12];

                    if (String.valueOf(formats.getFormattingCode()).equals(part.substring(0, 1)))
                    {
                        format = formats;
                    }
                }

                if (format != null)
                {
                    ChatComponentText var14 = new ChatComponentText(part.substring(1));

                    if (format.equals(EnumChatFormatting.RESET))
                    {
                        var14.getChatStyle().setColor(EnumChatFormatting.WHITE);
                        var14.getChatStyle().setBold(Boolean.valueOf(false));
                        var14.getChatStyle().setItalic(Boolean.valueOf(false));
                        var14.getChatStyle().setObfuscated(Boolean.valueOf(false));
                        var14.getChatStyle().setStrikethrough(Boolean.valueOf(false));
                        var14.getChatStyle().setUnderlined(Boolean.valueOf(false));
                    }
                    else
                    {
                        var14.setChatStyle(last.getChatStyle().createDeepCopy());
                    }

                    if (format.isColor())
                    {
                        var14.getChatStyle().setColor(format);
                    }

                    if (format.equals(EnumChatFormatting.BOLD))
                    {
                        var14.getChatStyle().setBold(Boolean.valueOf(true));
                    }

                    if (format.equals(EnumChatFormatting.ITALIC))
                    {
                        var14.getChatStyle().setItalic(Boolean.valueOf(true));
                    }

                    if (format.equals(EnumChatFormatting.UNDERLINE))
                    {
                        var14.getChatStyle().setUnderlined(Boolean.valueOf(true));
                    }

                    if (format.equals(EnumChatFormatting.OBFUSCATED))
                    {
                        var14.getChatStyle().setObfuscated(Boolean.valueOf(true));
                    }

                    if (format.equals(EnumChatFormatting.STRIKETHROUGH))
                    {
                        var14.getChatStyle().setStrikethrough(Boolean.valueOf(true));
                    }

                    newChat.appendSibling(var14);
                }
                else
                {
                    last.appendText("\u00a7" + part);
                }
            }
        }

        return newChat;
    }

    public static IChatComponent subComponent(IChatComponent chat, int index)
    {
        ChatComponentText result = new ChatComponentText("");
        int pos = 0;
        boolean found = false;
        Iterator iter = chat.iterator();

        while (iter.hasNext())
        {
            IChatComponent ichat = (IChatComponent)iter.next();

            if (ichat.getSiblings().isEmpty())
            {
                String text = ichat.getUnformattedTextForChat();

                if (text.length() + pos >= index)
                {
                    if (found)
                    {
                        result.appendSibling(ichat);
                    }
                    else
                    {
                        found = true;
                        ChatComponentText local = new ChatComponentText(text.substring(index - pos));
                        local.setChatStyle(ichat.getChatStyle().createDeepCopy());
                        result.appendSibling(local);
                    }
                }

                pos += text.length();
            }
        }

        return result;
    }

    public static IChatComponent subComponent(IChatComponent chat, int start, int end)
    {
        ChatComponentText result = new ChatComponentText("");
        int pos = start;
        Iterator iter = ChatComponentStyle.createDeepCopyIterator(subComponent(chat, start).getSiblings());

        while (iter.hasNext())
        {
            IChatComponent ichat = (IChatComponent)iter.next();

            if (ichat.getSiblings().isEmpty())
            {
                String text = ichat.getUnformattedTextForChat();

                if (pos + text.length() >= end)
                {
                    ChatComponentText local = new ChatComponentText(text.substring(0, end - pos));
                    local.getChatStyle().setParentStyle(chat.getChatStyle().createDeepCopy());
                    result.appendSibling(local);
                    break;
                }

                result.appendSibling(ichat);
                pos += text.length();
            }
        }

        return result;
    }
}
