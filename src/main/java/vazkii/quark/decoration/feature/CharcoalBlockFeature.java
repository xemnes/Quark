package vazkii.quark.decoration.feature;

import vazkii.quark.base.moduleloader.Config;
import vazkii.quark.base.moduleloader.LoadModule;
import vazkii.quark.base.moduleloader.Module;
import vazkii.quark.base.moduleloader.ModuleCategory;

@LoadModule(category = ModuleCategory.DECORATION,
			antiOverlap = { "actuallyadditions" })
public final class CharcoalBlockFeature extends Module {

	@Config public static boolean burnsForever = true; 
	
}
