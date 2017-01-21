/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 *
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 *
 * File Created @ [21/03/2016, 01:05:19 (GMT)]
 */
package vazkii.quark.vanity.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemFirework;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class FireworkCloningRecipe implements IRecipe {

	@Override
	public boolean matches(InventoryCrafting var1, World var2) {
		boolean foundSource = false;
		boolean foundTarget = false;
		ItemStack source = ItemStack.EMPTY;
		ItemStack target = ItemStack.EMPTY;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(!stack.isEmpty()) {
				if(stack.getItem() instanceof ItemFirework) {
					if(stack.getTagCompound() != null && hasExplosions(stack)) {
						if(foundSource)
							return false;
						source = stack;
						foundSource = true;
					} else {
						if(foundTarget)
							return false;
						
						target = stack;
						foundTarget = true;
					}
				}  else return false;
			}
		}
		
		return foundSource && foundTarget && getFlight(source) == getFlight(target);
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting var1) {
		ItemStack source = ItemStack.EMPTY;
		ItemStack target = ItemStack.EMPTY;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(!stack.isEmpty()) {
				if(hasExplosions(stack))
					source = stack;
				else target = stack;
			}
		}
		
		if(!source.isEmpty() && !target.isEmpty()) {
			ItemStack copy = target.copy();
			NBTTagCompound cmp = new NBTTagCompound();
			cmp.setTag("Fireworks", source.getTagCompound().getTag("Fireworks"));
			copy.setTagCompound(cmp);
			copy.setCount(1);

			return copy;
		}

		return ItemStack.EMPTY;
	}

	@Override
	public int getRecipeSize() {
		return 10;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return ItemStack.EMPTY;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
		NonNullList<ItemStack> remaining = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(!stack.isEmpty() && hasExplosions(stack)) {
				ItemStack copy = stack.copy();
				copy.setCount(1);
				remaining.set(i, copy);
			}
		}

		return remaining;
	}
	

	private byte getFlight(ItemStack stack) {
		if(!stack.hasTagCompound())
			return 0;
		
		return stack.getTagCompound().getCompoundTag("Fireworks").getByte("Flight");
	}
	
	private boolean hasExplosions(ItemStack stack) {
		return stack.hasTagCompound() && stack.getTagCompound().hasKey("Fireworks") && stack.getTagCompound().getCompoundTag("Fireworks").hasKey("Explosions");
	}
	

}
