package vazkii.quark.world.feature;

import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.arl.block.BlockMod;
import vazkii.arl.block.BlockModSlab;
import vazkii.arl.block.BlockModStairs;
import vazkii.arl.util.RecipeHandler;
import vazkii.quark.base.module.Feature;
import vazkii.quark.base.module.ModuleLoader;
import vazkii.quark.building.feature.VanillaWalls;
import vazkii.quark.world.block.BlockLimestone;
import vazkii.quark.world.block.BlockMarble;
import vazkii.quark.world.block.slab.BlockLimestoneSlab;
import vazkii.quark.world.block.slab.BlockMarbleSlab;
import vazkii.quark.world.block.stairs.BlockLimestoneStairs;
import vazkii.quark.world.block.stairs.BlockMarbleStairs;
import vazkii.quark.world.world.StoneInfoBasedGenerator;

public class RevampStoneGen extends Feature {

	public static BlockMod marble;
	public static BlockMod limestone;

	boolean enableStairsAndSlabs;
	boolean enableWalls;
	public static boolean enableMarble;
	public static boolean enableLimestone;

	public static StoneInfo graniteInfo, dioriteInfo, andesiteInfo, marbleInfo, limestoneInfo;

	@Override
	public void setupConfig() {
		enableStairsAndSlabs = loadPropBool("Enable stairs and slabs", "", true);
		enableWalls = loadPropBool("Enable walls", "", true);
		enableMarble = loadPropBool("Enable Marble", "", true);
		enableLimestone = loadPropBool("Enable Limestone", "", true);

		int defSize = 200;
		int defRarity = 50;
		int defUpper = 80;
		int defLower = 20;

		graniteInfo = loadStoneInfo("granite", defSize, defRarity, defUpper, defLower, true);
		dioriteInfo = loadStoneInfo("diorite", defSize, defRarity, defUpper, defLower, true);
		andesiteInfo = loadStoneInfo("andesite", defSize, defRarity, defUpper, defLower, true);
		marbleInfo = loadStoneInfo("marble", defSize, defRarity, defUpper, defLower, enableMarble);
		limestoneInfo = loadStoneInfo("limestone", defSize, defRarity, defUpper, defLower, enableLimestone);
	}

	private StoneInfo loadStoneInfo(String name, int clusterSize, int clusterRarity, int upperBound, int lowerBound, boolean enabled) {
		String category = configCategory + "." + name;
		StoneInfo info = new StoneInfo(category, clusterSize, clusterRarity, upperBound, lowerBound, enabled);

		return info;
	}

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		if(enableMarble) {
			marble = new BlockMarble();

			if(enableStairsAndSlabs) {
				BlockModSlab.initSlab(marble, 0, new BlockMarbleSlab(false), new BlockMarbleSlab(true));
				BlockModStairs.initStairs(marble, 0, new BlockMarbleStairs());
			}

			VanillaWalls.add("marble", marble, 0, enableWalls);

			OreDictionary.registerOre("stoneMarble", new ItemStack(marble, 1, 0));
			OreDictionary.registerOre("stoneMarblePolished", new ItemStack(marble, 1, 1));

			RecipeHandler.addOreDictRecipe(new ItemStack(marble, 4, 1),
					"BB", "BB",
					'B', new ItemStack(marble, 1, 0));
		}

		if(enableLimestone) {
			limestone = new BlockLimestone();

			if(enableStairsAndSlabs) {
				BlockModSlab.initSlab(limestone, 0, new BlockLimestoneSlab(false), new BlockLimestoneSlab(true));
				BlockModStairs.initStairs(limestone, 0, new BlockLimestoneStairs());
			}

			VanillaWalls.add("limestone", limestone, 0, enableWalls);
			OreDictionary.registerOre("stoneLimestone", new ItemStack(limestone, 1, 0));
			OreDictionary.registerOre("stoneLimestonePolished", new ItemStack(limestone, 1, 1));

			RecipeHandler.addOreDictRecipe(new ItemStack(limestone, 4, 1),
					"BB", "BB",
					'B', new ItemStack(limestone, 1, 0));
		}

		IBlockState graniteState = Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE);
		IBlockState dioriteState = Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE);
		IBlockState andesiteState = Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE);

		GameRegistry.registerWorldGenerator(new StoneInfoBasedGenerator(() -> graniteInfo, graniteState, "granite"), 0);
		GameRegistry.registerWorldGenerator(new StoneInfoBasedGenerator(() -> dioriteInfo, dioriteState, "diorite"), 0);
		GameRegistry.registerWorldGenerator(new StoneInfoBasedGenerator(() -> andesiteInfo, andesiteState, "andesite"), 0);

		if(enableMarble)
			GameRegistry.registerWorldGenerator(new StoneInfoBasedGenerator(() -> marbleInfo, marble.getDefaultState(), "marble"), 0);
		if(enableLimestone)
			GameRegistry.registerWorldGenerator(new StoneInfoBasedGenerator(() -> limestoneInfo, limestone.getDefaultState(), "limestone"), 0);
	}

	@SubscribeEvent
	public void onOreGenerate(OreGenEvent.GenerateMinable event) {
		switch(event.getType()) {
		case GRANITE:
			if(graniteInfo.enabled)
				event.setResult(Result.DENY);
			break;
		case DIORITE:
			if(dioriteInfo.enabled)
				event.setResult(Result.DENY);
			break;
		case ANDESITE:
			if(andesiteInfo.enabled)
				event.setResult(Result.DENY);
			break;
		default: return;
		}
	}


	@Override
	public boolean hasOreGenSubscriptions() {
		return true;
	}

	@Override
	public boolean requiresMinecraftRestartToEnable() {
		return true;
	}

	public static class StoneInfo {

		public final boolean enabled;
		public final int clusterSize, clusterRarity, upperBound, lowerBound;
		public final boolean clustersRarityPerChunk;

		private StoneInfo(String category, int clusterSize, int clusterRarity, int upperBound, int lowerBound, boolean enabled) {
			this.enabled = ModuleLoader.config.getBoolean("Enabled", category, true, "") && enabled;
			this.clusterSize = ModuleLoader.config.getInt("Cluster Size", category, clusterSize, 0, Integer.MAX_VALUE, "");
			this.clusterRarity = ModuleLoader.config.getInt("Cluster Rarity", category, clusterRarity, 0, Integer.MAX_VALUE, "Out of how many chunks would one of these clusters generate");
			this.upperBound = ModuleLoader.config.getInt("Y Level Max", category, upperBound, 0, 255, "");
			this.lowerBound = ModuleLoader.config.getInt("Y Level Min", category, lowerBound, 0, 255, "");
			clustersRarityPerChunk = ModuleLoader.config.getBoolean("Invert Cluster Rarity", category, false, "Setting this to true will make the 'Cluster Rarity' feature be X per chunk rather than 1 per X chunks");
		}
	}

}

