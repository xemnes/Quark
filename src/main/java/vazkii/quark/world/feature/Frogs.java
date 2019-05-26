package vazkii.quark.world.feature;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.quark.base.Quark;
import vazkii.quark.base.lib.LibEntityIDs;
import vazkii.quark.base.module.Feature;
import vazkii.quark.world.client.render.RenderFrog;
import vazkii.quark.world.entity.EntityFrog;

public class Frogs extends Feature {

	public static boolean frogsDoTheFunny;
	public static int weight, min, max;

	@Override
	public void setupConfig() {
		frogsDoTheFunny = loadPropBool("Frogs know what day it is", "", false);

		weight = loadPropInt("Spawn Weight", "The higher, the more will spawn", 40);
		min = loadPropInt("Smallest spawn group", "", 1);
		max = loadPropInt("Largest spawn group", "", 3);
	}

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		String frogName = "quark:frog";
		EntityRegistry.registerModEntity(new ResourceLocation(frogName), EntityFrog.class, frogName, LibEntityIDs.FROG, Quark.instance, 80, 3, true, 0xbc9869, 0xffe6ad);
	}
	
	@Override
	public void init() {
		EntityRegistry.addSpawn(EntityFrog.class, weight, min, max, EnumCreatureType.CREATURE, Biomes.SWAMPLAND);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void preInitClient() {
		RenderingRegistry.registerEntityRenderingHandler(EntityFrog.class, RenderFrog.FACTORY);
	}
	
	@Override
	public boolean requiresMinecraftRestartToEnable() {
		return true;
	}
	
}
