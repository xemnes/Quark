package vazkii.quark.world.module;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.GameData;
import vazkii.arl.util.RegistryHelper;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.experimental.debug.FloodFillItem;
import vazkii.quark.world.gen.structure.BigDungeonStructure;

@LoadModule(category = ModuleCategory.WORLD)
public class BigDungeonModule extends Module {

	public static Structure<NoFeatureConfig> structure;

	@Override
	public void construct() {
		new FloodFillItem(this); // TODO remove

		structure = new BigDungeonStructure();
		RegistryHelper.register(structure);
	}

	@Override
	public void setup() {
		if(enabled)
			for(Biome b : ForgeRegistries.BIOMES.getValues()) {
				b.addStructure(structure, NoFeatureConfig.NO_FEATURE_CONFIG);
				b.addFeature(Decoration.UNDERGROUND_STRUCTURES, Biome.createDecoratedFeature(structure, IFeatureConfig.NO_FEATURE_CONFIG, Placement.NOPE, IPlacementConfig.NO_PLACEMENT_CONFIG));
			}
	}

	@Override
	public void loadComplete() {
		GameData.getStructureFeatures().keySet().forEach(rl -> System.out.println("Structure: " + rl));
	}

}
