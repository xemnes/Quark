package vazkii.quark.decoration.feature;

import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import vazkii.arl.recipe.RecipeHandler;
import vazkii.arl.util.ProxyRegistry;
import vazkii.quark.base.lib.LibMisc;
import vazkii.quark.base.module.Feature;
import vazkii.quark.decoration.block.BlockColoredFlowerPot;

public class ColoredFlowerPots extends Feature {

	public static BlockColoredFlowerPot[] pots;
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		pots = new BlockColoredFlowerPot[EnumDyeColor.values().length];
		for(int i = 0; i < pots.length; i++) {
			pots[i] = new BlockColoredFlowerPot(EnumDyeColor.byMetadata(i));
			RecipeHandler.addShapelessOreDictRecipe(ProxyRegistry.newStack(pots[i]), ProxyRegistry.newStack(Items.FLOWER_POT), LibMisc.OREDICT_DYES.get(15 - i));
		}
	}

}
