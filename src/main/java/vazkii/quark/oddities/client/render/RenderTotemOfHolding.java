package vazkii.quark.oddities.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import vazkii.arl.client.AtlasSpriteHelper;
import vazkii.arl.util.ClientTicker;
import vazkii.quark.management.client.render.RenderChestPassenger;
import vazkii.quark.oddities.entity.EntityTotemOfHolding;
import vazkii.quark.oddities.feature.TotemOfHolding;
import vazkii.quark.world.client.render.RenderAshen;
import net.minecraft.client.renderer.entity.Render;
import vazkii.quark.oddities.entity.EntityTotemOfHolding;

public class RenderTotemOfHolding extends Render<EntityTotemOfHolding> {

	protected RenderTotemOfHolding(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(EntityTotemOfHolding entity, double x, double y, double z, float entityYaw, float partialTicks) {
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.rotate(ClientTicker.total, 0F, 1F, 0F);
		GlStateManager.translate(-0.5, 0, 0);
		renderTotemIcon();
		GlStateManager.popMatrix();
	}
	
	private void renderTotemIcon() {
		Minecraft mc = Minecraft.getMinecraft();
		mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		AtlasSpriteHelper.renderIconThicc(TotemOfHolding.totemSprite, 1F / 32F);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityTotemOfHolding entity) {
		return null;
	}
	
	public static IRenderFactory factory() {
		return manager -> new RenderTotemOfHolding(manager);
	}

}
