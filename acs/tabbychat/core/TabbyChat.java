package acs.tabbychat.core;

import acs.tabbychat.gui.ChatBox;
import acs.tabbychat.gui.TCSettingsAdvanced;
import acs.tabbychat.gui.TCSettingsFilters;
import acs.tabbychat.gui.TCSettingsGeneral;
import acs.tabbychat.gui.TCSettingsServer;
import acs.tabbychat.gui.TCSettingsSpelling;
import acs.tabbychat.jazzy.TCSpellCheckManager;
import acs.tabbychat.settings.ChannelDelimEnum;
import acs.tabbychat.settings.ColorCodeEnum;
import acs.tabbychat.settings.FormatCodeEnum;
import acs.tabbychat.settings.TCChatFilter;
import acs.tabbychat.threads.BackgroundUpdateCheck;
import acs.tabbychat.util.ChatComponentUtils;
import acs.tabbychat.util.TabbyChatUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Logger;

public class TabbyChat
{
    private static Logger log = TabbyChatUtils.log;
    private static Gson gson = (new GsonBuilder()).excludeFieldsWithoutExposeAnnotation().enableComplexMapKeySerialization().registerTypeAdapter(IChatComponent.class, new IChatComponent.Serializer()).create();
    private volatile TCChatLine lastChat;
    private static boolean firstRun = true;
    public static boolean liteLoaded = false;
    public static boolean modLoaded = false;
    public static boolean forgePresent = false;
    private static boolean updateChecked = false;
    private static String mcversion = (new ServerData("", "")).gameVersion;
    public static boolean defaultUnicode;
    public static String version = "@@VERSION@@";
    public static Minecraft mc;
    public static TCSettingsGeneral generalSettings;
    public static TCSettingsServer serverSettings;
    public static TCSettingsFilters filterSettings;
    public static TCSettingsSpelling spellingSettings;
    public static TCSettingsAdvanced advancedSettings;
    public static TCSpellCheckManager spellChecker;
    public LinkedHashMap<String, ChatChannel> channelMap = new LinkedHashMap();
    public Map<ChatChannel, String> lastChatMap = Maps.newHashMap();
    private static File chanDataFile;
    protected Calendar cal = Calendar.getInstance();
    protected Semaphore serverDataLock = new Semaphore(0, true);
    private Pattern chatChannelPatternClean = Pattern.compile("^\\[([\\p{L}0-9_]{1,10})\\]");
    private Pattern chatChannelPatternDirty = Pattern.compile("^\\[([\\p{L}0-9_]{1,10})\\]");
    private Pattern chatPMfromMePattern = null;
    private Pattern chatPMtoMePattern = null;
    private final ReentrantReadWriteLock lastChatLock = new ReentrantReadWriteLock(true);
    private final Lock lastChatReadLock;
    private final Lock lastChatWriteLock;
    private static GuiNewChatTC gnc;
    private static TabbyChat instance = null;

    public static TabbyChat getInstance()
    {
        return instance;
    }

    public static TabbyChat getInstance(GuiNewChatTC gncInstance)
    {
        if (instance == null)
        {
            instance = new TabbyChat(gncInstance);
        }

        return instance;
    }

    public static String getNewestVersion()
    {
        String updateURL;

        if (liteLoaded)
        {
            updateURL = "http://tabbychat.port0.org/tabbychat/current_version.php?type=LL&mc=" + mcversion;
        }
        else
        {
            updateURL = "http://tabbychat.port0.org/tabbychat/current_version.php?mc=" + mcversion;
        }

        try
        {
            HttpURLConnection e = (HttpURLConnection)(new URL(updateURL)).openConnection();
            BufferedReader buffer = new BufferedReader(new InputStreamReader(e.getInputStream()));
            String newestVersion = buffer.readLine();
            buffer.close();
            return newestVersion;
        }
        catch (Throwable var4)
        {
            printErr("Unable to check for TabbyChat update.");
            return version;
        }
    }

    public static void printErr(String err)
    {
        log.warn("[TABBYCHAT] " + err);
    }

    public static void printException(String err, Exception e)
    {
        log.warn("[TABBYCHAT] " + err, e);
    }

