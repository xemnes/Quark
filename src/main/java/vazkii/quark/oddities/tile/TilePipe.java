package vazkii.quark.oddities.tile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import scala.actors.threadpool.Arrays;
import vazkii.arl.block.tile.TileMod;

public class TilePipe extends TileMod implements ITickable {

	private static final String TAG_PIPE_ITEMS = "pipeItems";

	public static final int MAX_PIPE_ITEMS = 16;
	public static final int ITEM_TRAVEL_TIME = 10;

	List<PipeItem> pipeItems = new LinkedList();

	@Override
	public void update() {
		ListIterator<PipeItem> itemItr = pipeItems.listIterator();
		while(itemItr.hasNext()) {
			PipeItem item = itemItr.next();
			if(item.tick(this)) {
				itemItr.remove();

				if(item.valid)
					passOut(item);
				else dropItem(item.stack);
			}
		}
	}

	public boolean passIn(ItemStack stack, EnumFacing face) {
		if(pipeItems.size() > MAX_PIPE_ITEMS)
			return false;

		System.out.println("Passing " + stack + " into " + getPos() + " from " + face);
		PipeItem item = new PipeItem(stack, face);
		pipeItems.add(item);
		
		if(!world.isRemote) {
			BlockPos pos = getPos();
			((WorldServer) world).spawnParticle(EnumParticleTypes.CLOUD, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 10, 0.1, 0.1, 0.1, 0);
		}
		
		return true;
	}

	void passOut(PipeItem item) {
		System.out.println("Passing " + item.stack + " out " + item.outgoingFace);
		BlockPos targetPos = getPos().offset(item.outgoingFace);
		TileEntity tile = world.getTileEntity(targetPos);
		boolean did = false;
		if(tile instanceof TilePipe)
			did = ((TilePipe) tile).passIn(item.stack, item.outgoingFace.getOpposite());
		else {
			ItemStack result = putIntoInv(item.stack, tile, item.outgoingFace.getOpposite(), false);
			if(result.getCount() != item.stack.getCount()) {
				did = true;
				if(!result.isEmpty())
					dropItem(result);
			}
		}
		
		if(!did)
			dropItem(item.stack);
	}

	public void dropItem(ItemStack stack) {
		System.out.println("Dropping " + stack);
	}

	@Override
	public void readSharedNBT(NBTTagCompound cmp) {
		super.readSharedNBT(cmp);

		NBTTagList pipeItemList = cmp.getTagList(TAG_PIPE_ITEMS, cmp.getId());
		pipeItems.clear();
		pipeItemList.forEach(listCmp -> {
			PipeItem item = PipeItem.readFromNBT((NBTTagCompound) cmp);
			pipeItems.add(item);
		});
	}

	@Override
	public void writeSharedNBT(NBTTagCompound cmp) {
		super.writeSharedNBT(cmp);

		NBTTagList pipeItemList = new NBTTagList();
		for(PipeItem item : pipeItems) {
			NBTTagCompound listCmp = new NBTTagCompound();
			item.writeToNBT(listCmp);
			pipeItemList.appendTag(listCmp);
		}
		cmp.setTag(TAG_PIPE_ITEMS, pipeItemList);
	}

	boolean canFit(ItemStack stack, BlockPos pos, EnumFacing face) {
		TileEntity tile = world.getTileEntity(pos);
		if(tile == null)
			return false;
		
		if(tile instanceof TilePipe)
			return true;
		else {
			ItemStack result = putIntoInv(stack, tile, face, true);
			return result.isEmpty();
		}
	}
	
	ItemStack putIntoInv(ItemStack stack, TileEntity tile, EnumFacing face, boolean simulate) {
		IItemHandler handler = null;
		if(tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, face))
			handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, face);
		else if(tile instanceof ISidedInventory)
			handler = new SidedInvWrapper((ISidedInventory) tile, face);
		else if(tile instanceof IInventory)
			handler = new InvWrapper((IInventory) tile);
		
		if(handler != null)
			return ItemHandlerHelper.insertItem(handler, stack, simulate);
		return stack;
	}

	public static class PipeItem {

		private static final String TAG_TICKS = "ticksInPipe";
		private static final String TAG_INCOMING = "incomingFace";
		private static final String TAG_OUTGOING = "outgoingFace";

		private static final List<EnumFacing> HORIZONTAL_SIDES_LIST = Arrays.asList(EnumFacing.HORIZONTALS);

		public final ItemStack stack;
		public int ticksInPipe;
		public EnumFacing incomingFace;
		public EnumFacing outgoingFace;
		public boolean valid = true;

		public PipeItem(ItemStack stack, EnumFacing face) {
			this.stack = stack;
			ticksInPipe = 0;
			incomingFace = outgoingFace = face;
		}

		boolean tick(TilePipe pipe) {
			ticksInPipe++;

			if(ticksInPipe == ITEM_TRAVEL_TIME / 2) {
				outgoingFace = getTargetFace(pipe);
				System.out.println(stack + " going " + outgoingFace);
			}

			if(outgoingFace == null) {
				valid = false;
				return true;
			}

			return ticksInPipe >= ITEM_TRAVEL_TIME;
		}

		EnumFacing getTargetFace(TilePipe pipe) {
			BlockPos pipePos = pipe.getPos();
			if(incomingFace != EnumFacing.DOWN && pipe.canFit(stack, pipePos.offset(EnumFacing.DOWN), EnumFacing.UP))
				return EnumFacing.DOWN;

			List<EnumFacing> sides = new ArrayList(HORIZONTAL_SIDES_LIST);
			sides.remove(incomingFace);
			Collections.shuffle(sides); // TODO make deterministic
			for(EnumFacing side : sides) {
				if(pipe.canFit(stack, pipePos.offset(side), side.getOpposite()))
					return side;
			}

			if(incomingFace != EnumFacing.UP && pipe.canFit(stack, pipePos.offset(EnumFacing.UP), EnumFacing.DOWN))
				return EnumFacing.UP;

			return null;
		}

		public float getTimeFract(float pticks) {
			return (float) (ticksInPipe + pticks) / ITEM_TRAVEL_TIME;
		}

		public float getTimeFract() {
			return getTimeFract(0F);
		}

		public void writeToNBT(NBTTagCompound cmp) {
			stack.writeToNBT(cmp);
			cmp.setInteger(TAG_TICKS, ticksInPipe);
			cmp.setInteger(TAG_INCOMING, incomingFace.ordinal());
			cmp.setInteger(TAG_OUTGOING, outgoingFace.ordinal());
		}

		public static PipeItem readFromNBT(NBTTagCompound cmp) {
			ItemStack stack = new ItemStack(cmp);
			EnumFacing inFace = EnumFacing.VALUES[cmp.getInteger(TAG_INCOMING)];
			PipeItem item = new PipeItem(stack, inFace);
			item.ticksInPipe = cmp.getInteger(TAG_TICKS);
			item.outgoingFace = EnumFacing.VALUES[cmp.getInteger(TAG_OUTGOING)];

			return item;
		}

	}

}
