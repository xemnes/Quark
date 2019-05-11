package vazkii.quark.world.client.render;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import vazkii.arl.util.ClientTicker;
import vazkii.quark.world.client.model.ModelStoneling;
import vazkii.quark.world.entity.EntityStoneling;

public class RenderStoneling extends RenderLiving<EntityStoneling> {

	private static final ResourceLocation[] TEXTURES = new ResourceLocation[] {
			new ResourceLocation("quark", "textures/entity/stoneling.png"),
			new ResourceLocation("quark", "textures/entity/stoneling_andesite.png"),
			new ResourceLocation("quark", "textures/entity/stoneling_diorite.png"),
			new ResourceLocation("quark", "textures/entity/stoneling_granite.png"),
			new ResourceLocation("quark", "textures/entity/stoneling_limestone.png")
	};

	public static final IRenderFactory FACTORY = (RenderManager manager) -> new RenderStoneling(manager);
	
	protected RenderStoneling(RenderManager renderManager) {
		super(renderManager, new ModelStoneling(), 0.3F);
	}
	
	@Override
	protected void renderLivingAt(EntityStoneling stoneling, double x, double y, double z) {
		super.renderLivingAt(stoneling, x, y, z);
		
		if(stoneling.deathTime > 0)
			return;
		
		float scale = 0.75F;
		float rot = stoneling.rotationYaw * ClientTicker.partialTicks + stoneling.prevRotationYaw * (1F - ClientTicker.partialTicks);
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(0F, 1.01F, 0F);
		GlStateManager.rotate((int) stoneling.getEntityId() % 360 - rot, 0F, 1F, 0F);
		
		GlStateManager.rotate(90F, 1F, 0F, 0F);
		GlStateManager.scale(scale, scale, scale);
		ItemStack stack = stoneling.getCarryingItem();
		Minecraft mc = Minecraft.getMinecraft();
		mc.getRenderItem().renderItem(stack, TransformType.FIXED);
		GlStateManager.popMatrix();
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityStoneling entity) {
		return TEXTURES[Math.abs((int) entity.getUniqueID().getLeastSignificantBits()) % TEXTURES.length];
	}

}
