package vazkii.quark.base.world;

import java.util.Random;
import java.util.function.Supplier;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import vazkii.quark.base.world.config.DimensionConfig;

public abstract class Generator {
	
	public final DimensionConfig dimConfig;
	private final Supplier<Boolean> condition;
	
	public Generator(DimensionConfig dimConfig, Supplier<Boolean> condition) {
		this.dimConfig = dimConfig;
		this.condition = condition;
	}

	public abstract void generate(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos);
	
	public boolean isEnabled() {
		return condition.get();
	}
	
}
