package vazkii.quark.oddities.inventory;

import java.util.List;
import java.util.Random;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.quark.oddities.tile.TileMatrixEnchanter;

import javax.annotation.Nonnull;

public class ContainerMatrixEnchanting extends Container {

	private final List<ItemStack> lapisTypes = OreDictionary.getOres("gemLapis");

	public TileMatrixEnchanter enchanter;
	Random rand;

	public ContainerMatrixEnchanting(InventoryPlayer playerInv, TileMatrixEnchanter tile) {
		rand = new Random();
		enchanter = tile;

		// Item Slot
		addSlotToContainer(new Slot(tile, 0, 15, 20) {
			@Override 
			public int getSlotStackLimit() { 
				return 1; 
			}
		});

		// Lapis Slot
		addSlotToContainer(new Slot(tile, 1, 15, 44) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return isLapis(stack);
			}
		});

		// Output Slot
		addSlotToContainer(new Slot(tile, 2, 59, 32) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return false;
			}

			@Nonnull
			@Override
			public ItemStack onTake(EntityPlayer thePlayer, @Nonnull ItemStack stack) {
				finish(thePlayer, stack);
				return super.onTake(thePlayer, stack);
			}
		});

		// Player Inv
		for(int i = 0; i < 3; ++i)
			for(int j = 0; j < 9; ++j)
				addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
		for(int k = 0; k < 9; ++k)
			addSlotToContainer(new Slot(playerInv, k, 8 + k * 18, 142));
	}

	private boolean isLapis(ItemStack stack) {
		for(ItemStack ore : lapisTypes)
			if(OreDictionary.itemMatches(ore, stack, false)) 
				return true;

		return false;
	}

	private void finish(EntityPlayer player, ItemStack stack) {
		enchanter.setInventorySlotContents(0, ItemStack.EMPTY);

		player.addStat(StatList.ITEM_ENCHANTED);

		if(player instanceof EntityPlayerMP)
			CriteriaTriggers.ENCHANTED_ITEM.trigger((EntityPlayerMP) player, stack, 1);

		player.world.playSound(null, enchanter.getPos(), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0F,  player.world.rand.nextFloat() * 0.1F + 0.9F);
	}

	@Override
	public boolean canInteractWith(@Nonnull EntityPlayer playerIn) {
		World world = enchanter.getWorld();
		BlockPos pos = enchanter.getPos();
		if(world.getBlockState(pos).getBlock() != Blocks.ENCHANTING_TABLE)
			return false;
		else
			return playerIn.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64.0D;
	}

	@Nonnull
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if(index < 3) {
				if (!mergeItemStack(itemstack1, 3, 39, true))
					return ItemStack.EMPTY;
			}
			else if(isLapis(itemstack1)) {
				if(!mergeItemStack(itemstack1, 1, 2, true))
					return ItemStack.EMPTY;
			}
			else {
				if(inventorySlots.get(0).getHasStack() || !inventorySlots.get(0).isItemValid(itemstack1))
					return ItemStack.EMPTY;

				if(itemstack1.hasTagCompound()) // Forge: Fix MC-17431
					inventorySlots.get(0).putStack(itemstack1.splitStack(1));

				else if(!itemstack1.isEmpty()) {
					inventorySlots.get(0).putStack(new ItemStack(itemstack1.getItem(), 1, itemstack1.getMetadata()));
					itemstack1.shrink(1);
				}
			}

			if(itemstack1.isEmpty())
				slot.putStack(ItemStack.EMPTY);
			else slot.onSlotChanged();

			if(itemstack1.getCount() == itemstack.getCount())
				return ItemStack.EMPTY;

			slot.onTake(playerIn, itemstack1);
		}

		return itemstack;
	}

}
