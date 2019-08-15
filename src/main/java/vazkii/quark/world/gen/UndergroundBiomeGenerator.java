package vazkii.quark.world.gen;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.world.generator.MultiChunkFeatureGenerator;
import vazkii.quark.world.config.UndergroundBiomeConfig;

import java.util.*;

public class UndergroundBiomeGenerator extends MultiChunkFeatureGenerator {

	public final UndergroundBiomeConfig info;

	private final long seedXor;

	public UndergroundBiomeGenerator(UndergroundBiomeConfig info, Module module) {
		super(info.dimensions, () -> module.enabled && info.enabled);
		this.info = info;

		seedXor = info.biomeObj.getClass().toString().hashCode();
	}

	@Override
	public int getFeatureRadius() {
		return info.horizontalSize + info.horizontalVariation;
	}

	@Override
	public void generateChunkPart(BlockPos src, ChunkGenerator<? extends GenerationSettings> generator, Random random, BlockPos chunkCorner, IWorld world) {
		int radiusX = info.horizontalSize + random.nextInt(info.horizontalVariation);
		int radiusY = info.verticalSize + random.nextInt(info.verticalVariation);
		int radiusZ = info.horizontalSize + random.nextInt(info.horizontalVariation);
		
		UndergroundBiomeGenerationContext context = new UndergroundBiomeGenerationContext(world, generator, random);
		apply(context, src, chunkCorner, radiusX, radiusY, radiusZ);
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
	public long modifyWorldSeed(long seed) {
		return seed ^ seedXor;
	}

	@Override
	public boolean isSourceValid(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, BlockPos pos) {
		Biome biome = getBiome(generator, pos);
		return info.biomes.canSpawn(biome);
	}

	public void apply(UndergroundBiomeGenerationContext context, BlockPos center, BlockPos chunkCorner, int radiusX, int radiusY, int radiusZ) {
		int centerX = center.getX();
		int centerY = center.getY();
		int centerZ = center.getZ();

		double radiusX2 = radiusX * radiusX;
		double radiusY2 = radiusY * radiusY;
		double radiusZ2 = radiusZ * radiusZ;

		forEachChunkBlock(chunkCorner, centerY - radiusY, centerY + radiusY, (pos) -> {
			int x = pos.getX() - centerX;
			int y = pos.getY() - centerY;
			int z = pos.getZ() - centerZ;

			double distX = x * x;
			double distY = y * y;
			double distZ = z * z;
			double dist = distX / radiusX2 + distY / radiusY2 + distZ / radiusZ2;
			boolean inside = dist <= 1;

			if(inside)
				info.biomeObj.fill(context, pos);
		});

		context.floorList.forEach(pos -> info.biomeObj.finalFloorPass(context, pos));
		context.ceilingList.forEach(pos -> info.biomeObj.finalCeilingPass(context, pos));
		context.wallMap.keySet().forEach(pos -> info.biomeObj.finalWallPass(context, pos));
		context.insideList.forEach(pos -> info.biomeObj.finalInsidePass(context, pos));

		//		if(info.biome.hasDungeon() && world instanceof ServerWorld && random.nextDouble() < info.biome.dungeonChance) {
		//			List<BlockPos> candidates = new ArrayList<>(context.wallMap.keySet());
		//			candidates.removeIf(pos -> {
		//				BlockPos down = pos.down();
		//				BlockState state = world.getBlockState(down);
		//				return info.biome.isWall(world, down, state) || state.getBlock().isAir(state, world, down);
		//			});
		//
		//			if(!candidates.isEmpty()) {
		//				BlockPos pos = candidates.get(world.rand.nextInt(candidates.size()));
		//
		//				Direction border = context.wallMap.get(pos);
		//				if(border != null)
		//					info.biome.spawnDungeon((ServerWorld) world, pos, border);
		//			}
		//		}
	}

	public static class UndergroundBiomeGenerationContext {

		public final IWorld world;
		public final ChunkGenerator<? extends GenerationSettings> generator;
		public final Random random;
		
		public final List<BlockPos> floorList = new LinkedList<>();
		public final List<BlockPos> ceilingList = new LinkedList<>();
		public final List<BlockPos> insideList = new LinkedList<>();

		public final Map<BlockPos, Direction> wallMap = new HashMap<>();
		
		public UndergroundBiomeGenerationContext(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random random) {
			this.world = world;
			this.generator = generator;
			this.random = random;
		}
		
	}
}
