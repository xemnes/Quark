package vazkii.quark.world.world.underground;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BasicUndergroundBiome extends UndergroundBiome {

	IBlockState floorState, ceilingState, wallState;
	
	public BasicUndergroundBiome(IBlockState floorState, IBlockState ceilingState, IBlockState wallState) {
		this.floorState = floorState;
		this.ceilingState = ceilingState;
		this.wallState = wallState;
	}
	
	@Override
	public void fillFloor(World world, BlockPos pos, IBlockState state) {
		if(floorState != null)
			world.setBlockState(pos, floorState, 2);
	}

	@Override
	public void fillCeiling(World world, BlockPos pos, IBlockState state) {	
		if(ceilingState != null)
			world.setBlockState(pos, ceilingState, 2);
	}

	@Override
	public void fillWall(World world, BlockPos pos, IBlockState state) {
		if(wallState != null)
			world.setBlockState(pos, wallState, 2);
	}

}
