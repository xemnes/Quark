package vazkii.quark.oddities.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import vazkii.arl.util.InventoryIIH;
import vazkii.quark.oddities.feature.Backpacks;

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
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);

        if(index >= 9 && index < 36 && slot != null && slot.getHasStack()) {
			ItemStack stack = slot.getStack();
			if(!mergeItemStack(stack, 46, 72, false))
                return ItemStack.EMPTY;
		}
		
		return super.transferStackInSlot(playerIn, index);
	}

}
