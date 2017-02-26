package vazkii.quark.world.world.underground;

import net.minecraft.block.BlockSandStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.quark.base.module.Feature;
import vazkii.quark.base.module.ModuleLoader;

public class UndergroundBiomeSandstone extends BasicUndergroundBiome {

	int stalactiteChance, chiseledSandstoneChance, deadBushChance;
	boolean enableSand;
	
	public UndergroundBiomeSandstone() {
		super(Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState());
	}
	
	@Override
	public void fillCeiling(World world, BlockPos pos, IBlockState state) {
		if(world.rand.nextInt(stalactiteChance) == 0)
			world.setBlockState(pos.down(), ceilingState, 2);
		
		super.fillCeiling(world, pos, state);
	}
	
	@Override
	public void fillFloor(World world, BlockPos pos, IBlockState state) {
		if(enableSand && world.rand.nextBoolean()) {
			world.setBlockState(pos, Blocks.SAND.getDefaultState(), 2);
			if(world.rand.nextInt(deadBushChance) == 0)
				world.setBlockState(pos.up(), Blocks.DEADBUSH.getDefaultState(), 2);
		} else super.fillFloor(world, pos, state);
	}
	
	@Override
	public void fillWall(World world, BlockPos pos, IBlockState state) {
		if(world.rand.nextInt(chiseledSandstoneChance) == 0)
			world.setBlockState(pos, wallState.withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.CHISELED), 2);
		else super.fillWall(world, pos, state);
	}
	
	@Override
	public void setupConfig(String category) {
		stalactiteChance = ModuleLoader.config.getInt("Stalactite Chance", category, 10, 0, Integer.MAX_VALUE, "The higher, the less stalactites will spawn");
		chiseledSandstoneChance = ModuleLoader.config.getInt("Chiseled Sandstone Chance", category, 10, 0, Integer.MAX_VALUE, "The higher, the less chiseled sandstone will spawn");
		deadBushChance = ModuleLoader.config.getInt("Dead Bush Chance", category, 20, 0, Integer.MAX_VALUE, "The higher, the less dead bushes will spawn");
		enableSand = ModuleLoader.config.getBoolean("Enable Sand Floors", category, true, "");
	}
	
}
