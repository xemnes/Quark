package vazkii.quark.base.world;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class DeferedFeature extends Feature<NoFeatureConfig> {

	private final GenerationStage.Decoration stage;

	public DeferedFeature(GenerationStage.Decoration stage) {
		super(a -> IFeatureConfig.NO_FEATURE_CONFIG);
		this.stage = stage;
	}

	@Override
	public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config) {
		WorldGenHandler.generateChunk(worldIn, generator, pos, stage);
		return true;
	}

}
