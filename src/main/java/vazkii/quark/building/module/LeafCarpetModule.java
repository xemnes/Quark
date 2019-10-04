package vazkii.quark.building.module;

import com.google.common.collect.ImmutableSet;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.building.block.LeafCarpetBlock;

@LoadModule(category = ModuleCategory.BUILDING)
public class LeafCarpetModule extends Module {

	@Override
	public void construct() {
		ImmutableSet.of(Blocks.OAK_LEAVES, Blocks.SPRUCE_LEAVES, Blocks.BIRCH_LEAVES, Blocks.JUNGLE_LEAVES, Blocks.ACACIA_LEAVES, Blocks.DARK_OAK_LEAVES).forEach(this::carpet);
	}
	
	private void carpet(Block base) {
		new LeafCarpetBlock(base.getRegistryName().getPath().replaceAll("_leaves", ""), base, this);
	}
	
}
