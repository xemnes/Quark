/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 *
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 *
 * File Created @ [02/07/2016, 21:09:04 (GMT)]
 */
package vazkii.quark.world.client.render;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSkeleton;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import vazkii.quark.world.client.layer.LayerAshenClothes;
import vazkii.quark.world.client.layer.LayerAshenHeldItem;

import java.util.Iterator;

public class RenderAshen extends RenderSkeleton {

	private static final ResourceLocation TEXTURE = new ResourceLocation("quark", "textures/entity/ashen.png");

	public static final IRenderFactory<EntitySkeleton> FACTORY = RenderAshen::new;

	public RenderAshen(RenderManager renderManagerIn) {
		super(renderManagerIn);
        
        Iterator<LayerRenderer<AbstractSkeleton>> it = layerRenderers.iterator();
        while (it.hasNext()) {
            LayerRenderer<? extends EntityLivingBase> layer = it.next();
            if (layer instanceof LayerHeldItem)
                it.remove();
        }

		addLayer(new LayerAshenHeldItem(this));
		addLayer(new LayerAshenClothes(this));
	}

	@Override
	protected ResourceLocation getEntityTexture(AbstractSkeleton entity) {
		return TEXTURE;
	}

}
