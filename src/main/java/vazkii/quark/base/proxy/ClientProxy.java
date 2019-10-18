package vazkii.quark.base.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.IAsyncReloader;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourcePack;
import net.minecraft.resources.ResourcePackInfo;
import net.minecraft.util.Unit;
import net.minecraft.util.Util;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.StartupMessageManager;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.resource.ReloadRequirements;
import net.minecraftforge.resource.SelectiveReloadStateHandler;
import net.minecraftforge.resource.VanillaResourceType;
import vazkii.quark.base.handler.ContributorRewardHandler;
import vazkii.quark.base.handler.ResourceProxy;
import vazkii.quark.base.module.ModuleLoader;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BooleanSupplier;
import java.util.stream.Collectors;

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
		bus.addListener(this::modelRegistry);
	}

	public void clientSetup(FMLClientSetupEvent event) {
		ModuleLoader.INSTANCE.clientSetup();
		
		ResourceProxy.instance().ensureNotLast();
	}

	public void modelRegistry(ModelRegistryEvent event) {
		ModuleLoader.INSTANCE.modelRegistry();
	}

	@Override
	public void loadComplete(FMLLoadCompleteEvent event) {
		super.loadComplete(event);
		
		if(ResourceProxy.instance().hasAny()) {
			StartupMessageManager.addModMessage("Quark: Applying vanilla resource overrides...");
			Minecraft mc = Minecraft.getInstance();
			List<IResourcePack> packs = mc.getResourcePackList().getEnabledPacks().stream().map(ResourcePackInfo::getResourcePack).collect(Collectors.toList());
			
			SelectiveReloadStateHandler.INSTANCE.beginReload(ReloadRequirements.include(VanillaResourceType.MODELS));
			IAsyncReloader async = ((IReloadableResourceManager) mc.getResourceManager()).reloadResources(Util.getServerExecutor(), mc, CompletableFuture.completedFuture(Unit.INSTANCE), packs);
			async.join();
			SelectiveReloadStateHandler.INSTANCE.endReload();
		}
	}

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

	@Override
	public void addResourceOverride(String type, String path, String file, BooleanSupplier isEnabled) {
		ResourceProxy.instance().addResource(type, path, file, isEnabled);
	}
	
	@Override
	protected void initContributorRewards() {
		ContributorRewardHandler.getLocalName();
		super.initContributorRewards();
	}

}
