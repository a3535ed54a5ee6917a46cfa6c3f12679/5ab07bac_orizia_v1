package acs.tabbychat.core;

import acs.tabbychat.api.IChatKeyboardExtension;
import acs.tabbychat.api.IChatMouseExtension;
import acs.tabbychat.api.IChatRenderExtension;
import acs.tabbychat.api.IChatUpdateExtension;
import acs.tabbychat.api.TCExtensionManager;
import acs.tabbychat.compat.MacroKeybindCompat;
import acs.tabbychat.gui.ChatBox;
import acs.tabbychat.gui.ChatButton;
import acs.tabbychat.gui.ChatChannelGUI;
import acs.tabbychat.gui.ChatScrollBar;
import acs.tabbychat.gui.PrefsButton;
import acs.tabbychat.gui.context.ChatContextMenu;
import acs.tabbychat.util.ChatExtensions;
import acs.tabbychat.util.TabbyChatUtils;
import com.google.common.collect.Lists;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Point;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.stream.GuiTwitchUserMode;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import tv.twitch.chat.ChatUserInfo;

public class GuiChatTC extends GuiChat
{
    private Logger log;
    public String historyBuffer;
    public String defaultInputFieldText;
    public int sentHistoryCursor2;
    private boolean playerNamesFound;
    private boolean waitingOnPlayerNames;
    private int playerNameIndex;
    private List<String> foundPlayerNames;
    private URI clickedURI2;
    public GuiTextField inputField2;
    public List<GuiTextField> inputList;
    public ChatScrollBar scrollBar;
    public GuiButton selectedButton2;
    public int eventButton2;
    public long field_85043_c2;
    public int field_92018_d2;
    public float zLevel2;
    private static ScaledResolution sr;
    private int spellCheckCounter;
    public TabbyChat tc;
    public GuiNewChatTC gnc;
    private ChatContextMenu contextMenu;
    private ChatExtensions extensions;
    private static final char[] codes = "0123456789abcdefklmnor".toCharArray();
    private static final char[] displays = "----------------RBSUI-".toCharArray();
    private static final char[] rainbowLoop = "4c6ea2b35d".toCharArray();
    private int nextInRainbow;
    private static final String codePrefix = "&";
    private static GuiButton[] buttons = new GuiButton[codes.length];
    private static GuiButton rainbowToggle;
    private static GuiButton unicodeTable;
    private static final ResourceLocation colorTexture = new ResourceLocation("minecraft", "textures/gui/color.png");
    private static final ResourceLocation rainbowTexture = new ResourceLocation("minecraft", "textures/gui/rainbow.png");
    private static Field inputField1 = null;
    private static Method signMethod = null;
    private static boolean isPressed = false;
    private static boolean isRainbow = false;
    private static boolean unicodeOpen = false;
    private static int currentChatLength = 0;
    private static int unicodeX = 100;
    private static int unicodeY = 100;
    private static int dragX;
    private static int dragY;
    private static boolean isDragging = false;
    private static boolean isScrolling = false;
    private static int scrollHeight = 0;
    private static char[] chars = "\u00a2\u00a3\u00a4\u00a5\u00a1\u00a7\u00a9\u00ae\u00b0\u00b1\u2070\u00b9\u00b2\u00b3\u2074\u2075\u2076\u2077\u2078\u2079\u00bd\u2153\u2154\u00bc\u00be\u215b\u215c\u215d\u215e\u00b5\u00b6\u00b7\u00bb\u00d7\u00f7\u00f8\u02c2\u02c3\u02c4\u02c5\u1d25\u2022\u2020\u2026\u2030\u2039\u203a\u203d\u205e\u20aa\u2116\u2122\u212e\u2126\u2190\u2191\u2192\u2193\u2194\u2195\u2211\u221a\u221e\u2248\u2260\u2261\u2264\u2265\u2500\u2502\u250c\u2510\u2514\u2518\u251c\u2524\u252c\u2534\u253c\u2550\u2551\u2552\u2553\u2554\u2555\u2556\u2557\u2558\u2559\u255a\u255b\u255c\u255d\u255e\u255f\u2560\u2561\u2562\u2563\u2564\u2565\u2566\u2567\u2568\u2569\u256a\u256b\u256c\u2580\u2584\u2588\u258c\u2590\u2591\u2592\u2593\u25a0\u25a1\u25aa\u25ab\u25ac\u25b2\u25ba\u25bc\u25c4\u25ca\u25cb\u25cf\u263a\u263b\u263c\u2640\u2642\u2660\u2663\u2665\u2666\u266a\u266b\u266f\u2600\u2601\u2602\u2603\u2604\u260e\u260f\u2610\u2611\u2612\u260d\u3010\u30c4\u3011\u261a\u261b\u261c\u261d\u261e\u261f\u2620\u2621\u2622\u2623\u2624\u2639\u262e\u262f\u263d\u263e\u2701\u2702\u2703\u2704\u2706\u2708\u2709\u270c\u270d\u270e\u270f\u2710\u2711\u2712\u2713\u2714\u2717\u2718\u2756\u2764\u2765\u2668\u2103\u2109\u278a\u278b\u278c\u278d\u278e\u278f\u2790\u2791\u2792\u2793\u24ea\u2460\u2461\u2462\u2463\u2464\u2465\u2466\u2467\u2468\u2469\u246a\u246b\u246c\u246d\u246e\u246f\u2470\u2471\u2472\u2473\u24b6\u24b7\u24b8\u24b9\u24ba\u24bb\u24bc\u24bd\u24be\u24bf\u24c0\u24c1\u24c2\u24c3\u24c4\u24c5\u24c6\u24c7\u24c8\u24c9\u24ca\u24cb\u24cc\u24cd\u24ce\u24cf\u24d0\u24d1\u24d2\u24d3\u24d4\u24d5\u24d6\u24d7\u24d8\u24d9\u24da\u24db\u24dc\u24dd\u24de\u24df\u24e0\u24e1\u24e2\u24e3\u24e4\u24e5\u24e6\u24e7\u24e8\u24e9\u265a\u265b\u265c\u265d\u265e\u265f\u27b4\u27b5\u27b6\u27b7\u27b8\u27b9\u27b2\u21ad\u21e6\u21e7\u21e8\u21e9\u21ea\u2605\u272a\u25a3\u25c9\u25dc\u25dd\u25de\u25df\u25e0\u25e1\u25e2\u25e3\u25e4\u25e5\u2042".toCharArray();
    List<String> devList;
    private static Tessellator t = Tessellator.instance;

    public GuiChatTC()
    {
        this.log = TabbyChatUtils.log;
        this.historyBuffer = "";
        this.defaultInputFieldText = "";
        this.sentHistoryCursor2 = -1;
        this.playerNamesFound = false;
        this.waitingOnPlayerNames = false;
        this.playerNameIndex = 0;
        this.foundPlayerNames = Lists.newArrayList();
        this.clickedURI2 = null;
        this.inputList = new ArrayList(3);
        this.selectedButton2 = null;
        this.eventButton2 = 0;
        this.field_85043_c2 = 0L;
        this.field_92018_d2 = 0;
        this.zLevel2 = 0.0F;
        this.spellCheckCounter = 0;
        this.nextInRainbow = 0;
        this.devList = Arrays.asList(new String[] {"Ketlark77", "Esteban98", "BrySi_"});
        this.mc = Minecraft.getMinecraft();
        sr = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        this.gnc = GuiNewChatTC.getInstance();
        this.tc = TabbyChat.getInstance();
        this.extensions = new ChatExtensions(TCExtensionManager.INSTANCE.getExtensions());
    }

