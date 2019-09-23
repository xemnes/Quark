/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 * <p>
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * <p>
 * File Created @ [May 11, 2019, 16:46 AM (EST)]
 */
package vazkii.quark.world.client.layer;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.quark.world.client.model.StonelingModel;
import vazkii.quark.world.entity.StonelingEntity;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class StonelingItemLayer extends LayerRenderer<StonelingEntity, StonelingModel> {

	public StonelingItemLayer(IEntityRenderer<StonelingEntity, StonelingModel> renderer) {
		super(renderer);
	}

	@Override
	public void render(@Nonnull StonelingEntity stoneling, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		ItemStack stack = stoneling.getCarryingItem();
		if (!stack.isEmpty()) {
			boolean isBlock = stack.getItem() instanceof BlockItem;
			
			GlStateManager.pushMatrix();
			GlStateManager.translatef(0F, 0.5F, 0F);
			if(!isBlock) {
				GlStateManager.rotatef(stoneling.getItemAngle() + 180, 0F, 1F, 0F);
				GlStateManager.rotatef(90F, 1F, 0F, 0F);
			} else GlStateManager.rotatef(180F, 1F, 0F, 0F);
			
			GlStateManager.scalef(0.725F, 0.725F, 0.725F);
			Minecraft mc = Minecraft.getInstance();
			mc.getItemRenderer().renderItem(stack, TransformType.FIXED);
			GlStateManager.popMatrix();
		}
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}
}
