package vazkii.quark.base.world.generator;

import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Supplier;

import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import vazkii.quark.base.handler.ReflectionKeys;
import vazkii.quark.base.world.Generator;
import vazkii.quark.base.world.config.DimensionConfig;

public abstract class MultiChunkFeatureGenerator extends Generator {

	public MultiChunkFeatureGenerator(DimensionConfig dimConfig, Supplier<Boolean> condition) {
		super(dimConfig, condition);
	}

	@Override
	public final void generate(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, SharedSeedRandom rand, BlockPos pos) {
		int radius = getFeatureRadius();
		int chunkRadius = (int) Math.ceil((double) radius / 16.0);
		
		long worldSeed = modifyWorldSeed(world.getSeed());
		Random worldRandom = new Random(worldSeed);
		long xSeed = worldRandom.nextLong();
		long zSeed = worldRandom.nextLong();
		
		int chunkX = pos.getX() >> 4;
		int chunkZ = pos.getZ() >> 4;
		
		long chunkSeed = (xSeed * chunkX + zSeed * chunkZ) ^ worldSeed;
		Random ourRandom = new Random(chunkSeed);
		
		for(int x = chunkX - chunkRadius; x <= chunkX + chunkRadius; x++)
			for(int z = chunkZ - chunkRadius; z <= chunkZ + chunkRadius; z++) {
				chunkSeed = (xSeed * x + zSeed * z) ^ worldSeed;
				Random chunkRandom = new Random(chunkSeed);
				BlockPos chunkCorner = new BlockPos(x << 4, 0, z << 4);

				BlockPos[] sources = getSourcesInChunk(chunkRandom, generator, chunkCorner);
				for(BlockPos source : sources)
					if(source != null && isSourceValid(world, generator, source))
						generateChunkPart(source, generator, ourRandom, pos, world);
			}
	}
	
	public long modifyWorldSeed(long seed) {
		return seed;
	}
	
	public boolean isSourceValid(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, BlockPos pos) {
		return true;
	}
	
	public abstract int getFeatureRadius();
	
	public abstract void generateChunkPart(BlockPos src, ChunkGenerator<? extends GenerationSettings> generator, Random random, BlockPos chunkCorner, IWorld world);
	
	public abstract BlockPos[] getSourcesInChunk(Random random, ChunkGenerator<? extends GenerationSettings> generator, BlockPos chunkLeft);
	
	public void forEachChunkBlock(BlockPos chunkCorner, int minY, int maxY, Consumer<BlockPos> func) {
		minY = Math.max(1, minY);
		maxY = Math.min(255, maxY);

		MutableBlockPos mutable = new MutableBlockPos(chunkCorner);
		for(int x = 0; x < 16; x++)
			for(int y = minY; y < maxY; y++)
				for(int z = 0; z < 16; z++) {
					mutable.setPos(chunkCorner.getX() + x, chunkCorner.getY() + y, chunkCorner.getZ() + z);
					func.accept(mutable);
				}
	}
	
	public boolean isInsideChunk(BlockPos pos, int chunkX, int chunkZ) {
		int x = chunkX * 16;
		int z = chunkZ * 16;
		return pos.getX() > x && pos.getZ() > z && pos.getX() < (x + 16) && pos.getZ() < (z + 16); 
	}
	
	public Biome getBiome(ChunkGenerator<? extends GenerationSettings> generator, BlockPos pos) {
		BiomeProvider provider = ObfuscationReflectionHelper.getPrivateValue(ChunkGenerator.class, generator, ReflectionKeys.ChunkGenerator.BIOME_PROVIDER);
		return provider.getBiome(pos);
	}

}
