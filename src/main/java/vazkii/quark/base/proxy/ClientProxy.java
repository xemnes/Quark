package vazkii.quark.base.proxy;

import java.time.LocalDateTime;
import java.time.Month;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import vazkii.quark.base.handler.ContributorRewardHandler;
import vazkii.quark.base.module.ModuleLoader;

public class ClientProxy extends CommonProxy {

	public static boolean jingleBellsMotherfucker = false;
	
	@Override
	public void start() {
		LocalDateTime now = LocalDateTime.now();
		if(now.getMonth() == Month.DECEMBER && now.getDayOfMonth() >= 16 || now.getMonth() == Month.JANUARY && now.getDayOfMonth() <= 6)
			jingleBellsMotherfucker = true;
		
		super.start();
	}

	@Override
	public void registerListeners(IEventBus bus) {
		super.registerListeners(bus);

		bus.addListener(this::clientSetup);
		bus.addListener(this::modelRegistry);
	}

	public void clientSetup(FMLClientSetupEvent event) {
		ModuleLoader.INSTANCE.clientSetup();
	}

	public void modelRegistry(ModelRegistryEvent event) {
		ModuleLoader.INSTANCE.modelRegistry();
	}

//	@Override
//	public void loadComplete(FMLLoadCompleteEvent event) {
//		super.loadComplete(event);
//		
//		if(ResourceProxy.instance().hasAny()) {
//			StartupMessageManager.addModMessage("Quark: Applying vanilla resource overrides...");
//			Minecraft mc = Minecraft.getInstance();
//			List<IResourcePack> packs = mc.getResourcePackList().getEnabledPacks().stream().map(ResourcePackInfo::getResourcePack).collect(Collectors.toList());
//			
//			SelectiveReloadStateHandler.INSTANCE.beginReload(ReloadRequirements.include(VanillaResourceType.MODELS));
//			IAsyncReloader async = ((IReloadableResourceManager) mc.getResourceManager()).reloadResources(Util.getServerExecutor(), mc, CompletableFuture.completedFuture(Unit.INSTANCE), packs);
//			async.join();
//			SelectiveReloadStateHandler.INSTANCE.endReload();
//		}
//	}

	@Override	
	public void handleQuarkConfigChange() {
		super.handleQuarkConfigChange();

		ModuleLoader.INSTANCE.configChangedClient();

		Minecraft mc = Minecraft.getInstance();
		if(mc.isSingleplayer() && mc.player != null && mc.getIntegratedServer() != null) {
			mc.player.sendMessage(new TranslationTextComponent("commands.reload.success"));
			mc.getIntegratedServer().reload();
		}
	}

//	@Override
//	public void addResourceOverride(String type, String path, String file, BooleanSupplier isEnabled) {
//		ResourceProxy.instance().addResource(type, path, file, isEnabled);
//	}
	
	@Override
	protected void initContributorRewards() {
		ContributorRewardHandler.getLocalName();
		super.initContributorRewards();
	}

}
