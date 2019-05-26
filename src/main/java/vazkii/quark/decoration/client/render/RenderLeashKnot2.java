package vazkii.quark.decoration.client.render;

import net.minecraft.client.model.ModelLeashKnot;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import org.lwjgl.opengl.GL11;
import vazkii.quark.decoration.entity.EntityLeashKnot2TheKnotting;

import javax.annotation.Nonnull;

// Basically a copy of RenderLeashKnot but with a 2 cuz of some render things
public class RenderLeashKnot2 extends Render<EntityLeashKnot2TheKnotting> {
	
	private static final ResourceLocation LEASH_KNOT_TEXTURES = new ResourceLocation("textures/entity/lead_knot.png");
	private final ModelLeashKnot leashKnotModel = new ModelLeashKnot();

	public static final IRenderFactory<EntityLeashKnot2TheKnotting> FACTORY = RenderLeashKnot2::new;
	
	public RenderLeashKnot2(RenderManager renderManagerIn) {
		super(renderManagerIn);
	}

	@Override
    public boolean shouldRender(EntityLeashKnot2TheKnotting livingEntity, @Nonnull ICamera camera, double camX, double camY, double camZ)  {
        if(super.shouldRender(livingEntity, camera, camX, camY, camZ))
            return true;
        else if(livingEntity.getLeashed()) {
            Entity entity = livingEntity.getLeashHolder();
            return camera.isBoundingBoxInFrustum(entity.getRenderBoundingBox());
        }
        else return false;
    }
	
	@Override
	public void doRender(@Nonnull EntityLeashKnot2TheKnotting entity, double x, double y, double z, float entityYaw, float partialTicks) {
		GlStateManager.pushMatrix();
		GlStateManager.disableCull();
		float f = 1F / 8F;
		GlStateManager.translate(x, y + f, z);
		GlStateManager.enableRescaleNormal();
		GlStateManager.scale(-1.0F, -1.0F, 1.0F);
		GlStateManager.enableAlpha();
		bindEntityTexture(entity);

		if(renderOutlines) {
			GlStateManager.enableColorMaterial();
			GlStateManager.enableOutlineMode(getTeamColor(entity));
		}

		leashKnotModel.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);

		if(renderOutlines) {
			GlStateManager.disableOutlineMode();
			GlStateManager.disableColorMaterial();
		}

		GlStateManager.popMatrix();
		
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		
		if(!renderOutlines)
			renderLeash(entity, x, y, z, partialTicks);
	}

	@Override
	protected ResourceLocation getEntityTexture(@Nonnull EntityLeashKnot2TheKnotting entity) {
		return LEASH_KNOT_TEXTURES;
	}
	
	// ================================ LEASH RENDER THINGS ================================ 
	
    protected void renderLeash(EntityLeashKnot2TheKnotting entityLivingIn, double x, double y, double z, float partialTicks)
    {
        Entity entity = entityLivingIn.getLeashHolder();

		y = y - (2.9D - entityLivingIn.height) * 0.5D;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		double d0 = this.interpolateValue(entity.prevRotationYaw, entity.rotationYaw, (partialTicks * 0.5F)) * 0.01745329238474369D;
		double d1 = this.interpolateValue(entity.prevRotationPitch, entity.rotationPitch, (partialTicks * 0.5F)) * 0.01745329238474369D;
		double d2 = Math.cos(d0);
		double d3 = Math.sin(d0);
		double d4 = Math.sin(d1);

		float off = 1.3F;
		if (entity instanceof EntityHanging)
		{
			off = 1.1F;
			d2 = 0.0D;
			d3 = 0.0D;
			d4 = -1.0D;
		}


		double d5 = Math.cos(d1);
		double d6 = this.interpolateValue(entity.prevPosX, entity.posX, partialTicks) - d2 * 0.7D - d3 * 0.5D * d5;
		double d7 = this.interpolateValue(entity.prevPosY + entity.getEyeHeight() * 0.7D + off, entity.posY + entity.getEyeHeight() * 0.7D + off, partialTicks) - d4 * 0.5D - 0.25D;
		double d8 = this.interpolateValue(entity.prevPosZ, entity.posZ, partialTicks) - d3 * 0.7D + d2 * 0.5D * d5;
		double d9 = this.interpolateValue(entityLivingIn.prevRenderYawOffset, entityLivingIn.renderYawOffset, partialTicks) * 0.01745329238474369D + (Math.PI / 2D);
		d2 = Math.cos(d9) * entityLivingIn.width * 0.4D;
		d3 = Math.sin(d9) * entityLivingIn.width * 0.4D;
		double d10 = this.interpolateValue(entityLivingIn.prevPosX, entityLivingIn.posX, partialTicks) + d2;
		double d11 = this.interpolateValue(entityLivingIn.prevPosY, entityLivingIn.posY, partialTicks);
		double d12 = this.interpolateValue(entityLivingIn.prevPosZ, entityLivingIn.posZ, partialTicks) + d3;
		x = x + d2;
		z = z + d3;
		double d13 = ((float)(d6 - d10));
		double d14 = ((float)(d7 - d11));
		double d15 = ((float)(d8 - d12));
		GlStateManager.disableTexture2D();
		GlStateManager.disableLighting();
		GlStateManager.disableCull();
		bufferbuilder.begin(GL11.GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION_COLOR);

		for (int j = 0; j <= 24; ++j)
		{
			float f = 0.5F;
			float f1 = 0.4F;
			float f2 = 0.3F;

			if (j % 2 == 0)
			{
				f *= 0.7F;
				f1 *= 0.7F;
				f2 *= 0.7F;
			}

			float f3 = j / 24.0F;
			bufferbuilder.pos(x + d13 * f3 + 0.0D, y + d14 * (f3 * f3 + f3) * 0.5D + ((24.0F - j) / 18.0F + 0.125F), z + d15 * f3).color(f, f1, f2, 1.0F).endVertex();
			bufferbuilder.pos(x + d13 * f3 + 0.025D, y + d14 * (f3 * f3 + f3) * 0.5D + ((24.0F - j) / 18.0F + 0.125F) + 0.025D, z + d15 * f3).color(f, f1, f2, 1.0F).endVertex();
		}

		tessellator.draw();
		bufferbuilder.begin(GL11.GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION_COLOR);

		for (int k = 0; k <= 24; ++k)
		{
			float f4 = 0.5F;
			float f5 = 0.4F;
			float f6 = 0.3F;

			if (k % 2 == 0)
			{
				f4 *= 0.7F;
				f5 *= 0.7F;
				f6 *= 0.7F;
			}

			float f7 = k / 24.0F;
			bufferbuilder.pos(x + d13 * f7 + 0.0D, y + d14 * (f7 * f7 + f7) * 0.5D + ((24.0F - k) / 18.0F + 0.125F) + 0.025D, z + d15 * f7).color(f4, f5, f6, 1.0F).endVertex();
			bufferbuilder.pos(x + d13 * f7 + 0.025D, y + d14 * (f7 * f7 + f7) * 0.5D + ((24.0F - k) / 18.0F + 0.125F), z + d15 * f7 + 0.025D).color(f4, f5, f6, 1.0F).endVertex();
		}

		tessellator.draw();
		GlStateManager.enableLighting();
		GlStateManager.enableTexture2D();
		GlStateManager.enableCull();
	}
    
    private double interpolateValue(double start, double end, double pct)
    {
        return start + (end - start) * pct;
    }
}