    public static void printMessageToChat(String msg)
    {
        if (instance != null)
        {
            if (!instance.channelMap.containsKey("Commerce"))
            {
                instance.channelMap.put("Commerce", new ChatChannel("Commerce"));
            }

            if (!instance.channelMap.containsKey("Recrutement"))
            {
                instance.channelMap.put("Recrutement", new ChatChannel("Recrutement"));
            }

            msg = msg.replace("\u00a7r\u00a7f\u00a77[\u00a7c!\u00a77]", "");
            msg = msg.replace("[CM] ", "");
            msg = msg.replace("[RC] ", "");

            if (msg.contains("\u00a7e[\u00a76\u00a7lCommerce\u00a7e]"))
            {
                instance.addToChannel("Commerce", new TCChatLine(mc.ingameGUI.getUpdateCounter(), new ChatComponentText(msg), 0, true), false);
            }
            else if (msg.contains("\u00a7b[\u00a7a\u00a7lRecrutement\u00a7b]"))
            {
                instance.addToChannel("Recrutement", new TCChatLine(mc.ingameGUI.getUpdateCounter(), new ChatComponentText(msg), 0, true), false);
            }
            else if (msg.contains("[Correction]"))
            {
                instance.addToChannel("\u00a7lReports", new TCChatLine(mc.ingameGUI.getUpdateCounter(), new ChatComponentText(msg), 0, true), false);
            }

            boolean firstLine = true;
            List split = mc.fontRenderer.listFormattedStringToWidth(msg, ChatBox.getMinChatWidth());
            Iterator var3 = split.iterator();

            while (var3.hasNext())
            {
                String splitMsg = (String)var3.next();

                if (firstLine)
                {
                    firstLine = false;
                }
            }
        }
    }

    private TabbyChat(GuiNewChatTC gncInstance)
    {
        this.lastChatReadLock = this.lastChatLock.readLock();
        this.lastChatWriteLock = this.lastChatLock.writeLock();
        mc = Minecraft.getMinecraft();
        gnc = gncInstance;
        generalSettings = new TCSettingsGeneral(this);
        serverSettings = new TCSettingsServer(this);
        filterSettings = new TCSettingsFilters(this);
        spellingSettings = new TCSettingsSpelling(this);
        advancedSettings = new TCSettingsAdvanced(this);
        spellChecker = TCSpellCheckManager.getInstance();
        generalSettings.loadSettingsFile();
        spellingSettings.loadSettingsFile();
        advancedSettings.loadSettingsFile();
        defaultUnicode = mc.fontRenderer.getUnicodeFlag();
    }

    public void activateIndex(int ind)
    {
        List actives = this.getActive();

        if (actives.size() == 1)
        {
            int i = 1;

            for (Iterator iter = this.channelMap.values().iterator(); iter.hasNext(); ++i)
            {
                ChatChannel chan = (ChatChannel)iter.next();

                if (i == ind)
                {
                    if (mc.currentScreen instanceof GuiChatTC)
                    {
                        ((GuiChatTC)mc.currentScreen).checkCommandPrefixChange((ChatChannel)this.channelMap.get(actives.get(0)), chan);
                    }

                    this.resetDisplayedChat();
                    return;
                }
            }
        }
    }

    public void activateNext()
    {
        List actives = this.getActive();

        if (actives.size() == 1)
        {
            Iterator iter = this.channelMap.values().iterator();
            ChatChannel chan;

            for (chan = (ChatChannel)iter.next(); iter.hasNext(); chan = (ChatChannel)iter.next())
            {
                if (chan.getTitle().equals(actives.get(0)))
                {
                    if (mc.currentScreen instanceof GuiChatTC)
                    {
                        ((GuiChatTC)mc.currentScreen).checkCommandPrefixChange(chan, (ChatChannel)iter.next());
                    }

                    this.resetDisplayedChat();
                    return;
                }
            }

            if (chan.getTitle().equals(actives.get(0)))
            {
                iter = this.channelMap.values().iterator();

                if (iter.hasNext() && mc.currentScreen instanceof GuiChatTC)
                {
                    ((GuiChatTC)mc.currentScreen).checkCommandPrefixChange(chan, (ChatChannel)iter.next());
                }

                this.resetDisplayedChat();
            }
        }
    }

    public void activatePrev()
    {
        List actives = this.getActive();

        if (actives.size() == 1)
        {
            ListIterator iter = Lists.newArrayList(this.channelMap.values()).listIterator(this.channelMap.size());
            ChatChannel chan;

            for (chan = (ChatChannel)iter.previous(); iter.hasPrevious(); chan = (ChatChannel)iter.previous())
            {
                if (chan.getTitle().equals(actives.get(0)))
                {
                    if (mc.currentScreen instanceof GuiChatTC)
                    {
                        ((GuiChatTC)mc.currentScreen).checkCommandPrefixChange(chan, (ChatChannel)iter.previous());
                    }

                    this.resetDisplayedChat();
                    return;
                }
            }

            if (chan.getTitle().equals(actives.get(0)))
            {
                chan.active = false;
                iter = (new ArrayList(this.channelMap.values())).listIterator(this.channelMap.size());

                if (iter.hasPrevious() && mc.currentScreen instanceof GuiChatTC)
                {
                    ((GuiChatTC)mc.currentScreen).checkCommandPrefixChange(chan, (ChatChannel)iter.previous());
                }

                this.resetDisplayedChat();
            }
        }
    }

