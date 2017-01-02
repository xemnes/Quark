package vazkii.quark.world.block.stairs;

import vazkii.quark.base.block.BlockQuarkStairs;
import vazkii.quark.world.feature.RevampStoneGen;

public class BlockMarbleStairs extends BlockQuarkStairs {

	public BlockMarbleStairs() {
		super("stone_marble_stairs", RevampStoneGen.marble.getDefaultState());
	}

}
