package vazkii.quark.world.entity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import vazkii.quark.misc.feature.Pickarang;

public class EntityPickarang extends EntityThrowable {

	private static final DataParameter<Boolean> RETURNING = EntityDataManager.createKey(EntityPickarang.class, DataSerializers.BOOLEAN);
	
	private int liveTime;
	private int slot;
	private ItemStack stack;
	
	private static final String TAG_RETURNING = "returning";
	private static final String TAG_LIVE_TIME = "liveTime";
	private static final String TAG_RETURN_SLOT = "returnSlot";
	private static final String TAG_ITEM_STACK = "itemStack";

	public EntityPickarang(World worldIn) {
		super(worldIn);
	}
	
    public EntityPickarang(World worldIn, EntityLivingBase throwerIn) {
    	super(worldIn, throwerIn);
    }
    
    public void setThrowData(int slot, ItemStack stack) {
    	this.slot = slot;
    	this.stack = stack.copy();
    }
    
    @Override
    protected void entityInit() {
    	super.entityInit();
    	
    	dataManager.register(RETURNING, false);
    }

	@Override
	protected void onImpact(RayTraceResult result) {
		if(dataManager.get(RETURNING))
			return;
		
		EntityLivingBase owner = getThrower();

		if(result.typeOfHit == Type.BLOCK) {
			dataManager.set(RETURNING, true);
			
			if(!(owner instanceof EntityPlayer))
				return;
			
			EntityPlayer player = (EntityPlayer) owner;
			BlockPos hit = result.getBlockPos();
			IBlockState state = world.getBlockState(hit);
			Block block = state.getBlock();
			
			float hardness = state.getBlockHardness(world, hit);
			int neededHarvestLevel = block.getHarvestLevel(state);
			int harvestLevel = 3;

			// TODO if not play clink sound
			if(neededHarvestLevel <= harvestLevel && hardness != -1 && hardness < 50F) {
				world.setBlockToAir(hit);
				world.playEvent(2001, hit, Block.getStateId(state));

				block.harvestBlock(world, player, hit, state, world.getTileEntity(hit), stack);
			}
		} else if(result.typeOfHit == Type.ENTITY) {
			Entity hit = result.entityHit;
			if(hit != owner) {
				dataManager.set(RETURNING, true);
				
				if(owner instanceof EntityPlayer)
					hit.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) owner), 3);
			}
		}
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		
		if(isDead)
			return;
		
		boolean returning = dataManager.get(RETURNING);
		liveTime++;
		
		if(!returning) {
			if(liveTime > Pickarang.timeout)
				dataManager.set(RETURNING, true);
		} else {
			noClip = true;
			
			List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class, getEntityBoundingBox().grow(2));
			Vec3d ourPos = getPositionVector();
			for(EntityItem item : items) {
				Vec3d itemPos = item.getPositionVector();
				Vec3d motion = ourPos.subtract(itemPos);
				if(motion.length() > 1)
					motion = motion.normalize();
				
				item.motionX = motion.x;
				item.motionY = motion.y;
				item.motionZ = motion.z;
				
				item.setPickupDelay(2);
			}
			
			EntityLivingBase owner = getThrower();
			if(owner == null || owner.isDead || !(owner instanceof EntityPlayer)) {
				if(!world.isRemote) {
					entityDropItem(stack, 0);
					setDead();
				}
				
				return;
			}
			
			Vec3d ownerPos = owner.getPositionVector().add(0, 1, 0);
			Vec3d motion = ownerPos.subtract(ourPos);
			
			if(motion.lengthSquared() < 3) {
				EntityPlayer player = (EntityPlayer) owner;
				ItemStack stackInSlot = player.inventory.getStackInSlot(slot);
				
		        if(stack.isItemStackDamageable())
		        	stack.damageItem(1, player);
				
		        if(!world.isRemote) { // TODO return sfx
			        if(!stack.isEmpty()) {
						if(stackInSlot.isEmpty())
							player.inventory.setInventorySlotContents(slot, stack);
						else if(!player.inventory.addItemStackToInventory(stack))
							entityDropItem(stack, 0);
			        }

					setDead();
		        }
			} else {
				motion = motion.normalize().scale(0.7);
				motionX = motion.x;
				motionY = motion.y;
				motionZ = motion.z;
			}
		}
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		
		dataManager.set(RETURNING, compound.getBoolean(TAG_RETURNING));
		liveTime = compound.getInteger(TAG_LIVE_TIME);
		slot = compound.getInteger(TAG_RETURN_SLOT);
		
		stack = new ItemStack(compound.getCompoundTag(TAG_ITEM_STACK));
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		
		compound.setBoolean(TAG_RETURNING, dataManager.get(RETURNING));
		compound.setInteger(TAG_LIVE_TIME, liveTime);
		compound.setInteger(TAG_RETURN_SLOT, slot);
		
		NBTTagCompound stackCmp = new NBTTagCompound();
		stack.writeToNBT(stackCmp);
		compound.setTag(TAG_ITEM_STACK, stackCmp);
	}
	
	@Override
	protected float getGravityVelocity() {
		return 0F;
	}

}
