package vazkii.quark.world.feature;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.quark.base.handler.BiomeTypeConfigHandler;
import vazkii.quark.base.module.Feature;
import vazkii.quark.base.module.ModuleLoader;
import vazkii.quark.world.feature.RevampStoneGen.StoneInfo;
import vazkii.quark.world.world.underground.UndergroundBiome;
import vazkii.quark.world.world.underground.UndergroundBiomeLush;

public class UndergroundBiomes extends Feature {

	public List<UndergroundBiomeInfo> biomes;
	
	int minXSize, minYSize, minZSize;
	int xVariation, yVariation, zVariation;
	
	@Override
	public void setupConfig() {
		biomes = new ArrayList();
		biomes.add(loadUndergrondBiomeInfo("Lush", new UndergroundBiomeLush(), 50, Type.JUNGLE));
		
		minXSize = loadPropInt("Min X Size", "", 26);
		minYSize = loadPropInt("Min Y Size", "", 12);
		minZSize = loadPropInt("Min Z Size", "", 26);
		
		xVariation = loadPropInt("X Axis Variation", "", 14);
		yVariation = loadPropInt("Y Axis Variation", "", 6);
		zVariation = loadPropInt("Z Axis Variation", "", 14);
	}
	
	@SubscribeEvent
	public void onOreGenerate(OreGenEvent.GenerateMinable event) {
		if(event.getType() == EventType.DIRT) {
			World world = event.getWorld();
			BlockPos pos = event.getPos();
			Random rand = event.getRand();
			
			BlockPos spawnPos = pos.add(rand.nextInt(16), rand.nextInt(world.getSeaLevel()), rand.nextInt(16));
			Biome biome = world.getBiome(spawnPos);

			for(UndergroundBiomeInfo biomeInfo : biomes) {
				if(BiomeTypeConfigHandler.biomeTypeIntersectCheck(biomeInfo.types, biome) && rand.nextInt(biomeInfo.rarity) == 0) {
					int radiusX = minXSize + rand.nextInt(xVariation);
					int radiusY = minYSize + rand.nextInt(yVariation);
					int radiusZ = minZSize + rand.nextInt(zVariation);
					
					System.out.println("SPAWNING AT " + spawnPos);
					biomeInfo.biome.apply(world, spawnPos, radiusX, radiusY, radiusZ);
					return;
				}
			}
		}
	}
	
	@Override
	public boolean hasOreGenSubscriptions() {
		return true;
	}
	
	@Override
	public boolean requiresMinecraftRestartToEnable() {
		return true;
	}
	
	private UndergroundBiomeInfo loadUndergrondBiomeInfo(String name, UndergroundBiome biome, int rarity, BiomeDictionary.Type... biomes) {
		String category = configCategory + "." + name;
		UndergroundBiomeInfo info = new UndergroundBiomeInfo(category, biome, rarity, biomes);

		return info;
	}
	
	public static class UndergroundBiomeInfo {
		
		public final boolean enabled;
		public final UndergroundBiome biome;
		public final List<BiomeDictionary.Type> types;
		public final int rarity;
		
		private UndergroundBiomeInfo(String category, UndergroundBiome biome, int rarity, BiomeDictionary.Type... biomes) {
			this.enabled = ModuleLoader.config.getBoolean("Enabled", category, true, "");
			this.biome = biome;
			this.types = BiomeTypeConfigHandler.parseBiomeTypeArrayConfig("Allowed Biomes Types", category, biomes);
			this.rarity = ModuleLoader.config.getInt("Rarity", category, rarity, 0, Integer.MAX_VALUE, "This biome will spawn in 1 of X valid chunks");
		}

		
	}
	
}

