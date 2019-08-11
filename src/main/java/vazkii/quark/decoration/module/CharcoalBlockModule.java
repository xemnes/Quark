package vazkii.quark.decoration.module;

import net.minecraft.block.Block;
import vazkii.quark.base.moduleloader.Config;
import vazkii.quark.base.moduleloader.LoadModule;
import vazkii.quark.base.moduleloader.Module;
import vazkii.quark.base.moduleloader.ModuleCategory;
import vazkii.quark.decoration.block.CharcoalBlock;

@LoadModule(category = ModuleCategory.DECORATION)
public final class CharcoalBlockModule extends Module {

	@Config public static boolean burnsForever = true; 
	
	public static Block charcoal_block;
	
	@Override
	public void start() {
		charcoal_block = new CharcoalBlock(this);
	}
	
	@Override
	public void configChanged() {
		System.out.println("On config change");
		System.out.println("Is enabled: " + enabled);
	}
	
}
