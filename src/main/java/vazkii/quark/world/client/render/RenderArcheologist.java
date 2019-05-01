package vazkii.quark.world.client.render;

import net.minecraft.client.model.ModelVillager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import vazkii.quark.world.client.layer.LayerArcheologistHat;
import vazkii.quark.world.entity.EntityArcheologist;

import javax.annotation.Nonnull;

public class RenderArcheologist extends RenderLiving<EntityArcheologist> {

	private static final ResourceLocation TEXTURE = new ResourceLocation("quark", "textures/entity/archeologist.png");
	
	public static final IRenderFactory<EntityArcheologist> FACTORY = RenderArcheologist::new;
	
	public RenderArcheologist(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelVillager(0.0F), 0.5F);
		addLayer(new LayerArcheologistHat(this));
	}

	@Nonnull
	@Override
    public ModelVillager getMainModel() {
    	return (ModelVillager) super.getMainModel();
    }
	
	@Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityArcheologist entity) {
    	return TEXTURE;
    }
	
}
