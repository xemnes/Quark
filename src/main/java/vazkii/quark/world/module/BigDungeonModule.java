package vazkii.quark.world.module;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.arl.util.RegistryHelper;
import vazkii.quark.base.module.Config;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.experimental.debug.FloodFillItem;
import vazkii.quark.world.gen.structure.BigDungeonStructure;

@LoadModule(category = ModuleCategory.WORLD)
public class BigDungeonModule extends Module {

	public static Structure<NoFeatureConfig> structure;

	@Config(description = "The chance that a big dungeon spawn candidate will be allowed to spawn. 0.2 is 20%, which is the same as the Pillager Outpost.")
	public static double spawnChance = 0.2;
	
	@Config
	public static String lootTable = "minecraft:chests/simple_dungeon";

	@Config 
	public static int maxRooms = 14;
	
	@Config
	public static double chestChance = 0.5;
	
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

}
