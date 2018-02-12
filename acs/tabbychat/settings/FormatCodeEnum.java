package acs.tabbychat.settings;

import net.minecraft.client.resources.I18n;

public enum FormatCodeEnum
{
    DEFAULT(I18n.format("formats.default", new Object[0]), ""),
    BOLD(I18n.format("formats.bold", new Object[0]), "\u00a7l"),
    STRIKED(I18n.format("formats.striked", new Object[0]), "\u00a7m"),
    UNDERLINE(I18n.format("formats.underline", new Object[0]), "\u00a7n"),
    ITALIC(I18n.format("formats.italic", new Object[0]), "\u00a7o"),
    MAGIC(I18n.format("formats.magic", new Object[0]), "\u00a7k");
    private String title;
    private String code;

    private FormatCodeEnum(String _name, String _code)
    {
        this.title = _name;
        this.code = _code;
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

    public static FormatCodeEnum cleanValueOf(String name)
    {
        try
        {
            return valueOf(name);
        }
        catch (Exception var2)
        {
            return DEFAULT;
        }
    }
}
