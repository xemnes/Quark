package vazkii.quark.base.module;

import java.util.ArrayList;
import java.util.List;

public final class ModuleCategory {

	// Categories
	public static final String AUTOMATION = "automation";
	public static final String BUILDING = "building";
	public static final String DECORATION = "decoration";
	public static final String MANAGEMENT = "management";
	public static final String TOOLS = "tools";
	public static final String TWEAKS = "tweaks";
	public static final String VANITY = "vanity";
	public static final String WORLD = "world";
	public static final String CLIENT = "client";
	
	public final String name;
	
	private List<Module> ownedModules = new ArrayList<>();
	
	public ModuleCategory(String name) {
		this.name = name;
	}
	
	public void addModule(Module module) {
		ownedModules.add(module);
	}
	
	public List<Module> getOwnedModules() {
		return ownedModules;
	}
	
}
