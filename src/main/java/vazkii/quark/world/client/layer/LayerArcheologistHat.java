package vazkii.quark.world.client.layer;

import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import vazkii.quark.world.client.model.ModelArcheologistHat;
import vazkii.quark.world.entity.EntityArcheologist;
import vazkii.quark.world.item.ItemArcheologistHat;

import javax.annotation.Nonnull;

public class LayerArcheologistHat implements LayerRenderer<EntityArcheologist> {

	private final RenderLivingBase<?> render;

	public LayerArcheologistHat(RenderLivingBase<?> render) {
		this.render = render;
	}
	
	@Override
	public void doRenderLayer(@Nonnull EntityArcheologist archaeologist, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		render.bindTexture(ItemArcheologistHat.TEXTURE);
		
		if(ItemArcheologistHat.headModel == null)
			ItemArcheologistHat.headModel = new ModelArcheologistHat();
		ItemArcheologistHat.headModel.render(archaeologist, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}

}
