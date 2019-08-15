package vazkii.quark.world.gen.underground;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import vazkii.quark.world.gen.UndergroundBiomeGenerator.UndergroundBiomeGenerationContext;

public class SlimeUndergroundBiome extends BasicUndergroundBiome {
	
	public SlimeUndergroundBiome() {
		super(Blocks.WATER.getDefaultState(), null, null);
	}
	
	@Override
	public void fillCeiling(UndergroundBiomeGenerationContext context, BlockPos pos, BlockState state) {
		BlockState setState = Blocks.GREEN_TERRACOTTA.getDefaultState();
		switch(context.random.nextInt(7)) {
		case 3: case 4: case 5:
			setState = Blocks.LIME_TERRACOTTA.getDefaultState();
			break;
		case 6:
			setState = Blocks.LIGHT_BLUE_TERRACOTTA.getDefaultState();
		}
		
		context.world.setBlockState(pos, setState, 2);
	}
	
	@Override
	public void fillWall(UndergroundBiomeGenerationContext context, BlockPos pos, BlockState state) {
		fillCeiling(context, pos, state);
	}

	@Override
	public void fillFloor(UndergroundBiomeGenerationContext context, BlockPos pos, BlockState state) {
		context.world.setBlockState(pos, floorState, 3);
		
		if(context.random.nextDouble() < 0.085)
			context.world.setBlockState(pos, Blocks.SLIME_BLOCK.getDefaultState(), 2);
	}
	

}
