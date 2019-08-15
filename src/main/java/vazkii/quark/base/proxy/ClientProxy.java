package vazkii.quark.base.proxy;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.resource.VanillaResourceType;
import vazkii.quark.base.handler.ResourceProxy;
import vazkii.quark.base.module.ModuleLoader;

public class ClientProxy extends CommonProxy {

	@Override
	public void start() {
		ResourceProxy.init();
		
		super.start();
	}
	
	@Override
	public void registerListeners(IEventBus bus) {
		super.registerListeners(bus);
		
		bus.addListener(this::clientSetup);
	}
	
	public void clientSetup(FMLClientSetupEvent event) {
		ModuleLoader.INSTANCE.clientSetup();
	}
	
	@Override
	public void loadComplete(FMLLoadCompleteEvent event) {
		super.loadComplete(event);
		
		if(ResourceProxy.instance().hasAny())
			ForgeHooksClient.refreshResources(Minecraft.getInstance(), VanillaResourceType.MODELS);
	}
	
	@Override	
	public void handleQuarkConfigChange() {
		super.handleQuarkConfigChange();
		
		Minecraft mc = Minecraft.getInstance();
		if(mc.isSingleplayer()) {
	        mc.player.sendMessage(new TranslationTextComponent("commands.reload.success"));
	        mc.getIntegratedServer().reload();
		}
	}
	
	@Override
	public void addResourceOverride(String type, String path, String file, Supplier<Boolean> isEnabled) {
		ResourceProxy.instance().addResource(type, path, file, isEnabled);
	}
	
}