    public GuiChatTC(String par1Str)
    {
        this();
        this.defaultInputFieldText = par1Str;
    }

    public void actionPerformed(GuiButton par1GuiButton)
    {
        Iterator _button = this.extensions.getListOf(IChatMouseExtension.class).iterator();

        while (_button.hasNext())
        {
            IChatMouseExtension preActiveTabs = (IChatMouseExtension)_button.next();

            if (preActiveTabs.actionPerformed(par1GuiButton))
            {
                return;
            }
        }

        if (par1GuiButton instanceof ChatButton)
        {
            ChatButton _button1 = (ChatButton)par1GuiButton;

            if (Keyboard.isKeyDown(42) && this.tc.channelMap.get("*") == _button1.channel)
            {
                this.mc.displayGuiScreen(TabbyChat.generalSettings);
            }
            else if (this.tc.enabled())
            {
                if (Keyboard.isKeyDown(42))
                {
                    if (_button1.channel.active)
                    {
                        this.tc.activatePrev();
                    }

                    this.buttonList.remove(_button1);
                    this.tc.channelMap.remove(_button1.channel.getTitle());
                }
                else if (Keyboard.isKeyDown(29))
                {
                    if (!_button1.channel.active)
                    {
                        this.gnc.mergeChatLines(_button1.channel);
                        _button1.channel.unread = false;
                    }

                    _button1.channel.active = !_button1.channel.active;

                    if (!_button1.channel.active)
                    {
                        this.tc.resetDisplayedChat();
                    }
                }
                else
                {
                    List preActiveTabs1 = this.tc.getActive();
                    Iterator var4 = this.tc.channelMap.values().iterator();

                    while (var4.hasNext())
                    {
                        ChatChannel chan = (ChatChannel)var4.next();

                        if (!_button1.equals(chan.tab))
                        {
                            chan.active = false;
                        }
                    }

                    if (!_button1.channel.active)
                    {
                        ChatScrollBar.scrollBarMouseWheel();

                        if (preActiveTabs1.size() == 1)
                        {
                            this.checkCommandPrefixChange((ChatChannel)this.tc.channelMap.get(preActiveTabs1.get(0)), _button1.channel);
                        }
                        else
                        {
                            _button1.channel.active = true;
                            _button1.channel.unread = false;
                        }
                    }

                    this.tc.resetDisplayedChat();
                }
            }
        }
    }

    public void addChannelLive(ChatChannel brandNewChan)
    {
        if (!this.buttonList.contains(brandNewChan.tab))
        {
            this.buttonList.add(brandNewChan.tab);
        }
    }

    public void checkCommandPrefixChange(ChatChannel oldChan, ChatChannel newChan)
    {
        String oldPrefix = oldChan.cmdPrefix.trim();
        String currentInput = this.inputField2.getText().trim();

        if (currentInput.equals(oldPrefix) || currentInput.length() == 0)
        {
            String newPrefix = newChan.cmdPrefix.trim();

            if (newPrefix.length() > 0 && !newChan.hidePrefix)
            {
                this.inputField2.setText(newPrefix + " ");
            }
            else
            {
                this.inputField2.setText("");
            }
        }

        oldChan.active = false;
        newChan.active = true;
        newChan.unread = false;
    }

    public void func_146404_p_()
    {
        String textBuffer;
        int low;

        if (this.playerNamesFound)
        {
            this.inputField2.func_146175_b(this.inputField2.func_146197_a(-1, this.inputField2.func_146198_h(), false) - this.inputField2.func_146198_h());

            if (this.playerNameIndex >= this.foundPlayerNames.size())
            {
                this.playerNameIndex = 0;
            }
        }
        else
        {
            low = this.inputField2.func_146197_a(-1, this.inputField2.func_146198_h(), false);
            this.foundPlayerNames.clear();
            this.playerNameIndex = 0;
            String high = this.inputField2.getText().substring(low).toLowerCase();
            textBuffer = this.inputField2.getText().substring(0, this.inputField2.func_146198_h());
            this.func_73893_a(textBuffer, high);

            if (this.foundPlayerNames.isEmpty())
            {
                return;
            }

            this.playerNamesFound = true;
            this.inputField2.func_146175_b(low - this.inputField2.func_146198_h());
        }

        if (this.foundPlayerNames.size() > 1)
        {
            low = this.playerNameIndex - 1;
            int var8 = this.playerNameIndex + 4;

            if (low < 0)
            {
                int newList = Math.abs(low);
                low = 0;
                var8 += newList;
            }

            if (var8 >= this.foundPlayerNames.size())
            {
                var8 = this.foundPlayerNames.size();
            }

            while (var8 - low < 5 && this.foundPlayerNames.size() >= 5)
            {
                --low;
            }

            if (var8 - low < 5)
            {
                low = 0;
            }

            ArrayList var9 = Lists.newArrayList();
            int counter;

            for (counter = low; counter < var8; ++counter)
            {
                var9.add(this.foundPlayerNames.get(counter));
            }

            counter = low;
            StringBuilder _sb = new StringBuilder();
            Iterator _iter = var9.iterator();

            while (_iter.hasNext())
            {
                textBuffer = (String)_iter.next();

                if (counter == this.playerNameIndex + 1)
                {
                    _sb.append(EnumChatFormatting.RESET);
                }

                if (_sb.length() > 0)
                {
                    _sb.append(", ");
                }

                if (counter == this.playerNameIndex)
                {
                    _sb.append(EnumChatFormatting.BOLD);
                }

                ++counter;
                _sb.append(textBuffer);
            }

            if (var8 < this.foundPlayerNames.size())
            {
                _sb.append(" ...");
            }

            this.mc.ingameGUI.getChatGUI().func_146234_a(new ChatComponentText(_sb.toString()), 1);
        }

        this.inputField2.func_146191_b((String)this.foundPlayerNames.get(this.playerNameIndex++));
    }

