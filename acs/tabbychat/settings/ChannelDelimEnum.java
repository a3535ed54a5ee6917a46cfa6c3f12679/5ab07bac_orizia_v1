package acs.tabbychat.settings;

import net.minecraft.client.resources.I18n;

public enum ChannelDelimEnum
{
    ANGLES(I18n.format("delims.angles", new Object[0]), "<", ">"),
    BRACES(I18n.format("delims.braces", new Object[0]), "{", "}"),
    BRACKETS(I18n.format("delims.brackets", new Object[0]), "[", "]"),
    PARENTHESIS(I18n.format("delims.parenthesis", new Object[0]), "(", ")"),
    ANGLESPARENSCOMBO(I18n.format("delims.anglesparenscombo", new Object[0]), "<\\(", ")(?: |\u00a7r)?[A-Za-z0-9_]{1,16}>"),
    ANGLESBRACKETSCOMBO(I18n.format("delims.anglesbracketscombo", new Object[0]), "<\\[", "](?: |\u00a7r)?[A-Za-z0-9_]{1,16}>");
    private String title;
    private String open;
    private String close;

    private ChannelDelimEnum(String title, String open, String close)
    {
        this.title = title;
        this.open = open;
        this.close = close;
    }

    public String getTitle()
    {
        return this.title;
    }

    public String toString()
    {
        return this.title;
    }

    public void setValue(String _title)
    {
        ChannelDelimEnum[] var2 = values();
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4)
        {
            ChannelDelimEnum tmp = var2[var4];

            if (_title.equals(tmp.title))
            {
                this.title = tmp.title;
                this.open = tmp.open;
                this.close = tmp.close;
                break;
            }
        }
    }

    public String open()
    {
        return this.open;
    }

    public String close()
    {
        return this.close;
    }
}
