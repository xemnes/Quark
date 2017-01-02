package vazkii.quark.building.block;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import vazkii.arl.block.BlockMetaVariants;
import vazkii.quark.base.block.IQuarkBlock;

public class BlockSoulSandstone extends BlockMetaVariants implements IQuarkBlock {

	public BlockSoulSandstone() {
		super("soul_sandstone", Material.ROCK, Variants.class);
		setHardness(1F);
		setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
	}

	public enum Variants implements EnumBase {
		SOUL_SANDSTONE,
		CHISELED_SOUL_SANDSTONE,
		SMOOTH_SOUL_SANDSTONE
	}
	
}
