package vazkii.quark.building.feature;

import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import vazkii.arl.block.BlockMod;
import vazkii.arl.block.BlockModSlab;
import vazkii.arl.block.BlockModStairs;
import vazkii.arl.recipe.RecipeHandler;
import vazkii.arl.util.ProxyRegistry;
import vazkii.quark.base.module.Feature;
import vazkii.quark.base.module.GlobalConfig;
import vazkii.quark.building.block.BlockPolishedNetherrack;
import vazkii.quark.building.block.slab.BlockPolishedNetherrackBricksSlab;
import vazkii.quark.building.block.stairs.BlockPolishedNetherrackBricksStairs;

public class PolishedNetherrack extends Feature {

	public static BlockMod polished_netherrack;

	public static boolean enableStairsAndSlabs;
	public static boolean enableWalls;

	@Override
	public void setupConfig() {
		enableStairsAndSlabs = loadPropBool("Enable stairs and slabs", "", true) && GlobalConfig.enableVariants;
		enableWalls = loadPropBool("Enable walls", "", true) && GlobalConfig.enableVariants;
	}

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		polished_netherrack = new BlockPolishedNetherrack();

		if(enableStairsAndSlabs) {
			BlockModStairs.initStairs(polished_netherrack, 1, new BlockPolishedNetherrackBricksStairs());
			BlockModSlab.initSlab(polished_netherrack, 1, new BlockPolishedNetherrackBricksSlab(false), new BlockPolishedNetherrackBricksSlab(true));
		}
		VanillaWalls.add("polished_netherrack_bricks", polished_netherrack, 1, enableWalls);
		
		RecipeHandler.addOreDictRecipe(ProxyRegistry.newStack(polished_netherrack), 
				"RR", "RR",
				'R', ProxyRegistry.newStack(Blocks.NETHERRACK));
		RecipeHandler.addOreDictRecipe(ProxyRegistry.newStack(polished_netherrack, 4, 1), 
				"RR", "RR",
				'R', ProxyRegistry.newStack(polished_netherrack));
	}

	@Override
	public boolean requiresMinecraftRestartToEnable() {
		return true;
	}

}
