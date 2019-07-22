package vazkii.quark.automation.feature;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.arl.recipe.MultiRecipe;
import vazkii.arl.recipe.RecipeHandler;
import vazkii.arl.util.ProxyRegistry;
import vazkii.quark.automation.block.BlockColorSlime;
import vazkii.quark.base.module.Feature;

public class ColorSlime extends Feature {

	public static Block color_slime;
	
	boolean renameVanillaSlime;
	
	private MultiRecipe multiRecipe;
	
	@Override
	public void setupConfig() {
		renameVanillaSlime = loadPropBool("Rename Vanilla Slime", "Set to false to not rename vanilla slime to Green Slime Block", true);
	}
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		color_slime = new BlockColorSlime();

		// we use a MultiRecipe to avoid a race condition with the slime block not being registered in time
		// (not sure why it happens but it happens!)
		multiRecipe = new MultiRecipe(new ResourceLocation("quark", "slime_block_recipes"));

		addOreDict("blockSlime", ProxyRegistry.newStack(color_slime, 1, OreDictionary.WILDCARD_VALUE));
	}
	
	@Override
	public void postPreInit() {
		if(renameVanillaSlime)
			Blocks.SLIME_BLOCK.setTranslationKey("green_slime_block");
	}
	
	@Override
	public void init() {
		RecipeHandler.addShapelessOreDictRecipe(multiRecipe, new ItemStack(Blocks.SLIME_BLOCK), "blockSlime", "dyeGreen");
		RecipeHandler.addShapelessOreDictRecipe(multiRecipe, ProxyRegistry.newStack(color_slime, 1, 0), "blockSlime", "dyeRed");
		RecipeHandler.addShapelessOreDictRecipe(multiRecipe, ProxyRegistry.newStack(color_slime, 1, 1), "blockSlime", "dyeBlue");
		
		RecipeHandler.addShapelessOreDictRecipe(multiRecipe, ProxyRegistry.newStack(color_slime, 2, 2), ProxyRegistry.newStack(Blocks.SLIME_BLOCK), ProxyRegistry.newStack(color_slime, 1, 1));
		RecipeHandler.addShapelessOreDictRecipe(multiRecipe, ProxyRegistry.newStack(color_slime, 2, 3), ProxyRegistry.newStack(color_slime, 1, 0), ProxyRegistry.newStack(color_slime, 1, 1));
		RecipeHandler.addShapelessOreDictRecipe(multiRecipe, ProxyRegistry.newStack(color_slime, 2, 4), ProxyRegistry.newStack(color_slime, 1, 0), ProxyRegistry.newStack(Blocks.SLIME_BLOCK));
	}
	
	@Override
	public boolean requiresMinecraftRestartToEnable() {
		return true;
	}
	
}
