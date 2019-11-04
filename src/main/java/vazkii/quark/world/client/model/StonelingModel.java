package vazkii.quark.world.client.model;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.ModelBox;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import vazkii.quark.world.entity.StonelingEntity;

public class StonelingModel extends EntityModel<StonelingEntity> {

	private final RendererModel body;
	private final RendererModel arm_right;
	private final RendererModel arm_left;
	private final RendererModel leg_right;
	private final RendererModel leg_left;

	public StonelingModel() {
		textureWidth = 32;
		textureHeight = 32;

		body = new RendererModel(this);
		body.setRotationPoint(0.0F, 14.0F, 0.0F);

		RendererModel head = new RendererModel(this);
		head.setRotationPoint(0.0F, 0.0F, 0.0F);
		body.addChild(head);
		head.cubeList.add(new ModelBox(head, 0, 0, -3.0F, -2.0F, -3.0F, 6, 8, 6, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 8, 24, -1.0F, -4.0F, -5.0F, 2, 4, 2, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 16, 20, -1.0F, 6.0F, -3.0F, 2, 2, 2, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 0, 24, -1.0F, -4.0F, 3.0F, 2, 4, 2, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 16, 24, -1.0F, -4.0F, -3.0F, 2, 2, 6, 0.0F, false));
		head.cubeList.add(new ModelBox(head, 24, 20, -1.0F, -4.0F, -1.0F, 2, 2, 2, 0.0F, false));

		arm_right = new RendererModel(this);
		arm_right.setRotationPoint(-3.0F, 2.0F, 0.0F);
		setRotationAngle(arm_right, 3.1416F, 0.0F, 0.0F);
		body.addChild(arm_right);
		arm_right.cubeList.add(new ModelBox(arm_right, 0, 14, -2.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F, false));

		arm_left = new RendererModel(this);
		arm_left.setRotationPoint(3.0F, 2.0F, 0.0F);
		setRotationAngle(arm_left, 3.1416F, 0.0F, 0.0F);
		body.addChild(arm_left);
		arm_left.cubeList.add(new ModelBox(arm_left, 8, 14, 0.0F, 0.0F, -1.0F, 2, 8, 2, 0.0F, false));

		leg_right = new RendererModel(this);
		leg_right.setRotationPoint(-2.0F, 4.0F, 0.0F);
		body.addChild(leg_right);
		leg_right.cubeList.add(new ModelBox(leg_right, 16, 14, -1.0F, 2.0F, -1.0F, 2, 4, 2, 0.0F, false));

		leg_left = new RendererModel(this);
		leg_left.setRotationPoint(1.0F, 4.0F, 0.0F);
		body.addChild(leg_left);
		leg_left.cubeList.add(new ModelBox(leg_left, 24, 14, 0.0F, 2.0F, -1.0F, 2, 4, 2, 0.0F, false));
	}

	@Override
	public void setRotationAngles(StonelingEntity stoneling, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
		leg_right.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * limbSwingAmount;
		leg_left.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * limbSwingAmount;
		
		ItemStack carry = stoneling.getCarryingItem();
		if(carry.isEmpty() && !stoneling.isBeingRidden()) {
			arm_right.rotateAngleX = 0F;
			arm_left.rotateAngleX = 0F;
		} else {
			arm_right.rotateAngleX = 3.1416F;
			arm_left.rotateAngleX = 3.1416F;
		}
	}

	@Override
	public void render(StonelingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		body.render(scale);
	}
	
	public void setRotationAngle(RendererModel modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
