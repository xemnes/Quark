package vazkii.quark.decoration.feature;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import vazkii.arl.recipe.RecipeHandler;
import vazkii.arl.util.ProxyRegistry;
import vazkii.quark.base.module.Feature;
import vazkii.quark.decoration.block.BlockRope;

public class Rope extends Feature {

	public static Block rope;
	
	public static boolean forceEnableMoveTEs;
	int recipeCount;
	
	@Override
	public void setupConfig() {
		forceEnableMoveTEs = loadPropBool("Force Enable Move TEs", "Set to true to allow ropes to move Tile Entities even if Pistons Push TEs is disabled\nNote that ropes will still use the same blacklist", false);
		recipeCount = loadPropInt("Recipe Output", "", 1);
	}
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		rope = new BlockRope();
		
		RecipeHandler.addOreDictRecipe(ProxyRegistry.newStack(rope, recipeCount), 
				"SS", "SS", "SS",
				'S', "string");
	}
	
	@Override
	public boolean requiresMinecraftRestartToEnable() {
		return true;
	}
	
}
