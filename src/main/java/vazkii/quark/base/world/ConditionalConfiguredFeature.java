package vazkii.quark.base.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.IFeatureConfig;

import java.util.Random;
import java.util.function.BooleanSupplier;

public class ConditionalConfiguredFeature<FC extends IFeatureConfig> extends ConfiguredFeature<FC> {

	public final ConfiguredFeature<FC> parent;
	public final BooleanSupplier condition;
	
	public ConditionalConfiguredFeature(ConfiguredFeature<FC> parent, BooleanSupplier condition) {
		super(parent.feature, parent.config);
		this.parent = parent;
		this.condition = condition;
	}
	
	@Override
	public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos) {
		return condition.getAsBoolean() && super.place(worldIn, generator, rand, pos);
	}
	
}
