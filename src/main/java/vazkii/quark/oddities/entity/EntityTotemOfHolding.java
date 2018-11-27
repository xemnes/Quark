package vazkii.quark.oddities.entity;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityTotemOfHolding extends Entity {

	private static final String TAG_ITEMS = "storedItems";

	List<ItemStack> storedItems = new LinkedList();

	public EntityTotemOfHolding(World worldIn) {
		super(worldIn);
		isImmuneToFire = true;
		setSize(0.5F, 1F);
	}

	public void addItem(ItemStack stack) {
		storedItems.add(stack);
	}

	@Override
	public boolean hitByEntity(Entity e) {
		if(!world.isRemote && e instanceof EntityPlayer) {
			int drops = Math.min(storedItems.size(), 2 + world.rand.nextInt(3));
			EntityPlayer player = (EntityPlayer) e;
			for(int i = 0; i < drops; i++) {
				ItemStack stack = storedItems.remove(0);
				if(!player.addItemStackToInventory(stack))
					entityDropItem(stack, 0);
			}

			return false;
		}

		return true;
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();

		if(!world.isRemote && storedItems.isEmpty())
			setDead();
	}

	@Override
	protected void entityInit() { }

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		NBTTagList list = compound.getTagList(TAG_ITEMS, 10);
		storedItems = new LinkedList();

		for(int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound cmp = list.getCompoundTagAt(i);
			ItemStack stack = new ItemStack(cmp);
			storedItems.add(stack);
		}
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		NBTTagList list = new NBTTagList();
		for(ItemStack stack : storedItems) {
			NBTTagCompound cmp = new NBTTagCompound();
			stack.writeToNBT(cmp);
			list.appendTag(cmp);
		}

		compound.setTag(TAG_ITEMS, list);
	}

}
