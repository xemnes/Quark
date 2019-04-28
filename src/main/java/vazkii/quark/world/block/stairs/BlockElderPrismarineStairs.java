package vazkii.quark.world.block.stairs;

import vazkii.quark.base.block.BlockQuarkStairs;
import vazkii.quark.world.block.BlockElderPrismarine;
import vazkii.quark.world.feature.UndergroundBiomes;

public class BlockElderPrismarineStairs extends BlockQuarkStairs {

	@SuppressWarnings("unchecked")
	public BlockElderPrismarineStairs(BlockElderPrismarine.Variants variant) {
		super(variant.getName() + "_stairs", UndergroundBiomes.elder_prismarine.getDefaultState()
				.withProperty(UndergroundBiomes.elder_prismarine.getVariantProp(), variant));
	}

}
