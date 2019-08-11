package vazkii.quark.base.proxy;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import vazkii.quark.base.Quark;
import vazkii.quark.base.moduleloader.ModuleLoader;

public class CommonProxy {

	protected ModuleLoader moduleLoader;
	
	public void start() {
		moduleLoader = new ModuleLoader();
		moduleLoader.start();
		
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		registerListeners(bus);
	}
	
	public void registerListeners(IEventBus bus) {
		bus.addListener(this::setup);
		bus.addListener(this::loadComplete);
		bus.addListener(this::configChanged);
	}
	
	public final void setup(FMLCommonSetupEvent event) {
//		moduleLoader.configChanged(true);
		moduleLoader.setup();
	}
	
	public final void loadComplete(FMLLoadCompleteEvent event) {
		moduleLoader.loadComplete();
	}
	
	public final void configChanged(ModConfig.ConfigReloading event) {
		if(event.getConfig().getModId().equals(Quark.MOD_ID))
			moduleLoader.configChanged(false);
	}
	
	public final ModuleLoader getModuleLoader() {
		return moduleLoader;
	}
	
}
