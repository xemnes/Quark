package vazkii.quark.building.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.model.ModelManager;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.item.*;
import net.minecraft.tileentity.BannerTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderItemInFrameEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;
import vazkii.quark.base.Quark;
import vazkii.quark.building.entity.GlassItemFrameEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author WireSegal
 * Created at 11:58 AM on 8/25/19.
 */

@OnlyIn(Dist.CLIENT)
public class GlassItemFrameRenderer extends EntityRenderer<GlassItemFrameEntity> {
    private static final ResourceLocation MAP_BACKGROUND_TEXTURES = new ResourceLocation("textures/map/map_background.png");

    // TODO: reinstate when Forge fixes itself
//    private static final ModelResourceLocation LOCATION_MODEL = new ModelResourceLocation(new ResourceLocation(Quark.MOD_ID, "glass_frame"), "normal");

    private static final ModelResourceLocation LOCATION_MODEL = new ModelResourceLocation(new ResourceLocation(Quark.MOD_ID, "glass_frame"), "inventory");

    private final BannerTileEntity banner = new BannerTileEntity();
    private final Minecraft mc = Minecraft.getInstance();
    private final ItemRenderer itemRenderer;
    private final ItemFrameRenderer defaultRenderer;

    public GlassItemFrameRenderer(EntityRendererManager renderManagerIn, ItemRenderer itemRendererIn) {
        super(renderManagerIn);
        this.itemRenderer = itemRendererIn;
        this.defaultRenderer = renderManagerIn.getRenderer(ItemFrameEntity.class);
    }

