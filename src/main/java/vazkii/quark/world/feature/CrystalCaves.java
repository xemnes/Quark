package vazkii.quark.world.feature;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import vazkii.quark.base.module.Feature;
import vazkii.quark.world.block.BlockCrystal;

public class CrystalCaves extends Feature {

	public static Block crystal;
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		crystal = new BlockCrystal();
	}
	
}
