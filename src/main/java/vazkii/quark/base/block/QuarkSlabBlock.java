package vazkii.quark.base.block;

import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import vazkii.arl.util.RegistryHelper;

public class QuarkSlabBlock extends SlabBlock {

	private final QuarkBlock parent;
	
	public QuarkSlabBlock(QuarkBlock parent) {
		super(Block.Properties.from(parent));
		
		this.parent = parent;
		RegistryHelper.registerBlock(this, parent.getRegistryName().toString() + "_slab");
		RegistryHelper.setCreativeTab(this, ItemGroup.BUILDING_BLOCKS);
	}
	
	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
		if(group == ItemGroup.SEARCH || parent.isEnabled())
			super.fillItemGroup(group, items);
	}

}
