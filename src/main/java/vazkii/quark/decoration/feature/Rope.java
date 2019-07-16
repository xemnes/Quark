package vazkii.quark.decoration.feature;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import vazkii.quark.base.module.Feature;
import vazkii.quark.decoration.block.BlockRope;

public class Rope extends Feature {

	public static Block rope;
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		rope = new BlockRope();
	}
	
	@Override
	public boolean requiresMinecraftRestartToEnable() {
		return true;
	}
	
}
