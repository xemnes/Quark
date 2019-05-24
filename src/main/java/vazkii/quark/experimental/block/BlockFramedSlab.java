package vazkii.quark.experimental.block;

import javax.annotation.Nonnull;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.arl.block.BlockModSlab;
import vazkii.quark.base.block.IQuarkBlock;
import vazkii.quark.experimental.tile.TileFramed;

public class BlockFramedSlab extends BlockModSlab implements IQuarkBlock, ITileEntityProvider {

	public BlockFramedSlab(boolean doubleSlab) {
		super("frame_slab", Material.WOOD, doubleSlab);
		setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		hasTileEntity = true;
	}

	@Override
	public void breakBlock(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
		super.breakBlock(worldIn, pos, state);
		worldIn.removeTileEntity(pos);
	}
	
	@Override
	public BlockStateContainer createBlockState() {
		return FramedBlockCommons.createStateContainer(this, super.createBlockState());
	}
	
	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		return FramedBlockCommons.getExtendedState(this, state, world, pos);
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileFramed();
	}
	
	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		TileFramed tile = (TileFramed) worldIn.getTileEntity(pos);
		
		ItemStack stack = playerIn.getHeldItem(hand);
		if(stack.getItem() instanceof ItemBlock)
			tile.setInventorySlotContents(0, stack.copy());
		
		worldIn.markBlockRangeForRenderUpdate(pos, pos);

		return true;
	}

}
