package vazkii.quark.world.feature;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import vazkii.quark.base.Quark;
import vazkii.quark.base.handler.DimensionConfig;
import vazkii.quark.base.lib.LibEntityIDs;
import vazkii.quark.base.module.Feature;
import vazkii.quark.world.client.render.RenderArcheologist;
import vazkii.quark.world.entity.EntityArcheologist;
import vazkii.quark.world.item.ItemArcheologistHat;
import vazkii.quark.world.world.ArcheologistHouseGenerator;

public class Archeologist extends Feature {

	public static final ResourceLocation HOUSE_STRUCTURE = new ResourceLocation("quark", "archeologist_house");

	public static int chance, maxY, minY;
	public static DimensionConfig dims;
	
	public static Item archeologist_hat;

	@Override
	public void setupConfig() {
		chance = loadPropInt("Chance Per Chunk", "The chance (1/N) that the generator will attempt to place an Archeologist per chunk. More = less spawns", 5);
		maxY = loadPropInt("Max Y", "", 50);
		minY = loadPropInt("Min Y", "", 20);
		dims = new DimensionConfig(configCategory);
	}

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		archeologist_hat = new ItemArcheologistHat();
		
		String archeologistName = "quark:archeologist";
		EntityRegistry.registerModEntity(new ResourceLocation(archeologistName), EntityArcheologist.class, archeologistName, LibEntityIDs.ARCHEOLOGIST, Quark.instance, 80, 3, true, 0xb5966e, 0xb37b62);

		GameRegistry.registerWorldGenerator(new ArcheologistHouseGenerator(), 3000);
	}


	@Override
	public void preInitClient(FMLPreInitializationEvent event) {
		RenderingRegistry.registerEntityRenderingHandler(EntityArcheologist.class, RenderArcheologist.FACTORY);
	}

	@Override
	public boolean requiresMinecraftRestartToEnable() {
		return true;
	}
	
}
