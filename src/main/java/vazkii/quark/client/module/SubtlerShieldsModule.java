package vazkii.quark.client.module;

import vazkii.quark.base.handler.ResourceProxy;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleCategory;

@LoadModule(category = ModuleCategory.CLIENT)
public class SubtlerShieldsModule extends Module {

	@Override
	public void clientSetup() {
		ResourceProxy.instance().addResource("models", "item", "shield.json", () -> enabled);
	}
	
}
