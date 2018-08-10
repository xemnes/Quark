package vazkii.quark.world.feature;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import vazkii.quark.base.handler.DimensionConfig;
import vazkii.quark.base.module.Feature;
import vazkii.quark.world.world.FairyRingGenerator;

public class FairyRings extends Feature {

	public static int forestChance, plainsChance;
	public static DimensionConfig dimensions;
	
	@Override
	public void setupConfig() {
		forestChance = loadPropInt("Forest Chance", "", 40);
		plainsChance = loadPropInt("Plains Chance", "", 100);
		dimensions = new DimensionConfig(configCategory, "0");
	}
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		GameRegistry.registerWorldGenerator(new FairyRingGenerator(), 100);
	}
	
}
