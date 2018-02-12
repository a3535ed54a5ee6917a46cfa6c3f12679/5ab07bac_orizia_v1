package acs.tabbychat.jazzy;

import acs.tabbychat.core.TabbyChat;
import acs.tabbychat.gui.ITCSettingsGUI;
import com.swabunga.spell.event.SpellCheckEvent;
import com.swabunga.spell.event.StringWordTokenizer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class TCSpellCheckManager
{
    private static TCSpellCheckManager instance = null;
    private TCSpellCheckListener listener;
    private HashMap<Integer, String> errorCache = new HashMap();
    private final ReentrantReadWriteLock errorLock = new ReentrantReadWriteLock();
    private final Lock errorReadLock;
    private final Lock errorWriteLock;
    private String lastAttemptedLocale;

    private TCSpellCheckManager()
    {
        this.errorReadLock = this.errorLock.readLock();
        this.errorWriteLock = this.errorLock.writeLock();
        this.reloadDictionaries();
    }

    public void addToIgnoredWords(String word)
    {
        if (!this.listener.spellCheck.isIgnored(word))
        {
            this.listener.spellCheck.ignoreAll(word);
        }
    }

    public void drawErrors(GuiScreen screen, List<GuiTextField> inputFields)
    {
        ArrayList inputCache = new ArrayList();
        int activeFields = 0;
        Iterator errors;
        GuiTextField inputs;

        for (errors = inputFields.iterator(); errors.hasNext(); inputCache.add(inputs.getText()))
        {
            inputs = (GuiTextField)errors.next();

            if (inputs.func_146176_q())
            {
                ++activeFields;
            }
        }

        if (activeFields != 0)
        {
            this.errorReadLock.lock();

            try
            {
                errors = this.errorCache.entrySet().iterator();

                while (errors.hasNext())
                {
                    Entry error = (Entry)errors.next();
                    ListIterator var18 = inputCache.listIterator(activeFields);

                    if (!var18.hasPrevious())
                    {
                        return;
                    }

                    String input = (String)var18.previous();

                    if (input.length() == 0)
                    {
                        return;
                    }

                    int y = screen.height - 4 - 12 * (activeFields - 1);
                    byte x = 4;
                    boolean width = false;
                    int wordIndex = ((Integer)error.getKey()).intValue();
                    int errLength;

                    for (errLength = ((String)error.getValue()).length(); wordIndex >= input.length(); input = (String)var18.previous())
                    {
                        wordIndex -= input.length();
                        y += 12;

                        if (!var18.hasPrevious())
                        {
                            return;
                        }
                    }

                    int var19;
                    int var20;

                    if (wordIndex + errLength > input.length())
                    {
                        var19 = x + Minecraft.getMinecraft().fontRenderer.getStringWidth(input.substring(0, wordIndex));
                        var20 = Minecraft.getMinecraft().fontRenderer.getStringWidth(input.substring(wordIndex, input.length()));
                        this.drawUnderline(screen, var19, y, var20);

                        if (var18.hasPrevious())
                        {
                            int remainder = errLength - input.length() + wordIndex;
                            input = (String)var18.previous();

                            if (input.length() == 0)
                            {
                                continue;
                            }

                            if (remainder > input.length())
                            {
                                return;
                            }

                            y += 12;
                            var19 = 4;
                            var20 = Minecraft.getMinecraft().fontRenderer.getStringWidth(input.substring(0, remainder));
                        }
                    }
                    else
                    {
                        var19 = x + Minecraft.getMinecraft().fontRenderer.getStringWidth(input.substring(0, wordIndex));
                        var20 = Minecraft.getMinecraft().fontRenderer.getStringWidth((String)error.getValue());
                    }

                    this.drawUnderline(screen, var19, y, var20);
                }
            }
            finally
            {
                this.errorReadLock.unlock();
            }
        }
    }

    private void drawUnderline(GuiScreen screen, int x, int y, int width)
    {
        for (int next = x + 1; next - x < width; next += 2)
        {
            Gui.drawRect(next - 1, y, next, y + 1, -1426128896);
        }
    }

    public static TCSpellCheckManager getInstance()
    {
        if (instance == null)
        {
            instance = new TCSpellCheckManager();
        }

        return instance;
    }

    protected void handleListenerEvent(SpellCheckEvent event)
    {
        this.errorWriteLock.lock();

        try
        {
            this.errorCache.put(Integer.valueOf(event.getWordContextPosition()), event.getInvalidWord());
        }
        finally
        {
            this.errorWriteLock.unlock();
        }
    }

    public boolean loadLocaleDictionary()
    {
        File localeDict = new File(ITCSettingsGUI.tabbyChatDir, Minecraft.getMinecraft().gameSettings.language + ".dic");

        if (localeDict.canRead())
        {
            this.listener = new TCSpellCheckListener(localeDict);
            return true;
        }
        else
        {
            return false;
        }
    }

    public void loadUserDictionary()
    {
        File userDict = new File(ITCSettingsGUI.tabbyChatDir, "user.dic");
        BufferedReader in = null;

        if (userDict.canRead())
        {
            try
            {
                in = new BufferedReader(new FileReader(userDict));
                String e;

                while ((e = in.readLine()) != null)
                {
                    this.listener.spellCheck.ignoreAll(e);
                }
            }
            catch (Exception var12)
            {
                TabbyChat.printException("Unable to load user dictionary for spell checking", var12);
            }
            finally
            {
                try
                {
                    if (in != null)
                    {
                        in.close();
                    }
                }
                catch (Exception var11)
                {
                    ;
                }
            }
        }
    }

    public void reloadDictionaries()
    {
        if (!this.loadLocaleDictionary())
        {
            this.listener = new TCSpellCheckListener();
        }

        this.lastAttemptedLocale = Minecraft.getMinecraft().gameSettings.language;
        this.loadUserDictionary();
    }

    public void update(List<GuiTextField> inputFields)
    {
        if (this.lastAttemptedLocale != Minecraft.getMinecraft().gameSettings.language)
        {
            this.reloadDictionaries();
        }

        this.errorWriteLock.lock();

        try
        {
            this.errorCache.clear();
        }
        finally
        {
            this.errorWriteLock.unlock();
        }

        String inputCache = "";
        Iterator var3 = inputFields.iterator();

        while (var3.hasNext())
        {
            GuiTextField inputField = (GuiTextField)var3.next();

            if (inputField.func_146176_q())
            {
                inputCache = inputField.getText() + inputCache;
            }
        }

        this.listener.checkSpelling(inputCache);
    }

    public List<String> getSuggestions(String word, int threshold)
    {
        return this.listener.spellCheck.getSuggestions(word, threshold);
    }

    public boolean isSpelledCorrectly(String word)
    {
        return this.listener.spellCheck.checkSpelling(new StringWordTokenizer(word)) == -1;
    }
}
