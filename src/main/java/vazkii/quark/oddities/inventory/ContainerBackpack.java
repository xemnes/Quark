package vazkii.quark.oddities.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import vazkii.arl.util.InventoryIIH;
import vazkii.quark.oddities.feature.Backpacks;

import javax.annotation.Nonnull;

public class ContainerBackpack extends ContainerPlayer {

	public ContainerBackpack(EntityPlayer player) {
		super(player.inventory, !player.world.isRemote, player);
		
		for(Slot slot : inventorySlots) {
			if(slot.inventory == player.inventory && slot.getSlotIndex() < player.inventory.getSizeInventory() - 5)
				slot.yPos += 58;
		}
		
		Slot anchor = inventorySlots.get(9);
		int left = anchor.xPos;
		int top = anchor.yPos - 58;
		
		ItemStack backpack = player.inventory.armorInventory.get(2);
		if(backpack.getItem() == Backpacks.backpack) {
			InventoryIIH inv = new InventoryIIH(backpack);
			
			for(int i = 0; i < 3; ++i)
				for(int j = 0; j < 9; ++j) {
					int k = j + i * 9;
					addSlotToContainer(new SlotItemHandler(inv, k, left + j * 18, top + i * 18));
				}
		}
	}
	
	@Nonnull
	@Override
	public ItemStack transferStackInSlot(@Nonnull EntityPlayer playerIn, int index) {
		Slot slot = inventorySlots.get(index);

		if(index >= 9 && index < 36 && slot != null && slot.getHasStack()) {
			ItemStack stack = slot.getStack();
			ItemStack origStack = stack.copy();
			if (!mergeItemStack(stack, 46, 72, false))
				return ItemStack.EMPTY;

			if (stack.isEmpty()) slot.putStack(ItemStack.EMPTY);
			else slot.onSlotChanged();

			if (origStack.getCount() == stack.getCount()) return ItemStack.EMPTY;

			slot.onTake(playerIn, stack);
		}
		
		return super.transferStackInSlot(playerIn, index);
	}

	// Shamelessly stolen from CoFHCore because KL is awesome
	// and was like yeah just take whatever you want lol
	// https://github.com/CoFH/CoFHCore/blob/d4a79b078d257e88414f5eed598d57490ec8e97f/src/main/java/cofh/core/util/helpers/InventoryHelper.java
	@Override
	public boolean mergeItemStack(ItemStack stack, int start, int length, boolean r) {
		boolean successful = false;
		int i = !r ? start : length - 1;
		int iterOrder = !r ? 1 : -1;

		Slot slot;
		ItemStack existingStack;

		if(stack.isStackable()) {
			while(stack.getCount() > 0 && (!r && i < length || r && i >= start)) {
				slot = inventorySlots.get(i);

				existingStack = slot.getStack();

				if(!existingStack.isEmpty()) {
					int maxStack = Math.min(stack.getMaxStackSize(), slot.getSlotStackLimit());
					int rmv = Math.min(maxStack, stack.getCount());

					if(slot.isItemValid(cloneStack(stack, rmv)) && existingStack.getItem().equals(stack.getItem()) && (!stack.getHasSubtypes() || stack.getItemDamage() == existingStack.getItemDamage()) && ItemStack.areItemStackTagsEqual(stack, existingStack)) {
						int existingSize = existingStack.getCount() + stack.getCount();

						if(existingSize <= maxStack) {
							stack.setCount(0);
							existingStack.setCount(existingSize);
							slot.putStack(existingStack);
							successful = true;
						} else if(existingStack.getCount() < maxStack) {
							stack.shrink(maxStack - existingStack.getCount());
							existingStack.setCount(maxStack);
							slot.putStack(existingStack);
							successful = true;
						}
					}
				}
				i += iterOrder;
			}
		}
		if(stack.getCount() > 0) {
			i = !r ? start : length - 1;
			while(stack.getCount() > 0 && (!r && i < length || r && i >= start)) {
				slot = inventorySlots.get(i);
				existingStack = slot.getStack();

				if(existingStack.isEmpty()) {
					int maxStack = Math.min(stack.getMaxStackSize(), slot.getSlotStackLimit());
					int rmv = Math.min(maxStack, stack.getCount());

					if(slot.isItemValid(cloneStack(stack, rmv))) {
						existingStack = stack.splitStack(rmv);
						slot.putStack(existingStack);
						successful = true;
					}
				}
				i += iterOrder;
			}
		}
		return successful;
	}

	private static ItemStack cloneStack(ItemStack stack, int size) {
		if(stack.isEmpty())
			return ItemStack.EMPTY;

		ItemStack copy = stack.copy();
		copy.setCount(size);
		return copy;
	}
	
	public static void saveCraftingInventory(EntityPlayer player) {
		InventoryCrafting crafting = ((ContainerPlayer) player.openContainer).craftMatrix;
		for(int i = 0; i < crafting.getSizeInventory(); i++) {
			ItemStack stack = crafting.getStackInSlot(i);
			if(!stack.isEmpty() && !player.addItemStackToInventory(stack))
				player.dropItem(stack, false);
		}
	}

}
