/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 *
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 *
 * File Created @ [30/03/2016, 18:22:12 (GMT)]
 */
package vazkii.quark.building.feature;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.arl.recipe.RecipeHandler;
import vazkii.quark.base.module.Feature;
import vazkii.quark.building.block.BlockPolishedStone;

public class PolishedStone extends Feature {

	public static Block polished_stone;

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		polished_stone = new BlockPolishedStone();

		RecipeHandler.addOreDictRecipe(new ItemStack(polished_stone, 2),
				"SS", "SS",
				'S', new ItemStack(Blocks.STONE_SLAB));
		RecipeHandler.addOreDictRecipe(new ItemStack(Blocks.STONE_SLAB, 6),
				"SSS",
				'S', new ItemStack(polished_stone));
		
		OreDictionary.registerOre("stonePolished", polished_stone);
	}

	@Override
	public boolean requiresMinecraftRestartToEnable() {
		return true;
	}
	
}