    public void addToChannel(String name, TCChatLine thisChat, boolean visible)
    {
        if (!serverSettings.ignoredChanPattern.matcher(name).matches())
        {
            thisChat = new TCChatLine(thisChat);
            thisChat.setChatLineString(thisChat.getChatComponent().createCopy());
            ChatChannel theChan = (ChatChannel)this.channelMap.get(name);

            if (theChan == null)
            {
                if (this.channelMap.size() >= 20)
                {
                    return;
                }

                theChan = new ChatChannel(name);
                this.channelMap.put(name, theChan);

                if (mc.currentScreen instanceof GuiChatTC)
                {
                    ((GuiChatTC)mc.currentScreen).addChannelLive(theChan);
                }
            }

            if (generalSettings.groupSpam.getValue().booleanValue())
            {
                this.spamCheck(theChan, thisChat);

                if (theChan.hasSpam)
                {
                    theChan.removeChatLine(0);
                }
            }
            else
            {
                this.lastChatMap.put(theChan, thisChat.getChatComponent().getUnformattedText());
            }

            theChan.addChat(thisChat, visible);
            theChan.trimLog();
        }
    }

    public void deleteChatLines(int id)
    {
        Iterator var2 = this.channelMap.values().iterator();

        while (var2.hasNext())
        {
            ChatChannel chan = (ChatChannel)var2.next();
            chan.deleteChatLines(id);
        }
    }

    public boolean channelExists(String name)
    {
        return this.channelMap.get(name) != null;
    }

    public void checkServer()
    {
        if (!updateChecked)
        {
            updateChecked = true;
            BackgroundUpdateCheck buc = new BackgroundUpdateCheck();
            buc.start();
        }

        if (!serverSettings.serverIP.equals(TabbyChatUtils.getServerIp()))
        {
            this.storeChannelData();
            this.channelMap.clear();

            if (this.enabled())
            {
                this.enable();
                this.resetDisplayedChat();
            }
            else
            {
                this.disable();
            }
        }
    }

    public void createNewChannel(String name)
    {
        if (!this.channelExists(name))
        {
            if (name != null && name.length() > 0 && this.channelMap.size() < 20)
            {
                this.channelMap.put(name, new ChatChannel(name));
            }
        }
    }

    public void disable()
    {
        this.storeChannelData();
    }

    public void enable()
    {
        this.storeChannelData();

        if (!this.channelMap.containsKey("*"))
        {
            this.channelMap.put("*", new ChatChannel("*"));
            ((ChatChannel)this.channelMap.get("*")).active = true;
        }

        if (firstRun)
        {
            firstRun = false;
        }
        else
        {
            if (!this.channelMap.containsKey("Commerce"))
            {
                this.channelMap.put("Commerce", new ChatChannel("Commerce"));
                ((ChatChannel)this.channelMap.get("Commerce")).cmdPrefix = "[CM]";
            }

            if (!this.channelMap.containsKey("Recrutement"))
            {
                this.channelMap.put("Recrutement", new ChatChannel("Recrutement"));
                ((ChatChannel)this.channelMap.get("Recrutement")).cmdPrefix = "[RC]";
                ((ChatChannel)this.channelMap.get("Recrutement")).notificationsOn = true;
            }

            printMessageToChat(EnumChatFormatting.GOLD + "\u00a7e[\u00a76\u00a7lCommerce\u00a7e] \u00a7eVous \u00eates dans le chat Commerce !");
            printMessageToChat(EnumChatFormatting.GOLD + "\u00a7b[\u00a7a\u00a7lRecrutement\u00a7b] \u00a7aVous \u00eates dans le chat Recrutement !");
            this.serverDataLock.tryAcquire();
            this.updateChanDataPath(false);
            serverSettings.updateForServer();
            filterSettings.updateForServer();
            this.reloadServerData();
            this.reloadSettingsData(false);

            if (serverSettings.serverIP.length() > 0)
            {
                this.loadPMPatterns();
            }

            this.serverDataLock.release();

            if (generalSettings.saveChatLog.getValue().booleanValue() && serverSettings.serverIP != null)
            {
                TabbyChatUtils.logChat("\nBEGIN CHAT LOGGING -- " + (new SimpleDateFormat()).format(Calendar.getInstance().getTime()), (ChatChannel)null);
            }
        }
    }