    public void confirmClicked(boolean zeroId, int worldNum)
    {
        if (worldNum == 0)
        {
            if (zeroId)
            {
                this.func_146407_a(this.clickedURI2);
            }

            this.clickedURI2 = null;
            this.mc.displayGuiScreen(this);
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int cursorX, int cursorY, float pointless)
    {
        boolean isSign = false;
        int scaleOffsetX;
        int i;
        int var27;
        float var31;

        if (this.mc.currentScreen != null && this.mc.currentScreen instanceof GuiChatTC)
        {
            ScaledResolution inputHeight = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
            int scaleSetting;

            if (isRainbow && !isSign)
            {
                String bgWidth = this.inputField2.getText();
                scaleSetting = bgWidth.length();

                if (scaleSetting > currentChatLength)
                {
                    if (!bgWidth.substring(scaleSetting - 1).equals(" "))
                    {
                        this.inputField2.setText(bgWidth.substring(0, scaleSetting - 1) + "&" + rainbowLoop[this.nextInRainbow] + bgWidth.substring(scaleSetting - 1));
                        ++this.nextInRainbow;

                        if (this.nextInRainbow >= rainbowLoop.length)
                        {
                            this.nextInRainbow = 0;
                        }
                    }

                    currentChatLength = scaleSetting + 2;
                }
                else if (scaleSetting < currentChatLength)
                {
                    currentChatLength = scaleSetting;
                }
            }

            this.width = Mouse.getX();
            this.height = this.mc.displayHeight - Mouse.getY();
            var27 = inputHeight.getScaleFactor();
            GL11.glPushMatrix();

            if (unicodeOpen)
            {
                scaleSetting = this.width / var27;
                scaleOffsetX = this.height / var27;
                short scaleOffsetY = 160;
                short icc = 200;
                int _button = unicodeY + this.mc.fontRenderer.FONT_HEIGHT + 10;
                i = unicodeX + scaleOffsetY;
                int extension = unicodeY + icc;
                this.renderBox(unicodeX, unicodeY, scaleOffsetY, icc + 2, 1426063360);
                this.drawGradientRect(unicodeX, unicodeY + 5, i, _button - 1, -1721342362, -1728053248);
                drawRect(i, _button, i + 10, extension, -1728053248);
                int cct = icc - this.mc.fontRenderer.FONT_HEIGHT - 48;

                if (isScrolling)
                {
                    scrollHeight = Math.min(cct, Math.max(0, scaleOffsetX - _button - 19));
                }
                else if (Mouse.isButtonDown(0) && !isPressed && !isScrolling && scaleSetting >= i && scaleSetting <= i + 10 && scaleOffsetX >= _button && scaleOffsetX <= extension)
                {
                    isScrolling = true;
                }

                if (!Mouse.isButtonDown(0) && isScrolling)
                {
                    isScrolling = false;
                }

                int s = _button + scrollHeight + 3;
                this.renderBox(i + 2, s, 6, 32, 1442840575);
                this.drawString(this.mc.fontRenderer, "Caract\u00e8res Unicode", unicodeX + 5, unicodeY + 8, 16777215);
                int arraylist = (scaleOffsetY - 10) / 10;
                int charOffset = Math.max(0, Math.round((float)(scrollHeight / cct * (chars.length / arraylist - (icc - this.mc.fontRenderer.FONT_HEIGHT - 20) / 10 + 5))) * arraylist);
                int max = Math.min(chars.length, charOffset + arraylist * ((icc - this.mc.fontRenderer.FONT_HEIGHT - 20) / 10));
                String name = "";

                for (int i1 = charOffset; i1 < max; ++i1)
                {
                    char currentChar = chars[i1];
                    int a = unicodeX + 5 + (i1 - charOffset) % arraylist * 10;
                    int b = _button + 5 + (i1 - charOffset) / arraylist * 10;
                    boolean hover = scaleSetting >= a && scaleSetting < a + 10 && scaleOffsetX >= b && scaleOffsetX < b + 10;

                    if (hover)
                    {
                        name = Character.getName(currentChar);

                        if (!isPressed && Mouse.isButtonDown(0))
                        {
                            this.writeText(this.mc, "" + currentChar, isSign);
                        }
                    }

                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, hover ? 1.0F : 0.5F);
                    this.mc.getTextureManager().bindTexture(new ResourceLocation("minecraft", "textures/gui/button.png"));
                    drawImage(a, b, 10, 10);
                    this.drawString(this.mc.fontRenderer, "" + currentChar, a + (11 - this.mc.fontRenderer.getStringWidth("" + chars[i1])) / 2, b + 1, -1);
                }

                if (name != "")
                {
                    this.renderBox(scaleSetting + 10, scaleOffsetX - 5, this.mc.fontRenderer.getStringWidth(name) + 10, this.mc.fontRenderer.FONT_HEIGHT + 10, -1728053248);
                    this.drawString(this.mc.fontRenderer, name, scaleSetting + 15, scaleOffsetX, 16777215);
                }

                if (!isPressed && !isDragging && scaleSetting >= unicodeX && scaleSetting <= unicodeX + scaleOffsetY && scaleOffsetX >= unicodeY && scaleOffsetX <= _button && Mouse.isButtonDown(0))
                {
                    isDragging = true;
                    dragX = scaleSetting - unicodeX;
                    dragY = scaleOffsetX - unicodeY;
                }

                if (isDragging)
                {
                    unicodeX = scaleSetting - dragX;
                    unicodeY = scaleOffsetX - dragY;

                    if (!Mouse.isButtonDown(0))
                    {
                        isDragging = false;
                    }
                }

                if (unicodeX < 0)
                {
                    unicodeX = 0;
                }
                else if (unicodeX > inputHeight.getScaledWidth() - scaleOffsetY - 10)
                {
                    unicodeX = inputHeight.getScaledWidth() - scaleOffsetY - 10;
                }

                if (unicodeY < 0)
                {
                    unicodeY = 0;
                }
                else if (unicodeY > inputHeight.getScaledHeight() - icc - 2)
                {
                    unicodeY = inputHeight.getScaledHeight() - icc - 2;
                }
            }

            GL11.glScalef(1.0F / (float)var27, 1.0F / (float)var27, 1.0F);
            scaleSetting = this.mc.currentScreen instanceof GuiChatTC ? this.mc.displayHeight - 26 * var27 : 6;
            rainbowToggle.field_146129_i = scaleSetting;
            rainbowToggle.drawButton(this.mc, this.width, this.height);
            this.mc.getTextureManager().bindTexture(rainbowTexture);
            var31 = isRainbow ? 1.0F : 0.5F;
            GL11.glColor3f(var31, var31, var31);
            drawImage(rainbowToggle);

            if (rainbowToggle.mousePressed(this.mc, this.width, this.height))
            {
                GL11.glScalef(2.0F, 2.0F, 1.0F);
                this.mc.currentScreen.drawString(this.mc.fontRenderer, "Rainbow Mode", rainbowToggle.field_146128_h / 2, (rainbowToggle.field_146129_i + rainbowToggle.field_146121_g) / 2 + 5, 16777215);
                GL11.glScalef(0.5F, 0.5F, 1.0F);

                if (!isPressed && Mouse.isButtonDown(0))
                {
                    isRainbow = !isRainbow;
                    this.nextInRainbow = 0;
                }
            }

            unicodeTable.field_146129_i = scaleSetting;
            unicodeTable.drawButton(this.mc, this.width, this.height);

            if (unicodeTable.mousePressed(this.mc, this.width, this.height))
            {
                GL11.glScalef(2.0F, 2.0F, 1.0F);
                this.mc.currentScreen.drawString(this.mc.fontRenderer, "Unicode", unicodeTable.field_146128_h / 2, (unicodeTable.field_146129_i + unicodeTable.field_146121_g) / 2 + 5, 16777215);
                GL11.glScalef(0.5F, 0.5F, 1.0F);

                if (!isPressed && Mouse.isButtonDown(0))
                {
                    unicodeOpen = !unicodeOpen;
                }
            }

            for (int var33 = 0; var33 < buttons.length; ++var33)
            {
                GuiButton var35 = buttons[var33];
                var35.field_146129_i = scaleSetting;
                var35.drawButton(this.mc, this.width, this.height);

                if (var33 < 16)
                {
                    this.mc.getTextureManager().bindTexture(colorTexture);
                    drawImage(var35);
                }

                if (var35.mousePressed(this.mc, this.width, this.height))
                {
                    char var37 = codes[var33];
                    GL11.glScalef(2.0F, 2.0F, 1.0F);
                    this.mc.currentScreen.drawString(this.mc.fontRenderer, WordUtils.capitalizeFully(ChatFormatting.getByChar(var37).name().replace("_", " ").toLowerCase()), var35.field_146128_h / 2, (var35.field_146129_i + var35.field_146121_g) / 2 + 5, 16777215);
                    GL11.glScalef(0.5F, 0.5F, 1.0F);

                    if (!isPressed && Mouse.isButtonDown(0))
                    {
                        this.writeText(this.mc, "&" + var37, isSign);
                    }
                }
            }

            GL11.glPopMatrix();
            isPressed = Mouse.isButtonDown(0);
        }

        sr = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        this.width = sr.getScaledWidth();
        this.height = sr.getScaledHeight();
        int var26 = 0;

        for (var27 = 0; var27 < this.inputList.size(); ++var27)
        {
            if (((GuiTextField)this.inputList.get(var27)).func_146176_q())
            {
                var26 += 12;
            }
        }

        var27 = MacroKeybindCompat.present ? this.width - 24 : this.width - 2;
        drawRect(2, this.height - 2 - var26, var27, this.height - 2, Integer.MIN_VALUE);
        Iterator var28 = this.inputList.iterator();

        while (var28.hasNext())
        {
            GuiTextField var32 = (GuiTextField)var28.next();

            if (var32.func_146176_q())
            {
                var32.drawTextBox();
            }
        }

        if (this.tc.enabled())
        {
            String var29 = Integer.valueOf(this.getCurrentSends()).toString();
            scaleOffsetX = sr.getScaledWidth() - 12;

            if (MacroKeybindCompat.present)
            {
                scaleOffsetX -= 22;
            }

            this.fontRendererObj.drawStringWithShadow(var29, scaleOffsetX, this.height - var26, 7368816);
        }

        if (TabbyChat.spellingSettings.spellCheckEnable.getValue().booleanValue() && this.inputField2.getText().length() > 0)
        {
            if (this.spellCheckCounter == 200)
            {
                this.spellCheckCounter = 0;
            }

            ++this.spellCheckCounter;
        }

        ChatBox.updateTabs(this.tc.channelMap);
        float var30 = this.gnc.getScaleSetting();
        GL11.glPushMatrix();
        var31 = (float)ChatBox.current.x * (1.0F - var30);
        float var34 = (float)(this.gnc.sr.getScaledHeight() + ChatBox.current.y) * (1.0F - var30);
        GL11.glTranslatef(var31, var34, 1.0F);
        GL11.glScalef(var30, var30, 1.0F);
        IChatComponent var36 = this.gnc.func_146236_a(Mouse.getX(), Mouse.getY());

        if (var36 != null && var36.getChatStyle().getChatHoverEvent() != null)
        {
            HoverEvent var38 = var36.getChatStyle().getChatHoverEvent();

            if (var38.getAction() == HoverEvent.Action.SHOW_ITEM)
            {
                ItemStack var40 = null;

                try
                {
                    NBTBase var43 = JsonToNBT.func_150315_a(var38.getValue().getUnformattedText());

                    if (var43 != null && var43 instanceof NBTTagCompound)
                    {
                        var40 = ItemStack.loadItemStackFromNBT((NBTTagCompound)var43);
                    }
                }
                catch (Exception var25)
                {
                    ;
                }

                if (var40 != null)
                {
                    this.func_146285_a(var40, cursorX, cursorY);
                }
                else
                {
                    this.func_146279_a(EnumChatFormatting.RED + "Invalid Item!", cursorX, cursorY);
                }
            }
            else if (var38.getAction() == HoverEvent.Action.SHOW_TEXT)
            {
                this.func_146279_a(var38.getValue().getFormattedText(), cursorX, cursorY);
            }
            else if (var38.getAction() == HoverEvent.Action.SHOW_ACHIEVEMENT)
            {
                StatBase var41 = StatList.func_151177_a(var38.getValue().getUnformattedText());

                if (var41 != null)
                {
                    IChatComponent var44 = var41.func_150951_e();
                    ChatComponentTranslation var46 = new ChatComponentTranslation("stats.tooltip.type." + (var41.isAchievement() ? "achievement" : "statistics"), new Object[0]);
                    var46.getChatStyle().setItalic(Boolean.valueOf(true));
                    String var47 = var41 instanceof Achievement ? ((Achievement)var41).getDescription() : null;
                    ArrayList var48 = Lists.newArrayList(new String[] {var44.getFormattedText(), var46.getFormattedText()});

                    if (var47 != null)
                    {
                        var48.addAll(this.fontRendererObj.listFormattedStringToWidth(var47, 150));
                    }

                    this.func_146283_a(var48, cursorX, cursorY);
                }
                else
                {
                    this.func_146279_a(EnumChatFormatting.RED + "Invalid statistic/achievement!", cursorX, cursorY);
                }
            }

            GL11.glDisable(GL11.GL_LIGHTING);
        }

        for (i = 0; i < this.buttonList.size(); ++i)
        {
            GuiButton var39 = (GuiButton)this.buttonList.get(i);

            if (var39 instanceof PrefsButton && var39.id == 1 && this.mc.thePlayer != null && !this.mc.thePlayer.isPlayerSleeping())
            {
                this.buttonList.remove(var39);
            }
            else
            {
                var39.drawButton(this.mc, cursorX, cursorY);
            }
        }

        if (this.contextMenu != null)
        {
            this.contextMenu.drawMenu(Mouse.getX(), Mouse.getY());
        }

        GL11.glPopMatrix();
        Iterator var42 = this.extensions.getListOf(IChatRenderExtension.class).iterator();

        while (var42.hasNext())
        {
            IChatRenderExtension var45 = (IChatRenderExtension)var42.next();
            var45.drawScreen(cursorX, cursorY, pointless);
        }
    }

