package vazkii.quark.management.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import vazkii.quark.management.entity.ChestPassengerEntity;

import javax.annotation.Nonnull;

/**
 * @author WireSegal
 * Created at 2:02 PM on 9/3/19.
 */
public class ChestPassengerRenderer extends EntityRenderer<ChestPassengerEntity> {

    public ChestPassengerRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(@Nonnull ChestPassengerEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {

        if(!entity.isPassenger())
            return;

        Entity riding = entity.getRidingEntity();
        if (riding == null)
            return;

        BoatEntity boat = (BoatEntity) riding;
        entityYaw = MathHelper.lerp(partialTicks, boat.prevRotationYaw, boat.rotationYaw);

        double dX = MathHelper.lerp(partialTicks, entity.lastTickPosX, entity.posX);
        double dY = MathHelper.lerp(partialTicks, entity.lastTickPosY, entity.posY);
        double dZ = MathHelper.lerp(partialTicks, entity.lastTickPosZ, entity.posZ);
        double renderX = dX - x;
        double renderY = dY - y;
        double renderZ = dZ - z;
        x = MathHelper.lerp(partialTicks, boat.lastTickPosX, boat.posX) - renderX;
        y = MathHelper.lerp(partialTicks, boat.lastTickPosY, boat.posY) - renderY;
        z = MathHelper.lerp(partialTicks, boat.lastTickPosZ, boat.posZ) - renderZ;

        super.doRender(entity, x, y, z, entityYaw, partialTicks);

        float rot = 180F - entityYaw;

        ItemStack stack = entity.getChestType();

        GlStateManager.pushMatrix();
        GlStateManager.translated(x, y + 0.375, z);
        GlStateManager.rotatef(rot, 0.0F, 1.0F, 0.0F);
        float timeSinceHit = boat.getTimeSinceHit() - partialTicks;
        float damageTaken = boat.getDamageTaken() - partialTicks;

        if (damageTaken < 0.0F)
            damageTaken = 0.0F;

        if (timeSinceHit > 0.0F)
            GlStateManager.rotatef(MathHelper.sin(timeSinceHit) * timeSinceHit * damageTaken / 10.0F * boat.getForwardDirection(), 1.0F, 0.0F, 0.0F);

        float rock = boat.getRockingAngle(partialTicks);
        if (!MathHelper.epsilonEquals(rock, 0.0F)) {
            GlStateManager.rotatef(rock, 1.0F, 0.0F, 1.0F);
        }

        if (riding.getControllingPassenger() == null) {
            if (riding.getPassengers().size() > 1)
                GlStateManager.translatef(0F, 0F, -0.9F);
            else
                GlStateManager.translatef(0F, 0F, -0.45F);
        }

        GlStateManager.translatef(0F, 0.7F - 0.375F, 0.6F - 0.15F);

        GlStateManager.scalef(1.75F, 1.75F, 1.75F);

        Minecraft.getInstance().getItemRenderer().renderItem(stack, TransformType.FIXED);
        GlStateManager.popMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(@Nonnull ChestPassengerEntity entity) {
        return null;
    }

}
