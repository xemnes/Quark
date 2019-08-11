package vazkii.quark.base.moduleloader;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public final class ModuleLoader {
	
	private Map<String, ModuleCategory> foundCategories = new LinkedHashMap<>();
	private Map<Class<?>, Module> foundModules = new HashMap<>();
	private ConfigResolver config;
	
	public void start() {
		findModules();
		resolveConfigSpec();
		dispatch(Module::start);
	}
	
	private void findModules() {
		ModuleFinder finder = new ModuleFinder();
		finder.findModules();
		foundCategories = finder.getFoundCategories();
		foundModules = finder.getFoundModules();
	}
	
	private void resolveConfigSpec() {
		config = new ConfigResolver(foundCategories);
		config.makeSpec();
	}
	
	public void configChanged(boolean firstLoad) {
		config.configChanged();
		dispatch(m -> m.configChanged(firstLoad));
	}
	
	public void setup() {
		dispatch(Module::setup);
		dispatch(Module::modulesLoaded);
	}
	
	@OnlyIn(Dist.CLIENT)
	public void clientSetup() {
		dispatch(Module::clientSetup);
	}
	
	public void loadComplete() {
		dispatch(Module::loadComplete);
	}
	
	private void dispatch(Consumer<Module> run) {
		foundModules.values().forEach(run);
	}
	
}
