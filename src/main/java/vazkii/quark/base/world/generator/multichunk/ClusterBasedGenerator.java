package vazkii.quark.base.world.generator.multichunk;

import java.util.Random;
import java.util.function.BooleanSupplier;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import vazkii.quark.base.world.config.ClusterSizeConfig;
import vazkii.quark.base.world.config.DimensionConfig;

public abstract class ClusterBasedGenerator extends MultiChunkFeatureGenerator {

	public final ClusterShape.Provider shapeProvider;
	
	public ClusterBasedGenerator(DimensionConfig dimConfig, BooleanSupplier condition, ClusterSizeConfig sizeConfig, long seedXor) {
		super(dimConfig, condition, seedXor);
		this.shapeProvider = new ClusterShape.Provider(sizeConfig, seedXor);
	}

	@Override
	public int getFeatureRadius() {
		return shapeProvider.getRadius();
	}

	@Override
	public void generateChunkPart(BlockPos src, ChunkGenerator<? extends GenerationSettings> generator, Random random, BlockPos chunkCorner, IWorld world) {
		final ClusterShape shape = shapeProvider.around(src);
		final IGenerationContext context = createContext(src, generator, random, chunkCorner, world);
		
		forEachChunkBlock(chunkCorner, shape.getLowerBound(), shape.getUpperBound(), (pos) -> {
			if(shape.isInside(pos))
				context.consume(world, pos);
		});
		context.finish(world);
	}
	
	public abstract IGenerationContext createContext(BlockPos src, ChunkGenerator<? extends GenerationSettings> generator, Random random, BlockPos chunkCorner, IWorld world);
	
	public static abstract interface IGenerationContext {
		public void consume(IWorld world, BlockPos pos);
		public void finish(IWorld world);
	}

}
