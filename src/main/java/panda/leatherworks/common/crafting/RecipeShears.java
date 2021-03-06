package panda.leatherworks.common.crafting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class RecipeShears implements IRecipe{


	private final ItemStack recipeOutput;
    public final List<ItemStack> recipeItems;

    public RecipeShears(ItemStack output, List<ItemStack> inputList)
    {
        this.recipeOutput = output;
        this.recipeItems = inputList;
    }

    @Nullable
    public ItemStack getRecipeOutput()
    {
        return this.recipeOutput;
    }

    

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    public boolean matches(InventoryCrafting inv, World worldIn)
    {
        List<ItemStack> list = Lists.newArrayList(this.recipeItems);

        for (int i = 0; i < inv.getHeight(); ++i)
        {
            for (int j = 0; j < inv.getWidth(); ++j)
            {
                ItemStack itemstack = inv.getStackInRowAndColumn(j, i);

                if (itemstack != null)
                {
                    boolean flag = false;

                    for (ItemStack itemstack1 : list)
                    {
                        if (itemstack.getItem() == itemstack1.getItem() && (itemstack1.getMetadata() == 32767 || itemstack.getMetadata() == itemstack1.getMetadata()))
                        {
                            flag = true;
                            list.remove(itemstack1);
                            break;
                        }
                    }

                    if (!flag)
                    {
                        return false;
                    }
                }
            }
        }

        return list.isEmpty();
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    @Nullable
    public ItemStack getCraftingResult(InventoryCrafting inv)
    {
        return this.recipeOutput.copy();
    }

    /**
     * Returns the size of the recipe area
     */
    public int getRecipeSize()
    {
        return this.recipeItems.size();
    }

	@Override
	public ItemStack[] getRemainingItems(InventoryCrafting inv) {
		
		ItemStack[] aitemstack = new ItemStack[inv.getSizeInventory()];

        for (int i = 0; i < aitemstack.length; ++i)
        {
            ItemStack itemstack = inv.getStackInSlot(i);
            if(itemstack != null){
            	if(itemstack.getItem() == Items.SHEARS){
                	ItemStack shearscopy = itemstack.copy();
					//TODO Fix this, it will crash
                	if(shearscopy.attemptDamageItem(1, Minecraft.getMinecraft().theWorld.rand)){
                		ForgeEventFactory.onPlayerDestroyItem(ForgeHooks.getCraftingPlayer(), itemstack, null);
                		aitemstack[i] = null;
                	}else{
                		aitemstack[i] = shearscopy;
                	}
            }
            }else{
            	aitemstack[i] = ForgeHooks.getContainerItem(itemstack);
            } 
        }

        return aitemstack;
	}
	
	
}
