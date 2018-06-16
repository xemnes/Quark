package vazkii.quark.misc.feature;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import vazkii.arl.recipe.RecipeHandler;
import vazkii.arl.util.ProxyRegistry;
import vazkii.quark.base.module.Feature;
import vazkii.quark.base.module.ModuleLoader;
import vazkii.quark.decoration.feature.VariedChests;

public class UtilityRecipes extends Feature {

	boolean enableDispenser, enableRepeater, enableTrappedChest;
	
	@Override
	public void setupConfig() {
		enableDispenser = loadPropBool("Dispenser Recipe", "", true);
		enableRepeater = loadPropBool("Repeater Recipe", "", true);
		enableTrappedChest = loadPropBool("Enable Trapped Chest", "", true);
	}
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		if(enableDispenser)
			RecipeHandler.addOreDictRecipe(ProxyRegistry.newStack(Blocks.DISPENSER), 
					"ST ", "SDT", "ST ",
					'S', "string",
					'D', ProxyRegistry.newStack(Blocks.DROPPER),
					'T', "stickWood");
		
		if(enableRepeater)
			RecipeHandler.addOreDictRecipe(ProxyRegistry.newStack(Items.REPEATER), 
					"R R", "TRT", "SSS",
					'S', "stone",
					'T', "stickWood",
					'R', "dustRedstone");
	}
	
}
