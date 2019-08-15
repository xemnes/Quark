package vazkii.quark.world.gen.underground;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import vazkii.quark.world.gen.UndergroundBiomeGenerator.UndergroundBiomeGenerationContext;
import vazkii.quark.world.module.underground.ElderPrismarineUndergroundBiomeModule;

public class ElderPrismarineUndergroundBiome extends BasicUndergroundBiome {

	private BlockState lanternState;
	
	public ElderPrismarineUndergroundBiome() {
		super(ElderPrismarineUndergroundBiomeModule.elder_prismarine.getDefaultState(), ElderPrismarineUndergroundBiomeModule.elder_prismarine.getDefaultState(), ElderPrismarineUndergroundBiomeModule.elder_prismarine.getDefaultState());
		lanternState = ElderPrismarineUndergroundBiomeModule.elder_sea_lantern.getDefaultState();
	}
	
	@Override
	public void fillWall(UndergroundBiomeGenerationContext context, BlockPos pos, BlockState state) {
		super.fillWall(context, pos, state);
		
		if(context.random.nextDouble() < 0.0085)
			context.world.setBlockState(pos, lanternState, 2);
	}
	
	@Override
	public void fillFloor(UndergroundBiomeGenerationContext context, BlockPos pos, BlockState state) {
		if(context.random.nextDouble() < 0.25 && !isBorder(context.world, pos))
			context.world.setBlockState(pos, Blocks.WATER.getDefaultState(), 2);
		else super.fillFloor(context, pos, state);
	}

}
