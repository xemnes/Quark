package vazkii.quark.automation.module;

import vazkii.quark.automation.block.MetalButtonBlock;
import vazkii.quark.base.module.Config;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleCategory;

@LoadModule(category = ModuleCategory.AUTOMATION)
public class MetalButtonsModule extends Module {

	@Config(flag = "iron_metal_button")
	public static boolean enableIron;
	@Config(flag = "gold_metal_button")
	public static boolean enableGold;

	@Override
	public void construct() {
		new MetalButtonBlock("iron_button", this, 100);
		new MetalButtonBlock("gold_button", this, 4);
	}
	
}
