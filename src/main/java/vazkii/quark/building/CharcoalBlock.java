package vazkii.quark.building;

import vazkii.quark.base.moduleloader.LoadModule;
import vazkii.quark.base.moduleloader.Module;
import vazkii.quark.base.moduleloader.ModuleCategory;

@LoadModule(category = ModuleCategory.BUILDING)
public final class CharcoalBlock extends Module {

	@Override
	public void loadComplete() {
		System.out.println("CharcoalBlock enabled: " + enabled);
	}
	
	@Override
	public void configChanged() {
		System.out.println("Config was changed yo");
		System.out.println("Current enabled status: " + enabled);
	}
	
}