    private void writeText(Minecraft mc, String text, boolean isSign)
    {
        if (isSign)
        {
            if (signMethod != null)
            {
                try
                {
                    char[] e = text.toCharArray();
                    int var5 = e.length;

                    for (int var6 = 0; var6 < var5; ++var6)
                    {
                        char c = e[var6];
                        signMethod.invoke(mc.currentScreen, new Object[] {Character.valueOf(c), Integer.valueOf(c)});
                    }
                }
                catch (Exception var9)
                {
                    var9.printStackTrace();
                }
            }
            else
            {
                System.err.println("Couldn\'t write to sign.");
            }
        }
        else if (this.inputField2 != null)
        {
            try
            {
                this.inputField2.func_146191_b(text);
            }
            catch (Exception var8)
            {
                var8.printStackTrace();
            }
        }
    }

    private static void drawImage(GuiButton button)
    {
        drawImage(button.field_146128_h + 2, button.field_146129_i + 2, 16, 16);
    }

    private static void drawImage(int x, int y, int width, int height)
    {
        t.startDrawingQuads();
        t.addVertexWithUV((double)x, (double)y, 0.0D, 0.0D, 0.0D);
        t.addVertexWithUV((double)x, (double)(y + height), 0.0D, 0.0D, 1.0D);
        t.addVertexWithUV((double)(x + width), (double)(y + height), 0.0D, 1.0D, 1.0D);
        t.addVertexWithUV((double)(x + width), (double)y, 0.0D, 1.0D, 0.0D);
        t.draw();
    }

