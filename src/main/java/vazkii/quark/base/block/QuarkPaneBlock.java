package vazkii.quark.base.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PaneBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import vazkii.arl.util.RegistryHelper;
import vazkii.quark.base.module.Module;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.BooleanSupplier;

/**
 * @author WireSegal
 * Created at 1:09 PM on 9/19/19.
 */
public class QuarkPaneBlock extends PaneBlock implements IQuarkBlock {
    public final IQuarkBlock parent;
    private BooleanSupplier enabledSupplier = () -> true;

    public QuarkPaneBlock(IQuarkBlock parent) {
        super(Block.Properties.from(parent.getBlock()));

        this.parent = parent;
        RegistryHelper.registerBlock(this, Objects.toString(parent.getBlock().getRegistryName()) + "_pane");
        RegistryHelper.setCreativeTab(this, ItemGroup.DECORATIONS);
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if(group == ItemGroup.SEARCH || parent.isEnabled())
            super.fillItemGroup(group, items);
    }

    @Nullable
    @Override
    public Module getModule() {
        return parent.getModule();
    }

    @Override
    public QuarkPaneBlock setCondition(BooleanSupplier enabledSupplier) {
        this.enabledSupplier = enabledSupplier;
        return this;
    }

    @Override
    public boolean doesConditionApply() {
        return enabledSupplier.getAsBoolean();
    }

    @Nullable
    @Override
    public float[] getBeaconColorMultiplier(BlockState state, IWorldReader world, BlockPos pos, BlockPos beaconPos) {
        return parent.getBlock().getBeaconColorMultiplier(parent.getBlock().getDefaultState(), world, pos, beaconPos);
    }

    @Nonnull
    public BlockRenderLayer getRenderLayer() {
        return parent.getBlock().getRenderLayer();
    }
}
