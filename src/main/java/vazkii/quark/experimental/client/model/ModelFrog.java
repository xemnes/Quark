package vazkii.quark.experimental.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelFrog extends ModelBase {
	
    public ModelRenderer Head;
    public ModelRenderer Body;
    public ModelRenderer RightArm;
    public ModelRenderer LeftArm;
    public ModelRenderer RightEye;
    public ModelRenderer LeftEye;

    // Todo make the frog's mouth open when active

    public ModelFrog() {
        textureWidth = 64;
        textureHeight = 32;
        RightArm = new ModelRenderer(this, 32, 0);
        RightArm.setRotationPoint(6.5F, 22.0F, 1.0F);
        RightArm.addBox(-1.0F, -1.0F, -5.0F, 3, 3, 6, 0.0F);
        LeftArm = new ModelRenderer(this, 32, 0);
        LeftArm.setRotationPoint(-6.5F, 22.0F, 1.0F);
        LeftArm.addBox(-2.0F, -1.0F, -5.0F, 3, 3, 6, 0.0F);
        Body = new ModelRenderer(this, 0, 9);
        Body.setRotationPoint(0.0F, 20.0F, 0.0F);
        Body.addBox(-5.5F, -3.0F, 0.0F, 11, 7, 11, 0.0F);
        Head = new ModelRenderer(this, 0, 0);
        Head.setRotationPoint(0.0F, 18.0F, 0.0F);
        Head.addBox(-5.5F, -1.0F, -5.0F, 11, 4, 5, 0.0F);
        RightEye = new ModelRenderer(this, 0, 0);
        RightEye.setRotationPoint(-2.0F, 17.5F, -3.5F);
        RightEye.addBox(-0.5F, -1.0F, -0.5F, 1, 1, 1, 0.0F);
        LeftEye = new ModelRenderer(this, 0, 0);
        LeftEye.setRotationPoint(2.0F, 17.5F, -3.5F);
        LeftEye.addBox(-0.5F, -1.0F, -0.5F, 1, 1, 1, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        RightArm.render(f5);
        LeftArm.render(f5);
        Body.render(f5);
        Head.render(f5);
        RightEye.render(f5);
        LeftEye.render(f5);
    }
    
}
