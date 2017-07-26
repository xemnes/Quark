package vazkii.quark.experimental.features;

import java.util.HashSet;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import vazkii.quark.api.IColoredLightSource;
import vazkii.quark.base.module.Feature;
import vazkii.quark.experimental.lighting.BlockTinter;
import vazkii.quark.experimental.lighting.ColoredLightSystem;

public class ColoredLights extends Feature {

	private static boolean enabled;
	
	public static void putColorsFlat(IBlockAccess world, IBlockState state, BlockPos pos, BufferBuilder buffer, BakedQuad quad, int lightColor) {
		if(!enabled)
			return;
		
		BlockTinter.tintBlockFlat(world, state, pos, buffer, quad, lightColor);
	}
	
	public static void addLightSource(IBlockAccess access, BlockPos pos, IBlockState state, int brightness) {
		if(enabled)
			ColoredLightSystem.addLightSource(access, pos, state, brightness);
	}

	@Override
	public void onEnabled() {
		enabled = true;
	}

	@Override
	public void onDisabled() {
		enabled = false;
	}

	@SubscribeEvent
	public void preRenderTick(RenderTickEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		if(event.phase != Phase.START)
			return;
		
		ColoredLightSystem.tick(mc);
	}

	@Override
	public boolean hasSubscriptions() {
		return isClient();
	}

}
