package vazkii.quark.world.config;

import net.minecraftforge.common.BiomeDictionary;
import vazkii.quark.base.module.Config;
import vazkii.quark.base.world.config.BiomeTypeConfig;
import vazkii.quark.base.world.config.ClusterSizeConfig;
import vazkii.quark.base.world.config.DimensionConfig;

public class BigStoneClusterConfig extends ClusterSizeConfig {

	@Config
	public boolean enabled = true;

	public BigStoneClusterConfig(boolean nether, BiomeDictionary.Type... types) {
		this(nether, 14, 9, 4, 20, 80, types);
	}

	public BigStoneClusterConfig(boolean nether, int clusterSize, int sizeVariation, int rarity, int minYLevel, int maxYLevel, BiomeDictionary.Type... types) {
		super(rarity, clusterSize, clusterSize, sizeVariation, sizeVariation, false, types);
		dimensions = nether ? DimensionConfig.nether(false) : DimensionConfig.overworld(false);
		biomes = new BiomeTypeConfig(false, types);
		
		this.minYLevel = minYLevel;
		this.maxYLevel = maxYLevel;
	}

}
