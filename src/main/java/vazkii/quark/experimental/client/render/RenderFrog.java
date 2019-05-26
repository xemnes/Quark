package vazkii.quark.experimental.client.render;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import vazkii.quark.experimental.client.model.ModelFrog;
import vazkii.quark.experimental.entity.EntityFrog;

import javax.annotation.Nonnull;

public class RenderFrog extends RenderLiving<EntityFrog> {

	private static final ResourceLocation TEXTURE = new ResourceLocation("quark", "textures/entity/frog.png");
	
	public static final IRenderFactory<EntityFrog> FACTORY = RenderFrog::new;
	
	public RenderFrog(RenderManager manager) {
		super(manager, new ModelFrog(), 0.4F);
	}

	@Override
	protected ResourceLocation getEntityTexture(@Nonnull EntityFrog entity) {
		return TEXTURE;
	}

}
