package vazkii.quark.building.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelManager;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.BannerTileEntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.item.*;
import net.minecraft.tileentity.BannerTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderItemInFrameEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;
import vazkii.quark.base.Quark;
import vazkii.quark.building.entity.ColoredItemFrameEntity;
import vazkii.quark.building.entity.GlassItemFrameEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author WireSegal
 * Created at 11:58 AM on 8/25/19.
 */

@OnlyIn(Dist.CLIENT)
public class GlassItemFrameRenderer extends EntityRenderer<GlassItemFrameEntity> {

    // TODO: reinstate when Forge fixes itself
//    private static final ModelResourceLocation LOCATION_MODEL = new ModelResourceLocation(new ResourceLocation(Quark.MOD_ID, "glass_frame"), "normal");

    private static final ModelResourceLocation LOCATION_MODEL = new ModelResourceLocation(new ResourceLocation(Quark.MOD_ID, "glass_frame"), "inventory");

//    private final BannerTileEntity banner = new BannerTileEntity();
    private final Minecraft mc = Minecraft.getInstance();
    private final ItemRenderer itemRenderer;
    private final ItemFrameRenderer defaultRenderer;

    public GlassItemFrameRenderer(EntityRendererManager renderManagerIn, ItemRenderer itemRendererIn) {
        super(renderManagerIn);
        this.itemRenderer = itemRendererIn;
        this.defaultRenderer = (ItemFrameRenderer) renderManagerIn.renderers.get(EntityType.ITEM_FRAME);
    }

    @Override
    public void render(GlassItemFrameEntity p_225623_1_, float p_225623_2_, float p_225623_3_, MatrixStack p_225623_4_, IRenderTypeBuffer p_225623_5_, int p_225623_6_) {
        super.render(p_225623_1_, p_225623_2_, p_225623_3_, p_225623_4_, p_225623_5_, p_225623_6_);
        p_225623_4_.push();
        Direction direction = p_225623_1_.getHorizontalFacing();
        Vec3d vec3d = this.getPositionOffset(p_225623_1_, p_225623_3_);
        p_225623_4_.translate(-vec3d.getX(), -vec3d.getY(), -vec3d.getZ());
        p_225623_4_.translate((double)direction.getXOffset() * 0.46875D, (double)direction.getYOffset() * 0.46875D, (double)direction.getZOffset() * 0.46875D);
        p_225623_4_.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(p_225623_1_.rotationPitch));
        p_225623_4_.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180.0F - p_225623_1_.rotationYaw));
        BlockRendererDispatcher blockrendererdispatcher = this.mc.getBlockRendererDispatcher();
        ModelManager modelmanager = blockrendererdispatcher.getBlockModelShapes().getModelManager();
        
        ItemStack itemstack = p_225623_1_.getDisplayedItem();

        if (itemstack.isEmpty()) {
            p_225623_4_.push();
            p_225623_4_.translate(-0.5D, -0.5D, -0.5D);
            blockrendererdispatcher.getBlockModelRenderer().render(p_225623_4_.peek(), p_225623_5_.getBuffer(Atlases.getEntityCutout()), (BlockState)null, modelmanager.getModel(LOCATION_MODEL), 1.0F, 1.0F, 1.0F, p_225623_6_, OverlayTexture.DEFAULT_UV);
            p_225623_4_.pop();
        } else {
        	renderItemStack(p_225623_1_, p_225623_2_, p_225623_3_, p_225623_4_, p_225623_5_, p_225623_6_, itemstack);
        	
//           MapData mapdata = FilledMapItem.getMapData(itemstack, p_225623_1_.world);
//           p_225623_4_.translate(0.0D, 0.0D, 0.4375D);
//           int i = mapdata != null ? p_225623_1_.getRotation() % 4 * 2 : p_225623_1_.getRotation();
//           p_225623_4_.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion((float)i * 360.0F / 8.0F));
//           if (!net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderItemInFrameEvent(p_225623_1_, defaultRenderer))) {
//           if (mapdata != null) {
//              p_225623_4_.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180.0F));
//              float f = 0.0078125F;
//              p_225623_4_.scale(0.0078125F, 0.0078125F, 0.0078125F);
//              p_225623_4_.translate(-64.0D, -64.0D, 0.0D);
//              p_225623_4_.translate(0.0D, 0.0D, -1.0D);
//              if (mapdata != null) {
//                 this.mc.gameRenderer.getMapItemRenderer().draw(p_225623_4_, p_225623_5_, mapdata, true, p_225623_6_);
//              }
//           } else {
//              p_225623_4_.scale(0.5F, 0.5F, 0.5F);
//              this.itemRenderer.renderItem(itemstack, ItemCameraTransforms.TransformType.FIXED, p_225623_6_, OverlayTexture.DEFAULT_UV, p_225623_4_, p_225623_5_);
//           }
//           }
        }

        p_225623_4_.pop();
     }

     public Vec3d getPositionOffset(GlassItemFrameEntity p_225627_1_, float p_225627_2_) {
        return new Vec3d((double)((float)p_225627_1_.getHorizontalFacing().getXOffset() * 0.3F), -0.25D, (double)((float)p_225627_1_.getHorizontalFacing().getZOffset() * 0.3F));
     }

     public ResourceLocation getEntityTexture(GlassItemFrameEntity p_110775_1_) {
        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
     }

     protected boolean canRenderName(GlassItemFrameEntity p_177070_1_) {
        if (Minecraft.isGuiEnabled() && !p_177070_1_.getDisplayedItem().isEmpty() && p_177070_1_.getDisplayedItem().hasDisplayName() && this.renderManager.pointedEntity == p_177070_1_) {
           double d0 = this.renderManager.getSquaredDistanceToCamera(p_177070_1_);
           float f = p_177070_1_.isSneaky() ? 32.0F : 64.0F;
           return d0 < (double)(f * f);
        } else {
           return false;
        }
     }

     protected void renderLabelIfPresent(GlassItemFrameEntity p_225629_1_, String p_225629_2_, MatrixStack p_225629_3_, IRenderTypeBuffer p_225629_4_, int p_225629_5_) {
        super.renderLabelIfPresent(p_225629_1_, p_225629_1_.getDisplayedItem().getDisplayName().getFormattedText(), p_225629_3_, p_225629_4_, p_225629_5_);
     }
    
