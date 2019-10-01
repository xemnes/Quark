package vazkii.quark.building.module;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import vazkii.quark.base.handler.ItemOverrideHandler;
import vazkii.quark.base.handler.MiscUtil;
import vazkii.quark.base.module.Config;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.building.block.IronLadderBlock;
import vazkii.quark.building.block.VariantLadderBlock;

@LoadModule(category = ModuleCategory.BUILDING)
public class VariantLaddersModule extends Module {

	@Config public static boolean changeNames = true;
	
	@Config(flag = "iron_ladder")
	public static boolean enableIronLadder = true;
	
	public static Block iron_ladder;

	@Override
	public void start() {
		for(String type : MiscUtil.VARIANT_WOOD_TYPES)
			new VariantLadderBlock(type, this);
		
		iron_ladder = new IronLadderBlock(this);
	}

	@Override
	public void configChanged() {
		ItemOverrideHandler.changeBlockLocalizationKey(Blocks.LADDER, "block.quark.oak_ladder", changeNames && enabled);
	}
	
}
