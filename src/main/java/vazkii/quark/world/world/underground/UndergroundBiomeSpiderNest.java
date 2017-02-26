package vazkii.quark.world.world.underground;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.quark.base.module.ModuleLoader;

public class UndergroundBiomeSpiderNest extends BasicUndergroundBiome {

	int floorCobwebChance, ceilingCobwebChance;
	
	public UndergroundBiomeSpiderNest() {
		super(Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState());
	}

	@Override
	public void fillCeiling(World world, BlockPos pos, IBlockState state) {
		super.fillCeiling(world, pos, state);
		placeCobweb(world, pos, EnumFacing.DOWN, ceilingCobwebChance);
	}
	
	@Override
	public void fillFloor(World world, BlockPos pos, IBlockState state) {
		super.fillFloor(world, pos, state);
		placeCobweb(world, pos, EnumFacing.UP, floorCobwebChance);
	}
	
	private void placeCobweb(World world, BlockPos pos, EnumFacing off, int chance) {
		if(chance > 0 && world.rand.nextInt(chance) == 0) {
			BlockPos placePos = pos.offset(off);
			world.setBlockState(placePos, Blocks.WEB.getDefaultState());
		}
	}
	
	@Override
	public void setupConfig(String category) {
		floorCobwebChance = ModuleLoader.config.getInt("Floor Cobweb Chance", category, 30, 0, Integer.MAX_VALUE, "The higher, the less floor cobwebs will spawn");
		ceilingCobwebChance = ModuleLoader.config.getInt("Ceiling Cobweb Chance", category, 10, 0, Integer.MAX_VALUE, "The higher, the less ceiling cobwebs will spawn");
	}

}