    public void renderBox(int x, int y, int width, int height, int color)
    {
        drawRect(x + 1, y, x + width - 1, y + height, color);
        drawRect(x, y + 1, x + 1, y + height - 1, color);
        drawRect(x + width - 1, y + 1, x + width, y + height - 1, color);
        drawRect(x + 1, y + 1, x + (width - 1), y + 2, color);
        drawRect(x + 1, y + height - 2, x + (width - 1), y + height - 1, color);
        drawRect(x + 1, y + 2, x + 2, y + height - 2, color);
        drawRect(x + (width - 2), y + 2, x + (width - 1), y + height - 2, color);
    }

    public void func_73893_a(String nameStart, String buffer)
    {
        if (nameStart.length() >= 1)
        {
            this.mc.thePlayer.sendQueue.addToSendQueue(new C14PacketTabComplete(nameStart));
            this.waitingOnPlayerNames = true;
        }
    }

    public void func_146406_a(String[] par1ArrayOfStr)
    {
        if (this.waitingOnPlayerNames)
        {
            this.foundPlayerNames.clear();
            String[] _copy = par1ArrayOfStr;
            int _len = par1ArrayOfStr.length;

            for (int i = 0; i < _len; ++i)
            {
                String name = _copy[i];

                if (name.length() > 0)
                {
                    this.foundPlayerNames.add(name);
                }
            }

            if (this.foundPlayerNames.size() > 0)
            {
                this.playerNamesFound = true;
                this.func_146404_p_();
            }
        }
    }

    private void func_146407_a(URI _uri)
    {
        try
        {
            Class t = Class.forName("java.awt.Desktop");
            Object theDesktop = t.getMethod("getDesktop", new Class[0]).invoke((Object)null, new Object[0]);
            t.getMethod("browse", new Class[] {URI.class}).invoke(theDesktop, new Object[] {_uri});
        }
        catch (Throwable var4)
        {
            this.log.error("Couldn\'t open link", var4);
        }
    }

    public int getCurrentSends()
    {
        int lng = 0;
        int _s = this.inputList.size() - 1;

        for (int i = _s; i >= 0; --i)
        {
            lng += ((GuiTextField)this.inputList.get(i)).getText().length();
        }

        return lng == 0 ? 0 : (lng + 100 - 1) / 100;
    }

    public int getFocusedFieldIndex()
    {
        int _s = this.inputList.size();

        for (int i = 0; i < _s; ++i)
        {
            if (((GuiTextField)this.inputList.get(i)).isFocused() && ((GuiTextField)this.inputList.get(i)).func_146176_q())
            {
                return i;
            }
        }

        return 0;
    }

    public int getInputListSize()
    {
        int size = 0;
        Iterator var2 = this.inputList.iterator();

        while (var2.hasNext())
        {
            GuiTextField field = (GuiTextField)var2.next();

            if (!field.getText().isEmpty())
            {
                ++size;
            }
        }

        return size;
    }

    public void getSentHistory(int _dir)
    {
        int loc = this.sentHistoryCursor2 + _dir;
        int historyLength = this.gnc.func_146238_c().size();
        loc = Math.max(0, loc);
        loc = Math.min(historyLength, loc);

        if (loc != this.sentHistoryCursor2)
        {
            if (loc == historyLength)
            {
                this.sentHistoryCursor2 = historyLength;
                this.setText(new StringBuilder(this.historyBuffer), 1);
            }
            else
            {
                if (this.sentHistoryCursor2 == historyLength)
                {
                    this.historyBuffer = this.inputField2.getText();
                }

                StringBuilder _sb = new StringBuilder((String)this.gnc.func_146238_c().get(loc));
                this.setText(_sb, _sb.length());
                this.sentHistoryCursor2 = loc;
            }
        }
    }

