package vazkii.quark.building.client.render;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.model.ModelManager;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderItemInFrameEvent;
import net.minecraftforge.common.MinecraftForge;
import vazkii.quark.base.Quark;
import vazkii.quark.building.entity.ColoredItemFrameEntity;

/**
 * @author WireSegal
 * Created at 11:58 AM on 8/25/19.
 */

@OnlyIn(Dist.CLIENT)
public class ColoredItemFrameRenderer extends EntityRenderer<ColoredItemFrameEntity> {
    private static final ResourceLocation MAP_BACKGROUND_TEXTURES = new ResourceLocation("textures/map/map_background.png");

    private static final Map<DyeColor, ModelResourceLocation> LOCATIONS_MODEL = new HashMap<>();
    private static final Map<DyeColor, ModelResourceLocation> LOCATIONS_MODEL_MAP = new HashMap<>();
    
    private final Minecraft mc = Minecraft.getInstance();
    private final ItemRenderer itemRenderer;
    private final ItemFrameRenderer defaultRenderer;

    public ColoredItemFrameRenderer(EntityRendererManager renderManagerIn, ItemRenderer itemRendererIn) {
        super(renderManagerIn);
        this.itemRenderer = itemRendererIn;
        this.defaultRenderer = renderManagerIn.getRenderer(ItemFrameEntity.class);

        for (DyeColor color : DyeColor.values()) {
            // TODO: reinstate when Forge fixes itself
//            LOCATIONS_MODEL.put(color, new ModelResourceLocation(new ResourceLocation(Quark.MOD_ID, color.getName() + "_frame"), "map=false"));
//            LOCATIONS_MODEL_MAP.put(color, new ModelResourceLocation(new ResourceLocation(Quark.MOD_ID, color.getName() + "_frame"), "map=true"));

            LOCATIONS_MODEL.put(color, new ModelResourceLocation(new ResourceLocation(Quark.MOD_ID, color.getName() + "_frame_empty"), "inventory"));
            LOCATIONS_MODEL_MAP.put(color, new ModelResourceLocation(new ResourceLocation(Quark.MOD_ID, color.getName() + "_frame_map"), "inventory"));
        }
    }

    @Override
    public void doRender(@Nonnull ColoredItemFrameEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        RenderSystem.pushMatrix();
        BlockPos blockpos = entity.getHangingPosition();
        double d0 = (double)blockpos.getX() - entity.posX + x;
        double d1 = (double)blockpos.getY() - entity.posY + y;
        double d2 = (double)blockpos.getZ() - entity.posZ + z;
        RenderSystem.translated(d0 + 0.5D, d1 + 0.5D, d2 + 0.5D);
        RenderSystem.rotatef(entity.rotationPitch, 1.0F, 0.0F, 0.0F);
        RenderSystem.rotatef(180.0F - entity.rotationYaw, 0.0F, 1.0F, 0.0F);
        this.renderManager.textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        BlockRendererDispatcher blockrendererdispatcher = this.mc.getBlockRendererDispatcher();
        ModelManager modelmanager = blockrendererdispatcher.getBlockModelShapes().getModelManager();

        DyeColor color = entity.getColor();
        ModelResourceLocation modelresourcelocation = entity.getDisplayedItem().getItem() instanceof FilledMapItem ? LOCATIONS_MODEL_MAP.get(color) : LOCATIONS_MODEL.get(color);
        RenderSystem.pushMatrix();
        RenderSystem.translatef(-0.5F, -0.5F, -0.5F);
        if (this.renderOutlines) {
            RenderSystem.enableColorMaterial();
            RenderSystem.setupSolidRenderingTextureCombine(this.getTeamColor(entity));
        }

        blockrendererdispatcher.getBlockModelRenderer().renderModelBrightnessColor(modelmanager.getModel(modelresourcelocation), 1.0F, 1.0F, 1.0F, 1.0F);
        if (this.renderOutlines) {
            RenderSystem.tearDownSolidRenderingTextureCombine();
            RenderSystem.disableColorMaterial();
        }

        RenderSystem.popMatrix();
        RenderSystem.enableLighting();
        if (entity.getDisplayedItem().getItem() == Items.FILLED_MAP) {
            RenderSystem.pushLightingAttributes();
            RenderHelper.enableStandardItemLighting();
        }

        RenderSystem.translatef(0.0F, 0.0F, 0.4375F);
        this.renderItem(entity);
        if (entity.getDisplayedItem().getItem() == Items.FILLED_MAP) {
            RenderHelper.disableStandardItemLighting();
            RenderSystem.popAttributes();
        }

        RenderSystem.enableLighting();
        RenderSystem.popMatrix();
        this.renderName(entity, x + (double)((float)entity.getHorizontalFacing().getXOffset() * 0.3F), y - 0.25D, z + (double)((float)entity.getHorizontalFacing().getZOffset() * 0.3F));
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(@Nonnull ColoredItemFrameEntity entity) {
        return null;
    }

    private void renderItem(ColoredItemFrameEntity itemFrame) {
        ItemStack stack = itemFrame.getDisplayedItem();
        if (!stack.isEmpty()) {
            RenderSystem.pushMatrix();
            MapData mapdata = FilledMapItem.getMapData(stack, itemFrame.world);
            int rotation = mapdata != null ? itemFrame.getRotation() % 4 * 2 : itemFrame.getRotation();
            RenderSystem.rotatef((float)rotation * 360.0F / 8.0F, 0.0F, 0.0F, 1.0F);
            if (!MinecraftForge.EVENT_BUS.post(new RenderItemInFrameEvent(itemFrame, defaultRenderer))) {
                if (mapdata != null) {
                    RenderSystem.disableLighting();
                    this.renderManager.textureManager.bindTexture(MAP_BACKGROUND_TEXTURES);
                    RenderSystem.rotatef(180.0F, 0.0F, 0.0F, 1.0F);
                    RenderSystem.scalef(0.0078125F, 0.0078125F, 0.0078125F);
                    RenderSystem.translatef(-64.0F, -64.0F, 0.0F);
                    RenderSystem.translatef(0.0F, 0.0F, -1.0F);
                    this.mc.gameRenderer.getMapItemRenderer().renderMap(mapdata, true);
                } else {
                    RenderSystem.scalef(0.5F, 0.5F, 0.5F);
                    this.itemRenderer.renderItem(stack, TransformType.FIXED);
                }
            }

            RenderSystem.popMatrix();
        }
    }

    @Override
    protected void renderName(@Nonnull ColoredItemFrameEntity entity, double x, double y, double z) {
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
