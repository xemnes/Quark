package vazkii.quark.base.module;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ConfigResolver {

	private final Map<String, ModuleCategory> categories;
	private final ConfigFlagManager flagManager;
	
	private List<Runnable> refreshRunnables = new LinkedList<>();
	
	public ConfigResolver(Map<String, ModuleCategory> categories) {
		this.categories = categories;
		this.flagManager = new ConfigFlagManager();
	}
	
	public void makeSpec() {
		ForgeConfigSpec spec = new ForgeConfigSpec.Builder().configure(this::build).getRight();
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, spec);
	}
	
	public void configChanged() {
		flagManager.clear();
		refreshRunnables.forEach(Runnable::run);
	}
	
	private Void build(ForgeConfigSpec.Builder builder) {
		for(String s : categories.keySet()) {
			ModuleCategory category = categories.get(s);
			buildCategory(builder, category);
		}
		
		return null;
	}
	
	private void buildCategory(ForgeConfigSpec.Builder builder, ModuleCategory category) {
		builder.push(category.name);
		
		List<Module> modules = category.getOwnedModules();
		Map<Module, Runnable> setEnabledRunnables = new HashMap<>();
		
		for(Module module : modules) {
			ForgeConfigSpec.ConfigValue<Boolean> value = builder.define(module.displayName, module.enabledByDefault);
			setEnabledRunnables.put(module, () -> {
				module.setEnabled(value.get());
				flagManager.putEnabledFlag(module);
			});
		}
	
		for(Module module : modules)
			buildModule(builder, module, setEnabledRunnables.get(module));
		
		builder.pop();
	}
	
	private void buildModule(ForgeConfigSpec.Builder builder, Module module, Runnable setEnabled) {
		if(!module.description.isEmpty())
			builder.comment(module.description);
		
		builder.push(module.lowercaseName);
		
		if(module.antiOverlap != null && module.antiOverlap.size() > 0)
			addModuleAntiOverlap(builder, module);
		
		refreshRunnables.add(setEnabled);
		
		try {
			ConfigObjectSerializer.serialize(builder, flagManager, refreshRunnables, module);
		} catch(ReflectiveOperationException e) {
			throw new RuntimeException("Failed to create config spec for module " + module.displayName, e);
		}
		
		refreshRunnables.add(() -> module.pushFlags(flagManager));
		module.buildConfigSpec(builder, refreshRunnables);
		
		builder.pop();
	}
	
	private void addModuleAntiOverlap(ForgeConfigSpec.Builder builder, Module module) {
		StringBuilder desc = new StringBuilder("This feature disables itself if any of the following mods are loaded: \n");
		for(String s : module.antiOverlap)
			desc.append(" - ").append(s).append("\n");
		desc.append("This is done to prevent content overlap.\nYou can turn this on to force the feature to be loaded even if the above mods are also loaded.");
		String descStr = desc.toString();
		
		builder.comment(descStr);
		ForgeConfigSpec.ConfigValue<Boolean> value = builder.define("Ignore Anti Overlap", false);
		refreshRunnables.add(() -> module.ignoreAntiOverlap = value.get());
	}
	
}
