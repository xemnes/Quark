package vazkii.quark.world.module.underground;

import net.minecraftforge.common.BiomeDictionary.Type;
import vazkii.quark.base.moduleloader.LoadModule;
import vazkii.quark.base.moduleloader.ModuleCategory;
import vazkii.quark.world.config.UndergroundBiomeConfig;
import vazkii.quark.world.gen.underground.LushUndergroundBiome;

@LoadModule(category = ModuleCategory.WORLD)
public class LushUndergroundBiomeModule extends UndergroundBiomeModule {

	@Override
	protected UndergroundBiomeConfig getBiomeConfig() {
		return new UndergroundBiomeConfig(new LushUndergroundBiome(), 80, Type.JUNGLE);
	}

}
