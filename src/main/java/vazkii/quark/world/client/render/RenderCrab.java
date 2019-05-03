package vazkii.quark.world.client.render;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import vazkii.quark.world.client.model.ModelCrab;
import vazkii.quark.world.entity.EntityCrab;

public class RenderCrab extends RenderLiving<EntityCrab> {

	private static final ResourceLocation TEXTURE = new ResourceLocation("quark", "textures/entity/archeologist.png");
	
	public static final IRenderFactory FACTORY = (RenderManager manager) -> new RenderCrab(manager);

	public RenderCrab(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelCrab(), 0.5F);
	}
	
	@Override
    protected ResourceLocation getEntityTexture(EntityCrab entity) {
        return TEXTURE;
    }
	
}
