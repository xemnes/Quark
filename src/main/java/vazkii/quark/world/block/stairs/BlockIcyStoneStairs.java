package vazkii.quark.world.block.stairs;

import vazkii.quark.base.block.BlockQuarkStairs;
import vazkii.quark.world.feature.UndergroundBiomes;

public class BlockIcyStoneStairs extends BlockQuarkStairs {

	@SuppressWarnings("deprecation")
	public BlockIcyStoneStairs() {
		super("icy_stone_stairs", UndergroundBiomes.biome_cobblestone.getStateFromMeta(1));
	}
	
}
