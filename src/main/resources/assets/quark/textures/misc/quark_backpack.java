package model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * quark_backpack - wiiv
 * Created using Tabula 7.0.0
 */
public class quark_backpack extends ModelBase {
    public ModelRenderer straps;
    public ModelRenderer backpack;
    public ModelRenderer fitting;
    public ModelRenderer lock;

    public quark_backpack() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.straps = new ModelRenderer(this, 24, 0);
        this.straps.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.straps.addBox(-4.0F, 0.0F, -3.0F, 8, 8, 5, 0.0F);
        this.fitting = new ModelRenderer(this, 50, 0);
        this.fitting.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.fitting.addBox(-1.0F, 3.0F, 6.0F, 2, 3, 1, 0.0F);
        this.backpack = new ModelRenderer(this, 0, 0);
        this.backpack.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.backpack.addBox(-4.0F, 0.0F, 2.0F, 8, 10, 4, 0.0F);
        this.lock = new ModelRenderer(this, 50, 4);
        this.lock.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.lock.addBox(-2.0F, 4.0F, 6.0F, 4, 3, 2, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.straps.render(f5);
        this.fitting.render(f5);
        this.backpack.render(f5);
        this.lock.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
