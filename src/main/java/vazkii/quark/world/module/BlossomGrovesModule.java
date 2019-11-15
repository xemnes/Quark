package vazkii.quark.world.module;

import net.minecraft.block.material.MaterialColor;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.world.block.BlossomLeavesBlock;
import vazkii.quark.world.block.BlossomSaplingBlock;

@LoadModule(category = ModuleCategory.WORLD)
public class BlossomGrovesModule extends Module {

	@Override
	public void construct() {
		add("blue", MaterialColor.LIGHT_BLUE);
		add("lavender", MaterialColor.PINK);
		add("orange", MaterialColor.ORANGE_TERRACOTTA);
		add("pink", MaterialColor.PINK);
		add("yellow", MaterialColor.YELLOW);
	}
	
	private void add(String colorName, MaterialColor color) {
		new BlossomSaplingBlock(colorName, this, new BlossomLeavesBlock(colorName, this, color));
	}
	
}
