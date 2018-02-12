package acs.tabbychat.settings;

import java.util.Properties;
import net.minecraft.client.Minecraft;

public interface ITCSetting
{
    void actionPerformed();

    void clear();

    void disable();

    void drawButton(Minecraft var1, int var2, int var3);

    void enable();

    boolean enabled();

    Object getDefault();

    String getProperty();

    Object getTempValue();

    String getType();

    Boolean hovered(int var1, int var2);

    void loadSelfFromProps(Properties var1);

    void mouseClicked(int var1, int var2, int var3);

    void reset();

    void resetDescription();

    void save();

    void saveSelfToProps(Properties var1);

    void setButtonDims(int var1, int var2);

    void setButtonLoc(int var1, int var2);

    void setLabelLoc(int var1);

    void setTempValue(Object var1);

    void setCleanValue(Object var1);

    void setValue(Object var1);
}
