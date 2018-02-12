package acs.tabbychat.settings;

import acs.tabbychat.gui.PrefsButton;
import acs.tabbychat.jazzy.TCSpellCheckManager;
import com.google.common.collect.Lists;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.MathHelper;
import org.apache.commons.io.IOUtils;

public class TCSettingList extends Gui
{
    private List<TCSettingList.Entry> list = Lists.newArrayList();
    private List<TCSettingList.Entry> selected = Lists.newArrayList();
    private final File dictionary;
    private int currentPage = 1;
    private int id = 0;
    private int xPosition;
    private int yPosition;
    private int width;
    private int height;

    public TCSettingList(File file)
    {
        this.dictionary = file;
    }

    public void drawList(Minecraft mc, int cursorX, int cursorY)
    {
        Gui.drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, Integer.MIN_VALUE);
        Gui.drawRect(this.xPosition - 1, this.yPosition - 1, this.xPosition, this.yPosition + this.height, -16777215);
        Gui.drawRect(this.xPosition - 1, this.yPosition + this.height, this.xPosition + this.width + 1, this.yPosition + this.height + 1, -16777215);
        Gui.drawRect(this.xPosition + this.width, this.yPosition + this.height + 1, this.xPosition + this.width + 1, this.yPosition - 1, -16777215);
        Gui.drawRect(this.xPosition - 1, this.yPosition - 1, this.xPosition + this.width + 1, this.yPosition, -16777215);
        int i = 0;

