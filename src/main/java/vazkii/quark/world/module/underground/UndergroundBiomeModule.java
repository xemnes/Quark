package vazkii.quark.world.module.underground;

import net.minecraft.world.gen.GenerationStage.Decoration;
import vazkii.quark.base.module.Config;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.world.WorldGenHandler;
import vazkii.quark.base.world.WorldGenWeights;
import vazkii.quark.world.config.UndergroundBiomeConfig;
import vazkii.quark.world.gen.UndergroundBiomeGenerator;

public abstract class UndergroundBiomeModule extends Module {

	@Config public UndergroundBiomeConfig biomeSettings;
	
	@Override
	public void start() {
		biomeSettings = getBiomeConfig();
	}
	
	@Override
	public void setup() {
		WorldGenHandler.addGenerator(new UndergroundBiomeGenerator(biomeSettings, this), Decoration.UNDERGROUND_DECORATION, WorldGenWeights.UNDERGROUND_BIOMES);
	}
	
	protected abstract UndergroundBiomeConfig getBiomeConfig();
	
}
