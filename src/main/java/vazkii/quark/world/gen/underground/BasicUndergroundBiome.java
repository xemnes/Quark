package vazkii.quark.world.gen.underground;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import vazkii.quark.world.gen.UndergroundBiomeGenerator.UndergroundBiomeGenerationContext;

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
	public void fillFloor(UndergroundBiomeGenerationContext context, BlockPos pos, BlockState state) {
		if(floorState != null)
			context.world.setBlockState(pos, floorState, 2);
	}

	@Override
	public void fillCeiling(UndergroundBiomeGenerationContext context, BlockPos pos, BlockState state) {	
		if(ceilingState != null)
			context.world.setBlockState(pos, ceilingState, 2);
	}

	@Override
	public void fillWall(UndergroundBiomeGenerationContext context, BlockPos pos, BlockState state) {
		if(wallState != null)
			context.world.setBlockState(pos, wallState, 2);
	}

	@Override
	public void fillInside(UndergroundBiomeGenerationContext context, BlockPos pos, BlockState state) {
		if(mimicInside)
			fillWall(context, pos, state);
	} 

}
