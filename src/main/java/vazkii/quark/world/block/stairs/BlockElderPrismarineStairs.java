package vazkii.quark.world.block.stairs;

import vazkii.quark.base.block.BlockQuarkStairs;
import vazkii.quark.world.block.BlockElderPrismarine;
import vazkii.quark.world.feature.RevampStoneGen;

public class BlockElderPrismarineStairs extends BlockQuarkStairs {

	public BlockElderPrismarineStairs(BlockElderPrismarine.Variants variant) {
		super(variant.getName() + "_stairs", RevampStoneGen.marble.getDefaultState());
	}

}
