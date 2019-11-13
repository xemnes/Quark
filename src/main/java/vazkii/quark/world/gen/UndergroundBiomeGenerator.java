package vazkii.quark.world.gen;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.world.generator.multichunk.ClusterBasedGenerator;
import vazkii.quark.base.world.generator.multichunk.MultiChunkFeatureGenerator;
import vazkii.quark.world.config.UndergroundBiomeConfig;

import java.util.*;

public class UndergroundBiomeGenerator extends ClusterBasedGenerator {

	public final UndergroundBiomeConfig info;

	public UndergroundBiomeGenerator(UndergroundBiomeConfig info, String name) {
		super(info.dimensions, info, name.hashCode());
		this.info = info;
	}

	@Override
	public int getFeatureRadius() {
		return info.horizontalSize + info.horizontalVariation;
	}

	@Override
	public BlockPos[] getSourcesInChunk(Random random, ChunkGenerator<? extends GenerationSettings> generator, BlockPos chunkCorner) {
		if(info.rarity > 0 && random.nextInt(info.rarity) == 0) {
			return new BlockPos[] {
					chunkCorner.add(random.nextInt(16), info.minYLevel + random.nextInt(info.maxYLevel - info.minYLevel), random.nextInt(16))
			};
		}

		return new BlockPos[0];
	}
	
	@Override
	public IGenerationContext createContext(BlockPos src, ChunkGenerator<? extends GenerationSettings> generator, Random random, BlockPos chunkCorner, IWorld world) {
		return new Context(world, src, generator, random, info);
	}

	@Override
	public boolean isSourceValid(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, BlockPos pos) {
		Biome biome = getBiome(generator, pos);
		return info.biomes.canSpawn(biome);
	}
	
	@Override
	public String toString() {
		return "UndergroundBiomeGenerator[" + info.biomeObj + "]";
	}

	public static class Context implements IGenerationContext {

		public final IWorld world;
		public final BlockPos source;
		public final ChunkGenerator<? extends GenerationSettings> generator;
		public final Random random;
		public final UndergroundBiomeConfig info;

		public final List<BlockPos> floorList = new LinkedList<>();
		public final List<BlockPos> ceilingList = new LinkedList<>();
		public final List<BlockPos> insideList = new LinkedList<>();

		public final Map<BlockPos, Direction> wallMap = new HashMap<>();
		
		public Context(IWorld world, BlockPos source, ChunkGenerator<? extends GenerationSettings> generator, Random random, UndergroundBiomeConfig info) {
			this.world = world;
			this.source = source;
			this.generator = generator;
			this.random = random;
			this.info = info;
		}

		@Override
		public void consume(IWorld world, BlockPos pos) {
			info.biomeObj.fill(this, pos);			
		}

		@Override
		public void finish(IWorld world) {
			floorList.forEach(pos -> info.biomeObj.finalFloorPass(this, pos));
			ceilingList.forEach(pos -> info.biomeObj.finalCeilingPass(this, pos));
			wallMap.keySet().forEach(pos -> info.biomeObj.finalWallPass(this, pos));
			insideList.forEach(pos -> info.biomeObj.finalInsidePass(this, pos));			
		}
		
	}
}
