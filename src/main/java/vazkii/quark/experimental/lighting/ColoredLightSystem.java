package vazkii.quark.experimental.lighting;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeModContainer;
import vazkii.quark.api.IColoredLightSource;
import vazkii.quark.base.client.ClientTicker;

public final class ColoredLightSystem {

	// TODO figure out how to get light updates to work on chunks that don't need it 
	
	private static Set<LightSource> lightSources = new HashSet();
	private static Set<LightSource> currentSources = new HashSet();
	
	private static int lastFrame;
	
	public static void tick(Minecraft mc) {
		ForgeModContainer.forgeLightPipelineEnabled = false;
		mc.gameSettings.ambientOcclusion = 0; // TODO implement smooth light

		World world = mc.world;
		if(world == null) {
			lightSources.clear();
			currentSources.clear();
		}
		
		HashSet<LightSource> tempSet = new HashSet(lightSources);
		tempSet.removeIf((src) -> !mc.world.isBlockLoaded(src.pos)|| !(mc.world.getBlockState(src.pos).getBlock() instanceof IColoredLightSource));
		currentSources = tempSet;
	}
	
	public static float[] getLightColor(IBlockAccess world, BlockPos pos) {
		float totalIncidence = 0;
		float maxBrightness = 0;
		float addR, addG, addB, r, g, b;
		addR = addG = addB = 0;
		
		int time = ClientTicker.ticksInGame;
		if(time != lastFrame)
			prepareFrame();
		lastFrame = time;
		
		for(LightSource src : currentSources) {
			BlockPos srcpos = src.pos;
			IBlockState srcState = world.getBlockState(srcpos);
			Block srcBlock = srcState.getBlock();
			if(!(srcBlock instanceof IColoredLightSource))
				continue;
			
			int srcLight = srcState.getLightValue(world, srcpos);
			float brightness = (float) srcLight / 15F;

			int incidence = src.getIndidence(pos);
			
			if(incidence > 0) {
				float incidenceF = (float) incidence / 15F;
				float negIncidence = 1F - incidenceF;
				float localBrightness = brightness * incidenceF;
				
				float[] colors = ((IColoredLightSource) srcBlock).getColoredLight(world, srcpos);
				if(colors.length != 3)
					colors = new float[] { 1F, 1F, 1F };
				
				maxBrightness = Math.max(maxBrightness, localBrightness);
				
				addR += colors[0] * localBrightness;
				addG += colors[1] * localBrightness;
				addB += colors[2] * localBrightness;
			}
		}
		
		float strongestColor = Math.max(addR, Math.max(addG, addB));
		
		if(maxBrightness > 0 && strongestColor > 0) {
			float lower = 1F - maxBrightness;
			addR /= strongestColor;
			addG /= strongestColor;
			addB /= strongestColor;
			
			addR = MathHelper.clamp(addR, lower, 1F);
			addG = MathHelper.clamp(addG, lower, 1F);
			addB = MathHelper.clamp(addB, lower, 1F);
			
			return new float[] { addR, addG, addB };
		}
		
		return new float[0];
	}
	
	private static void prepareFrame() {
		for(LightSource src : currentSources)
			src.newFrame();
	}
	
	public static void addLightSource(IBlockAccess access, BlockPos pos, IBlockState state, int brightness) {
		// TODO rework for vanilla lights (probably ASM)
		lightSources.add(new LightSource(access, pos, state, brightness));
	}
	
}
