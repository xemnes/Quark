package vazkii.quark.building.block;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.LadderBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import vazkii.arl.util.RegistryHelper;
import vazkii.quark.base.module.Module;

public class VariantLadderBlock extends LadderBlock {

	private final Module module;
	
	public VariantLadderBlock(String type, Module module, Block.Properties props) {
		super(props);
		
		RegistryHelper.registerBlock(this, type + "_ladder");
		RegistryHelper.setCreativeTab(this, ItemGroup.DECORATIONS);
		
		this.module = module;
	}
	
	public VariantLadderBlock(String type, Module module) {
		this(type, module, Block.Properties.from(Blocks.LADDER));
	}
	
	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
		if(isEnabled() || group == ItemGroup.SEARCH)
			super.fillItemGroup(group, items);
	}
	
	public boolean isEnabled() {
		return module.enabled;
	}

}
