package acs.tabbychat.util;
 
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.zip.GZIPOutputStream;
 
import org.apache.commons.compress.compressors.gzip.GzipUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
 
import com.google.common.collect.Lists;
 
import acs.tabbychat.api.TCExtensionManager;
import acs.tabbychat.compat.MacroKeybindCompat;
import acs.tabbychat.core.ChatChannel;
import acs.tabbychat.core.GuiChatTC;
import acs.tabbychat.core.GuiNewChatTC;
import acs.tabbychat.core.GuiSleepTC;
import acs.tabbychat.core.TabbyChat;
import acs.tabbychat.gui.ITCSettingsGUI;
import acs.tabbychat.gui.context.ChatContextMenu;
import acs.tabbychat.gui.context.ContextCopy;
import acs.tabbychat.gui.context.ContextCut;
import acs.tabbychat.gui.context.ContextPaste;
import acs.tabbychat.gui.context.ContextSpellingSuggestion;
import acs.tabbychat.settings.ChannelDelimEnum;
import acs.tabbychat.settings.ColorCodeEnum;
import acs.tabbychat.settings.FormatCodeEnum;
import acs.tabbychat.settings.NotificationSoundEnum;
import acs.tabbychat.settings.TimeStampEnum;
import acs.tabbychat.threads.BackgroundChatThread;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSleepMP;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.StringUtils;
 
public class TabbyChatUtils
{
    private static Calendar logDay = Calendar.getInstance();
    private static File logDir = new File(new File(Minecraft.getMinecraft().mcDataDir, "logs"), "TabbyChat");
    private static SimpleDateFormat logNameFormat = new SimpleDateFormat("\'_\'yyyy-MM-dd\'.log\'");
    public static final String version = "@@VERSION@@";
    public static final String name = "TabbyChat";
    public static final String modid = "tabbychat";
    public static Logger log = LogManager.getLogger("TabbyChat");
 
    public static void startup()
    {
        try
        {
            Class.forName("net.minecraftforge.common.MinecraftForge");
            TabbyChat.forgePresent = true;
            log.info("MinecraftForge detected.  Will check for client-commands.");
        }
        catch (ClassNotFoundException var1)
        {
            TabbyChat.forgePresent = false;
        }
 
        compressLogs();
        ChatContextMenu.addContext(new ContextSpellingSuggestion());
        ChatContextMenu.addContext(new ContextCut());
        ChatContextMenu.addContext(new ContextCopy());
        ChatContextMenu.addContext(new ContextPaste());
        TCExtensionManager.INSTANCE.registerExtension(MacroKeybindCompat.class);
    }
 
    private static void compressLogs()
    {
        if (logDir.exists())
        {
            Collection logs = FileUtils.listFiles(logDir, new String[] {"txt", "log"}, true);
            Iterator var1 = logs.iterator();
 
            while (var1.hasNext())
            {
                File file = (File)var1.next();
                String name = file.getName();
 
                if (!name.contains(logNameFormat.format(logDay.getTime())))
                {
                    try
                    {
                        gzipFile(file);
                    }
                    catch (IOException var5)
                    {
                        var5.printStackTrace();
                    }
                }
            }
        }
    }
 
    private static void gzipFile(File file) throws IOException
    {
        File dest = new File(file.getParentFile(), GzipUtils.getCompressedFilename(file.getName()));
        FileOutputStream os = new FileOutputStream(dest);
 
        try
        {
            OutputStreamWriter writer = new OutputStreamWriter(new GZIPOutputStream(os), "UTF-8");
 
            try
            {
                writer.write(FileUtils.readFileToString(file, "UTF-8"));
            }
            finally
            {
                writer.close();
                file.delete();
            }
        }
        finally
        {
            os.close();
        }
    }
 
