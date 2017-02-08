package vazkii.quark.misc.feature;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import vazkii.arl.util.RecipeHandler;
import vazkii.quark.base.Quark;
import vazkii.quark.base.lib.LibEntityIDs;
import vazkii.quark.base.module.Feature;
import vazkii.quark.base.module.ModuleLoader;
import vazkii.quark.misc.entity.EntitySoulPowder;
import vazkii.quark.misc.item.ItemSoulPowder;
import vazkii.quark.world.feature.Wraiths;

public class SoulPowder extends Feature {

	public static Item soul_powder;
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		soul_powder = new ItemSoulPowder();
		
		String soulPowderName = "quark:soul_powder";
		EntityRegistry.registerModEntity(new ResourceLocation(soulPowderName), EntitySoulPowder.class, soulPowderName, LibEntityIDs.SOUL_POWDER, Quark.instance, 80, 10, false);
	}
	
	@Override
	public void init(FMLInitializationEvent event) {
		if(ModuleLoader.isFeatureEnabled(Wraiths.class))
			RecipeHandler.addShapelessOreDictRecipe(new ItemStack(soul_powder), new ItemStack(Wraiths.soul_bead), new ItemStack(Blocks.SOUL_SAND), new ItemStack(Blocks.SOUL_SAND), new ItemStack(Blocks.SOUL_SAND));
		else RecipeHandler.addShapelessOreDictRecipe(new ItemStack(soul_powder), new ItemStack(Items.MAGMA_CREAM), new ItemStack(Blocks.SOUL_SAND), new ItemStack(Blocks.SOUL_SAND), new ItemStack(Blocks.SOUL_SAND));
	}
	
}