    public boolean enabled()
    {
        return generalSettings.tabbyChatEnable.getValue().booleanValue();
    }

    protected void finalize()
    {
        this.storeChannelData();
    }

    public List<String> getActive()
    {
        int n = this.channelMap.size();
        ArrayList actives = new ArrayList(n);
        Iterator var3 = this.channelMap.values().iterator();

        while (var3.hasNext())
        {
            ChatChannel chan = (ChatChannel)var3.next();

            if (chan.active)
            {
                actives.add(chan.getTitle());
            }
        }

        return actives;
    }

    protected void loadChannelData()
    {
        Map importData = null;

        if (chanDataFile.exists())
        {
            GZIPInputStream inputstream = null;
            label94:
            {
                try
                {
                    Type oldIDs = (new TypeToken()
                    {
                    }).getType();
                    inputstream = new GZIPInputStream(FileUtils.openInputStream(chanDataFile));
                    String e = IOUtils.toString(inputstream, Charsets.UTF_8);
                    importData = (Map)gson.fromJson(e, oldIDs);
                    break label94;
                }
                catch (Exception var11)
                {
                    printException("Unable to read channel data file : \'" + var11.getLocalizedMessage() + "\'", var11);
                }
                finally
                {
                    IOUtils.closeQuietly(inputstream);
                }

                return;
            }

            if (importData != null)
            {
                int var13 = 0;

                try
                {
                    Iterator var14 = importData.entrySet().iterator();

                    while (var14.hasNext())
                    {
                        Entry chan = (Entry)var14.next();

                        if (!((String)chan.getKey()).contentEquals("TabbyChat"))
                        {
                            ChatChannel _new = null;

                            if (!this.channelMap.containsKey(chan.getKey()))
                            {
                                _new = new ChatChannel((String)chan.getKey());
                                _new.chanID = ((ChatChannel)chan.getValue()).chanID;
                                this.channelMap.put(_new.getTitle(), _new);
                            }
                            else
                            {
                                _new = (ChatChannel)this.channelMap.get(chan.getKey());
                            }

                            _new.setAlias(((ChatChannel)chan.getValue()).getAlias());
                            _new.active = ((ChatChannel)chan.getValue()).active;
                            _new.notificationsOn = ((ChatChannel)chan.getValue()).notificationsOn;
                            _new.hidePrefix = ((ChatChannel)chan.getValue()).hidePrefix;
                            _new.cmdPrefix = ((ChatChannel)chan.getValue()).cmdPrefix;
                            _new.importOldChat((ChatChannel)chan.getValue());
                            this.addToChannel((String)chan.getKey(), new TCChatLine(-1, new ChatComponentText("-- chat history from " + (new SimpleDateFormat()).format(Long.valueOf(chanDataFile.lastModified()))), 0, true), true);
                            ++var13;
                        }
                    }
                }
                catch (ClassCastException var10)
                {
                    printMessageToChat("Unable to load channel history data due to upgrade (sorry!)");
                    printException("Unable to load channel history", var10);
                }

                ChatChannel.nextID = 3600 + var13;
                this.resetDisplayedChat();
            }
        }
    }

    protected void loadPatterns()
    {
        ChannelDelimEnum delims = (ChannelDelimEnum)serverSettings.delimiterChars.getValue();
        String colCode = "";
        String fmtCode = "";

        if (serverSettings.delimColorBool.getValue().booleanValue())
        {
            colCode = ((ColorCodeEnum)serverSettings.delimColorCode.getValue()).toCode();
        }

        if (serverSettings.delimFormatBool.getValue().booleanValue())
        {
            fmtCode = ((FormatCodeEnum)serverSettings.delimFormatCode.getValue()).toCode();
        }

        String frmt = colCode + fmtCode;

        if (((ColorCodeEnum)serverSettings.delimColorCode.getValue()).toString().equals("White"))
        {
            frmt = "(" + colCode + ")?" + fmtCode;
        }
        else if (frmt.length() > 7)
        {
            frmt = "[" + frmt + "]{2}";
        }

        if (frmt.length() > 0)
        {
            frmt = "(?i:" + frmt + ")";
        }

        if (frmt.length() == 0)
        {
            frmt = "(?i:\u00a7[0-9A-FK-OR])*";
        }

        this.chatChannelPatternDirty = Pattern.compile("^(\u00a7r)?" + frmt + "\\" + delims.open() + "([\\p{L}0-9_\u00a7]+)\\" + delims.close());
        this.chatChannelPatternClean = Pattern.compile("^\\" + delims.open() + "([\\p{L}0-9_]{1," + advancedSettings.maxLengthChannelName.getValue() + "})\\" + delims.close());
    }

