package acs.tabbychat.gui.context;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ResourceLocation;

public class ContextSpellingSuggestion extends ChatContext
{
    private String[] suggestions;
    private String title;

    public void onClicked() {}

    public String getDisplayString()
    {
        return this.title;
    }

    public ResourceLocation getDisplayIcon()
    {
        return null;
    }

    public List<ChatContext> getChildren()
    {
        ArrayList list = Lists.newArrayList();

        if (this.suggestions == null)
        {
            return null;
        }
        else
        {
            String[] var2 = this.suggestions;
            int var3 = var2.length;

            for (int var4 = 0; var4 < var3; ++var4)
            {
                String word = var2[var4];
                list.add(this.makeBaby(word));
            }

            return list;
        }
    }

    public boolean isPositionValid(int x, int y)
    {
        this.title = "Spelling";
        GuiTextField text = this.getMenu().screen.inputField2;
        int start = text.func_146187_c(-1);
        int end = text.func_146187_c(1);
        String word = text.getText().substring(start, end);

        if (word != null && !word.isEmpty())
        {
            ;
        }

        this.suggestions = null;
        return false;
    }

    private String[] objectToStringArray(Object[] object)
    {
        String[] array = new String[object.length];

        for (int i = 0; i < array.length; ++i)
        {
            array[i] = object[i].toString();
        }

        return array;
    }

    public ChatContext.Behavior getDisabledBehavior()
    {
        return this.suggestions == null ? ChatContext.Behavior.HIDE : ChatContext.Behavior.GRAY;
    }

    private ChatContext makeBaby(final String word)
    {
        return new ChatContext()
        {
            public void onClicked()
            {
                GuiTextField field = this.getMenu().screen.inputField2;
                int start = field.func_146187_c(-1);
                int end = field.func_146187_c(1);
                field.func_146190_e(start);
                field.func_146199_i(end);
                String sel = field.func_146207_c();
                char pref = sel.charAt(0);
                char suff = sel.charAt(sel.length() - 1);

                if (Character.isLetter(pref))
                {
                    pref = 0;
                }

                if (Character.isLetter(suff))
                {
                    suff = 32;
                }

                this.getMenu().screen.inputField2.func_146191_b((pref != 0 ? Character.valueOf(pref) : "") + word + suff);
            }
            public boolean isPositionValid(int x, int y)
            {
                return true;
            }
            public String getDisplayString()
            {
                return word;
            }
            public ResourceLocation getDisplayIcon()
            {
                return null;
            }
            public ChatContext.Behavior getDisabledBehavior()
            {
                return ChatContext.Behavior.GRAY;
            }
            public List<ChatContext> getChildren()
            {
                return null;
            }
        };
    }
}
