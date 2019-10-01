package vazkii.quark.base.module;

import java.util.ArrayList;
import java.util.List;

public enum ModuleCategory {

	// Categories
	AUTOMATION("automation"),
	BUILDING("building"),
	MANAGEMENT("management"),
	TOOLS("tools"),
	TWEAKS("tweaks"),
	VANITY("vanity"),
	WORLD("world"),
	CLIENT("client");
	
	public final String name;
	
	private List<Module> ownedModules = new ArrayList<>();
	
	ModuleCategory(String name) {
		this.name = name;
	}
	
	public void addModule(Module module) {
		ownedModules.add(module);
	}
	
	public List<Module> getOwnedModules() {
		return ownedModules;
	}
	
}