    protected void loadPMPatterns()
    {
        StringBuilder toMePM = new StringBuilder();
        StringBuilder fromMePM = new StringBuilder();
        toMePM.append("^\\[([\\p{L}\\p{N}_]{3,16})[ ]?\\-\\>[ ]?me\\]");
        fromMePM.append("^\\[me[ ]?\\-\\>[ ]?([\\p{L}\\p{N}_]{3,16})\\]");
        toMePM.append("|^\\(From ([\\p{L}\\p{N}_]{3,16})\\)[ ]?:");
        fromMePM.append("|^\\(To ([\\p{L}\\p{N}_]{3,16})\\)[ ]?:");
        toMePM.append("|^From ([\\p{L}\\p{N}_]{3,16})[ ]?:");
        fromMePM.append("|^To ([\\p{L}\\p{N}_]{3,16})[ ]?:");
        toMePM.append("|^([\\p{L}\\p{N}_]{3,16}) whispers to you:");
        fromMePM.append("|^You whisper to ([\\p{L}\\p{N}_]{3,16}):");
        String e;

        if (mc.thePlayer != null && mc.thePlayer.getCommandSenderName() != null)
        {
            e = mc.thePlayer.getCommandSenderName();
            toMePM.append("|^\\[([\\p{L}\\p{N}_]{3,16})[ ]?\\-\\>[ ]?").append(e).append("\\]");
            fromMePM.append("|^\\[").append(e).append("[ ]?\\-\\>[ ]?([\\p{L}\\p{N}_]{3,16})\\]");
        }

        try
        {
            e = serverSettings.pmTabRegexToMe.getValue().replace("{$player}", "([\\p{L}\\p{N}_]{3,16})");

            if (!e.isEmpty())
            {
                Pattern.compile(e);
                toMePM.append("|").append(e);
            }
        }
        catch (PatternSyntaxException var5)
        {
            log.error("Error while setting \'To me\' regex.", var5);
            serverSettings.pmTabRegexToMe.setValue("");
            printMessageToChat(ColorCodeEnum.RED.toCode() + "Unable to set \'To me\' pm regex. See console for details.");
        }

        try
        {
            e = serverSettings.pmTabRegexFromMe.getValue().replace("{$player}", "([\\p{L}\\p{N}_]{3,16})");

            if (!e.isEmpty())
            {
                Pattern.compile(e);
                fromMePM.append("|").append(e);
            }
        }
        catch (PatternSyntaxException var4)
        {
            log.error("Error while setting \'From me\' regex.", var4);
            serverSettings.pmTabRegexFromMe.setValue("");
            printMessageToChat(ColorCodeEnum.RED.toCode() + "Unable to set \'From me\' pm regex. See console for details.");
        }

        this.chatPMtoMePattern = Pattern.compile(toMePM.toString());
        this.chatPMfromMePattern = Pattern.compile(fromMePM.toString());
    }

    public void pollForUnread(Gui _gui, int _tick)
    {
        if (!this.getActive().contains("*"))
        {
            boolean _opacity = false;
            int tickdiff = 50;
            this.lastChatReadLock.lock();

            try
            {
                if (this.lastChat != null)
                {
                    tickdiff = _tick - this.lastChat.getUpdatedCounter();
                }
            }
            finally
            {
                this.lastChatReadLock.unlock();
            }

            if (tickdiff < 50)
            {
                float var6 = mc.gameSettings.chatOpacity * 0.9F + 0.1F;
                double var10 = (double)tickdiff / 50.0D;
                var10 = 1.0D - var10;
                var10 *= 10.0D;

                if (var10 < 0.0D)
                {
                    var10 = 0.0D;
                }

                if (var10 > 1.0D)
                {
                    var10 = 1.0D;
                }

                var10 *= var10;
                int _opacity1 = (int)(255.0D * var10);
                _opacity1 = (int)((float)_opacity1 * var6);

                if (_opacity1 <= 3)
                {
                    return;
                }

                ChatBox.updateTabs(this.channelMap);
                Iterator var8 = this.channelMap.values().iterator();

                while (var8.hasNext())
                {
                    ChatChannel chan = (ChatChannel)var8.next();

                    if (chan.unread && chan.notificationsOn)
                    {
                        chan.unreadNotify(_gui, _opacity1);
                    }
                }
            }
        }
    }

