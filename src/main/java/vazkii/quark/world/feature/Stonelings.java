package vazkii.quark.world.feature;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import vazkii.quark.base.Quark;
import vazkii.quark.base.lib.LibEntityIDs;
import vazkii.quark.base.module.Feature;
import vazkii.quark.world.client.render.RenderStoneling;
import vazkii.quark.world.entity.EntityStoneling;
import vazkii.quark.world.entity.EntityWraith;

public class Stonelings extends Feature {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		String stonelingName = "quark:stoneling";
		EntityRegistry.registerModEntity(new ResourceLocation(stonelingName), EntityStoneling.class, stonelingName, LibEntityIDs.STONELING, Quark.instance, 80, 3, true, 0xA1A1A1, 0x505050);
	}
	
	@Override
	public void preInitClient(FMLPreInitializationEvent event) {
		RenderingRegistry.registerEntityRenderingHandler(EntityStoneling.class, RenderStoneling.FACTORY);
	}
	
}
