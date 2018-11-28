package vazkii.quark.oddities.entity;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityTotemOfHolding extends Entity {

	private static final String TAG_ITEMS = "storedItems";
	private static final String TAG_DYING = "dying";
	
    private static final DataParameter<Boolean> DYING = EntityDataManager.<Boolean>createKey(EntityTotemOfHolding.class, DataSerializers.BOOLEAN);
	
    public static final int DEATH_TIME = 40;

    int deathTicks = 0;
	List<ItemStack> storedItems = new LinkedList();

	public EntityTotemOfHolding(World worldIn) {
		super(worldIn);
		isImmuneToFire = true;
		setSize(0.5F, 1F);
	}
	
	@Override
	protected void entityInit() { 
		dataManager.register(DYING, false);
	}

	public void addItem(ItemStack stack) {
		storedItems.add(stack);
	}

	@Override
	public boolean hitByEntity(Entity e) {
		if(!world.isRemote && e instanceof EntityPlayer) {
			int drops = Math.min(storedItems.size(), 3 + world.rand.nextInt(4));
			EntityPlayer player = (EntityPlayer) e;
			for(int i = 0; i < drops; i++) {
				ItemStack stack = storedItems.remove(0);
				if(!player.addItemStackToInventory(stack))
					entityDropItem(stack, 0);
			}
			
			if(world instanceof WorldServer) {
				((WorldServer) world).spawnParticle(EnumParticleTypes.DAMAGE_INDICATOR, false, posX, posY + 0.5, posZ, drops, 0.1, 0.5, 0.1, 0);
				((WorldServer) world).spawnParticle(EnumParticleTypes.CRIT_MAGIC, false, posX, posY + 0.5, posZ, drops, 0.4, 0.5, 0.4, 0);
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

		if(storedItems.isEmpty() && !world.isRemote)
			dataManager.set(DYING, true);
		
		if(isDying()) {
			if(deathTicks > DEATH_TIME)
				setDead();
			else deathTicks++;
		}
		
		else if(world.isRemote)
			world.spawnParticle(EnumParticleTypes.PORTAL, posX, posY + (Math.random() - 0.5) * 0.2, posZ, Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5);
	}

	public int getDeathTicks() {
		return deathTicks;
	}
	
	public boolean isDying() {
		return dataManager.get(DYING); 
	}
				
	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		NBTTagList list = compound.getTagList(TAG_ITEMS, 10);
		storedItems = new LinkedList();

		for(int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound cmp = list.getCompoundTagAt(i);
			ItemStack stack = new ItemStack(cmp);
			storedItems.add(stack);
		}
		
		boolean dying = compound.getBoolean(TAG_DYING);
		dataManager.set(DYING, dying);
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
		compound.setBoolean(TAG_DYING, isDying());
	}

}