    public void processChat(TCChatLine theChat)
    {
        if (this.serverDataLock.availablePermits() == 0)
        {
            this.serverDataLock.acquireUninterruptibly();
            this.serverDataLock.release();
        }

        if (theChat != null)
        {
            ArrayList toTabs = new ArrayList(20);
            ArrayList filterTabs = new ArrayList(20);
            String channelTab = null;
            String pmTab = null;
            toTabs.add("*");

            if (this.channelMap.containsKey("Commerce"))
            {
                ((ChatChannel)this.channelMap.get("Commerce")).hidePrefix = true;
            }

            if (this.channelMap.containsKey("Recrutement"))
            {
                ((ChatChannel)this.channelMap.get("Recrutement")).hidePrefix = true;
            }

            IChatComponent raw = theChat.getChatComponent();
            IChatComponent filtered = this.processChatForFilters(raw, filterTabs);

            if (generalSettings.saveChatLog.getValue().booleanValue())
            {
                TabbyChatUtils.logChat(raw.getUnformattedText(), (ChatChannel)null);
            }

            if (filtered != null)
            {
                ChatChannel tabSet = null;

                if (serverSettings.autoChannelSearch.getValue().booleanValue())
                {
                    channelTab = this.processChatForChannels(raw);
                }

                if (channelTab == null)
                {
                    if (serverSettings.autoPMSearch.getValue().booleanValue())
                    {
                        pmTab = this.processChatForPMs(raw.getUnformattedText());

                        if (pmTab != null)
                        {
                            tabSet = new ChatChannel(pmTab);

                            if (generalSettings.saveChatLog.getValue().booleanValue() && generalSettings.splitChatLog.getValue().booleanValue())
                            {
                                TabbyChatUtils.logChat(raw.getUnformattedText(), tabSet);
                            }
                        }
                    }
                }
                else
                {
                    toTabs.add(channelTab);
                    tabSet = new ChatChannel(channelTab);

                    if (generalSettings.saveChatLog.getValue().booleanValue() && generalSettings.splitChatLog.getValue().booleanValue())
                    {
                        TabbyChatUtils.logChat(raw.getUnformattedText(), tabSet);
                    }
                }

                toTabs.addAll(filterTabs);
                TCChatLine resultChatLine = new TCChatLine(theChat.getUpdatedCounter(), filtered, theChat.getChatLineID(), theChat.statusMsg);
                resultChatLine.timeStamp = Calendar.getInstance().getTime();
                HashSet tabSet1 = new HashSet(toTabs);
                List activeTabs = this.getActive();
                boolean visible = false;

                if (pmTab != null)
                {
                    if (!this.channelMap.containsKey(pmTab))
                    {
                        ChatChannel tabIter = new ChatChannel(pmTab);
                        tabIter.cmdPrefix = "/msg " + pmTab;
                        this.channelMap.put(pmTab, tabIter);
                        this.addToChannel(pmTab, resultChatLine, false);

                        if (mc.currentScreen instanceof GuiChatTC)
                        {
                            ((GuiChatTC)mc.currentScreen).addChannelLive(tabIter);
                        }
                    }
                    else if (this.channelMap.containsKey(pmTab))
                    {
                        if (activeTabs.contains(pmTab))
                        {
                            visible = true;
                        }

                        this.addToChannel(pmTab, resultChatLine, visible);
                    }
                }

                if (!visible)
                {
                    Set tabIter1 = (Set)tabSet1.clone();
                    tabIter1.retainAll(activeTabs);

                    if (tabIter1.size() > 0)
                    {
                        visible = true;
                    }
                }

                Iterator tabIter2 = tabSet1.iterator();

                while (tabIter2.hasNext())
                {
                    String tab = (String)tabIter2.next();
                    this.addToChannel(tab, resultChatLine, visible);
                }

                Iterator tab1;

                if (!Collections.disjoint(this.getActive(), tabSet1))
                {
                    tab1 = tabSet1.iterator();

                    while (tab1.hasNext())
                    {
                        String channel = (String)tab1.next();
                        ChatChannel channel1 = (ChatChannel)this.channelMap.get(channel);

                        if (channel1 != null && channel1.active && channel1.hasSpam)
                        {
                            resultChatLine.getChatComponent().appendText(" [" + channel1.spamCount + "x]");
                            break;
                        }
                    }
                }

                ChatChannel channel2;

                for (tab1 = this.channelMap.values().iterator(); tab1.hasNext(); channel2.hasSpam = false)
                {
                    channel2 = (ChatChannel)tab1.next();
                }

                this.lastChatWriteLock.lock();

                try
                {
                    this.lastChat = resultChatLine;
                }
                finally
                {
                    this.lastChatWriteLock.unlock();
                }

                this.lastChatReadLock.lock();

                try
                {
                    if (visible)
                    {
                        gnc.addChatLines(0, this.lastChat);
                    }
                }
                finally
                {
                    this.lastChatReadLock.unlock();
                }
            }
        }
    }

