package vazkii.quark.world.world.underground;

import net.minecraft.block.BlockPrismarine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.quark.base.module.ModuleLoader;

public class UndergroundBiomePrismarine extends BasicUndergroundBiome {

	int seaLanternChance;
	
	public UndergroundBiomePrismarine() {
		super(Blocks.PRISMARINE.getDefaultState(), Blocks.PRISMARINE.getDefaultState(), Blocks.PRISMARINE.getDefaultState());
	}
	
	@Override
	public void fillWall(World world, BlockPos pos, IBlockState state) {
		super.fillWall(world, pos, state);
		
		if(seaLanternChance > 0 && world.rand.nextInt(seaLanternChance) == 0)
			world.setBlockState(pos, Blocks.SEA_LANTERN.getDefaultState(), 2);
	}
	
	@Override
	public void setupConfig(String category) {
		seaLanternChance = ModuleLoader.config.getInt("Sea Lantern Chance", category, 20, 0, Integer.MAX_VALUE, "The higher, the less sea lanterns will spawn");
	}
	
	
}
