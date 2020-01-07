package vazkii.quark.building.module;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.building.block.GrateBlock;

/**
 * @author WireSegal
 * Created at 8:57 AM on 8/27/19.
 */
@LoadModule(category = ModuleCategory.BUILDING, hasSubscriptions = true, subscribeOn = Dist.CLIENT)
public class GrateModule extends Module {
    public static final ThreadLocal<Boolean> RENDER_SHAPE = ThreadLocal.withInitial(() -> false);

    @Override
    public void construct() {
        new GrateBlock("grate", this, ItemGroup.DECORATIONS, Block.Properties.create(Material.IRON)
                .hardnessAndResistance(5, 10).sound(SoundType.METAL));
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void drawBlockHighlight(DrawBlockHighlightEvent event) {
        if (event.getTarget().getType() == RayTraceResult.Type.BLOCK) {
            BlockPos blockpos = ((BlockRayTraceResult)event.getTarget()).getPos();
            World world = Minecraft.getInstance().world;
            if (world != null) { // Should be impossible, but who knows
                BlockState state = world.getBlockState(blockpos);
                if (state.getBlock() instanceof GrateBlock)
                    RENDER_SHAPE.set(true);
            }
        }
    }
}
