package vazkii.quark.base.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import vazkii.quark.base.module.ModuleLoader;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerListeners(IEventBus bus) {
		super.registerListeners(bus);
		
		bus.addListener(this::clientSetup);
	}
	
	public void clientSetup(FMLClientSetupEvent event) {
		ModuleLoader.INSTANCE.clientSetup();
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
	
}
