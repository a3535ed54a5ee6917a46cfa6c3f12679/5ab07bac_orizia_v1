package acs.tabbychat.jazzy;

import acs.tabbychat.core.TabbyChat;
import com.swabunga.spell.engine.SpellDictionaryHashMap;
import com.swabunga.spell.event.SpellCheckEvent;
import com.swabunga.spell.event.SpellCheckListener;
import com.swabunga.spell.event.SpellChecker;
import com.swabunga.spell.event.StringWordTokenizer;
import java.io.File;
import java.io.InputStream;

public class TCSpellCheckListener implements SpellCheckListener
{
    protected SpellChecker spellCheck = null;

    public TCSpellCheckListener()
    {
        try
        {
            InputStream e = TCSpellCheckListener.class.getResourceAsStream("/english.0");
        }
        catch (Exception var2)
        {
            TabbyChat.printException("", var2);
        }
    }

    public TCSpellCheckListener(File dict)
    {
        try
        {
            SpellDictionaryHashMap e = new SpellDictionaryHashMap(dict);
            this.spellCheck = new SpellChecker(e);
            this.spellCheck.addSpellCheckListener(this);
        }
        catch (Exception var3)
        {
            TabbyChat.printException("Error instantiating spell checker", var3);
        }
    }

    public void spellingError(SpellCheckEvent event)
    {
        TabbyChat.spellChecker.handleListenerEvent(event);
    }

    public void checkSpelling(String line)
    {
        this.spellCheck.checkSpelling(new StringWordTokenizer(line));
    }
}
