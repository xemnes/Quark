package vazkii.quark.oddities.block;

import net.minecraft.block.BlockEnchantmentTable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.arl.util.ProxyRegistry;
import vazkii.quark.base.Quark;
import vazkii.quark.base.lib.LibGuiIDs;
import vazkii.quark.oddities.tile.TileMatrixEnchanter;

public class BlockEnchantingTableReplacement extends BlockEnchantmentTable {

	public BlockEnchantingTableReplacement() {
		setHardness(5.0F);
		setResistance(2000.0F);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileMatrixEnchanter();
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		playerIn.openGui(Quark.instance, LibGuiIDs.MATRIX_ENCHANTING, worldIn, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

		if(stack.hasDisplayName()) {
			TileEntity tileentity = worldIn.getTileEntity(pos);

			if(tileentity instanceof TileMatrixEnchanter)
				((TileMatrixEnchanter) tileentity).setCustomName(stack.getDisplayName());
		}
	}

	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntity tileentity = worldIn.getTileEntity(pos);

		if(tileentity instanceof TileMatrixEnchanter) {
			TileMatrixEnchanter enchanter = (TileMatrixEnchanter) tileentity;
			enchanter.dropItem(0);
			enchanter.dropItem(1);
		}
	}

}
