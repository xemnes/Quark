package vazkii.quark.world.module;

import java.util.LinkedList;
import java.util.List;

import com.google.common.base.Functions;

import net.minecraft.block.material.MaterialColor;
import vazkii.quark.base.handler.VariantHandler;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.world.block.BlossomLeavesBlock;
import vazkii.quark.world.block.BlossomSaplingBlock;

@LoadModule(category = ModuleCategory.WORLD)
public class BlossomTreesModule extends Module {

	// used by LeafCarpetModule
	public static List<BlossomLeavesBlock> leafList = new LinkedList<>();
	
	@Override
	public void construct() {
		add("blue", MaterialColor.LIGHT_BLUE);
		add("lavender", MaterialColor.PINK);
		add("orange", MaterialColor.ORANGE_TERRACOTTA);
		add("pink", MaterialColor.PINK);
		add("yellow", MaterialColor.YELLOW);
	}
	
	private void add(String colorName, MaterialColor color) {
		BlossomLeavesBlock leaves = new BlossomLeavesBlock(colorName, this, color);
		BlossomSaplingBlock sapling = new BlossomSaplingBlock(colorName, this, leaves);
		VariantHandler.addFlowerPot(sapling, sapling.getRegistryName().getPath(), Functions.identity());
		leafList.add(leaves);
	}
	
}
