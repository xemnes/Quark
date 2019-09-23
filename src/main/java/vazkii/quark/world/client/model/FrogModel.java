package vazkii.quark.world.client.model;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.util.math.MathHelper;
import vazkii.quark.world.entity.FrogEntity;

public class FrogModel extends EntityModel<FrogEntity> {
	
    public final RendererModel headTop;
    public final RendererModel headBottom;
    public final RendererModel body;
    public final RendererModel rightArm;
    public final RendererModel leftArm;
    public final RendererModel rightEye;
    public final RendererModel leftEye;

    public FrogModel() {
        textureWidth = 64;
        textureHeight = 32;
        rightArm = new RendererModel(this, 33, 7);
        rightArm.mirror = true;
        rightArm.setRotationPoint(6.5F, 22.0F, 1.0F);
        rightArm.addBox(-1.0F, -1.0F, -5.0F, 3, 3, 6, 0.0F);
        leftArm = new RendererModel(this, 33, 7);
        leftArm.setRotationPoint(-6.5F, 22.0F, 1.0F);
        leftArm.addBox(-2.0F, -1.0F, -5.0F, 3, 3, 6, 0.0F);
        body = new RendererModel(this, 0, 7);
        body.setRotationPoint(0.0F, 20.0F, 0.0F);
        body.addBox(-5.5F, -3.0F, 0.0F, 11, 7, 11, 0.0F);
        headTop = new RendererModel(this, 0, 0);
        headTop.setRotationPoint(0.0F, 18.0F, 0.0F);
        headTop.addBox(-5.5F, -1.0F, -5.0F, 11, 2, 5, 0.0F);
        headBottom = new RendererModel(this, 32, 0);
        headBottom.setRotationPoint(0.0F, 18.0F, 0.0F);
        headBottom.addBox(-5.5F, 1.0F, -5.0F, 11, 2, 5, 0.0F);
        rightEye = new RendererModel(this, 0, 0);
        rightEye.mirror = true;
        rightEye.setRotationPoint(0.0F, 18.0F, 0.0F);
        rightEye.addBox(1.5F, -1.5F, -4.0F, 1, 1, 1, 0.0F);
        leftEye = new RendererModel(this, 0, 0);
        leftEye.setRotationPoint(0.0F, 18.0F, 0.0F);
        leftEye.addBox(-2.5F, -1.5F, -4.0F, 1, 1, 1, 0.0F);
    }


    @Override
    public void setLivingAnimations(FrogEntity frog, float limbSwing, float limbSwingAmount, float partialTickTime) {
        int rawTalkTime = frog.getTalkTime();

        headBottom.rotateAngleX = (float) Math.PI / 120;

        if (rawTalkTime != 0) {
            float talkTime = rawTalkTime - partialTickTime;

            int speed = 10;

            headBottom.rotateAngleX += Math.PI / 8 * (1 - MathHelper.cos(talkTime * (float) Math.PI * 2 / speed));
        }
    }

    @Override
    public void setRotationAngles(FrogEntity frog, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        rightArm.rotateAngleX = MathHelper.cos(limbSwing * 2 / 3) * 1F * limbSwingAmount;
        leftArm.rotateAngleX = MathHelper.cos(limbSwing * 2 / 3) * 1F * limbSwingAmount;

        headTop.rotateAngleX = headPitch * (float) Math.PI / 180;
        rightEye.rotateAngleX = leftEye.rotateAngleX = headTop.rotateAngleX;
        headBottom.rotateAngleX += headPitch * (float) Math.PI / 180;
    }

    @Override
    public void render(FrogEntity frog, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {

        GlStateManager.pushMatrix();
        float sizeModifier = frog.getSizeModifier();


        GlStateManager.translated(0, 1.5 - sizeModifier * 1.5, 0);

        GlStateManager.scalef(sizeModifier, sizeModifier, sizeModifier);

        if (isChild) {
            GlStateManager.pushMatrix();
            GlStateManager.translated(0, 0.6, 0);
            GlStateManager.scaled(0.625, 0.625, 0.625);
        }

        headTop.render(scale);
        headBottom.render(scale);
        rightEye.render(scale);
        leftEye.render(scale);

        if (isChild) {
            GlStateManager.popMatrix();
            GlStateManager.scaled(0.5, 0.5, 0.5);
            GlStateManager.translated(0, 1.5, 0);
        }

        rightArm.render(scale);
        leftArm.render(scale);
        body.render(scale);

        GlStateManager.popMatrix();
    }
    
}
