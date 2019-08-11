package vazkii.quark.building.module;

import vazkii.quark.base.handler.VariantHandler;
import vazkii.quark.base.moduleloader.Config;
import vazkii.quark.base.moduleloader.LoadModule;
import vazkii.quark.base.moduleloader.Module;
import vazkii.quark.base.moduleloader.ModuleCategory;
import vazkii.quark.building.block.ThatchBlock;

@LoadModule(category = ModuleCategory.BUILDING)
public class ThatchModule extends Module {

	@Config public static float fallDamageMultiplier = 0.5F;
	
	@Override
	public void start() {
		VariantHandler.addSlabAndStairs(new ThatchBlock(this));
	}
	
}
