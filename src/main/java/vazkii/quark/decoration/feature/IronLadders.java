package vazkii.quark.decoration.feature;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import vazkii.arl.recipe.RecipeHandler;
import vazkii.quark.base.module.Feature;
import vazkii.quark.decoration.block.BlockIronLadder;

public class IronLadders extends Feature {

	public static Block iron_ladder;
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		iron_ladder = new BlockIronLadder();
		
		RecipeHandler.addOreDictRecipe(new ItemStack(iron_ladder, 16), 
				"I I", "III", "I I",
				'I', "ingotIron");
	}	
	
	@Override
	public boolean requiresMinecraftRestartToEnable() {
		return true;
	}
	
}