    @Override
    public void doRender(@Nonnull GlassItemFrameEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();

        ItemStack stack = entity.getDisplayedItem();
        BlockPos pos = entity.getHangingPosition();
        double d0 = (double)pos.getX() - entity.posX + x;
        double d1 = (double)pos.getY() - entity.posY + y;
        double d2 = (double)pos.getZ() - entity.posZ + z;
        GlStateManager.translated(d0 + 0.5D, d1 + 0.5D, d2 + 0.5D);
        GlStateManager.rotatef(entity.rotationPitch, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotatef(180.0F - entity.rotationYaw, 0.0F, 1.0F, 0.0F);
        this.renderManager.textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        BlockRendererDispatcher blockrendererdispatcher = this.mc.getBlockRendererDispatcher();
        if (stack.isEmpty()) {
            ModelManager modelmanager = blockrendererdispatcher.getBlockModelShapes().getModelManager();
            GlStateManager.pushMatrix();
            GlStateManager.translatef(-0.5F, -0.5F, -0.5F);
            if (this.renderOutlines) {
                GlStateManager.enableColorMaterial();
                GlStateManager.setupSolidRenderingTextureCombine(this.getTeamColor(entity));
            }

            blockrendererdispatcher.getBlockModelRenderer().renderModelBrightnessColor(modelmanager.getModel(LOCATION_MODEL), 1.0F, 1.0F, 1.0F, 1.0F);
            if (this.renderOutlines) {
                GlStateManager.tearDownSolidRenderingTextureCombine();
                GlStateManager.disableColorMaterial();
            }

            GlStateManager.popMatrix();
            GlStateManager.enableLighting();
        }

        if (stack.getItem() == Items.FILLED_MAP) {
            GlStateManager.pushLightingAttributes();
            RenderHelper.enableStandardItemLighting();
        }

        GlStateManager.translatef(0.0F, 0.0F, 0.4375F);
        this.renderItemStack(entity, stack);
        if (stack.getItem() == Items.FILLED_MAP) {
            RenderHelper.disableStandardItemLighting();
            GlStateManager.popAttributes();
        }

        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
        this.renderName(entity, x + (double)((float)entity.getHorizontalFacing().getXOffset() * 0.3F), y - 0.25D, z + (double)((float)entity.getHorizontalFacing().getZOffset() * 0.3F));
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(@Nonnull GlassItemFrameEntity entity) {
        return null;
    }

    protected void renderItemStack(ItemFrameEntity itemFrame, ItemStack stack) {
        if (stack.getItem() instanceof BannerItem) {
            banner.loadFromItemStack(stack, ((BannerItem) stack.getItem()).getColor());
            ResourceLocation res = BannerTextures.BANNER_DESIGNS.getResourceLocation(banner.getPatternResourceLocation(), banner.getPatternList(), banner.getColorList());
            if (res != null) {
                Minecraft.getInstance().getTextureManager().bindTexture(res);
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder buffer = tessellator.getBuffer();

                float f = 1F / 64F;
                float u = 1 * f;
                float v = 1 * f;
                float w = 20 * f;
                float h = 40 * f;

                GlStateManager.pushMatrix();
                GlStateManager.rotatef(180, 0F, 0F, 1F);
                GlStateManager.translatef(-0.5F, -0.5F, 0.060546875F);
                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
                buffer.pos(0, 1, -0.000078125F).tex(u + 0, v + h).endVertex();
                buffer.pos(1, 1, -0.000078125F).tex(u + w, v + h).endVertex();
                buffer.pos(1, 0, -0.000078125F).tex(u + w, v + 0).endVertex();
                buffer.pos(0, 0, -0.000078125F).tex(u + 0, v + 0).endVertex();
                tessellator.draw();
                GlStateManager.popMatrix();
            }
        } else {
            if (!stack.isEmpty()) {
                GlStateManager.pushMatrix();
                MapData mapdata = FilledMapItem.getMapData(stack, itemFrame.world);
                int rotation = mapdata != null ? itemFrame.getRotation() % 4 * 2 : itemFrame.getRotation();
                GlStateManager.rotatef((float) rotation * 360.0F / 8.0F, 0.0F, 0.0F, 1.0F);
                if (!MinecraftForge.EVENT_BUS.post(new RenderItemInFrameEvent(itemFrame, defaultRenderer))) {
                    if (mapdata != null) {
                        GlStateManager.disableLighting();
                        this.renderManager.textureManager.bindTexture(MAP_BACKGROUND_TEXTURES);
                        GlStateManager.rotatef(180.0F, 0.0F, 0.0F, 1.0F);
                        GlStateManager.scalef(0.0078125F, 0.0078125F, 0.0078125F);
                        GlStateManager.translatef(-64.0F, -64.0F, 7.75F);

                        this.mc.gameRenderer.getMapItemRenderer().renderMap(mapdata, true);
                    } else {

                        float s = 1.5F;
                        if (stack.getItem() instanceof ShieldItem) {
                            s = 4F;
                            GlStateManager.translatef(-0.25F, 0F, 0.2F);
                            GlStateManager.scalef(s, s, s);
                        } else {
                            GlStateManager.translatef(0F, 0F, 0.05F);
                            GlStateManager.scalef(s, s, s);
                        }
                        GlStateManager.scalef(0.5F, 0.5F, 0.5F);
                        this.itemRenderer.renderItem(stack, TransformType.FIXED);
                    }
                }

                GlStateManager.popMatrix();
            }
        }
    }

    @Override
    protected void renderName(@Nonnull GlassItemFrameEntity entity, double x, double y, double z) {
        if (Minecraft.isGuiEnabled() && !entity.getDisplayedItem().isEmpty() && entity.getDisplayedItem().hasDisplayName() && this.renderManager.pointedEntity == entity) {
            double d0 = entity.getDistanceSq(this.renderManager.info.getProjectedView());
            float f = entity.shouldRenderSneaking() ? 32.0F : 64.0F;
            if (!(d0 >= (double)(f * f))) {
                String s = entity.getDisplayedItem().getDisplayName().getFormattedText();
                this.renderLivingLabel(entity, s, x, y, z, 64);
            }
        }
    }
}
