package acs.tabbychat.compat;

import acs.tabbychat.gui.context.ChatContext;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class MacrosContext extends ChatContext
{
    private int id;

    public MacrosContext(int name)
    {
        this.id = name;

        switch (name)
        {
            case 0:
                this.displayString = "Execute Macro";
                break;

            case 1:
                this.displayString = "Edit Macro";
                break;

            case 2:
                try
                {
                    Class e = Class.forName("net.eq2online.macros.compatibility.LocalisationProvider");
                    Method locStr = e.getMethod("getLocalisedString", new Class[] {String.class});
                    this.displayString = "\u0a7e" + locStr.invoke((Object)null, new Object[] {"tooltip.guiedit"});
                }
                catch (Exception var4)
                {
                    var4.printStackTrace();
                }
        }
    }

    public void onClicked()
    {
        try
        {
            Class e = Class.forName("net.eq2online.macros.gui.ext.GuiChatAdapter");
            Class guiControl = Class.forName("net.eq2online.macros.gui.designable.DesignableGuiControl");
            Class guiMacroEdit = Class.forName("net.eq2online.macros.gui.screens.GuiMacroEdit");
            Class guiDesigner = Class.forName("net.eq2online.macros.gui.screens.GuiDesigner");
            Constructor editConst = guiMacroEdit.getConstructor(new Class[] {Integer.TYPE, GuiScreen.class});
            Constructor designerConst = guiDesigner.getConstructor(new Class[] {String.class, GuiScreen.class, Boolean.TYPE});
            Method bindable = guiControl.getMethod("getWidgetIsBindable", new Class[0]);
            Method playMacro = e.getDeclaredMethod("playMacro", new Class[0]);
            playMacro.setAccessible(true);
            Field controlId = guiControl.getField("id");
            Object control = MacroKeybindCompat.getControl();
            boolean isBindable = false;

            if (control != null)
            {
                isBindable = ((Boolean)bindable.invoke(control, new Object[0])).booleanValue();
            }

            switch (this.id)
            {
                case 0:
                    if (isBindable && control != null)
                    {
                        playMacro.invoke(MacroKeybindCompat.getChatHook(), new Object[0]);
                    }

                    break;

                case 1:
                    if (isBindable && control != null)
                    {
                        int screen2 = controlId.getInt(control);
                        GuiScreen screen1 = (GuiScreen)editConst.newInstance(new Object[] {Integer.valueOf(screen2), Minecraft.getMinecraft().currentScreen});
                        Minecraft.getMinecraft().displayGuiScreen(screen1);
                    }

                    break;

                case 2:
                    GuiScreen screen = (GuiScreen)designerConst.newInstance(new Object[] {"inchat", Minecraft.getMinecraft().currentScreen, Boolean.valueOf(true)});
                    Minecraft.getMinecraft().displayGuiScreen(screen);
            }
        }
        catch (Exception var14)
        {
            var14.printStackTrace();
        }
    }

    public String getDisplayString()
    {
        return this.displayString;
    }

    public ResourceLocation getDisplayIcon()
    {
        return null;
    }

    public List<ChatContext> getChildren()
    {
        return null;
    }

    public boolean isPositionValid(int x, int y)
    {
        return true;
    }

    public ChatContext.Behavior getDisabledBehavior()
    {
        return ChatContext.Behavior.HIDE;
    }
}