    public static void chatGuiTick(Minecraft mc)
    {
        GuiScreen screen = mc.currentScreen;
 
        if (screen != null)
        {
            if (screen instanceof GuiChat)
            {
                if (screen.getClass() != GuiChatTC.class)
                {
                    if (screen.getClass() != GuiSleepTC.class)
                    {
                        String inputBuffer = "";
 
                        try
                        {
                            int e = 0;
                            Field[] var4 = GuiChat.class.getDeclaredFields();
                            int var5 = var4.length;
 
                            for (int var6 = 0; var6 < var5; ++var6)
                            {
                                Field fields = var4[var6];
 
                                if (fields.getType() == String.class)
                                {
                                    if (e == 1)
                                    {
                                        fields.setAccessible(true);
                                        inputBuffer = (String)fields.get(mc.currentScreen);
                                        break;
                                    }
 
                                    ++e;
                                }
                            }
                        }
                        catch (Exception var8)
                        {
                            TabbyChat.printException("Unable to display chat interface", var8);
                        }
 
                        if (screen instanceof GuiSleepMP)
                        {
                            mc.displayGuiScreen(new GuiSleepTC());
                        }
                        else
                        {
                            mc.displayGuiScreen(new GuiChatTC(inputBuffer));
                        }
                    }
                }
            }
        }
    }
 
    public static ServerData getServerData()
    {
        Minecraft mc = Minecraft.getMinecraft();
        ServerData serverData = null;
        Field[] var2 = Minecraft.class.getDeclaredFields();
        int var3 = var2.length;
 
        for (int var4 = 0; var4 < var3; ++var4)
        {
            Field field = var2[var4];
 
            if (field.getType() == ServerData.class)
            {
                field.setAccessible(true);
 
                try
                {
                    serverData = (ServerData)field.get(mc);
                }
                catch (Exception var7)
                {
                    TabbyChat.printException("Unable to find server information", var7);
                }
 
                break;
            }
        }
 
        return serverData;
    }
 
    public static File getServerDir() {
        String ip = getServerIp();
        if (ip.contains(":")) {
            ip = ip.replaceAll(":", "(") + ")";
        }
        return new File(ITCSettingsGUI.tabbyChatDir, ip);
    }
   
   
    public static String getServerIp()
    {
        String ip;
 
        if (Minecraft.getMinecraft().isSingleplayer())
        {
            ip = "singleplayer";
        }
        else if (getServerData() == null)
        {
            ip = "unknown";
        }
        else
        {
            ip = getServerData().serverIP;
        }
 
        return ip;
    }
 
    public static File getTabbyChatDir()
    {
        return new File(new File(Minecraft.getMinecraft().mcDataDir, "config"), "tabbychat");
    }
 
    public static void hookIntoChat(GuiNewChatTC _gnc)
    {
        if (Minecraft.getMinecraft().ingameGUI.getChatGUI().getClass() != GuiNewChatTC.class)
        {
            try
            {
                Class e = GuiIngame.class;
                Field persistantGuiField = e.getDeclaredFields()[7];
                persistantGuiField.setAccessible(true);
                persistantGuiField.set(Minecraft.getMinecraft().ingameGUI, _gnc);
                int tmp = 0;
                Field[] var4 = GuiNewChat.class.getDeclaredFields();
                int var5 = var4.length;
 
                for (int var6 = 0; var6 < var5; ++var6)
                {
                    Field fields = var4[var6];
 
                    if (fields.getType() == List.class)
                    {
                        fields.setAccessible(true);
 
                        if (tmp == 0)
                        {
                            _gnc.sentMessages = (List)fields.get(_gnc);
                        }
                        else if (tmp == 1)
                        {
                            _gnc.backupLines = (List)fields.get(_gnc);
                        }
                        else if (tmp == 2)
                        {
                            _gnc.chatLines = (List)fields.get(_gnc);
                            break;
                        }
 
                        ++tmp;
                    }
                }
            }
            catch (Exception var8)
            {
                TabbyChat.printException("Error loading chat hook.", var8);
            }
        }
    }
 
