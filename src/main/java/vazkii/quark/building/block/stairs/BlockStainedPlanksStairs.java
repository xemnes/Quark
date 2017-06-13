package vazkii.quark.building.block.stairs;

import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.item.crafting.IRecipe;
import vazkii.arl.interf.IRecipeGrouped;
import vazkii.quark.base.block.BlockQuarkStairs;
import vazkii.quark.building.block.BlockStainedPlanks;
import vazkii.quark.building.feature.StainedPlanks;

public class BlockStainedPlanksStairs extends BlockQuarkStairs implements IRecipeGrouped {

	public BlockStainedPlanksStairs(BlockStainedPlanks.Variants variant) {
		super(variant.getName() + "_stairs", StainedPlanks.stained_planks.getDefaultState().withProperty(StainedPlanks.stained_planks.getVariantProp(), variant));
	}

	@Override
	public String getRecipeGroup() {
		return "stained_planks_stairs";
	}
	
}