    private String processChatForChannels(IChatComponent raw)
    {
        Matcher findChannelClean = this.chatChannelPatternClean.matcher(EnumChatFormatting.getTextWithoutFormattingCodes(raw.getUnformattedText()));
        Matcher findChannelDirty = this.chatChannelPatternDirty.matcher(raw.getFormattedText());
        boolean dirtyCheck = !serverSettings.delimColorBool.getValue().booleanValue() && !serverSettings.delimFormatBool.getValue().booleanValue() ? true : findChannelDirty.find();
        return findChannelClean.find() && dirtyCheck ? findChannelClean.group(1) : null;
    }

    private IChatComponent processChatForFilters(IChatComponent raw, List<String> destinations)
    {
        if (raw == null)
        {
            return null;
        }
        else
        {
            IChatComponent chat = raw;

            for (Entry iFilter = filterSettings.filterMap.firstEntry(); iFilter != null; iFilter = filterSettings.filterMap.higherEntry((Integer) iFilter.getKey()))
            {
                if (((TCChatFilter)iFilter.getValue()).applyFilterToDirtyChat(chat))
                {
                    if (((TCChatFilter)iFilter.getValue()).removeMatches)
                    {
                        return null;
                    }

                    if (((TCChatFilter)iFilter.getValue()).highlightBool)
                    {
                        int[] destTab = ((TCChatFilter)iFilter.getValue()).getLastMatch();

                        for (int chan = 0; chan < destTab.length; chan += 2)
                        {
                            int start = destTab[chan];
                            int end = destTab[chan + 1];
                            IChatComponent chat1 = ChatComponentUtils.subComponent(chat, 0, start);
                            IChatComponent chat2 = ChatComponentUtils.subComponent(chat, start, end);
                            IChatComponent chat3 = ChatComponentUtils.subComponent(chat, end);
                            ChatStyle style = chat2.getChatStyle();

                            if (((TCChatFilter)iFilter.getValue()).highlightColor != ColorCodeEnum.DEFAULT)
                            {
                                style.setColor(((TCChatFilter)iFilter.getValue()).highlightColor.toVanilla());
                            }

                            switch (destTab[((TCChatFilter)iFilter.getValue()).highlightFormat.ordinal()])
                            {
                                case 1:
                                    style.setBold(Boolean.valueOf(true));
                                    break;

                                case 2:
                                    style.setItalic(Boolean.valueOf(true));
                                    break;

                                case 3:
                                    style.setStrikethrough(Boolean.valueOf(true));
                                    break;

                                case 4:
                                    style.setUnderlined(Boolean.valueOf(true));
                                    break;

                                case 5:
                                    style.setObfuscated(Boolean.valueOf(true));

                                case 6:
                            }

                            chat = chat1.appendSibling(chat2).appendSibling(chat3);
                        }

                        raw = chat;
                    }

                    if (((TCChatFilter)iFilter.getValue()).sendToTabBool)
                    {
                        if (((TCChatFilter)iFilter.getValue()).sendToAllTabs)
                        {
                            Iterator destTab1 = this.channelMap.values().iterator();

                            while (destTab1.hasNext())
                            {
                                ChatChannel chan1 = (ChatChannel)destTab1.next();
                                destinations.add(chan1.getTitle());
                            }
                        }
                        else
                        {
                            String destTab2 = ((TCChatFilter)iFilter.getValue()).getTabName();

                            if (destTab2 != null && destTab2.length() > 0 && !destinations.contains(destTab2))
                            {
                                destinations.add(destTab2);
                            }
                        }
                    }

                    if (((TCChatFilter)iFilter.getValue()).audioNotificationBool)
                    {
                        ((TCChatFilter)iFilter.getValue()).audioNotification();
                    }
                }
            }

            return raw;
        }
    }

    private String processChatForPMs(String raw)
    {
        raw = EnumChatFormatting.getTextWithoutFormattingCodes(raw);

        if (this.chatPMtoMePattern != null)
        {
            Matcher findPMtoMe = this.chatPMtoMePattern.matcher(raw);

            if (findPMtoMe.find())
            {
                for (int findPMfromMe = 1; findPMfromMe <= findPMtoMe.groupCount(); ++findPMfromMe)
                {
                    if (findPMtoMe.group(findPMfromMe) != null)
                    {
                        return findPMtoMe.group(findPMfromMe);
                    }
                }
            }
            else if (this.chatPMfromMePattern != null)
            {
                Matcher var5 = this.chatPMfromMePattern.matcher(raw);

                if (var5.find())
                {
                    for (int i = 1; i <= var5.groupCount(); ++i)
                    {
                        if (var5.group(i) != null)
                        {
                            return var5.group(i);
                        }
                    }
                }
            }
        }

        return null;
    }

