/**
 * This class was created by <SanAndreasP>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 *
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 *
 * File Created @ [20/03/2016, 22:33:44 (GMT)]
 */
package vazkii.quark.decoration.tile;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.quark.decoration.feature.VariedChests;

public class TileCustomChest extends TileEntityChest {

	public VariedChests.ChestType chestType = VariedChests.ChestType.NONE;

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setString("type", chestType.name);
		return nbt;
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound nbt = super.getUpdateTag();
		nbt.setString("type", chestType.name);
		return nbt;
	}

	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		super.handleUpdateTag(tag);
		chestType = VariedChests.ChestType.getType(tag.getString("type"));
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}

	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("type", chestType.name);
		return new SPacketUpdateTileEntity(pos, getBlockMetadata(), nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		chestType = VariedChests.ChestType.getType(pkt.getNbtCompound().getString("type"));
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		chestType = VariedChests.ChestType.getType(nbt.getString("type"));
	}

	@SuppressWarnings("incomplete-switch")
	private void setNeighbor(TileEntityChest chestTe, EnumFacing side) {
		if( chestTe.isInvalid() ) {
			adjacentChestChecked = false;
		} else if( adjacentChestChecked ) {
			switch(side) {
			case NORTH:
				if(adjacentChestZNeg != chestTe)
					adjacentChestChecked = false;
				break;
			case SOUTH:
				if(adjacentChestZPos != chestTe)
					adjacentChestChecked = false;
				break;
			case EAST:
				if(adjacentChestXPos != chestTe)
					adjacentChestChecked = false;
				break;
			case WEST:
				if(adjacentChestXNeg != chestTe)
					adjacentChestChecked = false;
			}
		}
	}

	@Nullable
	@Override
	protected TileEntityChest getAdjacentChest(EnumFacing side) {
		BlockPos blockpos = pos.offset(side);

		if(isChestAt(blockpos)) {
			TileEntity tileentity = getWorld().getTileEntity(blockpos);

			if(tileentity instanceof TileCustomChest) {
				TileCustomChest tileentitychest = (TileCustomChest)tileentity;
				tileentitychest.setNeighbor(this, side.getOpposite());
				return tileentitychest;
			}
		}

		return null;
	}

	private boolean isChestAt(BlockPos posIn) {
		if(getWorld() == null) {
			return false;
		} else {
			Block block = getWorld().getBlockState(posIn).getBlock();
			TileEntity te = getWorld().getTileEntity(posIn);
			return block instanceof BlockChest && ((BlockChest) block).chestType == getChestType() && te instanceof TileCustomChest && ((TileCustomChest) te).chestType == chestType;
		}
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return new AxisAlignedBB(pos.getX() - 1, pos.getY(), pos.getZ() - 1, pos.getX() + 2, pos.getY() + 2, pos.getZ() + 2);
	}
}
