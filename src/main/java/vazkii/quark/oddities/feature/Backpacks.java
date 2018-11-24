package vazkii.quark.oddities.feature;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import vazkii.quark.base.module.Feature;
import vazkii.quark.oddities.item.ItemBackpack;

public class Backpacks extends Feature {

	public static Item backpack;
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		backpack = new ItemBackpack();
	}
	
	@Override
	public boolean requiresMinecraftRestartToEnable() {
		return true;
	}

}
