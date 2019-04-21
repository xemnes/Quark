package vazkii.quark.world.feature;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import vazkii.quark.base.module.Feature;
import vazkii.quark.world.block.BlockRoots;
import vazkii.quark.world.world.CaveRootGenerator;

public class CaveRoots extends Feature {

	public static int chunkAttempts, minY, maxY;
	public static boolean enableFlowers;
	public static float flowerChance;
	
	public static Block roots;
	public static Block roots_blue_flower, roots_black_flower, roots_white_flower;

	@Override
	public void setupConfig() {
		chunkAttempts = loadPropInt("Attempts per Chunk", "How many times the world generator will try to place roots per chunk", 300);
		minY = loadPropInt("Min Y", "", 16);
		maxY = loadPropInt("Max Y", "", 52);
		enableFlowers = loadPropBool("Enable Flowers", "", true);
		flowerChance = (float) loadPropDouble("Flower Chance", "The chance for a root to sprout a flower when it grows. 0 is 0%, 1 is 100%", 0.1);
	}
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		roots = new BlockRoots();
		
		if(enableFlowers) {
			roots_blue_flower = new BlockRoots("roots_blue_flower");
			roots_black_flower = new BlockRoots("roots_black_flower");
			roots_white_flower = new BlockRoots("roots_white_flower");
		}
		
		GameRegistry.registerWorldGenerator(new CaveRootGenerator(), 2000);
	}
	
}
