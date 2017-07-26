package vazkii.quark.experimental.lighting;

import java.util.Arrays;

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
import net.minecraftforge.client.model.pipeline.LightUtil;

public class BlockTinter {

	public static void tintBlockFlat(IBlockAccess world, IBlockState state, BlockPos pos, BufferBuilder buffer, BakedQuad quad, int lightColor) {
		if((lightColor & 0xFF) == 0)
			return;
		
		float[] colors = ColoredLightSystem.getLightColor(world, pos.offset(quad.getFace()));
		if(colors.length > 0) {
			float[] quadTint = tintQuad(quad, state, world, pos, buffer);
			if(quadTint != null)
				for(int i = 0; i < 3; i++)
					colors[i] *= quadTint[i];
			
			for(int i = 1; i < 5; i++)
				buffer.putColorRGB_F(colors[0], colors[1], colors[2], i);
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
	
}
