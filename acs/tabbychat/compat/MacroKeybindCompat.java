package acs.tabbychat.compat;

import acs.tabbychat.api.IChatMouseExtension;
import acs.tabbychat.api.IChatRenderExtension;
import acs.tabbychat.api.IChatUpdateExtension;
import acs.tabbychat.gui.context.ChatContextMenu;
import acs.tabbychat.gui.context.ContextDummy;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class MacroKeybindCompat implements IChatMouseExtension, IChatUpdateExtension, IChatRenderExtension
{
    public static final ResourceLocation ICONS_MAIN = new ResourceLocation("macros", "textures/gui/macrosGuiMain.png");
    private static Object inChatLayout = null;
    private static Object inChatGUI = null;
    private static Object btnGui = null;
    private static Object dropDownMenu = null;
    private static Class<?> guiCustomGui = null;
    private static Constructor<?> createDesignerScreen = null;
    private static Constructor<?> macroEdit = null;
    private static Constructor<?> macroDesign = null;
    private static Method draw = null;
    private static Method controlClicked = null;
    private static Method drawBtnGui = null;
    private static Method layoutTick = null;
    private static Method drawDropDown = null;
    private static Method dropDownSize = null;
    private static Method mousePressed = null;
    private static Method onControlClicked = null;
    private static Method displayScreen = null;
    private static Method coreInstance = null;
    private static Method mkgetBoundLayout;
    private static Field menuLocation;
    private static Field dropDownVisible;
    private static Field clickedControl;
    private static Field boundingBox;
    private static Field chatGuiHook;
    public static boolean present = true;
    private static boolean hovered = false;
    private GuiScreen screen;

    public void load()
    {
        if (present)
        {
            Class e;

            if (inChatLayout != null && inChatGUI != null && btnGui != null && dropDownMenu != null && guiCustomGui != null && createDesignerScreen != null && draw != null && controlClicked != null && drawBtnGui != null && layoutTick != null && drawDropDown != null && dropDownSize != null && menuLocation != null && dropDownVisible != null && clickedControl != null && mousePressed != null && onControlClicked != null && displayScreen != null && macroEdit != null && macroDesign != null && boundingBox != null)
            {
                try
                {
                    e = Class.forName("net.eq2online.macros.gui.designable.DesignableGuiLayout");
                    Constructor guiConstructor2 = guiCustomGui.getConstructor(new Class[] {e, GuiScreen.class});
                    inChatGUI = guiConstructor2.newInstance(new Object[] {inChatLayout, null});
                }
                catch (Exception var16)
                {
                    present = false;
                }
            }
            else
            {
                try
                {
                    e = Class.forName("net.eq2online.macros.gui.designable.LayoutManager");
                    Class guiConstructor = Class.forName("net.eq2online.macros.gui.designable.DesignableGuiLayout");
                    Class buttonClass = Class.forName("net.eq2online.macros.gui.controls.GuiMiniToolbarButton");
                    Class guiDesigner = Class.forName("net.eq2online.macros.gui.screens.GuiDesigner");
                    Class guiDropDownMenu = Class.forName("net.eq2online.macros.gui.controls.GuiDropDownMenu");
                    Class guiControl = Class.forName("net.eq2online.macros.gui.designable.DesignableGuiControl");
                    Class abstractionLayer = Class.forName("net.eq2online.macros.compatibility.AbstractionLayer");
                    Class guiMacroEdit = Class.forName("net.eq2online.macros.gui.screens.GuiMacroEdit");
                    guiCustomGui = Class.forName("net.eq2online.macros.gui.screens.GuiCustomGui");
                    Class localisationProvider = Class.forName("net.eq2online.macros.compatibility.LocalisationProvider");
                    Class macroModCore = Class.forName("net.eq2online.macros.core.MacroModCore");
                    Constructor mkButtonConstructor = buttonClass.getDeclaredConstructor(new Class[] {Minecraft.class, Integer.TYPE, Integer.TYPE, Integer.TYPE});
                    Constructor guiConstructor1 = guiCustomGui.getConstructor(new Class[] {guiConstructor, GuiScreen.class});
                    createDesignerScreen = guiDesigner.getDeclaredConstructor(new Class[] {String.class, GuiScreen.class, Boolean.TYPE});
                    macroEdit = guiMacroEdit.getDeclaredConstructor(new Class[] {Integer.TYPE, GuiScreen.class});
                    macroDesign = guiDesigner.getDeclaredConstructor(new Class[] {String.class, GuiScreen.class, Boolean.TYPE});
                    mkgetBoundLayout = e.getDeclaredMethod("getBoundLayout", new Class[] {String.class, Boolean.TYPE});
                    layoutTick = guiConstructor.getDeclaredMethod("onTick", (Class[])null);
                    drawBtnGui = buttonClass.getDeclaredMethod("drawControlAt", new Class[] {Minecraft.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE});
                    drawDropDown = guiDropDownMenu.getDeclaredMethod("drawControlAt", new Class[] {Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE});
                    draw = guiConstructor.getDeclaredMethod("draw", new Class[] {Rectangle.class, Integer.TYPE, Integer.TYPE});
                    controlClicked = guiCustomGui.getDeclaredMethod("controlClicked", new Class[] {Integer.TYPE, Integer.TYPE, Integer.TYPE});
                    dropDownSize = guiDropDownMenu.getDeclaredMethod("getSize", (Class[])null);
                    mousePressed = guiDropDownMenu.getDeclaredMethod("mousePressed", new Class[] {Integer.TYPE, Integer.TYPE});
                    onControlClicked = guiCustomGui.getDeclaredMethod("onControlClicked", new Class[] {guiControl});
                    displayScreen = abstractionLayer.getDeclaredMethod("displayGuiScreen", new Class[] {GuiScreen.class});
                    Method dropDownAdd = guiDropDownMenu.getDeclaredMethod("addItem", new Class[] {String.class, String.class, Integer.TYPE, Integer.TYPE});
                    Method getLocalisedString = localisationProvider.getDeclaredMethod("getLocalisedString", new Class[] {String.class});
                    coreInstance = macroModCore.getMethod("getInstance", new Class[0]);
                    controlClicked.setAccessible(true);
                    onControlClicked.setAccessible(true);
                    Field mkContextMenu = guiCustomGui.getDeclaredField("contextMenu");
                    menuLocation = guiCustomGui.getDeclaredField("contextMenuLocation");
                    clickedControl = guiCustomGui.getDeclaredField("clickedControl");
                    dropDownVisible = guiDropDownMenu.getDeclaredField("dropDownVisible");
                    boundingBox = guiCustomGui.getDeclaredField("boundingBox");
                    chatGuiHook = macroModCore.getDeclaredField("chatGuiHook");
                    mkContextMenu.setAccessible(true);
                    menuLocation.setAccessible(true);
                    clickedControl.setAccessible(true);
                    dropDownVisible.setAccessible(true);
                    boundingBox.setAccessible(true);
                    chatGuiHook.setAccessible(true);
                    inChatLayout = mkgetBoundLayout.invoke((Object)null, new Object[] {"inchat", Boolean.valueOf(false)});
                    btnGui = mkButtonConstructor.newInstance(new Object[] {Minecraft.getMinecraft(), Integer.valueOf(4), Integer.valueOf(104), Integer.valueOf(64)});
                    inChatGUI = guiConstructor1.newInstance(new Object[] {inChatLayout, null});
                    dropDownMenu = mkContextMenu.get(inChatGUI);
                    dropDownAdd.invoke(dropDownMenu, new Object[] {"design", "\u00a7e" + getLocalisedString.invoke((Object)null, new Object[]{"tooltip.guiedit"}), Integer.valueOf(26), Integer.valueOf(16)});
                    ChatContextMenu.insertContextAtPos(1, new MacrosContext(1));
                    ChatContextMenu.insertContextAtPos(2, new MacrosContext(2));
                    ChatContextMenu.insertContextAtPos(3, new ContextDummy("-------"));
                }
                catch (Exception var17)
                {
                    present = false;
                }
            }
        }
    }

    public boolean controlClicked(int par1, int par2, int par3)
    {
        if (!present)
        {
            return false;
        }
        else
        {
            boolean clicked = false;

            try
            {
                boundingBox.set(inChatGUI, new Rectangle(0, 0, this.screen.width, this.screen.height - 14));
                clicked = ((Boolean)controlClicked.invoke(inChatGUI, new Object[] {Integer.valueOf(par1), Integer.valueOf(par2), Integer.valueOf(par3)})).booleanValue();

                if (clicked && par3 == 1)
                {
                    dropDownVisible.set(dropDownMenu, Boolean.valueOf(true));
                    Dimension e = (Dimension)dropDownSize.invoke(dropDownMenu, (Object[])null);
                    menuLocation.set(inChatGUI, new Point(Math.min(par1, this.screen.width - e.width), Math.min(par2 - 8, this.screen.height - e.height)));
                }

                if (clicked)
                {
                    return true;
                }

                if (hovered)
                {
                    GuiScreen e1 = (GuiScreen)createDesignerScreen.newInstance(new Object[] {"inchat", this.screen, Boolean.valueOf(true)});
                    Minecraft.getMinecraft().displayGuiScreen(e1);
                    return true;
                }
            }
            catch (Exception var6)
            {
                present = false;
            }

            return clicked;
        }
    }

    public void drawScreen(int par1, int par2, float par3)
    {
        if (present)
        {
            Object[] args = new Object[] {new Rectangle(0, 0, this.screen.width, this.screen.height - 14), Integer.valueOf(par1), Integer.valueOf(par2)};
            Object[] args2 = new Object[] {Minecraft.getMinecraft(), Integer.valueOf(par1), Integer.valueOf(par2), Integer.valueOf(this.screen.width - 20), Integer.valueOf(this.screen.height - 14), Integer.valueOf(16716288), Integer.valueOf(Integer.MIN_VALUE)};

            try
            {
                layoutTick.invoke(inChatLayout, (Object[])null);
                draw.invoke(inChatLayout, args);
                Object e = drawBtnGui.invoke(btnGui, args2);
                hovered = ((Boolean)e).booleanValue();
            }
            catch (Exception var7)
            {
                present = false;
            }
        }
    }

    public void initGui(GuiScreen screen)
    {
        this.screen = screen;

        if (present)
        {
            try
            {
                inChatLayout = mkgetBoundLayout.invoke((Object)null, new Object[] {"inchat", Boolean.valueOf(false)});
            }
            catch (Exception var3)
            {
                var3.printStackTrace();
            }
        }
    }

    public void onGuiClosed() {}

    public boolean mouseClicked(int x, int y, int button)
    {
        return this.controlClicked(x, y, button);
    }

    public void handleMouseInput() {}

    public boolean actionPerformed(GuiButton button)
    {
        return false;
    }

    public void updateScreen() {}

    public static Object getControl()
    {
        try
        {
            return clickedControl.get(inChatGUI);
        }
        catch (Exception var1)
        {
            var1.printStackTrace();
            return null;
        }
    }

    public static Object getChatHook()
    {
        try
        {
            return chatGuiHook.get(coreInstance.invoke((Object)null, new Object[0]));
        }
        catch (Exception var1)
        {
            var1.printStackTrace();
            return null;
        }
    }
}
