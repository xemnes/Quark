package vazkii.quark.world.module;

import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.common.BiomeDictionary.Type;
import vazkii.arl.util.RegistryHelper;
import vazkii.quark.base.Quark;
import vazkii.quark.base.module.Config;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.base.world.config.BiomeTypeConfig;
import vazkii.quark.world.gen.structure.BigDungeonStructure;

@LoadModule(category = ModuleCategory.WORLD)
public class BigDungeonModule extends Module {

	@Config(description = "The chance that a big dungeon spawn candidate will be allowed to spawn. 0.2 is 20%, which is the same as the Pillager Outpost.")
	public static double spawnChance = 0.1;

	@Config
	public static String lootTable = "minecraft:chests/simple_dungeon";

	@Config 
	public static int maxRooms = 10;

	@Config
	public static double chestChance = 0.5;

	@Config
	public static BiomeTypeConfig biomeTypes = new BiomeTypeConfig(true, Type.OCEAN, Type.BEACH, Type.NETHER, Type.END);

	public static final BigDungeonStructure STRUCTURE = new BigDungeonStructure(NoFeatureConfig.field_236558_a_);
	
	@Override
	public void construct() {
		//		new FloodFillItem(this);
		RegistryHelper.register(STRUCTURE);
		
		Structure.field_236365_a_.put(Quark.MOD_ID + ":big_dungeon", STRUCTURE);
	}

	@Override
	@SuppressWarnings({ "rawtypes" })
	public void setup() {
		STRUCTURE.setup();	
		
//		if(enabled) TODO re-enable after I figure out StructureSeparationSettings
//			for(Biome b : ForgeRegistries.BIOMES.getValues()) { 
//				StructureFeature structure = STRUCTURE.func_236391_a_(NoFeatureConfig.NO_FEATURE_CONFIG);
//
//				if(biomeTypes.canSpawn(b))
//					b.func_235063_a_(structure);
//			}
	}

}
