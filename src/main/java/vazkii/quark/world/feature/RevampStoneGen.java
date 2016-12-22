package vazkii.quark.world.feature;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.arl.block.BlockMod;
import vazkii.arl.block.BlockModSlab;
import vazkii.arl.block.BlockModStairs;
import vazkii.arl.util.RecipeHandler;
import vazkii.quark.base.module.Feature;
import vazkii.quark.building.feature.VanillaWalls;
import vazkii.quark.world.block.BlockLimestone;
import vazkii.quark.world.block.BlockMarble;
import vazkii.quark.world.block.slab.BlockLimestoneSlab;
import vazkii.quark.world.block.slab.BlockMarbleSlab;
import vazkii.quark.world.block.stairs.BlockLimestoneStairs;
import vazkii.quark.world.block.stairs.BlockMarbleStairs;

public class RevampStoneGen extends Feature {

	public static BlockMod marble;
	public static BlockMod limestone;

	boolean enableStairsAndSlabs;
	boolean enableWalls;

	@Override
	public void setupConfig() {
		enableStairsAndSlabs = loadPropBool("Enable stairs and slabs", "", true);
		enableWalls = loadPropBool("Enable walls", "", true);
	}

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		marble = new BlockMarble();
		limestone = new BlockLimestone();

		if(enableStairsAndSlabs) {
			BlockModSlab.initSlab(marble, 0, new BlockMarbleSlab(false), new BlockMarbleSlab(true));
			BlockModStairs.initStairs(marble, 0, new BlockMarbleStairs());
			
			BlockModSlab.initSlab(limestone, 0, new BlockLimestoneSlab(false), new BlockLimestoneSlab(true));
			BlockModStairs.initStairs(limestone, 0, new BlockLimestoneStairs());
		}
		VanillaWalls.add("marble", marble, 0, enableWalls);
		VanillaWalls.add("limestone", limestone, 0, enableWalls);

		OreDictionary.registerOre("stoneMarble", new ItemStack(marble, 1, 0));
		OreDictionary.registerOre("stoneMarblePolished", new ItemStack(marble, 1, 1));
		OreDictionary.registerOre("stoneLimestone", new ItemStack(limestone, 1, 0));
		OreDictionary.registerOre("stoneLimestonePolished", new ItemStack(limestone, 1, 1));
		
		RecipeHandler.addOreDictRecipe(new ItemStack(marble, 4, 1),
				"BB", "BB",
				'B', new ItemStack(marble, 1, 0));
		RecipeHandler.addOreDictRecipe(new ItemStack(limestone, 4, 1),
				"BB", "BB",
				'B', new ItemStack(limestone, 1, 0));
		
//		GameRegistry.registerWorldGenerator(new BasaltGenerator(nether, overworld, clusterSizeOverworld, clusterSizeNether, clusterCountOverworld, clusterCountNether), 0);
	}

	@Override
	public boolean requiresMinecraftRestartToEnable() {
		return true;
	}

}
