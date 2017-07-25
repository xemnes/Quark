package vazkii.quark.experimental.features;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import vazkii.quark.api.IColoredLightSource;
import vazkii.quark.base.module.Feature;

public class ColoredLights extends Feature {

	private static Set<BlockPos> currentSources = new HashSet();
	private static Set<BlockPos> lightSources = new HashSet();

	private static boolean enabled;
	
	// TODO figure out how to get light updates to work on chunks that don't need it 
	// TODO do the thing for smooth
	
	public static void putColorsFlat(IBlockAccess world, IBlockState state, BlockPos pos, BufferBuilder buffer, BakedQuad quad, int lightColor) {
		if(!enabled)
			return;
		
		float totalIncidence = 0;
		float maxBrightness = 0;
		float addR, addG, addB, r, g, b;
		addR = addG = addB = 0;
		
		if((lightColor & 0xFF) == 0)
			return;
		
		for(BlockPos src : currentSources) {
			IBlockState srcState = world.getBlockState(src);
			Block srcBlock = srcState.getBlock();
			if(!(srcBlock instanceof IColoredLightSource))
				continue;
			
			int srcLight = srcState.getLightValue(world, src);
			float brightness = (float) srcLight / 15F;

			int dist = Math.abs(pos.getX() - src.getX()) + Math.abs(pos.getY() - src.getY()) + Math.abs(pos.getZ() - src.getZ());
			
			if(dist < 15) {
				float incidence = Math.min(1F, (15F - dist) / 15F);
				float negIncidence = 1F - incidence;
				float localBrightness = brightness * incidence;
				
				float[] colors = ((IColoredLightSource) srcBlock).getColoredLight(world, src);
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

			float[] quadTint = tintQuad(quad, state, world, pos, buffer);
			if(quadTint != null) {
				addR *= quadTint[0];
				addG *= quadTint[1];
				addB *= quadTint[2];
			}
			
			for(int i = 1; i < 5; i++)
				buffer.putColorRGB_F(addR, addG, addB, i);
		}
	}
	
	// Copied from vanilla BlockModelRenderer
	private static float[] tintQuad(BakedQuad bakedquad, IBlockState stateIn, IBlockAccess blockAccessIn, BlockPos posIn, BufferBuilder buffer) {
		if(bakedquad.hasTintIndex()) {
            int k = Minecraft.getMinecraft().getBlockColors().colorMultiplier(stateIn, blockAccessIn, posIn, bakedquad.getTintIndex());

            if(EntityRenderer.anaglyphEnable)
                k = TextureUtil.anaglyphColor(k);

            float f = (float)(k >> 16 & 255) / 255.0F;
            float f1 = (float)(k >> 8 & 255) / 255.0F;
            float f2 = (float)(k & 255) / 255.0F;
            if(bakedquad.shouldApplyDiffuseLighting()) {
                float diffuse = LightUtil.diffuseLight(bakedquad.getFace());
                f *= diffuse;
                f1 *= diffuse;
                f2 *= diffuse;
            }
            
            return new float[] { f, f1, f2 };
        }
        else if(bakedquad.shouldApplyDiffuseLighting()) {
            float diffuse = LightUtil.diffuseLight(bakedquad.getFace());
            return new float[] { diffuse, diffuse, diffuse };
        }
		
		return null;
	}

	public static void addLightSource(BlockPos pos) {
		if(enabled) // TODO implement a check that allows for vanilla blocks to have color
			lightSources.add(pos);
	}
 	
	public static void appendLightSource(BlockPos pos) {
		lightSources.add(pos);
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
		
		// XXX needs to be done for now to allow the colored light to work properly
		ForgeModContainer.forgeLightPipelineEnabled = false;
		mc.gameSettings.ambientOcclusion = 0;

		World world = mc.world;
		if(world == null)
			lightSources.clear();
		
		lightSources.removeIf((pos) -> !mc.world.isBlockLoaded(pos)|| !(mc.world.getBlockState(pos).getBlock() instanceof IColoredLightSource));
		currentSources = new HashSet(lightSources);
	}

	@Override
	public boolean hasSubscriptions() {
		return isClient();
	}

}
