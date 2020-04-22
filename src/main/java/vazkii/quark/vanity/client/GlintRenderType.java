package vazkii.quark.vanity.client;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.DyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.quark.base.Quark;

import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class GlintRenderType {
    public static Map<Integer, RenderType> glintColorMap = new HashMap<Integer, RenderType>() {{
        for (DyeColor color : DyeColor.values())
            put(color.getId(), buildGlintRenderType(color));
    }};

    public static Map<Integer, RenderType> entityGlintColorMap = new HashMap<Integer, RenderType>() {{
        for (DyeColor color : DyeColor.values())
            put(color.getId(), buildEntityGlintRenderType(color));
    }};

    public static void addGlintTypes(Object2ObjectLinkedOpenHashMap<RenderType, BufferBuilder> map) {
        glintColorMap.forEach((color, renderType) -> {
            if (!map.containsKey(renderType))
                map.put(renderType, new BufferBuilder(renderType.getBufferSize()));
        });

        entityGlintColorMap.forEach((color, renderType) -> {
            if (!map.containsKey(renderType))
                map.put(renderType, new BufferBuilder(renderType.getBufferSize()));
        });
    }

    private static RenderType buildGlintRenderType(DyeColor color) {
        String name = color.getName();
        final ResourceLocation res = new ResourceLocation(Quark.MOD_ID, "textures/misc/enchanted_item_glint_" + name + ".png");

        return RenderType.makeType("glint_" + name, DefaultVertexFormats.POSITION_TEX, 7, 256, RenderType.State.getBuilder()
            .texture(new RenderState.TextureState(res, true, false))
            .writeMask(RenderState.COLOR_WRITE)
            .cull(RenderState.CULL_DISABLED)
            .depthTest(RenderState.DEPTH_EQUAL)
            .transparency(RenderState.GLINT_TRANSPARENCY)
            .texturing(RenderState.GLINT_TEXTURING)
            .build(false));
    }

    private static RenderType buildEntityGlintRenderType(DyeColor color) {
        String name = color.getName();
        final ResourceLocation res = new ResourceLocation(Quark.MOD_ID, "textures/misc/enchanted_item_glint_" + name + ".png");

        return RenderType.makeType("entity_glint_" + name, DefaultVertexFormats.POSITION_TEX, 7, 256, RenderType.State.getBuilder()
            .texture(new RenderState.TextureState(res, true, false))
            .writeMask(RenderState.COLOR_WRITE)
            .cull(RenderState.CULL_DISABLED)
            .depthTest(RenderState.DEPTH_EQUAL)
            .transparency(RenderState.GLINT_TRANSPARENCY)
            .texturing(RenderState.ENTITY_GLINT_TEXTURING)
            .build(false));
    }
}
