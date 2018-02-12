package fr.shey.wiki;

import net.minecraft.client.gui.inventory.GuiCodex;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class CodexGUI extends Container
{
	
    /** The crafting matrix inventory (3x3). */
    public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
    public IInventory craftResult = new InventoryCraftResult();
    
    private World worldObj;
    private int posX;
    private int posY;
    private int posZ;
    
    private SlotCrafting resultcustomSlot;
    
    private CustomSlot customSlot1;
    private CustomSlot customSlot2;
    private CustomSlot customSlot3;
    
    private CustomSlot customSlot4;
    private CustomSlot customSlot5;
    private CustomSlot customSlot6;
    
    private CustomSlot customSlot7;
    private CustomSlot customSlot8;
    private CustomSlot customSlot9;

    public CodexGUI(EntityPlayer p_i1808_1_)
    {
    	
    	resultcustomSlot = new SlotCrafting(p_i1808_1_, this.craftMatrix, this.craftResult, 0, 124, 35);
        this.addSlotToContainer(resultcustomSlot);
        
        int var6;
        int var7;
        
    	customSlot1 = new CustomSlot(this.craftMatrix, 0 + 0 * 3, 30 + 0 * 18, 17 + 0 * 18);
    	customSlot2 = new CustomSlot(this.craftMatrix, 1 + 0 * 3, 30 + 1 * 18, 17 + 0 * 18);
    	customSlot3 = new CustomSlot(this.craftMatrix, 2 + 0 * 3, 30 + 2 * 18, 17 + 0 * 18);
    	
    	customSlot4 = new CustomSlot(this.craftMatrix, 0 + 1 * 3, 30 + 0 * 18, 17 + 1 * 18);
    	customSlot5 = new CustomSlot(this.craftMatrix, 1 + 1 * 3, 30 + 1 * 18, 17 + 1 * 18);
    	customSlot6 = new CustomSlot(this.craftMatrix, 2 + 1 * 3, 30 + 2 * 18, 17 + 1 * 18);
    	
    	customSlot7 = new CustomSlot(this.craftMatrix, 0 + 2 * 3, 30 + 0 * 18, 17 + 2 * 18);
    	customSlot8 = new CustomSlot(this.craftMatrix, 1 + 2 * 3, 30 + 1 * 18, 17 + 2 * 18);
    	customSlot9 = new CustomSlot(this.craftMatrix, 2 + 2 * 3, 30 + 2 * 18, 17 + 2 * 18);
    	
        this.addSlotToContainer(customSlot1);
        this.addSlotToContainer(customSlot2);
        this.addSlotToContainer(customSlot3);
        this.addSlotToContainer(customSlot4);
        this.addSlotToContainer(customSlot5);
        this.addSlotToContainer(customSlot6);
        this.addSlotToContainer(customSlot7);
        this.addSlotToContainer(customSlot8);
        this.addSlotToContainer(customSlot9);

        this.onCraftMatrixChanged(this.craftMatrix);
        
    }
    
    public void updatePage()
    {
    	int pageNumber = GuiCodex.pageNumber;
    	resetAllcustomSlots();
    	
    	if(pageNumber == 1)
    	{
    		GuiCodex.beforeButton.enabled = false;
    		customSlot1.putStack(new ItemStack(Blocks.diamond_block));
    		customSlot2.putStack(new ItemStack(Blocks.diamond_block));
    		customSlot3.putStack(new ItemStack(Blocks.diamond_block));
    		customSlot4.putStack(new ItemStack(Blocks.diamond_block));
    		customSlot6.putStack(new ItemStack(Blocks.diamond_block));
    		
    		resultcustomSlot.putStack(new ItemStack(Items.diamond_helmet));
    	}
    	
    	if(pageNumber == 2)
    	{
    		GuiCodex.beforeButton.enabled = true;
    		customSlot1.putStack(new ItemStack(Blocks.diamond_block));
    		customSlot3.putStack(new ItemStack(Blocks.diamond_block));
    		customSlot4.putStack(new ItemStack(Blocks.diamond_block));
    		customSlot5.putStack(new ItemStack(Blocks.diamond_block));
    		customSlot6.putStack(new ItemStack(Blocks.diamond_block));
    		customSlot7.putStack(new ItemStack(Blocks.diamond_block));
    		customSlot8.putStack(new ItemStack(Blocks.diamond_block));
    		customSlot9.putStack(new ItemStack(Blocks.diamond_block));
    		
    		resultcustomSlot.putStack(new ItemStack(Items.diamond_chestplate));
    	}
    	if(pageNumber == 3)
    	{
    		customSlot1.putStack(new ItemStack(Blocks.diamond_block));
    		customSlot2.putStack(new ItemStack(Blocks.diamond_block));
    		customSlot3.putStack(new ItemStack(Blocks.diamond_block));
    		customSlot4.putStack(new ItemStack(Blocks.diamond_block));
    		customSlot6.putStack(new ItemStack(Blocks.diamond_block));
    		customSlot7.putStack(new ItemStack(Blocks.diamond_block));
    		customSlot9.putStack(new ItemStack(Blocks.diamond_block));
    		
    		resultcustomSlot.putStack(new ItemStack(Items.diamond_leggings));
    	}
    	if(pageNumber == 4)
    	{
    		customSlot4.putStack(new ItemStack(Blocks.diamond_block));
    		customSlot6.putStack(new ItemStack(Blocks.diamond_block));
    		customSlot7.putStack(new ItemStack(Blocks.diamond_block));
    		customSlot9.putStack(new ItemStack(Blocks.diamond_block));
    		
    		resultcustomSlot.putStack(new ItemStack(Items.diamond_boots));
    	}
    	
    	if(pageNumber == 5)
    	{
    		customSlot1.putStack(new ItemStack(Items.pyrite));
    		customSlot2.putStack(new ItemStack(Items.pyrite));
    		customSlot3.putStack(new ItemStack(Items.pyrite));
    		customSlot4.putStack(new ItemStack(Items.pyrite));
    		customSlot6.putStack(new ItemStack(Items.pyrite));
    		
    		resultcustomSlot.putStack(new ItemStack(Items.pyrite_helmet));
    	}
    	
    	if(pageNumber == 6)
    	{
    		customSlot1.putStack(new ItemStack(Items.pyrite));
    		customSlot3.putStack(new ItemStack(Items.pyrite));
    		customSlot4.putStack(new ItemStack(Items.pyrite));
    		customSlot5.putStack(new ItemStack(Items.pyrite));
    		customSlot6.putStack(new ItemStack(Items.pyrite));
    		customSlot7.putStack(new ItemStack(Items.pyrite));
    		customSlot8.putStack(new ItemStack(Items.pyrite));
    		customSlot9.putStack(new ItemStack(Items.pyrite));
    		
    		resultcustomSlot.putStack(new ItemStack(Items.pyrite_chestplate));
    	}
    	if(pageNumber == 7)
    	{
    		customSlot1.putStack(new ItemStack(Items.pyrite));
    		customSlot2.putStack(new ItemStack(Items.pyrite));
    		customSlot3.putStack(new ItemStack(Items.pyrite));
    		customSlot4.putStack(new ItemStack(Items.pyrite));
    		customSlot6.putStack(new ItemStack(Items.pyrite));
    		customSlot7.putStack(new ItemStack(Items.pyrite));
    		customSlot9.putStack(new ItemStack(Items.pyrite));
    		
    		resultcustomSlot.putStack(new ItemStack(Items.pyrite_leggings));
    	}
    	if(pageNumber == 8)
    	{
    		customSlot4.putStack(new ItemStack(Items.pyrite));
    		customSlot6.putStack(new ItemStack(Items.pyrite));
    		customSlot7.putStack(new ItemStack(Items.pyrite));
    		customSlot9.putStack(new ItemStack(Items.pyrite));
    		
    		resultcustomSlot.putStack(new ItemStack(Items.pyrite_boots));
    	}
    	if(pageNumber == 9)
    	{
    		customSlot2.putStack(new ItemStack(Items.pyrite));
    		customSlot5.putStack(new ItemStack(Items.pyrite));
    		customSlot8.putStack(new ItemStack(Items.stick));
    		
    		resultcustomSlot.putStack(new ItemStack(Items.pyrite_sword));
    	}
    	if(pageNumber == 10)
    	{
    		customSlot1.putStack(new ItemStack(Items.pyrite));
    		customSlot2.putStack(new ItemStack(Items.pyrite));
    		customSlot3.putStack(new ItemStack(Items.pyrite));
    		customSlot5.putStack(new ItemStack(Items.stick));
    		customSlot8.putStack(new ItemStack(Items.stick));
    		
    		resultcustomSlot.putStack(new ItemStack(Items.pyrite_pickaxe));
    	}
    	if(pageNumber == 11)
    	{
    		customSlot1.putStack(new ItemStack(Items.pyrite));
    		customSlot2.putStack(new ItemStack(Items.pyrite));
    		customSlot4.putStack(new ItemStack(Items.pyrite));
    		customSlot5.putStack(new ItemStack(Items.stick));
    		customSlot8.putStack(new ItemStack(Items.stick));
    		
    		resultcustomSlot.putStack(new ItemStack(Items.pyrite_axe));
    	}
    	if(pageNumber == 12)
    	{
    		customSlot2.putStack(new ItemStack(Items.pyrite));
    		customSlot5.putStack(new ItemStack(Items.stick));
    		customSlot8.putStack(new ItemStack(Items.stick));
    		
    		resultcustomSlot.putStack(new ItemStack(Items.pyrite_shovel));
    	}
    	if(pageNumber == 13)
    	{
        	customSlot1.putStack(new ItemStack(Items.pyrite));
        	customSlot2.putStack(new ItemStack(Items.pyrite));
        	customSlot3.putStack(new ItemStack(Items.pyrite));
        	customSlot4.putStack(new ItemStack(Items.pyrite));
        	customSlot5.putStack(new ItemStack(Items.pyrite));
        	customSlot6.putStack(new ItemStack(Items.pyrite));
        	customSlot7.putStack(new ItemStack(Items.pyrite));
        	customSlot8.putStack(new ItemStack(Items.pyrite));
        	customSlot9.putStack(new ItemStack(Items.pyrite));
        		
        	resultcustomSlot.putStack(new ItemStack(Blocks.pyrite_block));
    	}
    }
    
    public void resetAllcustomSlots()
    {
    	customSlot1.putStack(new ItemStack(Item.getItemById(492)));
    	customSlot2.putStack(new ItemStack(Item.getItemById(492)));
    	customSlot3.putStack(new ItemStack(Item.getItemById(492)));
    	customSlot4.putStack(new ItemStack(Item.getItemById(492)));
    	customSlot5.putStack(new ItemStack(Item.getItemById(492)));
    	customSlot6.putStack(new ItemStack(Item.getItemById(492)));
    	customSlot7.putStack(new ItemStack(Item.getItemById(492)));
    	customSlot8.putStack(new ItemStack(Item.getItemById(492)));
    	customSlot9.putStack(new ItemStack(Item.getItemById(492)));
    		
    	resultcustomSlot.putStack(new ItemStack(Item.getItemById(492)));
    }

    /**
     * Callback for when the crafting matrix is changed.
     */
    public void onCraftMatrixChanged(IInventory p_75130_1_)
    {
    	super.onCraftMatrixChanged(p_75130_1_);
    }
    
    /**
     * Return whether this customSlot's stack can be taken from this customSlot.
     */
    public boolean canTakeStack(EntityPlayer p_82869_1_)
    {
        return false;
    }

    /**
     * Called when the container is closed.
     */
    public void onContainerClosed(EntityPlayer p_75134_1_)
    {
        super.onContainerClosed(p_75134_1_);
    }

    public boolean canInteractWith(EntityPlayer p_75145_1_)
    {
        return false;
    }

    /**
     * Called when a player shift-clicks on a customSlot. You must override this or you will crash when someone does that.
     */
    public ItemStack transferStackIncustomSlot(EntityPlayer p_82846_1_, int p_82846_2_)
    {
        ItemStack var3 = null;
        return var3;
    }

    public boolean func_94530_a(ItemStack p_94530_1_, CustomSlot p_94530_2_)
    {
        return false;
    }
}

