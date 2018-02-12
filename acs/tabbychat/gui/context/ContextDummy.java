package acs.tabbychat.gui.context;

import java.lang.reflect.Method;
import java.util.List;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;

public class ContextDummy extends ChatContext
{
    private Method onClick;
    private Method isValid;
    private ChatContext.Behavior behavior;
    private ResourceLocation icon;
    private List<ChatContext> children;

    public ContextDummy()
    {
        this.behavior = ChatContext.Behavior.GRAY;
    }

    public ContextDummy(String display)
    {
        this.behavior = ChatContext.Behavior.GRAY;
        this.displayString = display;
    }

    public void onClicked()
    {
        if (this.onClick != null)
        {
            try
            {
                this.onClick.invoke((Object)null, new Object[0]);
            }
            catch (Exception var2)
            {
                LogManager.getLogger().error(var2);
            }
        }
    }

    public String getDisplayString()
    {
        return this.displayString;
    }

    public ResourceLocation getDisplayIcon()
    {
        return this.icon;
    }

    public List<ChatContext> getChildren()
    {
        return this.children;
    }

    public boolean isPositionValid(int x, int y)
    {
        if (this.isValid == null)
        {
            return false;
        }
        else
        {
            try
            {
                return ((Boolean)this.isValid.invoke((Object)null, new Object[0])).booleanValue();
            }
            catch (Exception var4)
            {
                LogManager.getLogger().error(var4);
                return false;
            }
        }
    }

    public ChatContext.Behavior getDisabledBehavior()
    {
        return this.behavior;
    }

    public void setOnClickMethod(Method method)
    {
        this.onClick = method;
    }

    public Method getOnClickMethod()
    {
        return this.onClick;
    }

    public void setIsValidMethod(Method method)
    {
        this.isValid = method;
    }

    public Method getIsValidMethod()
    {
        return this.isValid;
    }

    public void setBehavior(ChatContext.Behavior behavior)
    {
        this.behavior = behavior;
    }

    public void setDisplayIcon(ResourceLocation icon)
    {
        this.icon = icon;
    }

    public void setDisplayString(String string)
    {
        this.displayString = string;
    }

    public void setChildren(List<ChatContext> children)
    {
        this.children = children;
    }
}
