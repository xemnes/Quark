package vazkii.quark.oddities.tile;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import vazkii.arl.util.ItemNBTHelper;
import vazkii.quark.oddities.inventory.ContainerMatrixEnchanting;
import vazkii.quark.oddities.inventory.EnchantmentMatrix;
import vazkii.quark.oddities.inventory.EnchantmentMatrix.Piece;

public class TileMatrixEnchanter extends TileMatrixEnchanterBase {

	public static final int OPER_ADD = 0;
	public static final int OPER_PLACE = 1;
	public static final int OPER_REMOVE = 2;
	public static final int OPER_ROTATE = 3;
	public static final int OPER_MERGE = 4;

	private static final String TAG_STACK_MATRIX = "quark:enchantingMatrix";
	private static final String TAG_MATRIX = "matrix";
	private static final String TAG_MATRIX_UUID_LESS = "uuidLess";
	private static final String TAG_MATRIX_UUID_MOST = "uuidMost";
	
	public EnchantmentMatrix matrix;
	private boolean matrixDirty = false;
	private UUID matrixId;
	
	@Override
	public void update() {
		super.update();

		ItemStack item = getStackInSlot(0);
		if(item.isEmpty()) {
			matrix = null;
			matrixDirty = true;
		} else
			loadMatrix(item);
		
		if(matrixDirty) {
			makeOutput();
			matrixDirty = false;
		}
	}
	
	public void onOperation(EntityPlayer player, int operation, int arg0, int arg1, int arg2) {
		if(matrix == null)
			return;
		
		switch(operation) {
		case OPER_ADD:
			apply(m -> generateAndPay(m, player));
			break;
		case OPER_PLACE:
			apply(m -> m.place(arg0, arg1, arg2));
			break;
		case OPER_REMOVE:
			apply(m -> m.remove(arg0));
			break;
		case OPER_ROTATE:
			apply(m -> m.rotate(arg0));
			break;
		case OPER_MERGE:
			apply(m -> m.merge(arg0, arg1));
			break;
		}
	}
	
	private void apply(Predicate<EnchantmentMatrix> oper) {
		if(oper.test(matrix)) {
			ItemStack item = getStackInSlot(0);
			commitMatrix(item);
		}
	}
	
	private boolean generateAndPay(EnchantmentMatrix matrix, EntityPlayer player) {
		ItemStack lapis = getStackInSlot(1);
		if(lapis.getCount() > 0) { // TODO test if matrix can and make player pay xp
			lapis.shrink(1);
			matrix.generatePiece();
		}
		
		return true;
	}
	
	private void makeOutput() {
		if(world.isRemote)
			return;
		
		setInventorySlotContents(2, ItemStack.EMPTY);
		ItemStack in = getStackInSlot(0);
		if(!in.isEmpty() && matrix != null && !matrix.placedPieces.isEmpty()) {
			ItemStack out = in.copy();
			Map<Enchantment, Integer> enchantments = new HashMap();
			
			for(int i : matrix.placedPieces) {
				Piece p = matrix.pieces.get(i);
				enchantments.put(p.enchant, p.level);
			}
			EnchantmentHelper.setEnchantments(enchantments, out);
			setInventorySlotContents(2, out);
			out.getTagCompound().removeTag(TAG_STACK_MATRIX);
		}
	}
	
	private void loadMatrix(ItemStack stack) {
		matrix = new EnchantmentMatrix();
		matrixDirty = true;
		makeUUID();
		
		if(stack.hasTagCompound() && stack.getTagCompound().hasKey(TAG_STACK_MATRIX)) {
			NBTTagCompound cmp = ItemNBTHelper.getCompound(stack, TAG_STACK_MATRIX, true);
			if(cmp != null)
				matrix.readFromNBT(cmp);
		} 
	}
	
	private void commitMatrix(ItemStack stack) {
		if(world.isRemote)
			return;
		
		NBTTagCompound cmp = new NBTTagCompound();
		matrix.writeToNBT(cmp);
		ItemNBTHelper.setCompound(stack, TAG_STACK_MATRIX, cmp);
		
		matrixDirty = true;
		makeUUID();
		sync();
	}
	
	private void makeUUID() {
		if(!world.isRemote)
			matrixId = UUID.randomUUID();
	}
	
	@Override
	public void writeSharedNBT(NBTTagCompound cmp) {
		super.writeSharedNBT(cmp);
		
		NBTTagCompound matrixCmp = new NBTTagCompound();
		if(matrix != null) {
			matrix.writeToNBT(matrixCmp);
			
			cmp.setTag(TAG_MATRIX, matrixCmp);
			cmp.setLong(TAG_MATRIX_UUID_LESS, matrixId.getLeastSignificantBits());
			cmp.setLong(TAG_MATRIX_UUID_MOST, matrixId.getMostSignificantBits());
		}
	}
	
	@Override
	public void readSharedNBT(NBTTagCompound cmp) {
		super.readSharedNBT(cmp);
		
		if(cmp.hasKey(TAG_MATRIX)) {
			long least = cmp.getLong(TAG_MATRIX_UUID_LESS);
			long most = cmp.getLong(TAG_MATRIX_UUID_MOST);
			UUID newId = new UUID(most, least);
			
			if(matrixId == null || !newId.equals(matrixId)) {
				NBTTagCompound matrixCmp = cmp.getCompoundTag(TAG_MATRIX);
				matrixId = newId;
				matrix = new EnchantmentMatrix();
				matrix.readFromNBT(matrixCmp);
			}
		} else matrix = null;
	}
	
    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new ContainerMatrixEnchanting(playerInventory, this);
    }
	
}
