package acs.tabbychat.api;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.List;

public class TCExtensionManager
{
    public static final TCExtensionManager INSTANCE = new TCExtensionManager();
    private List < Class <? extends IChatExtension >> list = Lists.newArrayList();

    public List < Class <? extends IChatExtension >> getExtensions()
    {
        return ImmutableList.copyOf(this.list);
    }

    public void registerExtension(Class <? extends IChatExtension > ext)
    {
        if (!this.list.contains(ext))
        {
            this.list.add(ext);
        }
    }

    public void unregisterExtension(Class <? extends IChatExtension > ext)
    {
        this.list.remove(ext);
    }
}
