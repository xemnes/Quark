package vazkii.quark.world.config;

import net.minecraftforge.common.BiomeDictionary;
import vazkii.quark.base.module.Config;
import vazkii.quark.base.module.IConfigType;
import vazkii.quark.base.world.config.BiomeTypeConfig;
import vazkii.quark.base.world.config.DimensionConfig;

public class BigStoneClusterConfig implements IConfigType {

	@Config
	public boolean enabled = true;

	@Config
	@Config.Min(0)
	public int clusterSize;

	@Config
	@Config.Min(0)
	public int clusterRarity;

	@Config
	@Config.Min(0)
	@Config.Max(255)
	public int minYLevel;

	@Config
	@Config.Min(0)
	@Config.Max(255)
	public int maxYLevel;

	@Config
	public DimensionConfig dimensions;
	@Config
	public BiomeTypeConfig biomes;

	public BigStoneClusterConfig(boolean nether, BiomeDictionary.Type... types) {
		this(nether, 14, 9, 20, 80, types);
	}

	public BigStoneClusterConfig(boolean nether, int clusterSize, int clusterRarity, int minYLevel, int maxYLevel, BiomeDictionary.Type... types) {
		dimensions = nether ? DimensionConfig.nether(false) : DimensionConfig.overworld(false);
		biomes = new BiomeTypeConfig(false, types);
		this.clusterSize = clusterSize;
		this.clusterRarity = clusterRarity;
		this.minYLevel = minYLevel;
		this.maxYLevel = maxYLevel;
	}


}
