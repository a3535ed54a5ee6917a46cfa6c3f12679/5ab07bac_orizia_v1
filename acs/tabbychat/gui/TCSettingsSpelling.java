package acs.tabbychat.gui;

import acs.tabbychat.core.TabbyChat;
import acs.tabbychat.settings.TCSettingBool;
import acs.tabbychat.settings.TCSettingList;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;

public class TCSettingsSpelling extends TCSettingsGUI
{
    private static final int SPELL_CHECK_ENABLE = 9108;
    private static final int ADD_WORD = 9502;
    private static final int REMOVE_WORD = 9503;
    private static final int CLEAR_WORDS = 9504;
    private static final int NEXT = 9506;
    private static final int PREV = 9507;
    private static final int OPEN = 9508;
    private static final int RELOAD = 9509;
    public TCSettingBool spellCheckEnable;
    private GuiTextField wordInput;
    private PrefsButton addWord;
    private PrefsButton removeWords;
    private PrefsButton clearWords;
    private PrefsButton next;
    private PrefsButton prev;
    private PrefsButton open;
    private PrefsButton reload;
    private File dictionary;
    public TCSettingList spellingList;

    public TCSettingsSpelling(TabbyChat _tc)
    {
        super(_tc);
        this.propertyPrefix = "settings.spelling";
        this.spellCheckEnable = new TCSettingBool(Boolean.valueOf(true), "spellCheckEnable", this.propertyPrefix, 9108);
        this.addWord = new PrefsButton(9502, 0, 0, 15, 12, ">");
        this.removeWords = new PrefsButton(9503, 0, 0, 15, 12, "<");
        this.clearWords = new PrefsButton(9504, 0, 0, 15, 12, "<<");
        this.next = new PrefsButton(9506, 0, 0, 15, 12, "->");
        this.prev = new PrefsButton(9507, 0, 0, 15, 12, "<-");
        this.open = new PrefsButton(9508, 0, 0, 85, 15, "");
        this.reload = new PrefsButton(9509, 0, 0, 85, 15, "");
        this.dictionary = new File(tabbyChatDir, "dictionary.txt");
        this.spellingList = new TCSettingList(this.dictionary);
        this.name = I18n.format("settings.spelling.name", new Object[0]);
        this.settingsFile = new File(tabbyChatDir, "spellcheck.cfg");
        this.bgcolor = 1728034351;
        this.defineDrawableSettings();
    }

    public void saveSettingsFile()
    {
        try
        {
            this.spellingList.saveEntries();
        }
        catch (IOException var2)
        {
            var2.printStackTrace();
        }

        super.saveSettingsFile();
    }

    public Properties loadSettingsFile()
    {
        super.loadSettingsFile();

        try
        {
            this.dictionary.getParentFile().mkdirs();
            this.dictionary.createNewFile();
            this.spellingList.loadEntries();
        }
        catch (IOException var2)
        {
            var2.printStackTrace();
        }

        return null;
    }

    public void defineDrawableSettings()
    {
        this.buttonList.add(this.spellCheckEnable);
        this.buttonList.add(this.addWord);
        this.buttonList.add(this.removeWords);
        this.buttonList.add(this.clearWords);
        this.buttonList.add(this.next);
        this.buttonList.add(this.prev);
        this.buttonList.add(this.open);
        this.buttonList.add(this.reload);
    }

