package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class BatonDeSoin extends Item{
	public BatonDeSoin()
	{
        this.maxStackSize = 1;
        this.setMaxDamage(8);
        this.setCreativeTab(CreativeTabs.tabBrewing);
	}

    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        par1ItemStack.damageItem(1, par3EntityPlayer);
        par3EntityPlayer.addPotionEffect(new PotionEffect(Potion.heal.id, 1, 0));
		return par1ItemStack;

    }

}