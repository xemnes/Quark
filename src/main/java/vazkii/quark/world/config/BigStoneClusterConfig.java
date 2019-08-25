package vazkii.quark.world.config;

import net.minecraftforge.common.BiomeDictionary;
import vazkii.quark.base.module.Config;
import vazkii.quark.base.module.IConfigType;
import vazkii.quark.base.world.config.BiomeTypeConfig;
import vazkii.quark.base.world.config.DimensionConfig;

public class BigStoneClusterConfig implements IConfigType {

	@Config public boolean enabled = true;
	@Config public int clusterSize = 14;
	@Config public int clusterRarity = 9;
	@Config public int minYLevel = 80;
	@Config public int maxYLevel = 20;
	
	@Config public DimensionConfig dimensions;
	@Config public BiomeTypeConfig biomes;
	
	public BigStoneClusterConfig(boolean nether, BiomeDictionary.Type... types) {
		dimensions = nether ? DimensionConfig.overworld(false) : DimensionConfig.nether(false);
		biomes = new BiomeTypeConfig(false, types);
	}
	
}
