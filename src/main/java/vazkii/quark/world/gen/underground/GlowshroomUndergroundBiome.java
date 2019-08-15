package vazkii.quark.world.gen.underground;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import vazkii.quark.world.gen.UndergroundBiomeGenerator.UndergroundBiomeGenerationContext;
import vazkii.quark.world.module.underground.GlowshroomUndergroundBiomeModule;

public class GlowshroomUndergroundBiome extends BasicUndergroundBiome {

	public static double mushroomChance;

	public GlowshroomUndergroundBiome() {
		super(GlowshroomUndergroundBiomeModule.glowcelium.getDefaultState(), Blocks.DIRT.getDefaultState(), Blocks.DIRT.getDefaultState());
	}

	@Override
	public void fillFloor(UndergroundBiomeGenerationContext context, BlockPos pos, BlockState state) {
		if(context.random.nextDouble() < 0.0625)
			context.world.setBlockState(pos.up(), GlowshroomUndergroundBiomeModule.glowshroom.getDefaultState(), 2);
	}

}
