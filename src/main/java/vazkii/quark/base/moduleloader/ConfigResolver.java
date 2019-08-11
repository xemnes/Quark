package vazkii.quark.base.moduleloader;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class ConfigResolver {

	private final Map<String, ModuleCategory> categories;
	
	private Queue<Runnable> refreshRunnables = new ArrayDeque<>();
	
	public ConfigResolver(Map<String, ModuleCategory> categories) {
		this.categories = categories;
	}
	
	public void makeSpec() {
		ForgeConfigSpec spec = new ForgeConfigSpec.Builder().configure(this::build).getRight();
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, spec);
	}
	
	public void configChanged() {
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
		
		for(Module module : category.getOwnedModules()) {
			ForgeConfigSpec.ConfigValue<Boolean> value = builder.define(module.displayName, module.enabledByDefault);
			refreshRunnables.add(() -> module.setEnabled(value.get()));
		}
		
		// TODO add module stuff
		
		builder.pop();
	}
	
}
