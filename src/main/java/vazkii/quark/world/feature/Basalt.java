/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 *
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 *
 * File Created @ [20/03/2016, 15:05:14 (GMT)]
 */
package vazkii.quark.world.feature;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.arl.block.BlockMod;
import vazkii.arl.block.BlockModSlab;
import vazkii.arl.block.BlockModStairs;
import vazkii.arl.recipe.RecipeHandler;
import vazkii.arl.util.ProxyRegistry;
import vazkii.quark.base.handler.DimensionConfig;
import vazkii.quark.base.module.Feature;
import vazkii.quark.base.module.GlobalConfig;
import vazkii.quark.base.module.ModuleLoader;
import vazkii.quark.building.feature.VanillaWalls;
import vazkii.quark.world.block.BlockBasalt;
import vazkii.quark.world.block.slab.BlockBasaltSlab;
import vazkii.quark.world.block.stairs.BlockBasaltStairs;
import vazkii.quark.world.feature.RevampStoneGen.StoneInfo;
import vazkii.quark.world.world.BasaltGenerator;
import vazkii.quark.world.world.StoneInfoBasedGenerator;

public class Basalt extends Feature {

	public static BlockMod basalt;

	StoneInfo basaltInfo;
	
	boolean enableStairsAndSlabs;
	boolean enableWalls;

	@Override
	public void setupConfig() {
		basaltInfo = RevampStoneGen.loadStoneInfo(configCategory, "basalt", 18, 20, 120, 20, true, "-1", BiomeDictionary.Type.NETHER);
		enableStairsAndSlabs = loadPropBool("Enable stairs and slabs", "", true) && GlobalConfig.enableVariants;
		enableWalls = loadPropBool("Enable walls", "", true) && GlobalConfig.enableVariants;
	}

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		basalt = new BlockBasalt();

		if(enableStairsAndSlabs) {
			BlockModSlab.initSlab(basalt, 0, new BlockBasaltSlab(false), new BlockBasaltSlab(true));
			BlockModStairs.initStairs(basalt, 0, new BlockBasaltStairs());
		}
		VanillaWalls.add("basalt", basalt, 0, enableWalls);

		RecipeHandler.addOreDictRecipe(ProxyRegistry.newStack(basalt, 4, 1),
				"BB", "BB",
				'B', ProxyRegistry.newStack(basalt, 1, 0));
		
		GameRegistry.registerWorldGenerator(new BasaltGenerator(() -> basaltInfo), 0);
	}
	
	@Override
	public void init(FMLInitializationEvent event) {
		OreDictionary.registerOre("stoneBasalt", ProxyRegistry.newStack(basalt, 1, 0));
		OreDictionary.registerOre("stoneBasaltPolished", ProxyRegistry.newStack(basalt, 1, 1));
	}

	@Override
	public void postPreInit(FMLPreInitializationEvent event) {
		ItemStack blackItem = ProxyRegistry.newStack(Items.COAL); 
		if(ModuleLoader.isFeatureEnabled(Biotite.class))
			blackItem = ProxyRegistry.newStack(Biotite.biotite);

		RecipeHandler.addOreDictRecipe(ProxyRegistry.newStack(basalt, 4, 0),
				"BI", "IB",
				'B', ProxyRegistry.newStack(Blocks.COBBLESTONE, 1, 0),
				'I', blackItem);
		RecipeHandler.addShapelessOreDictRecipe(ProxyRegistry.newStack(Blocks.STONE, 1, 5), ProxyRegistry.newStack(basalt), ProxyRegistry.newStack(Items.QUARTZ));
	}
	
	@Override
	public boolean requiresMinecraftRestartToEnable() {
		return true;
	}
	
}
