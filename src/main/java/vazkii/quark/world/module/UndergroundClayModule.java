package vazkii.quark.world.module;

import net.minecraft.block.Blocks;
import net.minecraft.world.gen.GenerationStage.Decoration;
import vazkii.quark.base.moduleloader.Config;
import vazkii.quark.base.moduleloader.LoadModule;
import vazkii.quark.base.moduleloader.Module;
import vazkii.quark.base.moduleloader.ModuleCategory;
import vazkii.quark.base.world.WorldGenHandler;
import vazkii.quark.base.world.WorldGenWeights;
import vazkii.quark.base.world.config.DimensionConfig;
import vazkii.quark.base.world.config.OrePocketConfig;
import vazkii.quark.base.world.generator.OreGenerator;

@LoadModule(category = ModuleCategory.WORLD)
public class UndergroundClayModule extends Module {

	@Config public static DimensionConfig dimensions = DimensionConfig.overworld(false);
	@Config public static OrePocketConfig oreSettings = new OrePocketConfig(20, 60, 20, 3);
	
	@Override
	public void setup() {
		WorldGenHandler.addGenerator(new OreGenerator(dimensions, oreSettings, Blocks.CLAY.getDefaultState(), OreGenerator.STONE_MATCHER, () -> enabled), Decoration.UNDERGROUND_ORES, WorldGenWeights.CLAY);
	}
	
}
