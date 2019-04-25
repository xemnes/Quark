package vazkii.quark.world.feature;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import vazkii.quark.base.handler.DimensionConfig;
import vazkii.quark.base.module.Feature;
import vazkii.quark.world.world.ArcheologistHouseGenerator;

public class Archeologist extends Feature {

	public static final ResourceLocation HOUSE_STRUCTURE = new ResourceLocation("quark", "archeologist_house");

	public static int chance, maxY, minY;
	public static DimensionConfig dims;
	
	@Override
	public void setupConfig() {
		chance = loadPropInt("Chance Per Chunk", "The chance (1/N) that the generator will attempt to place an Archeologist per chunk. More = less spawns", 5);
		maxY = loadPropInt("Max Y", "", 50);
		minY = loadPropInt("Min Y", "", 20);
		dims = new DimensionConfig(configCategory);
	}
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		GameRegistry.registerWorldGenerator(new ArcheologistHouseGenerator(), 3000);
	}
	
}
