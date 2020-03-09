package vazkii.quark.base.client;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.quark.base.Quark;
import vazkii.quark.vanity.module.ColorRunesModule;

// this only extends RenderState so we can use the constants
@OnlyIn(Dist.CLIENT)
public abstract class QuarkRenderTypes extends RenderState {

	public static final ResourceLocation RES_WHITE_GLINT = new ResourceLocation(Quark.MOD_ID, "textures/misc/white_glint.png");

	public static RenderType[][] generateRenderTypes(int[] colors) {
		RenderType[][] arr = new RenderType[2][colors.length + 1];
		
		int defaultColor = 0xFF8040CC;
		float itemScale = 8F;
		float entityScale = 0.16F;
		
		arr[0][0] = generateColoredGlint(defaultColor, itemScale);
		arr[1][0] = generateColoredGlint(defaultColor, entityScale);
		
		for(int i = 0; i < colors.length; i++) {
			int color = colors[i];
			arr[0][i + 1] = generateColoredGlint(color, itemScale);
			arr[1][i + 1] = generateColoredGlint(color, entityScale);
		}
		
		return arr;
	}
	
	public static RenderType generateColoredGlint(int color, float scale) {
		int a = (color >> 24) & 0xFF;
		int r = (color >> 16) & 0xFF;
		int g = (color >> 8) & 0xFF;
		int b = color & 0xFF;
		
		return RenderType.makeType("quark:color_entity_glint", DefaultVertexFormats.POSITION_TEX, 7, 256,
				RenderType.State.getBuilder()
				.texture(new RenderState.TextureState(RES_WHITE_GLINT, true, false))
				.writeMask(COLOR_WRITE)
				.cull(CULL_DISABLED)
				.depthTest(DEPTH_EQUAL)
				.transparency(GLINT_TRANSPARENCY)
				.texturing(new RenderState.TexturingState("quark:color_glint_texturing", () -> {
					RenderSystem.matrixMode(5890);
					RenderSystem.pushMatrix();
					RenderSystem.loadIdentity();
					long i = Util.milliTime() * 8L;
					float f = (float)(i % 110000L) / 110000.0F;
					float f1 = (float)(i % 30000L) / 30000.0F;
					RenderSystem.translatef(-f, f1, 0.0F);
					RenderSystem.rotatef(10.0F, 0.0F, 0.0F, 1.0F);
					RenderSystem.scalef(scale, scale, scale);
					RenderSystem.matrixMode(5888);
					RenderSystem.color4f((float) r / 255f, (float) g / 255f, (float) b / 255f, (float) a / 255f);
				}, () -> {
					RenderSystem.matrixMode(5890);
					RenderSystem.popMatrix();
					RenderSystem.matrixMode(5888);
				}))
				.build(false));
	}

	private QuarkRenderTypes(String nameIn, Runnable setupTaskIn, Runnable clearTaskIn) {
		super(nameIn, setupTaskIn, clearTaskIn);
	}

}
