package vazkii.quark.base.world;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraftforge.registries.ForgeRegistries;

public class WorldGenHandler {

	private static Map<GenerationStage.Decoration, SortedSet<WeightedGenerator>> generators = new HashMap<>();

	public static void loadComplete() {
		for(GenerationStage.Decoration stage : GenerationStage.Decoration.values()) {
			ConfiguredFeature<?> feature = Biome.createDecoratedFeature(new DeferedFeature(stage), IFeatureConfig.NO_FEATURE_CONFIG, new ChunkCornerPlacement(), IPlacementConfig.NO_PLACEMENT_CONFIG);
			ForgeRegistries.BIOMES.forEach(biome -> biome.addFeature(stage, feature));
		}
	}
	
	public static void addGenerator(Generator generator, GenerationStage.Decoration stage, int weight) {
		WeightedGenerator weighted = new WeightedGenerator(generator, weight);
		if(!generators.containsKey(stage))
			generators.put(stage, new TreeSet<>());
		
		generators.get(stage).add(weighted);
	}

	public static void generateChunk(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, BlockPos pos, GenerationStage.Decoration stage) {
		if(!(worldIn instanceof WorldGenRegion))
			return;

		WorldGenRegion region = (WorldGenRegion) worldIn;
		SharedSeedRandom random = new SharedSeedRandom();
		long seed = random.setDecorationSeed(region.getSeed(), region.getMainChunkX() * 16, region.getMainChunkZ() * 16);
		int i = 0;

		if(generators.containsKey(stage)) {
			SortedSet<WeightedGenerator> set = generators.get(stage);

			for(WeightedGenerator wgen : set) {
				Generator gen = wgen.generator;
				if(gen.isEnabled() && gen.dimConfig.canSpawnHere(worldIn.getWorld())) {
					random.setFeatureSeed(seed, i, stage.ordinal()); 

					gen.generate(worldIn, generator, random, pos);
					i++;
				}
			}
		}
	}

}
