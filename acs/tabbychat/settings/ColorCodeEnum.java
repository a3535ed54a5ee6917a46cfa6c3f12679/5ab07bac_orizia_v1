package acs.tabbychat.settings;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;

public enum ColorCodeEnum
{
    DEFAULT(I18n.format("colors.default", new Object[0]), "", (EnumChatFormatting)null),
    BLACK(I18n.format("colors.black", new Object[0]), "\u00a70", EnumChatFormatting.BLACK),
    DARKBLUE(I18n.format("colors.darkblue", new Object[0]), "\u00a71", EnumChatFormatting.DARK_BLUE),
    DARKGREEN(I18n.format("colors.darkgreen", new Object[0]), "\u00a72", EnumChatFormatting.DARK_GREEN),
    DARKAQUA(I18n.format("colors.darkaqua", new Object[0]), "\u00a73", EnumChatFormatting.DARK_AQUA),
    DARKRED(I18n.format("colors.darkred", new Object[0]), "\u00a74", EnumChatFormatting.DARK_RED),
    PURPLE(I18n.format("colors.purple", new Object[0]), "\u00a75", EnumChatFormatting.DARK_PURPLE),
    GOLD(I18n.format("colors.gold", new Object[0]), "\u00a76", EnumChatFormatting.GOLD),
    GRAY(I18n.format("colors.gray", new Object[0]), "\u00a77", EnumChatFormatting.GRAY),
    DARKGRAY(I18n.format("colors.darkgray", new Object[0]), "\u00a78", EnumChatFormatting.DARK_GRAY),
    INDIGO(I18n.format("colors.indigo", new Object[0]), "\u00a79", EnumChatFormatting.BLUE),
    BRIGHTGREEN(I18n.format("colors.brightgreen", new Object[0]), "\u00a7a", EnumChatFormatting.GREEN),
    AQUA(I18n.format("colors.aqua", new Object[0]), "\u00a7b", EnumChatFormatting.AQUA),
    RED(I18n.format("colors.red", new Object[0]), "\u00a7c", EnumChatFormatting.RED),
    PINK(I18n.format("colors.pink", new Object[0]), "\u00a7d", EnumChatFormatting.LIGHT_PURPLE),
    YELLOW(I18n.format("colors.yellow", new Object[0]), "\u00a7e", EnumChatFormatting.YELLOW),
    WHITE(I18n.format("colors.white", new Object[0]), "\u00a7f", EnumChatFormatting.WHITE);
    private String title;
    private String code;
    private EnumChatFormatting vanilla;

    private ColorCodeEnum(String _name, String _code, EnumChatFormatting _vanilla)
    {
        this.title = _name;
        this.code = _code;
        this.vanilla = _vanilla;
    }

    public String toString()
    {
        return this.code + this.title + "\u00a7r";
    }

    public String toCode()
    {
        return this.code;
    }

    public String color()
    {
        return this.title;
    }

    public EnumChatFormatting toVanilla()
    {
        return this.vanilla;
    }

    public static ColorCodeEnum cleanValueOf(String name)
    {
        try
        {
            return valueOf(name);
        }
        catch (Exception var2)
        {
            return YELLOW;
        }
    }
}
