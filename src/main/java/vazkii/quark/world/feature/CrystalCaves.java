package vazkii.quark.world.feature;

import net.minecraft.block.Block;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import vazkii.quark.base.module.Feature;
import vazkii.quark.world.block.BlockCrystal;
import vazkii.quark.world.world.CrystalCaveGenerator;

public class CrystalCaves extends Feature {

	public static Block crystal;
	
	public static int crystalCaveRarity;
	
	@Override
	public void setupConfig() {
		crystalCaveRarity = loadPropInt("Crystal Cave Rarity", "Given this value as X, crystal caves will spawn on average 1 per X chunks", 150);
	}
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		crystal = new BlockCrystal();
		
		GameRegistry.registerWorldGenerator(new CrystalCaveGenerator(), 1);
	}
	
}
