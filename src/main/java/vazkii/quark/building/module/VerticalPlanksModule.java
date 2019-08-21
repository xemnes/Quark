package vazkii.quark.building.module;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import vazkii.quark.base.block.QuarkBlock;
import vazkii.quark.base.handler.MiscUtil;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleCategory;

@LoadModule(category = ModuleCategory.BUILDING)
public class VerticalPlanksModule extends Module {

	@Override
	public void start() {
		for(String type : MiscUtil.ALL_WOOD_TYPES)
			new QuarkBlock("vertical_" + type + "_planks", this, ItemGroup.BUILDING_BLOCKS, Block.Properties.from(Blocks.OAK_PLANKS));
	}
	
}