    /**
     * Handles mouse input.
     */
    public void handleMouseInput()
    {
        if (ChatBox.resizing)
        {
            if (!Mouse.isButtonDown(0))
            {
                ChatBox.resizing = false;
            }
            else
            {
                ChatBox.handleMouseResize(Mouse.getEventX(), Mouse.getEventY());
            }
        }
        else if (ChatBox.dragging)
        {
            if (!Mouse.isButtonDown(0))
            {
                ChatBox.dragging = false;
            }
            else
            {
                ChatBox.handleMouseDrag(Mouse.getEventX(), Mouse.getEventY());
            }
        }
        else
        {
            if (Mouse.getEventButton() == 0 && Mouse.isButtonDown(0))
            {
                if (ChatBox.resizeHovered() && !ChatBox.dragging)
                {
                    ChatBox.startResizing(Mouse.getEventX(), Mouse.getEventY());
                }
                else if (ChatBox.pinHovered())
                {
                    ChatBox.pinned = !ChatBox.pinned;
                }
                else if (ChatBox.tabTrayHovered(Mouse.getEventX(), Mouse.getEventY()) && !ChatBox.resizing)
                {
                    ChatBox.startDragging(Mouse.getEventX(), Mouse.getEventY());
                }
            }

            int wheelDelta = Mouse.getEventDWheel();

            if (wheelDelta != 0)
            {
                wheelDelta = Math.min(1, wheelDelta);
                wheelDelta = Math.max(-1, wheelDelta);

                if (!isShiftKeyDown())
                {
                    wheelDelta *= 7;
                }

                this.gnc.func_146229_b(wheelDelta);

                if (this.tc.enabled())
                {
                    ChatScrollBar.scrollBarMouseWheel();
                }
            }
            else if (this.tc.enabled())
            {
                ChatScrollBar.handleMouse();
            }

            if (this.mc.currentScreen.getClass() != GuiChat.class)
            {
                super.handleMouseInput();
            }

            Iterator var2 = this.extensions.getListOf(IChatMouseExtension.class).iterator();

            while (var2.hasNext())
            {
                IChatMouseExtension ext = (IChatMouseExtension)var2.next();
                ext.handleMouseInput();
            }
        }
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        this.extensions = new ChatExtensions(TCExtensionManager.INSTANCE.getExtensions());
        this.buttonList.clear();
        this.inputList.clear();
        sr = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        this.width = sr.getScaledWidth();
        this.height = sr.getScaledHeight();
        this.tc.checkServer();

        if (this.tc.enabled())
        {
            if (this.scrollBar == null)
            {
                this.scrollBar = new ChatScrollBar();
            }

            Iterator textFieldWidth = this.tc.channelMap.values().iterator();

            while (textFieldWidth.hasNext())
            {
                ChatChannel text = (ChatChannel)textFieldWidth.next();
                this.buttonList.add(text.tab);
            }
        }
        else
        {
            this.buttonList.add(((ChatChannel)this.tc.channelMap.get("*")).tab);
        }

        this.sentHistoryCursor2 = this.gnc.func_146238_c().size();
        int var7 = MacroKeybindCompat.present ? this.width - 26 : this.width - 4;
        String var8 = this.defaultInputFieldText;

        if (this.inputField2 != null)
        {
            var8 = this.inputField2.getText();
        }

        this.inputField2 = new GuiTextField(this.fontRendererObj, 4, this.height - 12, var7, 12);
        this.inputField2.func_146203_f(100);
        this.inputField2.func_146205_d(false);
        this.inputField2.setFocused(true);
        this.inputField2.setText(var8);
        this.inputField2.func_146189_e(true);
        this.inputField2.func_146185_a(false);
        this.inputList.add(0, this.inputField2);

        if (this.tc.enabled())
        {
            int i;

            for (i = 1; i < 3; ++i)
            {
                GuiTextField placeholder = new GuiTextField(this.fontRendererObj, 4, this.height - 12 * (i + 1), var7, 12);
                placeholder.func_146203_f(100);
                placeholder.func_146205_d(false);
                placeholder.setFocused(false);
                placeholder.setText("");
                placeholder.func_146189_e(false);
                placeholder.func_146185_a(false);
                this.inputList.add(i, placeholder);
            }

            if (this.tc.enabled())
            {
                List var9 = this.tc.getActive();

                if (var9.size() != 1)
                {
                    this.inputField2.setText("");
                }
                else
                {
                    String extension = ((ChatChannel)this.tc.channelMap.get(var9.get(0))).cmdPrefix.trim();
                    boolean prefixHidden = ((ChatChannel)this.tc.channelMap.get(var9.get(0))).hidePrefix;

                    if (extension.length() > 0 && !prefixHidden && this.inputField2.getText().isEmpty())
                    {
                        this.inputField2.setText(((ChatChannel)this.tc.channelMap.get(var9.get(0))).cmdPrefix.trim() + " ");
                    }
                }

                ChatBox.enforceScreenBoundary(ChatBox.current);
            }

            for (i = 0; i < codes.length; ++i)
            {
                buttons[i] = new GuiButton(0, 6 + 22 * i, 5, 20, 20, ChatFormatting.getByChar(codes[i]) + "" + displays[i]);
            }

            rainbowToggle = new GuiButton(0, buttons[buttons.length - 1].field_146128_h + 25, 5, 20, 20, "");

            if (!this.devList.contains(this.mc.getSession().getUsername()))
            {
                for (i = 0; i < codes.length; ++i)
                {
                    buttons[i].enabled = false;
                }

                rainbowToggle.enabled = false;
            }

            unicodeTable = new GuiButton(0, buttons[buttons.length - 1].field_146128_h + 50, 5, 45, 20, "Unicode");
            Iterator var10 = this.extensions.getListOf(IChatUpdateExtension.class).iterator();

            while (var10.hasNext())
            {
                IChatUpdateExtension var11 = (IChatUpdateExtension)var10.next();
                var11.initGui(this);
            }
        }
    }

