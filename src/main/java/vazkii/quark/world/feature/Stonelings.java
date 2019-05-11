package vazkii.quark.world.feature;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import vazkii.quark.base.Quark;
import vazkii.quark.base.handler.DimensionConfig;
import vazkii.quark.base.lib.LibEntityIDs;
import vazkii.quark.base.module.Feature;
import vazkii.quark.world.client.render.RenderStoneling;
import vazkii.quark.world.entity.EntityAshen;
import vazkii.quark.world.entity.EntityStoneling;

public class Stonelings extends Feature {

	public static int maxYLevel, weight;
	public static DimensionConfig dimensions;
	
	@Override
	public void setupConfig() {
		maxYLevel = loadPropInt("Max Y Level", "", 24);
		weight = loadPropInt("Spawning Weight", "Higher = more stonelings", 70);
		
		dimensions = new DimensionConfig(configCategory);
	}
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		String stonelingName = "quark:stoneling";
		EntityRegistry.registerModEntity(new ResourceLocation(stonelingName), EntityStoneling.class, stonelingName, LibEntityIDs.STONELING, Quark.instance, 80, 3, true, 0xA1A1A1, 0x505050);
		
		EntityRegistry.addSpawn(EntityStoneling.class, weight, 1, 1, EnumCreatureType.MONSTER, DepthMobs.getBiomesWithMob(EntityZombie.class));
	}
	
	@Override
	public void preInitClient(FMLPreInitializationEvent event) {
		RenderingRegistry.registerEntityRenderingHandler(EntityStoneling.class, RenderStoneling.FACTORY);
	}

}