        for (Iterator var5 = this.getVisible().iterator(); var5.hasNext(); ++i)
        {
            TCSettingList.Entry entry = (TCSettingList.Entry)var5.next();
            entry.setPos(i);
            entry.drawButton(mc, cursorX, cursorY);
        }
    }

    public boolean contains(String srt)
    {
        Iterator var2 = this.list.iterator();
        TCSettingList.Entry entry;

        do
        {
            if (!var2.hasNext())
            {
                return false;
            }

            entry = (TCSettingList.Entry)var2.next();
        }
        while (!srt.equals(entry.displayString));

        return true;
    }

    public void addToList(String str)
    {
        if (str != null && !str.isEmpty())
        {
            if (!this.contains(str))
            {
                char[] chararray = str.toCharArray();
                int j = 0;
                label40:

                for (Iterator var4 = this.list.iterator(); var4.hasNext(); ++j)
                {
                    TCSettingList.Entry entry = (TCSettingList.Entry)var4.next();
                    char[] chararray1 = entry.displayString.toCharArray();

                    for (int i = 0; i < Math.min(chararray.length, chararray1.length); ++i)
                    {
                        char c = chararray[i];
                        char c1 = chararray1[i];

                        if (c > c1)
                        {
                            break;
                        }

                        if (c != c1 && c < c1)
                        {
                            this.list.add(j, new TCSettingList.Entry(this.id, str));
                            break label40;
                        }
                    }
                }

                if (!this.contains(str))
                {
                    this.list.add(new TCSettingList.Entry(this.id, str));
                }

                ++this.id;
            }
        }
    }

    public void removeFromList(String str)
    {
        Iterator var2 = this.list.iterator();

        while (var2.hasNext())
        {
            TCSettingList.Entry entry = (TCSettingList.Entry)var2.next();

            if (str.equals(entry.displayString))
            {
                this.list.remove(entry);
                this.deselectEntry(entry);
            }
        }
    }

    public void clearList()
    {
        this.list.clear();
    }

    public List<TCSettingList.Entry> getEntries()
    {
        return new ArrayList(this.list);
    }

    public List<TCSettingList.Entry> getVisible()
    {
        ArrayList list = Lists.newArrayList();
        int i = 0;

        for (int i1 = 0; i < this.currentPage; ++i)
        {
            list.clear();

            for (int i2 = 0; i1 < this.list.size() && list.size() < 8; ++i2)
            {
                TCSettingList.Entry entry = (TCSettingList.Entry)this.list.get(i1);
                entry.setPos(i2);
                list.add(entry);
                ++i1;
            }
        }

        return list;
    }

    public List<TCSettingList.Entry> getSelected()
    {
        return new ArrayList(this.selected);
    }

    public void selectEntry(TCSettingList.Entry entry)
    {
        if (!this.list.contains(entry))
        {
            this.list.add(entry);
        }

        if (!entry.isSelected())
        {
            this.selected.add(entry);
        }
    }

    public void deselectEntry(TCSettingList.Entry entry)
    {
        if (entry.isSelected())
        {
            this.selected.remove(entry);
        }
    }

    public void clearSelection()
    {
        this.selected.clear();
    }

    public int getTotalPages()
    {
        double pages = (double)this.list.size() / 8.0D;
        return MathHelper.ceiling_double_int(pages);
    }

    public Object getPageNum()
    {
        return Integer.valueOf(this.currentPage);
    }

    public void nextPage()
    {
        this.currentPage = Math.min(this.currentPage + 1, this.getTotalPages());
    }

    public void previousPage()
    {
        this.currentPage = Math.max(this.currentPage - 1, 1);
    }

    public void saveEntries() throws IOException
    {
        FileWriter writer = new FileWriter(this.dictionary);
        Iterator var2 = this.list.iterator();

        while (var2.hasNext())
        {
            TCSettingList.Entry entry = (TCSettingList.Entry)var2.next();
            writer.write(entry.displayString);
            writer.write("\n");
        }

        writer.close();
        this.loadEntries();
    }

    public void loadEntries() throws IOException
    {
        this.clearList();
        Iterator var1 = IOUtils.readLines(new FileReader(this.dictionary)).iterator();

        while (var1.hasNext())
        {
            String val = (String)var1.next();
            TCSpellCheckManager.getInstance().addToIgnoredWords(val);
            this.addToList(val);
        }
    }

    public void mouseClicked(int x, int y, int button)
    {
        if (x > this.x() && x < this.x() + this.width() && y > this.y() && y < this.y() + this.height())
        {
            Iterator var4 = this.getVisible().iterator();

            while (var4.hasNext())
            {
                TCSettingList.Entry entry = (TCSettingList.Entry)var4.next();

                if (y > entry.y() && y < entry.y() + entry.height())
                {
                    entry.func_146113_a(Minecraft.getMinecraft().getSoundHandler());
                    this.actionPerformed(entry);
                    return;
                }
            }
        }
    }

    private void actionPerformed(TCSettingList.Entry entry)
    {
        entry.setSelected(!entry.isSelected());
    }

    public int width()
    {
        return this.width;
    }

    public void width(int w)
    {
        this.width = w;
    }

    public int height()
    {
        return this.height;
    }

    public void height(int h)
    {
        this.height = h;
    }

    public int x()
    {
        return this.xPosition;
    }

    public void x(int x)
    {
        this.xPosition = x;
    }

    public int y()
    {
        return this.yPosition;
    }

    public void y(int y)
    {
        this.yPosition = y;
    }

    public class Entry extends PrefsButton
    {
        private TCSettingList list = TCSettingList.this;
        private int pos;

        public Entry(int id, String value)
        {
            super(id, TCSettingList.this.x(), 0, TCSettingList.this.width(), 12, value);
        }

        public void setPos(int y)
        {
            this.pos = y;
        }

        public boolean isSelected()
        {
            return this.list.getSelected().contains(this);
        }

        public void setSelected(boolean value)
        {
            if (value)
            {
                this.list.selectEntry(this);
            }
            else
            {
                this.list.deselectEntry(this);
            }
        }

        public void drawButton(Minecraft mc, int x, int y)
        {
            if (this.isSelected())
            {
                this.bgcolor = -577136231;
            }
            else
            {
                this.bgcolor = -587202560;
            }

            this.y(this.list.y() + this.pos * 12);
            super.drawButton(mc, x, y);
        }

        public void remove()
        {
            this.list.list.remove(this);
        }

        public void keyClicked(int x, int y, int button) {}
    }
}
