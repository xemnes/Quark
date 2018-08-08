package vazkii.quark.misc.feature;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.quark.base.Quark;
import vazkii.quark.base.lib.LibEntityIDs;
import vazkii.quark.base.module.Feature;
import vazkii.quark.misc.client.render.RenderParrotEgg;
import vazkii.quark.misc.entity.EntityParrotEgg;
import vazkii.quark.misc.item.ItemParrotEgg;

public class ParrotEggs extends Feature {

	public static Item parrot_egg;
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		parrot_egg = new ItemParrotEgg();
		
		String eggName = "quark:parrot_egg";
		EntityRegistry.registerModEntity(new ResourceLocation(eggName), EntityParrotEgg.class, eggName, LibEntityIDs.PARROT_EGG, Quark.instance, 64, 10, true);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void preInitClient(FMLPreInitializationEvent event) {
		RenderingRegistry.registerEntityRenderingHandler(EntityParrotEgg.class, RenderParrotEgg.factory());
	}
	
	
}
