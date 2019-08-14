package vazkii.quark.world.gen.underground;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

public class BasicUndergroundBiome extends UndergroundBiome {

	public BlockState floorState, ceilingState, wallState;
	public final boolean mimicInside;
	
	public BasicUndergroundBiome(BlockState floorState, BlockState ceilingState, BlockState wallState) {
		this(floorState, ceilingState, wallState, false);
	}
	
	public BasicUndergroundBiome(BlockState floorState, BlockState ceilingState, BlockState wallState, boolean mimicInside) {
		this.floorState = floorState;
		this.ceilingState = ceilingState;
		this.wallState = wallState;
		this.mimicInside = mimicInside;
	}
	
	@Override
	public void fillFloor(IWorld world, BlockPos pos, BlockState state, Random rand) {
		if(floorState != null)
			world.setBlockState(pos, floorState, 2);
	}

	@Override
	public void fillCeiling(IWorld world, BlockPos pos, BlockState state, Random rand) {	
		if(ceilingState != null)
			world.setBlockState(pos, ceilingState, 2);
	}

	@Override
	public void fillWall(IWorld world, BlockPos pos, BlockState state, Random rand) {
		if(wallState != null)
			world.setBlockState(pos, wallState, 2);
	}

	@Override
	public void fillInside(IWorld world, BlockPos pos, BlockState state, Random rand) {
		if(mimicInside)
			fillWall(world, pos, state, rand);
	} 

}
