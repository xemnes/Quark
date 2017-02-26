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
import vazkii.quark.world.world.underground.UndergroundBiome;
import vazkii.quark.world.world.underground.UndergroundBiomeLush;
import vazkii.quark.world.world.underground.UndergroundBiomeSandstone;

public class UndergroundBiomes extends Feature {

	public List<UndergroundBiomeInfo> biomes;
	
	@Override
	public void setupConfig() {
		biomes = new ArrayList();
		biomes.add(loadUndergrondBiomeInfo("Lush", new UndergroundBiomeLush(), 80, Type.JUNGLE));
		biomes.add(loadUndergrondBiomeInfo("Sandstone", new UndergroundBiomeSandstone(), 80, Type.SANDY));
	}
	
	@SubscribeEvent
	public void onOreGenerate(OreGenEvent.GenerateMinable event) {
		if(event.getType() == EventType.DIRT) {
			World world = event.getWorld();
			BlockPos pos = event.getPos();
			Random rand = event.getRand();
			
			for(UndergroundBiomeInfo biomeInfo : biomes) {
				BlockPos spawnPos = pos.add(rand.nextInt(16), biomeInfo.minY + rand.nextInt(biomeInfo.maxY - biomeInfo.minY), rand.nextInt(16));
				Biome biome = world.getBiome(spawnPos);
				
				if(BiomeTypeConfigHandler.biomeTypeIntersectCheck(biomeInfo.types, biome) && rand.nextInt(biomeInfo.rarity) == 0) {
					int radiusX = biomeInfo.minXSize + rand.nextInt(biomeInfo.xVariation);
					int radiusY = biomeInfo.minYSize + rand.nextInt(biomeInfo.yVariation);
					int radiusZ = biomeInfo.minZSize + rand.nextInt(biomeInfo.zVariation);
					
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
		int minXSize, minYSize, minZSize;
		int xVariation, yVariation, zVariation;
		int minY, maxY;
		
		private UndergroundBiomeInfo(String category, UndergroundBiome biome, int rarity, BiomeDictionary.Type... biomes) {
			this.enabled = ModuleLoader.config.getBoolean("Enabled", category, true, "");
			this.biome = biome;
			this.types = BiomeTypeConfigHandler.parseBiomeTypeArrayConfig("Allowed Biome Types", category, biomes);
			this.rarity = ModuleLoader.config.getInt("Rarity", category, rarity, 0, Integer.MAX_VALUE, "This biome will spawn in 1 of X valid chunks");
			
			minY = ModuleLoader.config.getInt("Minimum Y Level", category, 10, 0, 255, "");
			maxY = ModuleLoader.config.getInt("Maximum Y Level", category, 40, 0, 255, "");
			
			String sizeCategory = category + ".size";
			
			minXSize = ModuleLoader.config.getInt("X Minimum", sizeCategory, 26, 0, Integer.MAX_VALUE, "");
			minYSize = ModuleLoader.config.getInt("Y Minimum", sizeCategory, 12, 0, Integer.MAX_VALUE, "");
			minZSize = ModuleLoader.config.getInt("Z Minimum", sizeCategory, 26, 0, Integer.MAX_VALUE, "");
			
			xVariation = ModuleLoader.config.getInt("X Variation", sizeCategory, 14, 0, Integer.MAX_VALUE, "");
			yVariation = ModuleLoader.config.getInt("Y Variation", sizeCategory, 6, 0, Integer.MAX_VALUE, "");
			zVariation = ModuleLoader.config.getInt("Z Variation", sizeCategory, 14, 0, Integer.MAX_VALUE, "");
			
			biome.setupConfig(category + ".specific");
		}

		
	}
	
}

