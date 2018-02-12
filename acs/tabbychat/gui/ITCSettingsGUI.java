package acs.tabbychat.gui;

import acs.tabbychat.util.TabbyChatUtils;
import java.io.File;
import java.util.Properties;
import net.minecraft.client.gui.GuiButton;

public interface ITCSettingsGUI
{
    int SAVEBUTTON = 8901;
    int CANCELBUTTON = 8902;
    int MARGIN = 4;
    int LINE_HEIGHT = 14;
    int DISPLAY_WIDTH = 300;
    int DISPLAY_HEIGHT = 180;
    File tabbyChatDir = TabbyChatUtils.getTabbyChatDir();

    void actionPerformed(GuiButton var1);

    void defineDrawableSettings();

    void drawScreen(int var1, int var2, float var3);

    void handleMouseInput();

    void initDrawableSettings();

    void initGui();

    void keyTyped(char var1, int var2);

    Properties loadSettingsFile();

    void mouseClicked(int var1, int var2, int var3);

    void resetTempVars();

    int rowY(int var1);

    void saveSettingsFile();

    void saveSettingsFile(Properties var1);

    void storeTempVars();

    void validateButtonStates();
}
