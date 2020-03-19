package vazkii.quark.experimental.client.model;

import java.util.function.BiConsumer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.model.ModelRenderer.ModelBox;
import vazkii.arl.util.ClientTicker;
import vazkii.quark.experimental.entity.ToretoiseEntity;

public class ToretoiseModel extends EntityModel<ToretoiseEntity> {
	
	private ToretoiseEntity entity;
    private float animFrames;
	
    public ModelRenderer body;
    public ModelRenderer head;
    public ModelRenderer rightFrontLeg;
    public ModelRenderer leftFrontLeg;
    public ModelRenderer rightBackLeg;
    public ModelRenderer leftBackLeg;
    public ModelRenderer mouth;

    public ToretoiseModel() {
        textureWidth = 100;
        textureHeight = 100;
        mouth = new ModelRenderer(this, 66, 38);
        mouth.setRotationPoint(0.0F, 1.0F, -1.0F);
        mouth.addBox(-4.5F, -2.5F, -8.0F, 9, 4, 8, 0.0F);
        
        leftFrontLeg = new ModelRenderer(this, 34, 38);
        leftFrontLeg.mirror = true;
        leftFrontLeg.setRotationPoint(10.0F, 16.0F, -12.0F);
        leftFrontLeg.addBox(-4.0F, -2.0F, -4.0F, 8, 10, 8, 0.0F);
        setRotateAngle(leftFrontLeg, 0.0F, -0.7853981633974483F, 0.0F);
        
        rightBackLeg = new ModelRenderer(this, 34, 38);
        rightBackLeg.setRotationPoint(-10.0F, 16.0F, 12.0F);
        rightBackLeg.addBox(-4.0F, -2.0F, -4.0F, 8, 10, 8, 0.0F);
        setRotateAngle(rightBackLeg, 0.0F, 0.7853981633974483F, 0.0F);
        
        body = new ModelRenderer(this, 0, 0);
        body.setRotationPoint(0.0F, 8.0F, 0.0F);
        body.addBox(-11.0F, 0.0F, -13.0F, 22, 12, 26, 0.0F);
        
        head = new ModelRenderer(this, 0, 38);
        head.setRotationPoint(0.0F, 16.0F, -13.0F);
        head.addBox(-4.0F, -4.0F, -8.0F, 8, 5, 8, 0.0F);
        
        rightFrontLeg = new ModelRenderer(this, 34, 38);
        rightFrontLeg.setRotationPoint(-10.0F, 16.0F, -12.0F);
        rightFrontLeg.addBox(-4.0F, -2.0F, -4.0F, 8, 10, 8, 0.0F);
        setRotateAngle(rightFrontLeg, 0.0F, 0.7853981633974483F, 0.0F);
        
        leftBackLeg = new ModelRenderer(this, 34, 38);
        leftBackLeg.mirror = true;
        leftBackLeg.setRotationPoint(10.0F, 16.0F, 12.0F);
        leftBackLeg.addBox(-4.0F, -2.0F, -4.0F, 8, 10, 8, 0.0F);
        
        setRotateAngle(leftBackLeg, 0.0F, -0.7853981633974483F, 0.0F);
        head.addChild(mouth);
    }
    
	@Override
	public void setRotationAngles(ToretoiseEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.entity = entity;
		animFrames = limbSwing;
	}

    @Override
	public void render(MatrixStack matrix, IVertexBuilder vb, int p_225598_3_, int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_) {
        float animSpeed = 30;
        float animPause = 12;
        
        float actualFrames = animFrames * 10;
        
        float doubleAnimSpeed = animSpeed * 2;
        float animBuff = animSpeed - animPause;
    	
        float scale = 0.02F;
        float bodyTrans = (float) (Math.sin(actualFrames / doubleAnimSpeed * Math.PI) + 1F) * scale;

        float rideMultiplier = 0;
        
        if(entity.rideTime > 0)
        	rideMultiplier = (float) Math.min(30, entity.rideTime - 1 + ClientTicker.partialTicks) / 30.0F;  
        
        bodyTrans *= (1F - rideMultiplier); 
        
        matrix.push();
        matrix.translate(0, bodyTrans, 0);
        matrix.rotate(Vector3f.ZP.rotation((bodyTrans - scale) * 0.5F));
        
        body.render(matrix, vb, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
        
        matrix.push();
        matrix.translate(0, bodyTrans, rideMultiplier * 0.3);
        head.rotateAngleX = bodyTrans * 2;
        head.render(matrix, vb, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
        matrix.pop();

        float finalRideMultiplier = rideMultiplier;
        BiConsumer<ModelRenderer, Float> draw = (renderer, frames) -> {
        	float time = Math.min(animBuff, frames % doubleAnimSpeed);
            float trans = ((float) (Math.sin(time / animBuff * Math.PI) + 1.0) / -2F) * 0.12F + 0.06F;
            
            float rotTime = (frames % doubleAnimSpeed);
            float rot = ((float) Math.sin(rotTime / doubleAnimSpeed * Math.PI) + 1F) * -0.25F;
        	
            trans *= (1F - finalRideMultiplier);
            rot *= (1F - finalRideMultiplier);
            trans += finalRideMultiplier * -0.2;

            matrix.push();
            
            ModelBox box = renderer.getRandomCube(entity.getRNG());
            double spread = (1F / 16F) * -1.8 * finalRideMultiplier;
            double x = (renderer.rotationPointX + box.posX1);
            double z = (renderer.rotationPointZ + box.posZ1);
            x *= (spread / Math.abs(x));
            z *= (spread / Math.abs(z));
            matrix.translate(x, 0, z);
            
            matrix.translate(0, trans, 0);
            float yRot = renderer.rotateAngleY;
            renderer.rotateAngleX = rot;
            renderer.rotateAngleY *= (1F - finalRideMultiplier);
            renderer.render(matrix, vb, p_225598_3_, p_225598_4_, p_225598_5_, p_225598_6_, p_225598_7_, p_225598_8_);
            renderer.rotateAngleY = yRot;
            matrix.pop();
        };
        
        draw.accept(leftFrontLeg, actualFrames);
        draw.accept(rightFrontLeg, actualFrames + animSpeed);
        draw.accept(leftBackLeg, actualFrames + animSpeed * 0.5F);
        draw.accept(rightBackLeg, actualFrames + animSpeed * 1.5F);
        matrix.pop();
    }
    
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

	
}
