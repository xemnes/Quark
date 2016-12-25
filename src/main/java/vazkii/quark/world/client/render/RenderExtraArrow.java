/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 * 
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [17/07/2016, 04:34:07 (GMT)]
 */
package vazkii.quark.world.client.render;

import javax.annotation.Resource;

import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderExtraArrow extends RenderArrow {

	public static final IRenderFactory FACTORY_ENDER = (RenderManager manager) -> new RenderExtraArrow(manager, new ResourceLocation("quark", "textures/entity/arrow_ender.png"));
	public static final IRenderFactory FACTORY_EXPLOSIVE = (RenderManager manager) -> new RenderExtraArrow(manager, new ResourceLocation("quark", "textures/entity/arrow_explosive.png"));
	public static final IRenderFactory FACTORY_TORCH = (RenderManager manager) -> new RenderExtraArrow(manager, new ResourceLocation("quark", "textures/entity/arrow_torch.png"));
	
	ResourceLocation res;
	
	public RenderExtraArrow(RenderManager renderManagerIn, ResourceLocation res) {
		super(renderManagerIn);
		this.res = res;
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return res;
	}

}
