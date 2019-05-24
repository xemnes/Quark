package vazkii.quark.experimental.block;

import net.minecraft.block.Block;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.block.BlockModContainer;
import vazkii.quark.base.block.IQuarkBlock;
import vazkii.quark.experimental.tile.TileFramed;

import javax.annotation.Nonnull;

public class BlockFramed extends BlockModContainer implements IQuarkBlock, ITileEntityProvider {
	
	public BlockFramed() {
		super("frame", Material.WOOD);
		setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
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
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		TileFramed tile = (TileFramed) worldIn.getTileEntity(pos);
		
		ItemStack stack = playerIn.getHeldItem(hand);
		if(stack.getItem() instanceof ItemBlock)
			tile.setInventorySlotContents(0, stack.copy());
		
		worldIn.markBlockRangeForRenderUpdate(pos, pos);

		return true;
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
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings("deprecation")
	public boolean shouldSideBeRendered(IBlockState blockState, @Nonnull IBlockAccess blockAccess, @Nonnull BlockPos pos, EnumFacing side) {
		IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
		Block block = iblockstate.getBlock();

		return block != this && super.shouldSideBeRendered(blockState, blockAccess, pos, side);
	}

}
