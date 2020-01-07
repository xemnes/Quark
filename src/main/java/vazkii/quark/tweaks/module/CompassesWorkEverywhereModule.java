package vazkii.quark.tweaks.module;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import vazkii.quark.base.module.Config;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.tweaks.client.item.ClockTimeGetter;
import vazkii.quark.tweaks.client.item.CompassAngleGetter;

@LoadModule(category = ModuleCategory.TWEAKS, hasSubscriptions = true)
public class CompassesWorkEverywhereModule extends Module {

	@Config public static boolean enableCompassNerf =  true;
	@Config public static boolean enableClockNerf =  true;
	
	@Config public static boolean enableNether =  true;
	@Config public static boolean enableEnd =  true;
	
	@Override
	public void setup() {
		if(enabled && (enableCompassNerf || enableNether || enableEnd))
			Items.COMPASS.addPropertyOverride(new ResourceLocation("angle"), new CompassAngleGetter());
		
		if(enabled && enableClockNerf)
			Items.CLOCK.addPropertyOverride(new ResourceLocation("time"), new ClockTimeGetter());
	}
	
	@SubscribeEvent
	public void onUpdate(PlayerTickEvent event) {
		if(event.phase == Phase.START) {
			for(int i = 0; i < event.player.inventory.getSizeInventory(); i++) {
				ItemStack stack = event.player.inventory.getStackInSlot(i);
				if(stack.getItem() == Items.COMPASS)
					CompassAngleGetter.tickCompass(event.player, stack);
				else if(stack.getItem() == Items.CLOCK)
					ClockTimeGetter.tickClock(stack);
			}
		}
	}
	
}
