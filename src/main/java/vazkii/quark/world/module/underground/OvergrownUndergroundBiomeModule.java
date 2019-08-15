package vazkii.quark.world.module.underground;

import net.minecraftforge.common.BiomeDictionary.Type;
import vazkii.quark.base.moduleloader.LoadModule;
import vazkii.quark.base.moduleloader.ModuleCategory;
import vazkii.quark.world.config.UndergroundBiomeConfig;
import vazkii.quark.world.gen.underground.OvergrownUndergroundBiome;

@LoadModule(category = ModuleCategory.WORLD)
public class OvergrownUndergroundBiomeModule extends UndergroundBiomeModule {

	@Override
	protected UndergroundBiomeConfig getBiomeConfig() {
		return new UndergroundBiomeConfig(new OvergrownUndergroundBiome(), 80, Type.FOREST);
	}

}
