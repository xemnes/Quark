package vazkii.quark.decoration.module;

import net.minecraft.block.Block;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import vazkii.quark.base.moduleloader.Config;
import vazkii.quark.base.moduleloader.LoadModule;
import vazkii.quark.base.moduleloader.Module;
import vazkii.quark.base.moduleloader.ModuleCategory;
import vazkii.quark.base.moduleloader.SubscriptionTarget;
import vazkii.quark.decoration.block.CharcoalBlock;

@LoadModule(category = ModuleCategory.DECORATION, subscriptions = SubscriptionTarget.BOTH_SIDES)
public class CharcoalBlockModule extends Module {

	@Config public static boolean burnsForever = true; 
	@Config public static int fuelTime = 16000;
	
	private Block charcoal_block;
	
	@Override
	public void start() {
		charcoal_block = new CharcoalBlock(this);
	}

	@SubscribeEvent
	public void onFurnaceFuelEvent(FurnaceFuelBurnTimeEvent event) {
		if(event.getItemStack().getItem() == charcoal_block.asItem())
			event.setBurnTime(fuelTime);
	}
	
}
