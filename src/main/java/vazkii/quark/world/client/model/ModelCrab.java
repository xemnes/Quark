package vazkii.quark.world.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelCrab extends ModelBase {
	
	private final ModelRenderer group;
	private final ModelRenderer thigh_left_1;
	private final ModelRenderer leg_left_1;
	private final ModelRenderer thigh_left_2;
	private final ModelRenderer leg_left_2;
	private final ModelRenderer thigh_left_3;
	private final ModelRenderer leg_left_3;
	private final ModelRenderer thigh_left_4;
	private final ModelRenderer leg_left_4;
	private final ModelRenderer thigh_right_1;
	private final ModelRenderer leg_right_1;
	private final ModelRenderer thigh_right_2;
	private final ModelRenderer leg_right_2;
	private final ModelRenderer thigh_right_3;
	private final ModelRenderer leg_right_3;
	private final ModelRenderer thigh_right_4;
	private final ModelRenderer leg_right_4;
	private final ModelRenderer arm_left;
	private final ModelRenderer forearm_left;
	private final ModelRenderer pincers_left;
	private final ModelRenderer pincer_left_upper;
	private final ModelRenderer pincer_left_lower;
	private final ModelRenderer arm_right;
	private final ModelRenderer forearm_right;
	private final ModelRenderer pincers_right;
	private final ModelRenderer pincer_right_upper;
	private final ModelRenderer pincer_right_lower;

	public ModelCrab() {
		textureWidth = 32;
		textureHeight = 32;

		group = new ModelRenderer(this);
		group.setRotationPoint(0.0F, 18.0F, 0.0F);
		group.cubeList.add(new ModelBox(group, 0, 0, -4.0F, -2.0F, -4.0F, 8, 4, 8, 0.0F, false));
		group.cubeList.add(new ModelBox(group, 0, 0, -2.0F, -4.0F, -4.0F, 1, 2, 1, 0.0F, false));
		group.cubeList.add(new ModelBox(group, 0, 0, 1.0F, -4.0F, -4.0F, 1, 2, 1, 0.0F, false));

		thigh_left_1 = new ModelRenderer(this);
		thigh_left_1.setRotationPoint(-4.0F, 1.0F, -3.0F);
		setRotationAngle(thigh_left_1, 0.0F, -0.5236F, 0.0F);
		group.addChild(thigh_left_1);
		thigh_left_1.cubeList.add(new ModelBox(thigh_left_1, 0, 12, -4.0F, -1.0F, -1.0F, 4, 2, 2, 0.0F, false));

		leg_left_1 = new ModelRenderer(this);
		leg_left_1.setRotationPoint(-4.0F, 1.0F, 0.0F);
		setRotationAngle(leg_left_1, 0.0F, 0.0F, 0.2618F);
		thigh_left_1.addChild(leg_left_1);
		leg_left_1.cubeList.add(new ModelBox(leg_left_1, 4, 0, 0.0F, -2.0F, -0.5F, 1, 6, 1, 0.0F, false));

		thigh_left_2 = new ModelRenderer(this);
		thigh_left_2.setRotationPoint(-4.0F, 1.0F, -1.0F);
		setRotationAngle(thigh_left_2, 0.0F, -0.2618F, 0.0F);
		group.addChild(thigh_left_2);
		thigh_left_2.cubeList.add(new ModelBox(thigh_left_2, 0, 12, -4.0F, -1.0F, -1.0F, 4, 2, 2, 0.0F, false));

		leg_left_2 = new ModelRenderer(this);
		leg_left_2.setRotationPoint(-4.0F, 1.0F, 0.0F);
		setRotationAngle(leg_left_2, 0.0F, 0.0F, 0.5236F);
		thigh_left_2.addChild(leg_left_2);
		leg_left_2.cubeList.add(new ModelBox(leg_left_2, 4, 0, 0.0F, -2.0F, -0.5F, 1, 6, 1, 0.0F, false));

		thigh_left_3 = new ModelRenderer(this);
		thigh_left_3.setRotationPoint(-4.0F, 1.0F, 1.0F);
		setRotationAngle(thigh_left_3, 0.0F, 0.2618F, 0.0F);
		group.addChild(thigh_left_3);
		thigh_left_3.cubeList.add(new ModelBox(thigh_left_3, 0, 12, -4.0F, -1.0F, -1.0F, 4, 2, 2, 0.0F, false));

		leg_left_3 = new ModelRenderer(this);
		leg_left_3.setRotationPoint(-4.0F, 1.0F, 0.0F);
		setRotationAngle(leg_left_3, 0.0F, 0.0F, 0.5236F);
		thigh_left_3.addChild(leg_left_3);
		leg_left_3.cubeList.add(new ModelBox(leg_left_3, 4, 0, 0.0F, -2.0F, -0.5F, 1, 6, 1, 0.0F, false));

		thigh_left_4 = new ModelRenderer(this);
		thigh_left_4.setRotationPoint(-4.0F, 1.0F, 3.0F);
		setRotationAngle(thigh_left_4, 0.0F, 0.5236F, 0.0F);
		group.addChild(thigh_left_4);
		thigh_left_4.cubeList.add(new ModelBox(thigh_left_4, 0, 12, -4.0F, -1.0F, -1.0F, 4, 2, 2, 0.0F, false));

		leg_left_4 = new ModelRenderer(this);
		leg_left_4.setRotationPoint(-4.0F, 1.0F, 0.0F);
		setRotationAngle(leg_left_4, 0.0F, 0.0F, 0.2618F);
		thigh_left_4.addChild(leg_left_4);
		leg_left_4.cubeList.add(new ModelBox(leg_left_4, 4, 0, 0.0F, -2.0F, -0.5F, 1, 6, 1, 0.0F, false));

		thigh_right_1 = new ModelRenderer(this);
		thigh_right_1.setRotationPoint(4.0F, 1.0F, -3.0F);
		setRotationAngle(thigh_right_1, 0.0F, 0.5236F, 0.0F);
		group.addChild(thigh_right_1);
		thigh_right_1.cubeList.add(new ModelBox(thigh_right_1, 0, 12, 0.0F, -1.0F, -1.0F, 4, 2, 2, 0.0F, false));

		leg_right_1 = new ModelRenderer(this);
		leg_right_1.setRotationPoint(4.0F, 1.0F, 0.0F);
		setRotationAngle(leg_right_1, 0.0F, 0.0F, -0.2618F);
		thigh_right_1.addChild(leg_right_1);
		leg_right_1.cubeList.add(new ModelBox(leg_right_1, 4, 0, -1.0F, -2.0F, -0.5F, 1, 6, 1, 0.0F, false));

		thigh_right_2 = new ModelRenderer(this);
		thigh_right_2.setRotationPoint(4.0F, 1.0F, -1.0F);
		setRotationAngle(thigh_right_2, 0.0F, 0.2618F, 0.0F);
		group.addChild(thigh_right_2);
		thigh_right_2.cubeList.add(new ModelBox(thigh_right_2, 0, 12, 0.0F, -1.0F, -1.0F, 4, 2, 2, 0.0F, false));

		leg_right_2 = new ModelRenderer(this);
		leg_right_2.setRotationPoint(4.0F, 1.0F, 0.0F);
		setRotationAngle(leg_right_2, 0.0F, 0.0F, -0.5236F);
		thigh_right_2.addChild(leg_right_2);
		leg_right_2.cubeList.add(new ModelBox(leg_right_2, 4, 0, -1.0F, -2.0F, -0.5F, 1, 6, 1, 0.0F, false));

		thigh_right_3 = new ModelRenderer(this);
		thigh_right_3.setRotationPoint(4.0F, 1.0F, 1.0F);
		setRotationAngle(thigh_right_3, 0.0F, -0.2618F, 0.0F);
		group.addChild(thigh_right_3);
		thigh_right_3.cubeList.add(new ModelBox(thigh_right_3, 0, 12, 0.0F, -1.0F, -1.0F, 4, 2, 2, 0.0F, false));

		leg_right_3 = new ModelRenderer(this);
		leg_right_3.setRotationPoint(4.0F, 1.0F, 0.0F);
		setRotationAngle(leg_right_3, 0.0F, 0.0F, -0.5236F);
		thigh_right_3.addChild(leg_right_3);
		leg_right_3.cubeList.add(new ModelBox(leg_right_3, 4, 0, -1.0F, -2.0F, -0.5F, 1, 6, 1, 0.0F, false));

		thigh_right_4 = new ModelRenderer(this);
		thigh_right_4.setRotationPoint(4.0F, 1.0F, 3.0F);
		setRotationAngle(thigh_right_4, 0.0F, -0.5236F, 0.0F);
		group.addChild(thigh_right_4);
		thigh_right_4.cubeList.add(new ModelBox(thigh_right_4, 0, 12, 0.0F, -1.0F, -1.0F, 4, 2, 2, 0.0F, false));

		leg_right_4 = new ModelRenderer(this);
		leg_right_4.setRotationPoint(4.0F, 1.0F, 0.0F);
		setRotationAngle(leg_right_4, 0.0F, 0.0F, -0.2618F);
		thigh_right_4.addChild(leg_right_4);
		leg_right_4.cubeList.add(new ModelBox(leg_right_4, 4, 0, -1.0F, -2.0F, -0.5F, 1, 6, 1, 0.0F, false));

		arm_left = new ModelRenderer(this);
		arm_left.setRotationPoint(-2.0F, 1.0F, -4.0F);
		setRotationAngle(arm_left, -0.5236F, 0.5236F, 0.0F);
		group.addChild(arm_left);
		arm_left.cubeList.add(new ModelBox(arm_left, 24, 0, -1.0F, 0.0F, -2.0F, 1, 1, 2, 0.0F, false));

		forearm_left = new ModelRenderer(this);
		forearm_left.setRotationPoint(-1.0F, 0.0F, -4.0F);
		arm_left.addChild(forearm_left);
		forearm_left.cubeList.add(new ModelBox(forearm_left, 12, 12, -0.5F, -0.5F, -2.0F, 2, 2, 4, 0.0F, false));

		pincers_left = new ModelRenderer(this);
		pincers_left.setRotationPoint(-1.0F, 0.0F, -4.0F);
		arm_left.addChild(pincers_left);

		pincer_left_upper = new ModelRenderer(this);
		pincer_left_upper.setRotationPoint(0.0F, 0.0F, 0.0F);
		setRotationAngle(pincer_left_upper, -0.2618F, 0.0F, 0.0F);
		pincers_left.addChild(pincer_left_upper);
		pincer_left_upper.cubeList.add(new ModelBox(pincer_left_upper, 12, 18, 0.0F, -1.0F, -4.0F, 1, 2, 4, 0.1F, false));

		pincer_left_lower = new ModelRenderer(this);
		pincer_left_lower.setRotationPoint(0.0F, 0.5F, 0.0F);
		pincers_left.addChild(pincer_left_lower);
		pincer_left_lower.cubeList.add(new ModelBox(pincer_left_lower, 12, 24, 0.0F, 0.5F, -4.0F, 1, 1, 4, 0.0F, false));

		arm_right = new ModelRenderer(this);
		arm_right.setRotationPoint(2.0F, 1.0F, -4.0F);
		setRotationAngle(arm_right, -0.5236F, -0.5236F, 0.0F);
		group.addChild(arm_right);
		arm_right.cubeList.add(new ModelBox(arm_right, 24, 0, 0.0F, 0.0F, -2.0F, 1, 1, 2, 0.0F, false));

		forearm_right = new ModelRenderer(this);
		forearm_right.setRotationPoint(1.0F, 0.0F, -4.0F);
		arm_right.addChild(forearm_right);
		forearm_right.cubeList.add(new ModelBox(forearm_right, 12, 12, -1.5F, -0.5F, -2.0F, 2, 2, 4, 0.0F, false));

		pincers_right = new ModelRenderer(this);
		pincers_right.setRotationPoint(-5.0F, 0.0F, -4.0F);
		arm_right.addChild(pincers_right);

		pincer_right_upper = new ModelRenderer(this);
		pincer_right_upper.setRotationPoint(6.0F, 0.0F, 0.0F);
		setRotationAngle(pincer_right_upper, -0.2618F, 0.0F, 0.0F);
		pincers_right.addChild(pincer_right_upper);
		pincer_right_upper.cubeList.add(new ModelBox(pincer_right_upper, 12, 18, -1.0F, -1.0F, -4.0F, 1, 2, 4, 0.1F, false));

		pincer_right_lower = new ModelRenderer(this);
		pincer_right_lower.setRotationPoint(6.0F, 0.5F, 0.0F);
		pincers_right.addChild(pincer_right_lower);
		pincer_right_lower.cubeList.add(new ModelBox(pincer_right_lower, 12, 24, -1.0F, 0.5F, -4.0F, 1, 1, 4, 0.0F, false));
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
		leg_left_1.rotateAngleZ = 0.2618F + (-1 + MathHelper.cos(limbSwing * 0.6662F)) * 0.7F * limbSwingAmount;
		leg_left_2.rotateAngleZ = 0.5236F + (-1 + MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI)) * 0.7F * limbSwingAmount;
		leg_left_3.rotateAngleZ = 0.5236F + (-1 + MathHelper.cos(limbSwing * 0.6662F)) * 0.7F * limbSwingAmount;
		leg_left_4.rotateAngleZ = 0.2618F + (-1 + MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI)) * 0.7F * limbSwingAmount;
		leg_right_1.rotateAngleZ = -0.2618F + (1 + MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI)) * 0.7F * limbSwingAmount;
		leg_right_2.rotateAngleZ = -0.5236F + (1 + MathHelper.cos(limbSwing * 0.6662F)) * 0.7F * limbSwingAmount;
		leg_right_3.rotateAngleZ = -0.5236F + (1 + MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI)) * 0.7F * limbSwingAmount;
		leg_right_4.rotateAngleZ = -0.2618F + (1 + MathHelper.cos(limbSwing * 0.6662F)) * 0.7F * limbSwingAmount;
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		group.render(f5);
	}
	
	private void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
