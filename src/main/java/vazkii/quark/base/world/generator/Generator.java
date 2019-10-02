package vazkii.quark.base.world.generator;

import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.GenerationStage;
import vazkii.quark.base.world.config.DimensionConfig;

import java.util.Random;
import java.util.function.BooleanSupplier;

public abstract class Generator implements IGenerator {
	
	public final DimensionConfig dimConfig;
	private final BooleanSupplier condition;
	
	public Generator(DimensionConfig dimConfig, BooleanSupplier condition) {
		this.dimConfig = dimConfig;
		this.condition = condition;
	}

	@Override
	public int generate(int seedIncrement, long seed, GenerationStage.Decoration stage, IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, SharedSeedRandom rand, BlockPos pos) {
		rand.setFeatureSeed(seed, seedIncrement, stage.ordinal());
		generate(worldIn, generator, rand, pos);
		return seedIncrement + 1;
	}

	public abstract void generate(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos);

	@Override
	public boolean canGenerate(IWorld world) {
		return condition.getAsBoolean() && dimConfig.canSpawnHere(world.getWorld());
	}
	
}
