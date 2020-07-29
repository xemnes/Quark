package vazkii.quark.base.proxy;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import vazkii.quark.base.handler.ContributorRewardHandler;
import vazkii.quark.base.handler.RenderLayerHandler;
import vazkii.quark.base.module.ModuleLoader;

@OnlyIn(Dist.CLIENT)
public class ClientProxy extends CommonProxy {

	public static boolean jingleBellsMotherfucker = false;
	
	@Override
	public void start() {
		LocalDateTime now = LocalDateTime.now();
		if(now.getMonth() == Month.DECEMBER && now.getDayOfMonth() >= 16 || now.getMonth() == Month.JANUARY && now.getDayOfMonth() <= 6)
			jingleBellsMotherfucker = true;
		
		super.start();
		
		ModuleLoader.INSTANCE.clientStart();
	}

	@Override
	public void registerListeners(IEventBus bus) {
		super.registerListeners(bus);

		bus.addListener(this::clientSetup);
		bus.addListener(this::modelRegistry);
		bus.addListener(this::textureStitch);
		bus.addListener(this::postTextureStitch);
	}

	public void clientSetup(FMLClientSetupEvent event) {
		RenderLayerHandler.init();
		ModuleLoader.INSTANCE.clientSetup();
	}

	public void modelRegistry(ModelRegistryEvent event) {
		ModuleLoader.INSTANCE.modelRegistry();
	}
	
	public void textureStitch(TextureStitchEvent.Pre event) {
		ModuleLoader.INSTANCE.textureStitch(event);
	}

	public void postTextureStitch(TextureStitchEvent.Post event) {
		ModuleLoader.INSTANCE.postTextureStitch(event);
	}

	@Override	
	public void handleQuarkConfigChange() {
		super.handleQuarkConfigChange();

		ModuleLoader.INSTANCE.configChangedClient();

		Minecraft mc = Minecraft.getInstance();
		if(mc.isSingleplayer() && mc.player != null && mc.getIntegratedServer() != null) {
			mc.player.sendMessage(new TranslationTextComponent("quark.misc.reloaded"), UUID.randomUUID());
		}
	}
	
	@Override
	protected void initContributorRewards() {
		ContributorRewardHandler.getLocalName();
		super.initContributorRewards();
	}

}