    public static void logChat(String theChat, ChatChannel theChannel)
    {
        Calendar tmpcal = Calendar.getInstance();
        String time = SimpleDateFormat.getTimeInstance().format(Calendar.getInstance().getTime());
        theChat = StringUtils.stripControlCodes(String.format("[%s] %s", new Object[] {time, theChat}));
 
        if (theChannel == null || theChannel.getTitle() == null)
        {
            theChannel = new ChatChannel("*");
        }
 
        File fileDir;
 
        if (getServerIp() == "singleplayer")
        {
            IntegratedServer e = Minecraft.getMinecraft().getIntegratedServer();
            fileDir = new File(new File(logDir, "singleplayer"), e.getWorldName());
        }
        else
        {
            fileDir = new File(logDir, logNameFormat.format(logDay.getTime()).toString());
        }
 
        String basename;
 
        if (!theChannel.getTitle().equals("*"))
        {
            fileDir = new File(fileDir, theChannel.getTitle());
            basename = theChannel.getTitle();
        }
        else
        {
            basename = "all";
        }
 
        if (theChannel.getLogFile() == null || tmpcal.get(6) != logDay.get(6))
        {
            logDay = tmpcal;
            theChannel.setLogFile(new File(fileDir, basename + logNameFormat.format(logDay.getTime())));
        }
 
        if (!theChannel.getLogFile().exists())
        {
            try
            {
                fileDir.mkdirs();
                theChannel.getLogFile().createNewFile();
            }
            catch (IOException var8)
            {
                TabbyChat.printErr("Cannot create log file : \'" + var8.getLocalizedMessage() + "\' : " + var8.toString());
                return;
            }
        }
 
        try
        {
            FileUtils.writeLines(theChannel.getLogFile(), "UTF-8", Lists.newArrayList(new String[] {theChat.trim()}), true);
        }
        catch (IOException var7)
        {
            TabbyChat.printErr("Cannot write to log file : \'" + var7.getLocalizedMessage() + "\' : " + var7.toString());
        }
    }
 
    public static Float median(float val1, float val2, float val3)
    {
        return val1 < val2 && val1 < val3 ? Float.valueOf(Math.min(val2, val3)) : (val1 > val2 && val1 > val3 ? Float.valueOf(Math.max(val2, val3)) : Float.valueOf(val1));
    }
 
    public static ColorCodeEnum parseColor(Object _input)
    {
        if (_input == null)
        {
            return null;
        }
        else
        {
            String input = _input.toString();
 
            try
            {
                return ColorCodeEnum.valueOf(input);
            }
            catch (IllegalArgumentException var3)
            {
                return null;
            }
        }
    }
 
    public static ChannelDelimEnum parseDelimiters(Object _input)
    {
        if (_input == null)
        {
            return null;
        }
        else
        {
            String input = _input.toString();
 
            try
            {
                return ChannelDelimEnum.valueOf(input);
            }
            catch (IllegalArgumentException var3)
            {
                return null;
            }
        }
    }
 
    public static FormatCodeEnum parseFormat(Object _input)
    {
        if (_input == null)
        {
            return null;
        }
        else
        {
            String input = _input.toString();
 
            try
            {
                return FormatCodeEnum.valueOf(input);
            }
            catch (IllegalArgumentException var3)
            {
                return null;
            }
        }
    }
 
    public static Integer parseInteger(String _input, int min, int max, int fallback)
    {
        Integer result;
 
        try
        {
            result = Integer.valueOf(Integer.parseInt(_input));
            result = Integer.valueOf(Math.max(min, result.intValue()));
            result = Integer.valueOf(Math.min(max, result.intValue()));
        }
        catch (NumberFormatException var6)
        {
            result = Integer.valueOf(fallback);
        }
 
        return result;
    }
 
