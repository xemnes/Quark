package vazkii.quark.world.world.underground;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenShrub;
import vazkii.quark.base.module.ModuleLoader;

public class UndergroundBiomeLush extends BasicUndergroundBiome {

	WorldGenShrub shrubGen = new WorldGenShrub(Blocks.LOG.getDefaultState(), Blocks.LEAVES.getDefaultState());
	
	int grassChance, shrubChance;
	
	public UndergroundBiomeLush() {
		super(Blocks.GRASS.getDefaultState(), null, null);
	}
	
	@Override
	public void finalFloorPass(World world, BlockPos pos) {
		if(world.rand.nextInt(grassChance) == 0)
			ItemDye.applyBonemeal(new ItemStack(Items.DYE, 1, 14), world, pos);
		
		if(world.rand.nextInt(shrubChance) == 0)
			shrubGen.generate(world, world.rand, pos.up());
	}
	
	@Override
	public void setupConfig(String category) {
		grassChance = ModuleLoader.config.getInt("Grass Chance", category, 20, 0, Integer.MAX_VALUE, "The higher, the less grass will spawn");
		shrubChance = ModuleLoader.config.getInt("Shrub Chance", category, 20, 0, Integer.MAX_VALUE, "The higher, the less shrubs will spawn");
	}

}
