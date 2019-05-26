package vazkii.quark.experimental.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelFrog extends ModelBase {
	
    public final ModelRenderer head;
    public final ModelRenderer body;
    public final ModelRenderer rightArm;
    public final ModelRenderer leftArm;
    public final ModelRenderer rightEye;
    public final ModelRenderer leftEye;

    // Todo make the frog's mouth open when active

    public ModelFrog() {
        textureWidth = 64;
        textureHeight = 32;
        rightArm = new ModelRenderer(this, 32, 0);
        rightArm.setRotationPoint(6.5F, 22.0F, 1.0F);
        rightArm.addBox(-1.0F, -1.0F, -5.0F, 3, 3, 6, 0.0F);
        leftArm = new ModelRenderer(this, 32, 0);
        leftArm.setRotationPoint(-6.5F, 22.0F, 1.0F);
        leftArm.addBox(-2.0F, -1.0F, -5.0F, 3, 3, 6, 0.0F);
        body = new ModelRenderer(this, 0, 9);
        body.setRotationPoint(0.0F, 20.0F, 0.0F);
        body.addBox(-5.5F, -3.0F, 0.0F, 11, 7, 11, 0.0F);
        head = new ModelRenderer(this, 0, 0);
        head.setRotationPoint(0.0F, 18.0F, 0.0F);
        head.addBox(-5.5F, -1.0F, -5.0F, 11, 4, 5, 0.0F);
        rightEye = new ModelRenderer(this, 0, 0);
        rightEye.setRotationPoint(-2.0F, 17.5F, -3.5F);
        rightEye.addBox(-0.5F, -1.0F, -0.5F, 1, 1, 1, 0.0F);
        leftEye = new ModelRenderer(this, 0, 0);
        leftEye.setRotationPoint(2.0F, 17.5F, -3.5F);
        leftEye.addBox(-0.5F, -1.0F, -0.5F, 1, 1, 1, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        rightArm.render(f5);
        leftArm.render(f5);
        body.render(f5);
        head.render(f5);
        rightEye.render(f5);
        leftEye.render(f5);
    }
    
}