    public void insertCharsAtCursor(String _chars)
    {
        StringBuilder msg = new StringBuilder();
        int cPos = 0;
        boolean cFound = false;

        for (int i = this.inputList.size() - 1; i >= 0; --i)
        {
            msg.append(((GuiTextField)this.inputList.get(i)).getText());

            if (((GuiTextField)this.inputList.get(i)).isFocused())
            {
                cPos += ((GuiTextField)this.inputList.get(i)).func_146198_h();
                cFound = true;
            }
            else if (!cFound)
            {
                cPos += ((GuiTextField)this.inputList.get(i)).getText().length();
            }
        }

        if (this.fontRendererObj.getStringWidth(msg.toString()) + this.fontRendererObj.getStringWidth(_chars) < (sr.getScaledWidth() - 20) * this.inputList.size())
        {
            msg.insert(cPos, _chars);
            this.setText(msg, cPos + _chars.length());
        }
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    public void keyTyped(char _char, int _code)
    {
        this.waitingOnPlayerNames = false;

        if (_code != 15)
        {
            this.playerNamesFound = false;
        }

        int foc;
        int ext;
        int lng;
        int newPos;

        switch (_code)
        {
            case 1:
                this.mc.displayGuiScreen((GuiScreen)null);
                break;

            case 14:
                if (this.inputField2.isFocused() && this.inputField2.func_146198_h() > 0)
                {
                    this.inputField2.textboxKeyTyped(_char, _code);
                }
                else
                {
                    this.removeCharsAtCursor(-1);
                }

                break;

            case 15:
                if (GuiScreen.isCtrlKeyDown())
                {
                    if (GuiScreen.isShiftKeyDown())
                    {
                        this.tc.activatePrev();
                    }
                    else
                    {
                        this.tc.activateNext();
                    }
                }
                else
                {
                    this.func_146404_p_();
                }

                break;

            case 28:
            case 156:
                this.sendChat(ChatBox.pinned);
                break;

            case 200:
                if (GuiScreen.isCtrlKeyDown())
                {
                    this.getSentHistory(-1);
                }
                else
                {
                    foc = this.getFocusedFieldIndex();

                    if (foc + 1 < this.inputList.size() && ((GuiTextField)this.inputList.get(foc + 1)).func_146176_q())
                    {
                        ext = ((GuiTextField)this.inputList.get(foc)).func_146198_h();
                        lng = ((GuiTextField)this.inputList.get(foc + 1)).getText().length();
                        newPos = Math.min(ext, lng);
                        ((GuiTextField)this.inputList.get(foc)).setFocused(false);
                        ((GuiTextField)this.inputList.get(foc + 1)).setFocused(true);
                        ((GuiTextField)this.inputList.get(foc + 1)).func_146190_e(newPos);
                    }
                    else
                    {
                        this.getSentHistory(-1);
                    }
                }

                break;

            case 201:
                this.gnc.func_146229_b(19);

                if (this.tc.enabled())
                {
                    ChatScrollBar.scrollBarMouseWheel();
                }

                break;

            case 203:
                foc = this.getFocusedFieldIndex();

                if (foc < this.getInputListSize() - 1 && ((GuiTextField)this.inputList.get(foc)).func_146198_h() == 0)
                {
                    ((GuiTextField)this.inputList.get(foc)).setFocused(false);
                    ((GuiTextField)this.inputList.get(foc + 1)).setFocused(true);
                    ((GuiTextField)this.inputList.get(foc + 1)).func_146190_e(((GuiTextField)this.inputList.get(foc + 1)).getText().length());
                }

                ((GuiTextField)this.inputList.get(this.getFocusedFieldIndex())).textboxKeyTyped(_char, _code);
                break;

            case 205:
                ext = this.getFocusedFieldIndex();

                if (ext > 0 && ((GuiTextField)this.inputList.get(ext)).func_146198_h() >= ((GuiTextField)this.inputList.get(ext)).getText().length())
                {
                    ((GuiTextField)this.inputList.get(ext)).setFocused(false);
                    ((GuiTextField)this.inputList.get(ext - 1)).setFocused(true);
                    ((GuiTextField)this.inputList.get(ext - 1)).func_146190_e(0);
                }

                ((GuiTextField)this.inputList.get(this.getFocusedFieldIndex())).textboxKeyTyped(_char, _code);
                break;

            case 208:
                if (GuiScreen.isCtrlKeyDown())
                {
                    this.getSentHistory(1);
                }
                else
                {
                    foc = this.getFocusedFieldIndex();

                    if (foc - 1 >= 0 && ((GuiTextField)this.inputList.get(foc - 1)).func_146176_q())
                    {
                        ext = ((GuiTextField)this.inputList.get(foc)).func_146198_h();
                        lng = ((GuiTextField)this.inputList.get(foc - 1)).getText().length();
                        newPos = Math.min(ext, lng);
                        ((GuiTextField)this.inputList.get(foc)).setFocused(false);
                        ((GuiTextField)this.inputList.get(foc - 1)).setFocused(true);
                        ((GuiTextField)this.inputList.get(foc - 1)).func_146190_e(newPos);
                    }
                    else
                    {
                        this.getSentHistory(1);
                    }
                }

                break;

            case 209:
                this.gnc.func_146229_b(-19);

                if (this.tc.enabled())
                {
                    ChatScrollBar.scrollBarMouseWheel();
                }

                break;

            case 211:
                if (this.inputField2.isFocused())
                {
                    this.inputField2.textboxKeyTyped(_char, _code);
                }
                else
                {
                    this.removeCharsAtCursor(1);
                }

                break;

            default:
                if (GuiScreen.isCtrlKeyDown() && !Keyboard.isKeyDown(56) && !Keyboard.isKeyDown(184))
                {
                    if (_code > 1 && _code < 12)
                    {
                        this.tc.activateIndex(_code - 1);
                    }
                    else if (_code == 24)
                    {
                        this.mc.displayGuiScreen(TabbyChat.generalSettings);
                    }
                    else
                    {
                        this.inputField2.textboxKeyTyped(_char, _code);
                    }
                }
                else if (this.inputField2.isFocused() && this.fontRendererObj.getStringWidth(this.inputField2.getText()) < sr.getScaledWidth() - 20)
                {
                    this.inputField2.textboxKeyTyped(_char, _code);
                }
                else
                {
                    this.insertCharsAtCursor(Character.toString(_char));
                }
        }

        Iterator foc1 = this.extensions.getListOf(IChatKeyboardExtension.class).iterator();

        while (foc1.hasNext())
        {
            IChatKeyboardExtension ext1 = (IChatKeyboardExtension)foc1.next();
            ext1.keyTyped(_char, _code);
        }
    }

    protected void sendChat(boolean keepopen)
    {
        StringBuilder _msg = new StringBuilder(1500);
        int i;

        for (i = this.inputList.size() - 1; i >= 0; --i)
        {
            _msg.append(((GuiTextField)this.inputList.get(i)).getText());
        }

        if (_msg.toString().length() > 0)
        {
            TabbyChatUtils.writeLargeChat(_msg.toString());

            for (i = 1; i < this.inputList.size(); ++i)
            {
                ((GuiTextField)this.inputList.get(i)).setText("");
                ((GuiTextField)this.inputList.get(i)).setFocused(false);
            }
        }

        if (this.tc.enabled() && keepopen)
        {
            this.resetInputFields();
        }
        else
        {
            this.mc.displayGuiScreen((GuiScreen)null);
        }

        this.sentHistoryCursor2 = this.gnc.func_146238_c().size() + 1;
    }

    /**
     * Called when the mouse is clicked.
     */
    public void mouseClicked(int _x, int _y, int _button)
    {
        Point scaled = ChatBox.scaleMouseCoords(Mouse.getX(), Mouse.getY(), true);
        boolean clicked = false;

        if (_button == 0 && this.mc.gameSettings.chatLinks && (this.contextMenu == null || !this.contextMenu.isCursorOver(scaled.x, scaled.y)))
        {
            IChatComponent i = this.gnc.func_146236_a(Mouse.getX(), Mouse.getY());

            if (i != null)
            {
                ClickEvent _guibutton = i.getChatStyle().getChatClickEvent();

                if (_guibutton != null)
                {
                    if (isShiftKeyDown())
                    {
                        this.inputField2.func_146191_b(i.getChatStyle().getChatClickEvent().getValue());
                    }
                    else
                    {
                        URI _cb;

                        if (_guibutton.getAction() == ClickEvent.Action.OPEN_URL)
                        {
                            try
                            {
                                _cb = new URI(_guibutton.getValue());

                                if (this.mc.gameSettings.chatLinksPrompt)
                                {
                                    this.clickedURI2 = _cb;
                                    this.mc.displayGuiScreen(new GuiConfirmOpenLink(this, _guibutton.getValue(), 0, false));
                                }
                                else
                                {
                                    this.func_146407_a(_cb);
                                }
                            }
                            catch (URISyntaxException var12)
                            {
                                this.log.error("Can\'t open url for " + _guibutton, var12);
                            }
                        }
                        else if (_guibutton.getAction() == ClickEvent.Action.OPEN_FILE)
                        {
                            _cb = (new File(_guibutton.getValue())).toURI();
                            this.func_146407_a(_cb);
                        }
                        else if (_guibutton.getAction() == ClickEvent.Action.SUGGEST_COMMAND)
                        {
                            this.inputField2.setText(_guibutton.getValue());
                        }
                        else if (_guibutton.getAction() == ClickEvent.Action.RUN_COMMAND)
                        {
                            this.func_146403_a(_guibutton.getValue());
                        }
                        else if (_guibutton.getAction() == ClickEvent.Action.TWITCH_USER_INFO)
                        {
                            ChatUserInfo var8 = this.mc.func_152346_Z().func_152926_a(_guibutton.getValue());

                            if (var8 != null)
                            {
                                this.mc.displayGuiScreen(new GuiTwitchUserMode(this.mc.func_152346_Z(), var8));
                            }
                            else
                            {
                                this.log.error("Tried to handle twitch user but couldn\'t find them!");
                            }
                        }
                        else
                        {
                            this.log.error("Don\'t know how to handle " + _guibutton);
                        }
                    }
                }
                else
                {
                    try
                    {
                        URL var18 = new URL(i.getUnformattedText());

                        if (this.mc.gameSettings.chatLinksPrompt)
                        {
                            this.clickedURI2 = var18.toURI();
                            this.mc.displayGuiScreen(new GuiConfirmOpenLink(this, i.getUnformattedText(), 0, false));
                        }
                        else
                        {
                            this.func_146407_a(var18.toURI());
                        }
                    }
                    catch (MalformedURLException var10)
                    {
                        ;
                    }
                    catch (URISyntaxException var11)
                    {
                        ;
                    }
                }
            }
        }
        else if (this.contextMenu != null && this.contextMenu.isCursorOver(scaled.x, scaled.y))
        {
            clicked = !this.contextMenu.mouseClicked(scaled.x, scaled.y);
        }

        if (!clicked)
        {
            if (_button == 1 && (this.contextMenu == null || !this.contextMenu.isCursorOver(scaled.x, scaled.y)))
            {
                this.contextMenu = new ChatContextMenu(this, scaled.x, scaled.y);
            }
            else
            {
                this.contextMenu = null;
            }
        }

        for (int var13 = 0; var13 < this.inputList.size(); ++var13)
        {
            if (_y >= this.height - 12 * (var13 + 1) && ((GuiTextField)this.inputList.get(var13)).func_146176_q())
            {
                ((GuiTextField)this.inputList.get(var13)).setFocused(true);
                Iterator var15 = this.inputList.iterator();

                while (var15.hasNext())
                {
                    GuiTextField var19 = (GuiTextField)var15.next();

                    if (var19 != this.inputList.get(var13))
                    {
                        var19.setFocused(false);
                    }
                }

                ((GuiTextField)this.inputList.get(var13)).mouseClicked(_x, _y, _button);
                break;
            }
        }

        Iterator var14;

        if (!clicked)
        {
            var14 = this.extensions.getListOf(IChatMouseExtension.class).iterator();

            while (var14.hasNext())
            {
                IChatMouseExtension var16 = (IChatMouseExtension)var14.next();

                if (var16.mouseClicked(_x, _y, _button))
                {
                    return;
                }
            }
        }

        var14 = this.buttonList.iterator();

        while (var14.hasNext())
        {
            GuiButton var17 = (GuiButton)var14.next();

            if (var17 instanceof ChatButton && var17.mousePressed(this.mc, _x, _y))
            {
                if (_button == 0)
                {
                    this.selectedButton2 = var17;
                    this.mc.thePlayer.playSound("random.click", 1.0F, 1.0F);
                    this.actionPerformed(var17);
                    return;
                }

                if (_button == 1)
                {
                    ChatButton var20 = (ChatButton)var17;

                    if (var20.channel == this.tc.channelMap.get("*"))
                    {
                        return;
                    }

                    this.mc.displayGuiScreen(new ChatChannelGUI(var20.channel));
                }
            }
        }
    }

    public void mouseMovedOrUp(int _x, int _y, int _button)
    {
        if (this.selectedButton2 != null && _button == 0)
        {
            this.selectedButton2.mouseReleased(_x, _y);
            this.selectedButton2 = null;
        }
    }

    /**
     * "Called when the screen is unloaded. Used to disable keyboard repeat events."
     */
    public void onGuiClosed()
    {
        ChatBox.dragging = false;
        ChatBox.resizing = false;
        this.gnc.resetScroll();
        Iterator var1 = this.extensions.getListOf(IChatUpdateExtension.class).iterator();

        while (var1.hasNext())
        {
            IChatUpdateExtension ext = (IChatUpdateExtension)var1.next();
            ext.onGuiClosed();
        }
    }

    public void removeCharsAtCursor(int _del)
    {
        StringBuilder msg = new StringBuilder();
        int cPos = 0;
        boolean cFound = false;
        int other;

        for (other = this.inputList.size() - 1; other >= 0; --other)
        {
            msg.append(((GuiTextField)this.inputList.get(other)).getText());

            if (((GuiTextField)this.inputList.get(other)).isFocused())
            {
                cPos += ((GuiTextField)this.inputList.get(other)).func_146198_h();
                cFound = true;
            }
            else if (!cFound)
            {
                cPos += ((GuiTextField)this.inputList.get(other)).getText().length();
            }
        }

        other = cPos + _del;
        other = Math.min(msg.length() - 1, other);
        other = Math.max(0, other);

        if (other < cPos)
        {
            msg.replace(other, cPos, "");
            this.setText(msg, other);
        }
        else
        {
            if (other <= cPos)
            {
                return;
            }

            msg.replace(cPos, other, "");
            this.setText(msg, cPos);
        }
    }

    public void resetInputFields()
    {
        Iterator actives = this.inputList.iterator();

        while (actives.hasNext())
        {
            GuiTextField current = (GuiTextField)actives.next();
            current.setText("");
            current.setFocused(false);
            current.func_146189_e(false);
        }

        this.inputField2.setFocused(true);
        this.inputField2.func_146189_e(true);
        List actives1 = this.tc.getActive();

        if (actives1.size() == 1)
        {
            ChatChannel current1 = (ChatChannel)this.tc.channelMap.get(actives1.get(0));
            String pre = current1.cmdPrefix.trim();
            boolean hidden = current1.hidePrefix;

            if (pre.length() > 0 && !hidden)
            {
                this.inputField2.setText(pre + " ");
            }
        }

        this.inputField2.func_146202_e();
        this.sentHistoryCursor2 = this.gnc.func_146238_c().size();
    }

    public void setText(StringBuilder txt, int pos)
    {
        List txtList = this.stringListByWidth(txt, sr.getScaledWidth() - 20);
        int strings = Math.min(txtList.size() - 1, this.inputList.size() - 1);
        int j;

        for (j = strings; j >= 0; --j)
        {
            ((GuiTextField)this.inputList.get(j)).setText((String)txtList.get(strings - j));

            if (pos > ((String)txtList.get(strings - j)).length())
            {
                pos -= ((String)txtList.get(strings - j)).length();
                ((GuiTextField)this.inputList.get(j)).func_146189_e(true);
                ((GuiTextField)this.inputList.get(j)).setFocused(false);
            }
            else if (pos >= 0)
            {
                ((GuiTextField)this.inputList.get(j)).setFocused(true);
                ((GuiTextField)this.inputList.get(j)).func_146189_e(true);
                ((GuiTextField)this.inputList.get(j)).func_146190_e(pos);
                pos = -1;
            }
            else
            {
                ((GuiTextField)this.inputList.get(j)).func_146189_e(true);
                ((GuiTextField)this.inputList.get(j)).setFocused(false);
            }
        }

        if (pos > 0)
        {
            this.inputField2.func_146202_e();
        }

        if (this.inputList.size() > txtList.size())
        {
            for (j = txtList.size(); j < this.inputList.size(); ++j)
            {
                ((GuiTextField)this.inputList.get(j)).setText("");
                ((GuiTextField)this.inputList.get(j)).setFocused(false);
                ((GuiTextField)this.inputList.get(j)).func_146189_e(false);
            }
        }

        if (!this.inputField2.func_146176_q())
        {
            this.inputField2.setFocused(true);
            this.inputField2.func_146189_e(true);
        }
    }

    public List<String> stringListByWidth(StringBuilder _sb, int _w)
    {
        ArrayList result = new ArrayList(5);
        int _len = 0;
        StringBuilder bucket = new StringBuilder(_sb.length());

        for (int ind = 0; ind < _sb.length(); ++ind)
        {
            int _cw = this.fontRendererObj.getCharWidth(_sb.charAt(ind));

            if (_len + _cw > _w)
            {
                result.add(bucket.toString());
                bucket = new StringBuilder(_sb.length());
                _len = 0;
            }

            _len += _cw;
            bucket.append(_sb.charAt(ind));
        }

        if (bucket.length() > 0)
        {
            result.add(bucket.toString());
        }

        return result;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        this.inputField2.updateCursorCounter();
        Iterator var1 = this.extensions.getListOf(IChatUpdateExtension.class).iterator();

        while (var1.hasNext())
        {
            IChatUpdateExtension ext = (IChatUpdateExtension)var1.next();
            ext.updateScreen();
        }
    }
}
