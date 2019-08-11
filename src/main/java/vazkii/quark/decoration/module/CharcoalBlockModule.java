package vazkii.quark.decoration.module;

import vazkii.quark.base.moduleloader.Config;
import vazkii.quark.base.moduleloader.LoadModule;
import vazkii.quark.base.moduleloader.Module;
import vazkii.quark.base.moduleloader.ModuleCategory;
import vazkii.quark.decoration.block.CharcoalBlock;

@LoadModule(category = ModuleCategory.DECORATION)
public class CharcoalBlockModule extends Module {

	@Config public static boolean burnsForever = true; 
	
	@Override
	public void start() {
		new CharcoalBlock(this);
	}
	
}
