package vazkii.quark.building.block.stairs;

import vazkii.quark.base.block.BlockQuarkStairs;
import vazkii.quark.building.block.BlockPolishedNetherrack;
import vazkii.quark.building.feature.PolishedNetherrack;

public class BlockPolishedNetherrackBricksStairs extends BlockQuarkStairs {

	public BlockPolishedNetherrackBricksStairs() {
		super("polished_netherrack_bricks_stairs", PolishedNetherrack.polished_netherrack.getDefaultState().withProperty(PolishedNetherrack.polished_netherrack.getVariantProp(), BlockPolishedNetherrack.Variants.POLISHED_NETHERRACK_BRICKS));
	}

}
