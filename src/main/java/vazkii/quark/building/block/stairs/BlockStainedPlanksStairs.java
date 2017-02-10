package vazkii.quark.building.block.stairs;

import vazkii.quark.base.block.BlockQuarkStairs;
import vazkii.quark.building.block.BlockStainedPlanks;
import vazkii.quark.building.feature.StainedPlanks;

public class BlockStainedPlanksStairs extends BlockQuarkStairs {

	public BlockStainedPlanksStairs(BlockStainedPlanks.Variants variant) {
		super(variant.getName() + "_stairs", StainedPlanks.stained_planks.getDefaultState().withProperty(StainedPlanks.stained_planks.getVariantProp(), variant));
	}
	
}
