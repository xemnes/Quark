package vazkii.quark.base.proxy;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.config.ModConfig.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.arl.util.ClientTicker;
import vazkii.quark.base.Quark;
import vazkii.quark.base.capability.CapabilityHandler;
import vazkii.quark.base.handler.ContributorRewardHandler;
import vazkii.quark.base.handler.QuarkSounds;
import vazkii.quark.base.handler.StonecutterShiftClickHandler;
import vazkii.quark.base.module.ModuleLoader;
import vazkii.quark.base.network.QuarkNetwork;
import vazkii.quark.base.recipe.ExclusionRecipe;
import vazkii.quark.base.world.WorldGenHandler;

import java.util.function.BooleanSupplier;

public class CommonProxy {

	private int lastConfigChange = 0;
	
	public void start() {
		ForgeRegistries.RECIPE_SERIALIZERS.register(ExclusionRecipe.SERIALIZER);

		QuarkSounds.start();
		ModuleLoader.INSTANCE.start();
		
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		registerListeners(bus);
	}
	
	public void registerListeners(IEventBus bus) {
		bus.addListener(this::setup);
		bus.addListener(this::loadComplete);
		bus.addListener(this::configChanged);
	}
	
	public void setup(FMLCommonSetupEvent event) {
		QuarkNetwork.setup();
		CapabilityHandler.setup();
		ContributorRewardHandler.init();
		ModuleLoader.INSTANCE.setup();
	}
	
	public void loadComplete(FMLLoadCompleteEvent event) {
		ModuleLoader.INSTANCE.loadComplete();
		WorldGenHandler.loadComplete();
	}
	
	public void configChanged(ModConfigEvent event) {
		if(event.getConfig().getModId().equals(Quark.MOD_ID) && ClientTicker.ticksInGame - lastConfigChange > 10) { 
			lastConfigChange = ClientTicker.ticksInGame;
			handleQuarkConfigChange();
			StonecutterShiftClickHandler.configReload();
		}
	}
	
	public void handleQuarkConfigChange() {
		ModuleLoader.INSTANCE.configChanged();
	}
	
	public void addResourceOverride(String type, String path, String file, BooleanSupplier isEnabled) {
		// NO-OP, client only
	}

}
