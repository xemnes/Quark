package vazkii.quark.tools.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.util.ResourceLocation;
import vazkii.quark.tools.entity.PickarangEntity;

import javax.annotation.Nonnull;

public class PickarangRenderer extends EntityRenderer<PickarangEntity> {

	public PickarangRenderer(EntityRendererManager renderManager) {
		super(renderManager);
	}
	
	@Override
	public void doRender(@Nonnull PickarangEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
		GlStateManager.pushMatrix();
		GlStateManager.translated(x, y + 0.2, z);
		GlStateManager.rotatef(90F, 1F, 0F, 0F);
		
		Minecraft mc = Minecraft.getInstance();
		float time = entity.ticksExisted + (mc.isGamePaused() ? 0 : partialTicks);
		GlStateManager.rotatef(time * 20F, 0F, 0F, 1F);

		GlStateManager.enableBlend();
		mc.getItemRenderer().renderItem(entity.getStack(), TransformType.FIXED);
		
		GlStateManager.popMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(@Nonnull PickarangEntity entity) {
		return null;
	}

}
