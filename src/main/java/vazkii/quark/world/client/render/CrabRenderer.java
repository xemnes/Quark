package vazkii.quark.world.client.render;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import vazkii.quark.world.client.model.CrabModel;
import vazkii.quark.world.entity.CrabEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CrabRenderer extends MobRenderer<CrabEntity, CrabModel> {

	private static final ResourceLocation TEXTURE = new ResourceLocation("quark", "textures/model/entity/crab.png");

	public CrabRenderer(EntityRendererManager render) {
		super(render, new CrabModel(), 0.25F);
	}

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(@Nonnull CrabEntity entity) {
		return TEXTURE;
	}
}