    private void reloadServerData()
    {
        serverSettings.loadSettingsFile();
        filterSettings.loadSettingsFile();
        this.loadChannelData();
    }

    public void reloadSettingsData(boolean withSave)
    {
        this.updateDefaults();
        this.loadPatterns();
        this.loadPMPatterns();
        this.updateFilters();

        if (withSave)
        {
            this.storeChannelData();
        }
    }

    public void removeTab(String _name)
    {
        this.lastChatMap.remove(this.channelMap.get(_name));
        this.channelMap.remove(_name);
    }

    public void resetDisplayedChat()
    {
        gnc.clearChatLines();
        List actives = this.getActive();

        if (actives.size() >= 1)
        {
            gnc.addChatLines((ChatChannel)this.channelMap.get(actives.get(0)));
            int n = actives.size();

            for (int i = 1; i < n; ++i)
            {
                gnc.mergeChatLines((ChatChannel)this.channelMap.get(actives.get(i)));
            }
        }
    }

    private void spamCheck(ChatChannel theChan, TCChatLine lineChat)
    {
        String oldChat = "";
        String newChat = lineChat.getChatComponent().getUnformattedText();

        if (this.lastChatMap.containsKey(theChan))
        {
            oldChat = (String)this.lastChatMap.get(theChan);
        }

        if (oldChat.equals(newChat))
        {
            theChan.hasSpam = true;
            ++theChan.spamCount;
            lineChat.getChatComponent().appendText(" [" + theChan.spamCount + "x]");
        }
        else
        {
            theChan.hasSpam = false;
            theChan.spamCount = 1;
            this.lastChatMap.put(theChan, newChat);
        }
    }

    public void storeChannelData()
    {
        if (chanDataFile != null)
        {
            if (!chanDataFile.getParentFile().exists())
            {
                chanDataFile.getParentFile().mkdirs();
            }

            GZIPOutputStream output = null;

            try
            {
                String e = gson.toJson(this.channelMap);
                output = new GZIPOutputStream(new FileOutputStream(chanDataFile));
                IOUtils.write(e, output, Charsets.UTF_8);
            }
            catch (Exception var6)
            {
                printErr("Unable to write channel data to file : \'" + var6.getLocalizedMessage() + "\' : " + var6.toString());
            }
            finally
            {
                IOUtils.closeQuietly(output);
            }
        }
    }

    private void updateChanDataPath(boolean make)
    {
        String pName = "";

        if (mc.thePlayer != null && mc.thePlayer.getCommandSenderName() != null)
        {
            pName = mc.thePlayer.getCommandSenderName();
        }

        File parentDir = TabbyChatUtils.getServerDir();

        if (make && !parentDir.exists())
        {
            parentDir.mkdirs();
        }

        chanDataFile = new File(parentDir, pName + "_chanData.ser");
    }

    protected void updateDefaults()
    {
        if (generalSettings.tabbyChatEnable.getValue().booleanValue())
        {
            ArrayList dList = new ArrayList(serverSettings.defaultChanList);
            Iterator var3 = this.channelMap.values().iterator();

            while (var3.hasNext())
            {
                ChatChannel defChan = (ChatChannel)var3.next();
                int ind = dList.indexOf(defChan.getTitle());

                if (ind >= 0)
                {
                    dList.remove(ind);
                }
            }

            var3 = dList.iterator();

            while (var3.hasNext())
            {
                String defChan1 = (String)var3.next();

                if (defChan1.length() > 0)
                {
                    this.channelMap.put(defChan1, new ChatChannel(defChan1));
                }
            }
        }
    }

    protected void updateFilters()
    {
        if (generalSettings.tabbyChatEnable.getValue().booleanValue())
        {
            if (filterSettings.filterMap.size() != 0)
            {
                for (Entry iFilter = filterSettings.filterMap.firstEntry(); iFilter != null; iFilter = filterSettings.filterMap.higherEntry((Integer) iFilter.getKey()))
                {
                    String newName = ((TCChatFilter)iFilter.getValue()).sendToTabName;

                    if (((TCChatFilter)iFilter.getValue()).sendToTabBool && !((TCChatFilter)iFilter.getValue()).sendToAllTabs && !this.channelMap.containsKey(newName) && !newName.startsWith("%"))
                    {
                        this.channelMap.put(newName, new ChatChannel(newName));
                    }
                }
            }
        }
    }
}
