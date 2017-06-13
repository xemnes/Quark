/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 *
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 *
 * File Created @ [21/03/2016, 00:05:19 (GMT)]
 */
package vazkii.quark.vanity.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import vazkii.arl.util.ItemNBTHelper;
import vazkii.quark.vanity.feature.DyableElytra;

public class ElytraDyingRecipe implements IRecipe {

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
				} else if(stack.getItem() instanceof ItemDye) {
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
		int source = -1;
		ItemStack target = ItemStack.EMPTY;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(!stack.isEmpty()) {
				if(stack.getItem() instanceof ItemDye)
					source = stack.getItemDamage();
				else target = stack;
			}
		}

		if(!target.isEmpty()) {
			ItemStack copy = target.copy();
			ItemNBTHelper.setInt(copy, DyableElytra.TAG_ELYTRA_DYE, source);
			return copy;
		}

		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return ItemStack.EMPTY;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
		return ForgeHooks.defaultRecipeGetRemainingItems(inv);
	}

	@Override
	public boolean func_194133_a(int p_194133_1_, int p_194133_2_) {
		return false;
	}

}
