package vazkii.quark.world.client.render;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import vazkii.quark.base.proxy.ClientProxy;
import vazkii.quark.world.client.model.FrogModel;
import vazkii.quark.world.entity.FrogEntity;

import javax.annotation.Nonnull;

public class FrogRenderer extends MobRenderer<FrogEntity, FrogModel> {

	private static final ResourceLocation TEXTURE = new ResourceLocation("quark", "textures/model/entity/frog.png");
	private static final ResourceLocation TEXTURE_SWEATER = new ResourceLocation("quark", "textures/model/entity/events/sweater_frog.png");
	private static final ResourceLocation TEXTURE_FUNNY = new ResourceLocation("quark", "textures/model/entity/events/funny_rat_frog.png");

	public FrogRenderer(EntityRendererManager manager) {
		super(manager, new FrogModel(), 0.2F);
	}

	@Override
	public ResourceLocation getEntityTexture(@Nonnull FrogEntity entity) {
		if(entity.hasCustomName() && entity.getCustomName().getUnformattedComponentText().trim().equalsIgnoreCase("Alex"))
			return TEXTURE_FUNNY;
			
		return entity.hasSweater() ? TEXTURE_SWEATER : TEXTURE;
	}

}
