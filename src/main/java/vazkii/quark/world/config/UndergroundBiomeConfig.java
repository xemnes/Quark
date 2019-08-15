package vazkii.quark.world.config;

import net.minecraftforge.common.BiomeDictionary;
import vazkii.quark.base.module.Config;
import vazkii.quark.base.module.IConfigType;
import vazkii.quark.base.world.config.BiomeTypeConfig;
import vazkii.quark.base.world.config.DimensionConfig;
import vazkii.quark.world.gen.underground.UndergroundBiome;

public class UndergroundBiomeConfig implements IConfigType {

	@Config public boolean enabled = true;
	@Config public DimensionConfig dimensions = DimensionConfig.overworld(false);
	@Config public BiomeTypeConfig biomes;
	
	@Config public int rarity;
	@Config public int minYLevel = 10;
	@Config public int maxYLevel = 40;
	@Config public int horizontalSize = 26;
	@Config public int verticalSize = 14;
	@Config public int horizontalVariation = 14;
	@Config public int verticalVariation = 6;
	
	public final UndergroundBiome biomeObj;
	
	public UndergroundBiomeConfig(UndergroundBiome biomeObj, int rarity, BiomeDictionary.Type... types) {
		this.biomeObj = biomeObj;
		this.rarity = rarity;
		biomes = new BiomeTypeConfig(false, types);
	}
	
}
