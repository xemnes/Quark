package vazkii.quark.world.block.stairs;

import vazkii.quark.base.block.BlockQuarkStairs;
import vazkii.quark.world.feature.RevampStoneGen;

public class BlockLimestoneStairs extends BlockQuarkStairs {

	public BlockLimestoneStairs() {
		super("stone_limestone_stairs", RevampStoneGen.limestone.getDefaultState());
	}

}
