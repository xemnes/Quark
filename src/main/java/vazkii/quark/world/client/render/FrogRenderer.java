package vazkii.quark.world.client.render;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import vazkii.quark.world.client.model.FrogModel;
import vazkii.quark.world.entity.FrogEntity;

import javax.annotation.Nonnull;

public class FrogRenderer extends MobRenderer<FrogEntity, FrogModel> {

	private static final ResourceLocation TEXTURE = new ResourceLocation("quark", "textures/model/entity/frog.png");

	public FrogRenderer(EntityRendererManager manager) {
		super(manager, new FrogModel(), 0.2F);
	}

	@Override
	protected ResourceLocation getEntityTexture(@Nonnull FrogEntity entity) {
		return TEXTURE;
	}

}
