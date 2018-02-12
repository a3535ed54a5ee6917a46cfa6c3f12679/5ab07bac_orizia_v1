package net.minecraft.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemFraise extends ItemFood {
	
	public ItemFraise(int p_i45341_1_, float p_i45341_2_, boolean p_i45341_3_)
    {
        super(p_i45341_1_, p_i45341_2_, p_i45341_3_);
    }

	
	public void onUpdate(ItemStack p_77663_1, World p_77663_2, Entity p_77663_3, int p_77663_4, boolean p_77663_5_) 
    {
  if (p_77663_3 instanceof EntityPlayer){
      EntityPlayer pl = (EntityPlayer) p_77663_3;
      
      if (pl.inventory.hasItem(this)){
        pl.addPotionEffect(new PotionEffect(Potion.digSpeed.id, 1, 1));
      }
  }
    }
}
     