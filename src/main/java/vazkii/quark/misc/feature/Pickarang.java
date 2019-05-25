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
import vazkii.quark.misc.item.ItemPickarang;
import vazkii.quark.world.client.render.RenderPickarang;
import vazkii.quark.world.entity.EntityPickarang;

public class Pickarang extends Feature {
	
	public static int timeout, harvestLevel, durability;
	
	public static Item pickarang;
	
	@Override
	public void setupConfig() {
		timeout = loadPropInt("Timeout", "How long it takes for the pickarang to return to the player if it doesn't hit anything", 20);
		harvestLevel = loadPropInt("Harvest Level", "2 is Iron, 3 is Diamond", 3);
		durability = loadPropInt("Durability", "Set to -1 to have the Pickarang be unbreakable", 800);
	}
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		pickarang = new ItemPickarang();
		
		String pickarangName = "quark:pickarang";
		EntityRegistry.registerModEntity(new ResourceLocation(pickarangName), EntityPickarang.class, pickarangName, LibEntityIDs.PICKARANG, Quark.instance, 80, 3, true);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void preInitClient(FMLPreInitializationEvent event) {
		RenderingRegistry.registerEntityRenderingHandler(EntityPickarang.class, RenderPickarang.FACTORY);
	}
	
	@Override
	public boolean requiresMinecraftRestartToEnable() {
		return true;
	}

}
