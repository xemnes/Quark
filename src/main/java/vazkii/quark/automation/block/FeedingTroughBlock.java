package vazkii.quark.automation.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import vazkii.quark.automation.tile.FeedingTroughTileEntity;
import vazkii.quark.base.block.QuarkBlock;
import vazkii.quark.base.module.Module;

import javax.annotation.Nullable;

/**
 * @author WireSegal
 * Created at 9:39 AM on 9/20/19.
 */
public class FeedingTroughBlock extends QuarkBlock {
    public FeedingTroughBlock(String regname, Module module, ItemGroup creativeTab, Properties properties) {
        super(regname, module, creativeTab, properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new FeedingTroughTileEntity();
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean hasComparatorInputOverride(BlockState state) {
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getComparatorInputOverride(BlockState state, World world, BlockPos pos) {
        return Container.calcRedstone(world.getTileEntity(pos));
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {
        if (world.isRemote)
            return true;
        else {
            INamedContainerProvider container = this.getContainer(state, world, pos);
            if (container != null)
                player.openContainer(container);

            return true;
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean eventReceived(BlockState state, World world, BlockPos pos, int id, int param) {
        super.eventReceived(state, world, pos, id, param);
        TileEntity tile = world.getTileEntity(pos);
        return tile != null && tile.receiveClientEvent(id, param);
    }

    @Override
    @Nullable
    @SuppressWarnings("deprecation")
    public INamedContainerProvider getContainer(BlockState state, World world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        return tile instanceof INamedContainerProvider ? (INamedContainerProvider)tile : null;
    }

}