    public void initDrawableSettings()
    {
        int col1x = (this.width - 300) / 2 + 55;
        int col2x = this.width / 2 + 25;
        int buttonColor = (this.bgcolor & 16777215) + -16777216;
        this.spellCheckEnable.setButtonLoc(col1x, this.rowY(1));
        this.spellCheckEnable.setLabelLoc(col1x + 19);
        this.spellCheckEnable.buttonColor = buttonColor;
        this.spellingList.x(col2x);
        this.spellingList.y(this.rowY(4));
        this.spellingList.width(100);
        this.spellingList.height(96);
        this.wordInput = new GuiTextField(this.mc.fontRenderer, col1x, this.rowY(6), 75, 12);
        this.wordInput.func_146205_d(true);
        this.open.displayString = I18n.format("settings.spelling.opendictionary", new Object[0]);
        this.open.x(col1x);
        this.open.y(this.rowY(10));
        this.reload.displayString = I18n.format("settings.spelling.reloaddictionary", new Object[0]);
        this.reload.x(col1x);
        this.reload.y(this.rowY(9));
        this.addWord.x(col2x - 25);
        this.addWord.y(this.rowY(5));
        this.removeWords.x(col2x - 25);
        this.removeWords.y(this.rowY(6));
        this.clearWords.x(col2x - 25);
        this.clearWords.y(this.rowY(7));
        this.next.x(col2x + 53);
        this.next.y(this.rowY(10));
        this.prev.x(col2x + 33);
        this.prev.y(this.rowY(10));
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int x, int y, float f)
    {
        int col1x = (this.width - 300) / 2 + 55;
        int col2x = this.width / 2 + 45;
        super.drawScreen(x, y, f);
        this.wordInput.drawTextBox();
        this.spellingList.drawList(this.mc, x, y);
        this.drawString(this.fontRendererObj, I18n.format("settings.spelling.userdictionary", new Object[0]), col1x, this.rowY(3), 16777215);
        this.drawString(this.fontRendererObj, I18n.format("book.pageIndicator", new Object[] {this.spellingList.getPageNum(), Integer.valueOf(this.spellingList.getTotalPages())}), col2x, this.rowY(3), 16777215);
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        super.initGui();

        try
        {
            this.spellingList.loadEntries();
        }
        catch (IOException var2)
        {
            var2.printStackTrace();
        }
    }

    /**
     * Called when the mouse is clicked.
     */
    public void mouseClicked(int x, int y, int button)
    {
        super.mouseClicked(x, y, button);
        this.wordInput.mouseClicked(x, y, button);
        this.spellingList.mouseClicked(x, y, button);
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    public void keyTyped(char c, int i)
    {
        super.keyTyped(c, i);
        this.wordInput.textboxKeyTyped(c, i);
    }

    public void actionPerformed(GuiButton button)
    {
        label32:

        switch (button.id)
        {
            case 9502:
                this.spellingList.addToList(this.wordInput.getText());
                this.wordInput.setText("");
                break;

            case 9503:
                Iterator e = this.spellingList.getSelected().iterator();

                while (true)
                {
                    if (!e.hasNext())
                    {
                        break label32;
                    }

                    TCSettingList.Entry entry = (TCSettingList.Entry)e.next();
                    entry.remove();
                }

            case 9504:
                this.spellingList.clearList();

            case 9505:
            default:
                break;

            case 9506:
                this.spellingList.nextPage();
                break;

            case 9507:
                this.spellingList.previousPage();
                break;

            case 9508:
                try
                {
                    if (Desktop.isDesktopSupported())
                    {
                        Desktop.getDesktop().open(this.dictionary);
                    }
                }
                catch (IOException var5)
                {
                    ;
                }

                break;

            case 9509:
                try
                {
                    this.spellingList.loadEntries();
                }
                catch (IOException var4)
                {
                    var4.printStackTrace();
                }
        }

        super.actionPerformed(button);
    }

    public void validateButtonStates()
    {
        super.validateButtonStates();
    }

    public void storeTempVars()
    {
        super.storeTempVars();
    }

    public void saveSettingsFile(Properties var1)
    {
        super.saveSettingsFile(var1);
    }

    public int rowY(int var1)
    {
        return super.rowY(var1);
    }

    public void resetTempVars()
    {
        super.resetTempVars();
    }

    /**
     * Handles mouse input.
     */
    public void handleMouseInput()
    {
        super.handleMouseInput();
    }
}
