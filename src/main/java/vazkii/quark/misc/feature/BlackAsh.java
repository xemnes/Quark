package vazkii.quark.misc.feature;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import vazkii.quark.base.module.Feature;
import vazkii.quark.misc.block.BlockBlackAsh;

public class BlackAsh extends Feature {

	public static Block black_ash;
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		black_ash = new BlockBlackAsh();
	}
	
	@Override
	public boolean requiresMinecraftRestartToEnable() {
		return true;
	}
	
}
