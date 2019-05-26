package vazkii.quark.experimental.features;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import vazkii.quark.base.Quark;
import vazkii.quark.base.lib.LibEntityIDs;
import vazkii.quark.base.module.Feature;
import vazkii.quark.experimental.client.render.RenderFrog;
import vazkii.quark.experimental.entity.EntityFrog;

public class WednesdayFrog extends Feature {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		String frogName = "quark:frog";
		EntityRegistry.registerModEntity(new ResourceLocation(frogName), EntityFrog.class, frogName, LibEntityIDs.FROG, Quark.instance, 80, 3, true, 0xbc9869, 0xffe6ad);
	}
	
	@Override
	public void init() {
		EntityRegistry.addSpawn(EntityFrog.class, 40, 1, 3, EnumCreatureType.CREATURE, Biomes.SWAMPLAND);
	}
	
	@Override
	public void preInitClient() {
		RenderingRegistry.registerEntityRenderingHandler(EntityFrog.class, RenderFrog.FACTORY);
	}
	
	@Override
	public boolean requiresMinecraftRestartToEnable() {
		return true;
	}
	
}
