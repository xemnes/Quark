package vazkii.quark.decoration.module;

import net.minecraft.block.Blocks;
import vazkii.quark.base.handler.ItemOverrideHandler;
import vazkii.quark.base.handler.MiscUtil;
import vazkii.quark.base.module.Config;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.decoration.block.VariantLadderBlock;

@LoadModule(category = ModuleCategory.DECORATION)
public class VariantLaddersModule extends Module {

	@Config public static boolean changeNames = true;

	@Override
	public void start() {
		for(String type : MiscUtil.VARIANT_WOOD_TYPES)
			new VariantLadderBlock(type, this);
	}

	@Override
	public void configChanged() {
		ItemOverrideHandler.changeBlockLocalizationKey(Blocks.LADDER, "block.quark.oak_ladder", changeNames && enabled);
	}
	
}
