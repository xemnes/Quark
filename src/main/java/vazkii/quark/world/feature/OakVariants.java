package vazkii.quark.world.feature;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.quark.base.module.Feature;
import vazkii.quark.world.block.BlockVariantLeaves;

public class OakVariants extends Feature {

	public static Block variant_leaves;
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		variant_leaves = new BlockVariantLeaves();
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void renderEvent(RenderTickEvent event) {
		if(event.phase == Phase.START)
			((BlockLeaves) variant_leaves).setGraphicsLevel(Minecraft.getMinecraft().gameSettings.fancyGraphics);
	}
	
	@Override
	public boolean requiresMinecraftRestartToEnable() {
		return true;
	}
	
	@Override
	public boolean hasSubscriptions() {
		return isClient();
	}

}
