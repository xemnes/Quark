package vazkii.quark.experimental.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import vazkii.arl.block.tile.TileSimpleInventory;

public class TileFramed extends TileSimpleInventory {

	private static final IBlockState AIR = Blocks.AIR.getDefaultState();
	
	// TODO: unsafe, temporary
	public IBlockState getState() {
		ItemStack stack = getStackInSlot(0);
		if(stack.isEmpty() || !(stack.getItem() instanceof ItemBlock))
			return AIR;
		
		ItemBlock block = (ItemBlock) stack.getItem();
		return block.getBlock().getStateFromMeta(stack.getItemDamage()); // <--- bad
	}
	
	@Override
	public int getSizeInventory() {
		return 1;
	}
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return itemstack.isEmpty() && itemstack.getItem() instanceof ItemBlock;
	}
	
	@Override
	public boolean isAutomationEnabled() {
		return true;
	}

}
