package vazkii.quark.base.handler;

import java.util.function.Supplier;

import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import vazkii.quark.base.Quark;

// This is a massive hack to get around some funky race conditions
@EventBusSubscriber(modid = Quark.MOD_ID)
public class LoadingSynchronizationHandler {

	private static volatile Object mutex = new Object();
	private static volatile boolean loadDone = false;
	
	public static void synchronizeIfInGameLoad(Runnable r) {
		synchronizeIfInGameLoad(() -> {
			r.run();
			return null;
		});
	}
	
	public static <T> T synchronizeIfInGameLoad(Supplier<T> r) {
		if(loadDone)
			return r.get();
		
		else synchronized (mutex) {
			return r.get();
		}
	}
	
	@SubscribeEvent
	public static void worldTick(WorldTickEvent event) {
		loadDone = true;
	}
	
	
}
