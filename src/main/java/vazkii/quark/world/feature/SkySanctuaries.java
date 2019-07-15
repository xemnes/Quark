package vazkii.quark.world.feature;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import vazkii.arl.block.BlockModSlab;
import vazkii.arl.block.BlockModStairs;
import vazkii.arl.recipe.RecipeHandler;
import vazkii.arl.util.ProxyRegistry;
import vazkii.quark.base.block.BlockQuarkStairs;
import vazkii.quark.base.module.Feature;
import vazkii.quark.base.module.GlobalConfig;
import vazkii.quark.building.feature.VanillaWalls;
import vazkii.quark.world.block.BlockSkyfoam;
import vazkii.quark.world.block.BlockSkylather;
import vazkii.quark.world.block.BlockSkylatherPillar;
import vazkii.quark.world.block.slab.BlockSkylatherSlab;

public class SkySanctuaries extends Feature {

	public static Block skyfoam;
	public static Block skylather;
	public static Block skylather_pillar;

	public static boolean enableStairsAndSlabs;
	public static boolean enableWalls;

	@Override
	public void setupConfig() {
		enableStairsAndSlabs = loadPropBool("Enable stairs and slabs", "", true) && GlobalConfig.enableVariants;
		enableWalls = loadPropBool("Enable walls", "", true) && GlobalConfig.enableVariants;
	}

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		skyfoam = new BlockSkyfoam();	
		skylather = new BlockSkylather();	
		skylather_pillar = new BlockSkylatherPillar();

		if (enableStairsAndSlabs) {
			BlockModStairs.initStairs(skylather, 0, new BlockQuarkStairs("skylather_stairs", skylather.getDefaultState()));
			BlockModSlab.initSlab(skylather, 0, new BlockSkylatherSlab("skylather", false), new BlockSkylatherSlab("skylather", true));
			
			BlockModSlab slab = new BlockSkylatherSlab("skylather_brick", false);
			BlockModStairs.initStairs(skylather, 1, new BlockQuarkStairs("skylather_brick_stairs", skylather.getDefaultState()));
			BlockModSlab.initSlab(skylather, 1, slab, new BlockSkylatherSlab("skylather_brick", true));
			
			RecipeHandler.addOreDictRecipe(ProxyRegistry.newStack(skylather_pillar),
					"S", "S",
					'S', ProxyRegistry.newStack(slab));
		} else {
			RecipeHandler.addOreDictRecipe(ProxyRegistry.newStack(skylather_pillar, 2),
					"M", "M",
					'M', ProxyRegistry.newStack(skylather, 1, 1));
		}

		RecipeHandler.addOreDictRecipe(ProxyRegistry.newStack(skylather, 4, 1),
				"SS", "SS",
				'S', ProxyRegistry.newStack(skylather));
		
		VanillaWalls.add("skylather", skylather, 0, enableWalls);
		VanillaWalls.add("skylather_brick", skylather, 1, enableWalls);
	}
	
	@Override
	public void init() {
		FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(skyfoam), new ItemStack(skylather), 0);
	}

	@Override
	public boolean requiresMinecraftRestartToEnable() {
		return true;
	}
	
}
