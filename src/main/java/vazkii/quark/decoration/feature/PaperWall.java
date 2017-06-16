/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 *
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 *
 * File Created @ [24/03/2016, 03:18:35 (GMT)]
 */
package vazkii.quark.decoration.feature;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import vazkii.arl.recipe.RecipeHandler;
import vazkii.quark.base.module.Feature;
import vazkii.quark.decoration.block.BlockPaperWall;

public class PaperWall extends Feature {

	public static Block paper_wall;

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		paper_wall = new BlockPaperWall();

		RecipeHandler.addOreDictRecipe(new ItemStack(paper_wall, 6),
				"SSS", "PPP", "SSS",
				'S', new ItemStack(Items.STICK),
				'P', new ItemStack(Items.PAPER));
	}

	@Override
	public boolean requiresMinecraftRestartToEnable() {
		return true;
	}
	
}
