package vazkii.quark.world.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.math.MathHelper;

/**
 * ModelFoxhound - McVinnyq
 * Created using Tabula 7.0.0
 */
public class ModelFoxhound extends ModelBase {
    public ModelRenderer head;
    public ModelRenderer rightFrontLeg;
    public ModelRenderer leftFrontLeg;
    public ModelRenderer rightBackLeg;
    public ModelRenderer leftBackLeg;
    public ModelRenderer body;
    public ModelRenderer snout;
    public ModelRenderer rightEar;
    public ModelRenderer leftEar;
    public ModelRenderer tail;
    public ModelRenderer fluff;

    public ModelFoxhound() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.leftBackLeg = new ModelRenderer(this, 36, 32);
        this.leftBackLeg.setRotationPoint(3.0F, 12.0F, 9.5F);
        this.leftBackLeg.addBox(-1.5F, 0.0F, -1.5F, 3, 12, 3, 0.0F);
        this.rightFrontLeg = new ModelRenderer(this, 0, 32);
        this.rightFrontLeg.setRotationPoint(-2.0F, 12.0F, 2.0F);
        this.rightFrontLeg.addBox(-1.5F, 0.0F, -1.5F, 3, 12, 3, 0.0F);
        this.rightEar = new ModelRenderer(this, 0, 47);
        this.rightEar.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.rightEar.addBox(-4.0F, -5.0F, -5.0F, 2, 2, 3, 0.0F);
        this.tail = new ModelRenderer(this, 36, 16);
        this.tail.setRotationPoint(0.0F, 0.0F, 1.5F);
        this.tail.addBox(-2.0F, -4.0F, 0.0F, 4, 5, 10, 0.0F);
        this.setRotateAngle(tail, -1.3089969389957472F, 0.0F, 0.0F);
        this.body = new ModelRenderer(this, 0, 2);
        this.body.setRotationPoint(0.0F, 17.0F, 12.0F);
        this.body.addBox(-4.0F, -12.0F, 0.0F, 8, 12, 6, 0.0F);
        this.setRotateAngle(body, 1.5707963267948966F, 0.0F, 0.0F);
        this.fluff = new ModelRenderer(this, 28, 0);
        this.fluff.setRotationPoint(0.0F, -13.0F, 3.0F);
        this.fluff.addBox(-5.0F, 0.0F, -4.0F, 10, 8, 8, 0.05F);
        this.leftFrontLeg = new ModelRenderer(this, 12, 32);
        this.leftFrontLeg.setRotationPoint(2.0F, 12.0F, 2.0F);
        this.leftFrontLeg.addBox(-1.5F, 0.0F, -1.5F, 3, 12, 3, 0.0F);
        this.rightBackLeg = new ModelRenderer(this, 24, 32);
        this.rightBackLeg.setRotationPoint(-3.0F, 12.0F, 9.5F);
        this.rightBackLeg.addBox(-1.5F, 0.0F, -1.5F, 3, 12, 3, 0.0F);
        this.leftEar = new ModelRenderer(this, 10, 47);
        this.leftEar.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.leftEar.addBox(2.0F, -5.0F, -5.0F, 2, 2, 3, 0.0F);
        this.head = new ModelRenderer(this, 0, 20);
        this.head.setRotationPoint(0.0F, 14.5F, 0.0F);
        this.head.addBox(-4.0F, -3.0F, -6.0F, 8, 6, 6, 0.0F);
        this.snout = new ModelRenderer(this, 29, 18);
        this.snout.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.snout.addBox(-2.0F, 1.0F, -10.0F, 4, 2, 4, 0.0F);
        this.head.addChild(this.rightEar);
        this.body.addChild(this.tail);
        this.body.addChild(this.fluff);
        this.head.addChild(this.leftEar);
        this.head.addChild(this.snout);
    }

    @Override
    public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime) {
        EntityWolf entitywolf = (EntityWolf) entitylivingbaseIn;

        if (entitywolf.isSitting() || entitywolf.isAngry())
            this.tail.rotateAngleX = -0.6544984695F;
        else
            this.tail.rotateAngleX = -1.3089969389957472F + MathHelper.cos(limbSwing * 0.6662F) * limbSwingAmount;

        if (entitywolf.isSitting()) {
            this.body.setRotationPoint(0.0F, 25.0F, 6.0F);
            this.body.rotateAngleX = ((float) Math.PI / 4F);
            this.tail.setRotationPoint(0.0F, 0.0F, 1.5F);
            this.rightFrontLeg.setRotationPoint(-2.0F, 14.0F, 1.0F);
            this.leftFrontLeg.setRotationPoint(2.0F, 14.0F, 1.0F);
            this.rightBackLeg.setRotationPoint(-1.8F, 22.75F, 7F);
            this.leftBackLeg.setRotationPoint(1.8F, 22.75F, 7F);

            this.rightFrontLeg.rotateAngleX = ((float) Math.PI * 36F / 20F);
            this.leftFrontLeg.rotateAngleX = ((float) Math.PI * 36F / 20F);
            this.rightBackLeg.rotateAngleX = ((float) Math.PI * 3F / 2F);
            this.leftBackLeg.rotateAngleX = ((float) Math.PI * 3F / 2F);
        } else {
            this.body.setRotationPoint(0.0F, 17.0F, 12.0F);
            this.body.rotateAngleX = 1.5707963267948966F;
            this.tail.setRotationPoint(0.0F, 0.0F, 1.5F);
            this.rightFrontLeg.setRotationPoint(-2.0F, 12.0F, 2.0F);
            this.leftFrontLeg.setRotationPoint(2.0F, 12.0F, 2.0F);
            this.rightBackLeg.setRotationPoint(-3.0F, 12.0F, 9.5F);
            this.leftBackLeg.setRotationPoint(3.0F, 12.0F, 9.5F);
            this.rightFrontLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
            this.leftFrontLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
            this.rightBackLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
            this.leftBackLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        }

        this.head.rotateAngleY = entitywolf.getInterestedAngle(partialTickTime) + entitywolf.getShakeAngle(partialTickTime, 0.0F);
        this.body.rotateAngleZ = entitywolf.getShakeAngle(partialTickTime, -0.16F);
        this.tail.rotateAngleY = entitywolf.getShakeAngle(partialTickTime, -0.2F);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        this.head.rotateAngleX = headPitch * 0.017453292F;
        this.head.rotateAngleY = netHeadYaw * 0.017453292F;
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.leftBackLeg.render(scale);
        this.rightFrontLeg.render(scale);
        this.body.render(scale);
        this.leftFrontLeg.render(scale);
        this.rightBackLeg.render(scale);
        this.head.render(scale);
    }

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
