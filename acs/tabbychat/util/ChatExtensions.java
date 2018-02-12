package acs.tabbychat.util;

import acs.tabbychat.api.IChatExtension;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChatExtensions
{
    private List<IChatExtension> list = Lists.newArrayList();

    public ChatExtensions(List < Class <? extends IChatExtension >> list)
    {
        Iterator var2 = list.iterator();

        while (var2.hasNext())
        {
            Class ext = (Class)var2.next();

            try
            {
                IChatExtension e = (IChatExtension)ext.newInstance();
                e.load();
                this.list.add(e);
            }
            catch (Exception var5)
            {
                TabbyChatUtils.log.error("Unable to initialize " + ext.getName(), var5);
            }
        }
    }

    public <T extends Object & IChatExtension> List<T> getListOf(Class<T> extClass)
    {
        ArrayList t = Lists.newArrayList();
        Iterator var3 = this.list.iterator();

        while (var3.hasNext())
        {
            IChatExtension ext = (IChatExtension)var3.next();

            if (extClass.isInstance(ext))
            {
                t.add(ext);
            }
        }

        return t;
    }
}
