package acs.tabbychat.threads;

import acs.tabbychat.core.TabbyChat;
import acs.tabbychat.util.TabbyChatUtils;
import net.minecraft.client.resources.I18n;

public class BackgroundUpdateCheck extends Thread
{
    public void run()
    {
        String newest = TabbyChat.getNewestVersion();
        String current = "@@VERSION@@";
        boolean updateFound = false;

        if (TabbyChat.generalSettings.tabbyChatEnable.getValue().booleanValue() && TabbyChat.generalSettings.updateCheckEnable.getValue().booleanValue() && !newest.equalsIgnoreCase("invalid"))
        {
            String[] newVersionString = newest.split("\\.");
            String[] versionString = current.split("\\.");
            int[] newVersion = new int[newVersionString.length];
            int[] version = new int[versionString.length];
            int i;

            for (i = 0; i < newVersion.length; ++i)
            {
                newVersion[i] = TabbyChatUtils.parseInteger(newVersionString[i], Integer.MIN_VALUE, Integer.MAX_VALUE, 0).intValue();
            }

            for (i = 0; i < version.length; ++i)
            {
                version[i] = TabbyChatUtils.parseInteger(versionString[i], Integer.MIN_VALUE, Integer.MAX_VALUE, 0).intValue();
            }

            for (i = 0; i < Math.min(version.length, newVersion.length); ++i)
            {
                if (version[i] < newVersion[i])
                {
                    updateFound = true;
                    break;
                }

                if (version[i] > newVersion[i])
                {
                    break;
                }
            }

            if (updateFound)
            {
                TabbyChatUtils.log.info("Update Found!");
                StringBuilder updateReport = new StringBuilder("\u00a77");
                updateReport.append(I18n.format("messages.update1", new Object[0]));
                updateReport.append(current);
                updateReport.append(I18n.format("messages.update2", new Object[0]));
                updateReport.append(newest + ") ");
                updateReport.append(I18n.format("messages.update3", new Object[0]));
                updateReport.append("\u00a7r");
                TabbyChat.printMessageToChat(updateReport.toString());
            }
        }
    }
}
