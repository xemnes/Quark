/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 *
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 *
 * File Created @ [20/03/2016, 19:56:42 (GMT)]
 */
package vazkii.quark.building.feature;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import vazkii.arl.block.BlockMod;
import vazkii.arl.block.BlockModSlab;
import vazkii.arl.block.BlockModStairs;
import vazkii.arl.recipe.RecipeHandler;
import vazkii.arl.util.ProxyRegistry;
import vazkii.quark.base.handler.ModIntegrationHandler;
import vazkii.quark.base.module.Feature;
import vazkii.quark.base.module.GlobalConfig;
import vazkii.quark.building.block.BlockWorldStoneBricks;
import vazkii.quark.building.block.BlockWorldStoneCarved;
import vazkii.quark.building.block.slab.BlockVanillaSlab;
import vazkii.quark.building.block.stairs.BlockVanillaStairs;
import vazkii.quark.world.feature.Basalt;
import vazkii.quark.world.feature.RevampStoneGen;

public class WorldStoneBricks extends Feature {

	public static BlockMod world_stone_bricks;
	public static BlockMod world_stone_chiseled;

	public static final BlockModSlab[] slabs = new BlockVanillaSlab[BlockWorldStoneBricks.Variants.values().length];

	public static boolean enableStairsAndSlabs;
	public static boolean enableWalls;

	@Override
	public void setupConfig() {
		enableStairsAndSlabs = loadPropBool("Enable stairs and slabs", "", true) && GlobalConfig.enableVariants;
		enableWalls = loadPropBool("Enable walls", "", true) && GlobalConfig.enableVariants;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void preInit(FMLPreInitializationEvent event) {
		world_stone_bricks = new BlockWorldStoneBricks();
		world_stone_chiseled = new BlockWorldStoneCarved();

		if(enableStairsAndSlabs) {
			for(BlockWorldStoneBricks.Variants variant : BlockWorldStoneBricks.Variants.values()) {
				if(!variant.isEnabled())
					continue;

				IBlockState state = world_stone_bricks.getDefaultState().withProperty(world_stone_bricks.getVariantProp(), variant);
				BlockModStairs.initStairs(world_stone_bricks, variant.ordinal(), new BlockVanillaStairs(variant.getName() + "_stairs", state));

				slabs[variant.ordinal()] = BlockVanillaSlab.initSlab(world_stone_bricks, variant.ordinal(), state, variant.getName() + "_slab");
			}
		}

		if(enableWalls) {
			for (BlockWorldStoneBricks.Variants variant : BlockWorldStoneBricks.Variants.values()) {
				if (!variant.isEnabled())
					continue;

				world_stone_bricks.getDefaultState().withProperty(world_stone_bricks.getVariantProp(), variant);
				String name = variant.getName();
				VanillaWalls.add(name, world_stone_bricks, variant.ordinal(), true);
			}
		}
	}

	@Override
	public void postPreInit() {
		for(int i = 0; i < 3; i++) {
			RecipeHandler.addOreDictRecipe(ProxyRegistry.newStack(world_stone_bricks, 4, i),
					"SS", "SS",
					'S', ProxyRegistry.newStack(Blocks.STONE, 1, i * 2 + 2));

			if (enableStairsAndSlabs)
				RecipeHandler.addOreDictRecipe(ProxyRegistry.newStack(world_stone_chiseled, 1, i),
						"S", "S",
						'S', ProxyRegistry.newStack(slabs[i]));
			else
				RecipeHandler.addOreDictRecipe(ProxyRegistry.newStack(world_stone_chiseled, 8, i),
						"SSS", "S S", "SSS",
						'S', ProxyRegistry.newStack(world_stone_bricks, 1, i));

		}

		if(BlockWorldStoneBricks.Variants.STONE_BASALT_BRICKS.isEnabled()) {
			RecipeHandler.addOreDictRecipe(ProxyRegistry.newStack(world_stone_bricks, 4, 3),
					"SS", "SS",
					'S', ProxyRegistry.newStack(Basalt.basalt, 1, 1));

			if (enableStairsAndSlabs)
				RecipeHandler.addOreDictRecipe(ProxyRegistry.newStack(world_stone_chiseled, 1, 3),
						"S", "S",
						'S', ProxyRegistry.newStack(slabs[3]));
			else
				RecipeHandler.addOreDictRecipe(ProxyRegistry.newStack(world_stone_chiseled, 8, 3),
						"SSS", "S S", "SSS",
						'S', ProxyRegistry.newStack(world_stone_bricks, 1, 3));
		}
		
		if(BlockWorldStoneBricks.Variants.STONE_MARBLE_BRICKS.isEnabled()) {
			RecipeHandler.addOreDictRecipe(ProxyRegistry.newStack(world_stone_bricks, 4, 4),
					"SS", "SS",
					'S', ProxyRegistry.newStack(RevampStoneGen.marble, 1, 1));

			if (enableStairsAndSlabs)
				RecipeHandler.addOreDictRecipe(ProxyRegistry.newStack(world_stone_chiseled, 1, 4),
						"S", "S",
						'S', ProxyRegistry.newStack(slabs[4]));
			else
				RecipeHandler.addOreDictRecipe(ProxyRegistry.newStack(world_stone_chiseled, 8, 4),
						"SSS", "S S", "SSS",
						'S', ProxyRegistry.newStack(world_stone_bricks, 1, 4));
		}
		
		if(BlockWorldStoneBricks.Variants.STONE_LIMESTONE_BRICKS.isEnabled()) {
			RecipeHandler.addOreDictRecipe(ProxyRegistry.newStack(world_stone_bricks, 4, 5),
					"SS", "SS",
					'S', ProxyRegistry.newStack(RevampStoneGen.limestone, 1, 1));

			if (enableStairsAndSlabs)
				RecipeHandler.addOreDictRecipe(ProxyRegistry.newStack(world_stone_chiseled, 1, 4),
						"S", "S",
						'S', ProxyRegistry.newStack(slabs[4]));
			else
				RecipeHandler.addOreDictRecipe(ProxyRegistry.newStack(world_stone_chiseled, 8, 4),
						"SSS", "S S", "SSS",
						'S', ProxyRegistry.newStack(world_stone_bricks, 1, 4));
		}
	}

	@Override
	public void init() {
		ModIntegrationHandler.registerChiselVariant("granite", new ItemStack(world_stone_bricks, 1, 0));
		ModIntegrationHandler.registerChiselVariant("diorite", new ItemStack(world_stone_bricks, 1, 1));
		ModIntegrationHandler.registerChiselVariant("andesite", new ItemStack(world_stone_bricks, 1, 2));

		if(BlockWorldStoneBricks.Variants.STONE_BASALT_BRICKS.isEnabled())
			ModIntegrationHandler.registerChiselVariant("basalt", new ItemStack(world_stone_bricks, 1, 3));

		if(BlockWorldStoneBricks.Variants.STONE_MARBLE_BRICKS.isEnabled())
			ModIntegrationHandler.registerChiselVariant("marble", new ItemStack(world_stone_bricks, 1, 4));

		if(BlockWorldStoneBricks.Variants.STONE_LIMESTONE_BRICKS.isEnabled())
			ModIntegrationHandler.registerChiselVariant("limestone", new ItemStack(world_stone_bricks, 1, 5));
	}
	
	@Override
	public boolean requiresMinecraftRestartToEnable() {
		return true;
	}

}
