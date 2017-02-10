package vazkii.quark.building.feature;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import vazkii.arl.util.RecipeHandler;
import vazkii.quark.base.module.Feature;
import vazkii.quark.building.block.BlockSturdyStone;

public class SturdyStone extends Feature {

	public static Block sturdy_stone;

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		sturdy_stone = new BlockSturdyStone();

		RecipeHandler.addOreDictRecipe(new ItemStack(sturdy_stone, 4), 
				"SCS", "C C", "SCS",
				'S', "stone",
				'C', "cobblestone");
		RecipeHandler.addOreDictRecipe(new ItemStack(sturdy_stone, 4), 
				"CSC", "S S", "CSC",
				'S', "stone",
				'C', "cobblestone");
	}

	@Override
	public boolean requiresMinecraftRestartToEnable() {
		return true;
	}

}
