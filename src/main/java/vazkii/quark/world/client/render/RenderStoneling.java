package vazkii.quark.world.client.render;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.quark.world.client.layer.LayerStonelingItem;
import vazkii.quark.world.client.model.ModelStoneling;
import vazkii.quark.world.entity.EntityStoneling;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class RenderStoneling extends RenderLiving<EntityStoneling> {

	private static final ResourceLocation[] TEXTURES = new ResourceLocation[] {
			new ResourceLocation("quark", "textures/entity/stoneling.png"),
			new ResourceLocation("quark", "textures/entity/stoneling_andesite.png"),
			new ResourceLocation("quark", "textures/entity/stoneling_diorite.png"),
			new ResourceLocation("quark", "textures/entity/stoneling_granite.png"),
			new ResourceLocation("quark", "textures/entity/stoneling_limestone.png"),
			new ResourceLocation("quark", "textures/entity/stoneling_marble.png"),
			new ResourceLocation("quark", "textures/entity/stoneling_basalt.png")
	};

	public static final IRenderFactory<EntityStoneling> FACTORY = RenderStoneling::new;
	
	protected RenderStoneling(RenderManager renderManager) {
		super(renderManager, new ModelStoneling(), 0.3F);
		addLayer(new LayerStonelingItem());
	}
	
	@Override
	protected ResourceLocation getEntityTexture(@Nonnull EntityStoneling entity) {
		return TEXTURES[MathHelper.clamp(entity.getVariant(), 0, TEXTURES.length)];
	}

}
