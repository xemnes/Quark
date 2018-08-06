package vazkii.quark.automation.feature;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import vazkii.quark.automation.block.BlockRedstoneRandomizer;
import vazkii.quark.base.module.Feature;

public class RedstoneRandomizer extends Feature {

	public static Block redstone_randomizer;
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		redstone_randomizer = new BlockRedstoneRandomizer();
	}
	
	
}
