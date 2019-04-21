package vazkii.quark.world.feature;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import vazkii.quark.base.module.Feature;
import vazkii.quark.world.block.BlockRoots;
import vazkii.quark.world.world.CaveRootGenerator;
import vazkii.quark.world.world.NetherFossilGenerator;

public class CaveRoots extends Feature {

	public static int chunkAttempts, minY, maxY;
	
	public static Block roots;
	
	@Override
	public void setupConfig() {
		chunkAttempts = loadPropInt("Attempts per Chunk", "How many times the world generator will try to place roots per chunk", 300);
		minY = loadPropInt("Min Y", "", 16);
		maxY = loadPropInt("Max Y", "", 52);
	}
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		roots = new BlockRoots();
		
		GameRegistry.registerWorldGenerator(new CaveRootGenerator(), 2000);
	}
	
}
