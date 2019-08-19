package vazkii.quark.building.module;

import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.building.block.SturdyStoneBlock;

@LoadModule(category = ModuleCategory.DECORATION)
public class SturdyStoneModule extends Module {

	@Override
	public void start() {
		new SturdyStoneBlock(this);
	}
	
}
