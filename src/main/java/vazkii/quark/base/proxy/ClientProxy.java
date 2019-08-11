package vazkii.quark.base.proxy;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerListeners(IEventBus bus) {
		super.registerListeners(bus);
		
		bus.addListener(this::clientSetup);
	}
	
	public void clientSetup(FMLClientSetupEvent event) {
		moduleLoader.clientSetup();
	}
	
}
