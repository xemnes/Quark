package vazkii.quark.world.gen.underground;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import vazkii.quark.world.gen.UndergroundBiomeGenerator.UndergroundBiomeGenerationContext;

public class OvergrownUndergroundBiome extends BasicUndergroundBiome {
	
	public OvergrownUndergroundBiome() {
		super(Blocks.MOSSY_COBBLESTONE.getDefaultState(), Blocks.OAK_LEAVES.getDefaultState().with(LeavesBlock.PERSISTENT, true), null);
	}

	@Override
	public void finalCeilingPass(UndergroundBiomeGenerationContext context, BlockPos pos) {
		IWorld world = context.world;
		if(context.random.nextDouble() < 0.025) {
			int count = 0;
			for(int i = 0; i < 20; i++) {
				BlockPos checkPos = pos.add(0, -i, 0);
				if(isFloor(world, checkPos, world.getBlockState(checkPos))) {
					count = i;
					break;
				}
			}
			
			for(int i = 0; i <= count; i++) {
				BlockPos placePos = pos.add(0, -i, 0);
				world.setBlockState(placePos, Blocks.OAK_LOG.getDefaultState(), 2);
			}
			
		}
	}
	
	@Override
	public void fillFloor(UndergroundBiomeGenerationContext context, BlockPos pos, BlockState state) {
		if(context.random.nextBoolean())
			context.world.setBlockState(pos, Blocks.COARSE_DIRT.getDefaultState(), 2);
		else super.fillFloor(context, pos, state);
	}

}