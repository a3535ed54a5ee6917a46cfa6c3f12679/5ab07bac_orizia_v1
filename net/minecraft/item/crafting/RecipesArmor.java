package net.minecraft.item.crafting;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RecipesArmor
{
    private String[][] recipePatterns = new String[][] {{"XXX", "X X"}, {"X X", "XXX", "XXX"}, {"XXX", "X X", "X X"}, {"X X", "X X"}};
    private Object[][] recipeItems;
    private static final String __OBFID = "CL_00000080";

    public RecipesArmor()
    {
    	this.recipeItems = new Object[][] {{Items.leather, Blocks.fire, Items.iron_ingot, Items.diamond, Items.gold_ingot, Items.pyrite, Items.zironite, Items.uranium, Items.orizium},
        	{Items.leather_helmet, Items.chainmail_helmet, Items.iron_helmet, Items.diamond_helmet, Items.golden_helmet, Items.pyrite_helmet, Items.zironite_helmet, Items.uranium_helmet, Items.orizium_helmet},
        	{Items.leather_chestplate, Items.chainmail_chestplate, Items.iron_chestplate, Items.diamond_chestplate, Items.golden_chestplate, Items.pyrite_chestplate, Items.zironite_chestplate, Items.uranium_chestplate, Items.orizium_chestplate},
        	{Items.leather_leggings, Items.chainmail_leggings, Items.iron_leggings, Items.diamond_leggings, Items.golden_leggings, Items.pyrite_leggings, Items.zironite_leggings, Items.uranium_leggings, Items.orizium_leggings},
        	{Items.leather_boots, Items.chainmail_boots, Items.iron_boots, Items.diamond_boots, Items.golden_boots, Items.pyrite_boots, Items.zironite_boots, Items.uranium_boots, Items.orizium_boots }};    }

    /**
     * Adds the armor recipes to the CraftingManager.
     */
    public void addRecipes(CraftingManager p_77609_1_)
    {
        for (int var2 = 0; var2 < this.recipeItems[0].length; ++var2)
        {
            Object var3 = this.recipeItems[0][var2];

            for (int var4 = 0; var4 < this.recipeItems.length - 1; ++var4)
            {
                Item var5 = (Item)this.recipeItems[var4 + 1][var2];
                p_77609_1_.addRecipe(new ItemStack(var5), new Object[] {this.recipePatterns[var4], 'X', var3});
            }
        }
    }
}