    public static int parseInteger(String _input)
    {
        NumberFormat formatter = NumberFormat.getInstance();
        boolean state = formatter.isParseIntegerOnly();
        formatter.setParseIntegerOnly(true);
        ParsePosition pos = new ParsePosition(0);
        int result = formatter.parse(_input, pos).intValue();
        formatter.setParseIntegerOnly(state);
        return _input.length() == pos.getIndex() ? result : -1;
    }
 
    public static NotificationSoundEnum parseSound(Object _input)
    {
        if (_input == null)
        {
            return NotificationSoundEnum.ORB;
        }
        else
        {
            String input = _input.toString();
 
            try
            {
                return NotificationSoundEnum.valueOf(input);
            }
            catch (IllegalArgumentException var3)
            {
                return NotificationSoundEnum.ORB;
            }
        }
    }
 
    public static String parseString(Object _input)
    {
        return _input == null ? " " : _input.toString();
    }
 
    public static TimeStampEnum parseTimestamp(Object _input)
    {
        if (_input == null)
        {
            return null;
        }
        else
        {
            String input = _input.toString();
 
            try
            {
                return TimeStampEnum.valueOf(input);
            }
            catch (IllegalArgumentException var3)
            {
                return null;
            }
        }
    }
 
    public static LinkedHashMap<String, ChatChannel> swapChannels(LinkedHashMap<String, ChatChannel> currentMap, int _left, int _right)
    {
        if (_left == _right)
        {
            return currentMap;
        }
        else
        {
            if (_left > _right)
            {
                int arrayCopy = _left;
                _left = _right;
                _right = arrayCopy;
            }
 
            if (_right >= currentMap.size())
            {
                return currentMap;
            }
            else
            {
                String[] var8 = new String[currentMap.size()];
                var8 = (String[])currentMap.keySet().toArray(var8);
                String tmp = var8[_left];
                var8[_left] = var8[_right];
                var8[_right] = tmp;
                int n = var8.length;
                LinkedHashMap returnMap = new LinkedHashMap(n);
 
                for (int i = 0; i < n; ++i)
                {
                    returnMap.put(var8[i], currentMap.get(var8[i]));
                }
 
                return returnMap;
            }
        }
    }
 
    public static void writeLargeChat(String toSend)
    {
        List actives = TabbyChat.getInstance().getActive();
        BackgroundChatThread sendProc;
 
        if (TabbyChat.getInstance().enabled() && actives.size() == 1)
        {
            ChatChannel active = (ChatChannel)TabbyChat.getInstance().channelMap.get(actives.get(0));
            String tabPrefix = active.cmdPrefix;
            boolean hiddenPrefix = active.hidePrefix;
 
            if (TabbyChat.advancedSettings.convertUnicodeText.getValue().booleanValue())
            {
                toSend = convertUnicode(toSend);
            }
 
            if (tabPrefix != null && tabPrefix.length() > 0)
            {
                if (!hiddenPrefix)
                {
                    sendProc = new BackgroundChatThread(toSend, tabPrefix);
                }
                else if (!toSend.startsWith("/"))
                {
                    sendProc = new BackgroundChatThread(tabPrefix + " " + toSend, tabPrefix);
                }
                else
                {
                    sendProc = new BackgroundChatThread(toSend);
                }
            }
            else
            {
                sendProc = new BackgroundChatThread(toSend);
            }
        }
        else
        {
            sendProc = new BackgroundChatThread(toSend);
        }
 
        sendProc.start();
    }
 
    public static String convertUnicode(String chat)
    {
        String newChat = "";
        String[] var2 = chat.split("\\\u0000");
        int var3 = var2.length;
 
        for (int var4 = 0; var4 < var3; ++var4)
        {
            String s = var2[var4];
 
            if (s.contains("u"))
            {
                try
                {
                    newChat = newChat + StringEscapeUtils.unescapeJava(s);
                }
                catch (IllegalArgumentException var7)
                {
                    newChat = newChat + s;
                }
            }
            else
            {
                newChat = newChat + s;
            }
        }
 
        return newChat;
    }
}