//    @Override
//    public void render(GlassItemFrameEntity entity, float p_225623_2_, float p_225623_3_, MatrixStack p_225623_4_, IRenderTypeBuffer p_225623_5_, int p_225623_6_) {
//        GlStateManager.pushMatrix();
//
//        ItemStack stack = entity.getDisplayedItem();
//        BlockPos pos = entity.getHangingPosition();
//        double d0 = (double)pos.getX() - entity.posX + x;
//        double d1 = (double)pos.getY() - entity.posY + y;
//        double d2 = (double)pos.getZ() - entity.posZ + z;
//        GlStateManager.translated(d0 + 0.5D, d1 + 0.5D, d2 + 0.5D);
//        GlStateManager.rotatef(entity.rotationPitch, 1.0F, 0.0F, 0.0F);
//        GlStateManager.rotatef(180.0F - entity.rotationYaw, 0.0F, 1.0F, 0.0F);
//        this.renderManager.textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
//        BlockRendererDispatcher blockrendererdispatcher = this.mc.getBlockRendererDispatcher();
//        if (stack.isEmpty()) {
//            ModelManager modelmanager = blockrendererdispatcher.getBlockModelShapes().getModelManager();
//            GlStateManager.pushMatrix();
//            GlStateManager.translatef(-0.5F, -0.5F, -0.5F);
//            if (this.renderOutlines) {
//                GlStateManager.enableColorMaterial();
//                GlStateManager.setupSolidRenderingTextureCombine(this.getTeamColor(entity));
//            }
//
//            blockrendererdispatcher.getBlockModelRenderer().renderModelBrightnessColor(modelmanager.getModel(LOCATION_MODEL), 1.0F, 1.0F, 1.0F, 1.0F);
//            if (this.renderOutlines) {
//                GlStateManager.tearDownSolidRenderingTextureCombine();
//                GlStateManager.disableColorMaterial();
//            }
//
//            GlStateManager.popMatrix();
//            GlStateManager.enableLighting();
//        }
//
//        if (stack.getItem() == Items.FILLED_MAP) {
//            GlStateManager.pushLightingAttributes();
//            RenderHelper.enableStandardItemLighting();
//        }
//
//        GlStateManager.translatef(0.0F, 0.0F, 0.4375F);
//        this.renderItemStack(entity, stack);
//        if (stack.getItem() == Items.FILLED_MAP) {
//            RenderHelper.disableStandardItemLighting();
//            GlStateManager.popAttributes();
//        }
//
//        GlStateManager.enableLighting();
//        GlStateManager.popMatrix();
//        this.renderName(entity, x + (double)((float)entity.getHorizontalFacing().getXOffset() * 0.3F), y - 0.25D, z + (double)((float)entity.getHorizontalFacing().getZOffset() * 0.3F));
//    }
//
//    @Nullable
//    @Override
//    public ResourceLocation getEntityTexture(@Nonnull GlassItemFrameEntity entity) {
//        return null;
//    }
//
    protected void renderItemStack(ItemFrameEntity itemFrame, float p_225623_2_, float p_225623_3_, MatrixStack matrix, IRenderTypeBuffer p_225623_5_, int p_225623_6_, ItemStack stack) {
//        if (stack.getItem() instanceof BannerItem) {
//            banner.loadFromItemStack(stack, ((BannerItem) stack.getItem()).getColor()); 
//                Minecraft.getInstance().getTextureManager().bindTexture(res);
//                Tessellator tessellator = Tessellator.getInstance();
//                BufferBuilder buffer = tessellator.getBuffer();
//
//                float f = 1F / 64F;
//                float u = 1 * f;
//                float v = 1 * f;
//                float w = 20 * f;
//                float h = 40 * f;
//
//                GlStateManager.pushMatrix();
//                GlStateManager.rotatef(180, 0F, 0F, 1F);
//                GlStateManager.translatef(-0.5F, -0.5F, 0.060546875F);
//                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
//                buffer.pos(0, 1, -0.000078125F).tex(u + 0, v + h).endVertex();
//                buffer.pos(1, 1, -0.000078125F).tex(u + w, v + h).endVertex();
//                buffer.pos(1, 0, -0.000078125F).tex(u + w, v + 0).endVertex();
//                buffer.pos(0, 0, -0.000078125F).tex(u + 0, v + 0).endVertex();
//                tessellator.draw();
//                GlStateManager.popMatrix();
//        } else {// TODO figure this out
            if (!stack.isEmpty()) {
            	matrix.push();
            	MapData mapdata = FilledMapItem.getMapData(stack, itemFrame.world);
                int rotation = mapdata != null ? itemFrame.getRotation() % 4 * 2 : itemFrame.getRotation();
                matrix.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion((float) rotation * 360.0F / 8.0F));
                if (!MinecraftForge.EVENT_BUS.post(new RenderItemInFrameEvent(itemFrame, defaultRenderer))) {
                    if (mapdata != null) {
                    	matrix.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180.0F));
                       	matrix.scale(0.0078125F, 0.0078125F, 0.0078125F);
                        matrix.translate(-64.0F, -64.0F, 7.75F);

                      this.mc.gameRenderer.getMapItemRenderer().draw(matrix, p_225623_5_, mapdata, true, p_225623_6_);
                    } else {

                        float s = 1.5F;
                        if (stack.getItem() instanceof ShieldItem) {
                            s = 4F;
                            matrix.translate(-0.25F, 0F, 0.2F);
                            matrix.scale(s, s, s);
                        } else {
                        	matrix.translate(0F, 0F, 0.05F);
                        	matrix.scale(s, s, s);
                        }
                        matrix.scale(0.5F, 0.5F, 0.5F);
                        this.itemRenderer.renderItem(stack, ItemCameraTransforms.TransformType.FIXED, p_225623_6_, OverlayTexture.DEFAULT_UV, matrix, p_225623_5_);
                    }
                }

                GlStateManager.popMatrix();
            }
//        }
    }
//
//    @Override
//    protected void renderName(@Nonnull GlassItemFrameEntity entity, double x, double y, double z) {
//        if (Minecraft.isGuiEnabled() && !entity.getDisplayedItem().isEmpty() && entity.getDisplayedItem().hasDisplayName() && this.renderManager.pointedEntity == entity) {
//            double d0 = entity.getDistanceSq(this.renderManager.info.getProjectedView());
//            float f = entity.shouldRenderSneaking() ? 32.0F : 64.0F;
//            if (!(d0 >= (double)(f * f))) {
//                String s = entity.getDisplayedItem().getDisplayName().getFormattedText();
//                this.renderLivingLabel(entity, s, x, y, z, 64);
//            }
//        }
//    }
}
