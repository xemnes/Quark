package vazkii.quark.misc.recipe;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import vazkii.arl.recipe.ModRecipe;
import vazkii.quark.misc.feature.EnderdragonScales;

public class ElytraDuplicationRecipe extends ModRecipe {

	public ElytraDuplicationRecipe() {
		super(new ResourceLocation("quark", "elytra_duplication"));
	}
	
	@Override
	public boolean matches(InventoryCrafting var1, World var2) {
		boolean foundSource = false;
		boolean foundTarget = false;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(!stack.isEmpty()) {
				if(stack.getItem() instanceof ItemElytra) {
					if(foundTarget)
						return false;
					foundTarget = true;
				} else if(stack.getItem() == EnderdragonScales.enderdragonScale) {
					if(foundSource)
						return false;
					foundSource = true;
				} else return false;
			}
		}

		return foundSource && foundTarget;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting var1) {
		return getRecipeOutput();
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(Items.ELYTRA);
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
		NonNullList<ItemStack> ret = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
		
		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(stack.getItem() == Items.ELYTRA)
				ret.set(i, stack.copy());
		}
		
		return ret;
	}

	@Override
	public boolean canFit(int x, int y) {
		return true;
	}
	
	public NonNullList<Ingredient> getIngredients() {
		NonNullList<Ingredient> list = NonNullList.withSize(2, Ingredient.EMPTY);
		list.set(0, Ingredient.fromStacks(new ItemStack(Items.ELYTRA)));
		list.set(1, Ingredient.fromStacks(new ItemStack(EnderdragonScales.enderdragonScale)));
		return list;
	}
	
}
