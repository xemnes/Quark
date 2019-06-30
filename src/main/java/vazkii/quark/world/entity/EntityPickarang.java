package vazkii.quark.world.entity;

import com.google.common.collect.Multimap;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import vazkii.quark.base.sounds.QuarkSounds;
import vazkii.quark.misc.feature.Pickarang;

import javax.annotation.Nonnull;
import java.util.List;

public class EntityPickarang extends EntityThrowable {

	private static final DataParameter<ItemStack> STACK = EntityDataManager.createKey(EntityPickarang.class, DataSerializers.ITEM_STACK);
	private static final DataParameter<Boolean> RETURNING = EntityDataManager.createKey(EntityPickarang.class, DataSerializers.BOOLEAN);

	private int liveTime;
	private int slot;

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
    	setStack(stack.copy());
    }
    
    @Override
    protected void entityInit() {
    	super.entityInit();

		dataManager.register(STACK, ItemStack.EMPTY);
    	dataManager.register(RETURNING, false);
    }

	@Override
	protected void onImpact(@Nonnull RayTraceResult result) {
		if(dataManager.get(RETURNING))
			return;
		
		EntityLivingBase owner = getThrower();

		ItemStack stack = getStack();
		if(stack.isItemStackDamageable()) {
			stack.attemptDamageItem(1, world.rand, owner instanceof EntityPlayerMP ? (EntityPlayerMP) owner : null);
			setStack(stack);
		}

		if(result.typeOfHit == Type.BLOCK) {
			dataManager.set(RETURNING, true);
			
			if(!(owner instanceof EntityPlayerMP))
				return;
			
			EntityPlayerMP player = (EntityPlayerMP) owner;
			BlockPos hit = result.getBlockPos();

			IBlockState state = world.getBlockState(hit);
			if (state.getBlockHardness(world, hit) <= Pickarang.maxHardness) {
				ItemStack prev = player.getHeldItemMainhand();
				player.setHeldItem(EnumHand.MAIN_HAND, getStack());

				if (player.interactionManager.tryHarvestBlock(hit))
					world.playEvent(2001, hit, Block.getStateId(state));
				else
					playSound(QuarkSounds.ENTITY_PICKARANG_CLANK, 1, 1);

				player.setHeldItem(EnumHand.MAIN_HAND, prev);
			}

		} else if(result.typeOfHit == Type.ENTITY) {
			Entity hit = result.entityHit;
			if(hit != owner) {
				dataManager.set(RETURNING, true);
				
				if(owner instanceof EntityPlayer) {
					EntityPlayer player = (EntityPlayer) owner;
					ItemStack prev = player.getHeldItemMainhand();
					ItemStack pickarang = getStack();
					player.setHeldItem(EnumHand.MAIN_HAND, pickarang);
					Multimap<String, AttributeModifier> modifiers = pickarang.getAttributeModifiers(EntityEquipmentSlot.MAINHAND);
					player.getAttributeMap().applyAttributeModifiers(modifiers);

					int ticksSinceLastSwing = ObfuscationReflectionHelper.getPrivateValue(EntityLivingBase.class, player, "field_184617_aD");
					ObfuscationReflectionHelper.setPrivateValue(EntityLivingBase.class, player, (int) player.getCooldownPeriod(), "field_184617_aD");

					player.attackTargetEntityWithCurrentItem(hit);

					ObfuscationReflectionHelper.setPrivateValue(EntityLivingBase.class, player, ticksSinceLastSwing, "field_184617_aD");

					player.setHeldItem(EnumHand.MAIN_HAND, prev);
					player.getAttributeMap().removeAttributeModifiers(modifiers);
				}
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

			ItemStack stack = getStack();
			int eff = getEfficiencyModifier();
			
			List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class, getEntityBoundingBox().grow(2));
			Vec3d ourPos = getPositionVector();
			for(EntityItem item : items) {
				if (item.isRiding())
					continue;
				item.startRiding(this);
				
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
			double motionMag = 3.25 + eff * 0.25;

			if(motion.lengthSquared() < motionMag) {
				EntityPlayer player = (EntityPlayer) owner;
				ItemStack stackInSlot = player.inventory.getStackInSlot(slot);
				
		        if(!world.isRemote) {
		        	playSound(QuarkSounds.ENTITY_PICKARANG_PICKUP, 1, 1);

			        if(!stack.isEmpty()) {
						if(stackInSlot.isEmpty())
							player.inventory.setInventorySlotContents(slot, stack);
						else if(!player.inventory.addItemStackToInventory(stack))
							player.dropItem(stack, false);
			        }

			        for (EntityItem item : items) {
			        	ItemStack drop = item.getItem();
						if(!player.addItemStackToInventory(drop))
							player.dropItem(drop, false);
						item.setDead();
					}

					setDead();
		        }
			} else {
				motion = motion.normalize().scale(0.7 + eff * 0.25F);
				motionX = motion.x;
				motionY = motion.y;
				motionZ = motion.z;
			}
		}
	}

	@Override
	protected boolean canFitPassenger(Entity passenger) {
		return super.canFitPassenger(passenger) || passenger instanceof EntityItem;
	}

	@Override
	public double getMountedYOffset() {
		return 0;
	}

	@Nonnull
	@Override
	public SoundCategory getSoundCategory() {
		return SoundCategory.PLAYERS;
	}

	public int getEfficiencyModifier() {
		return EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, getStack());
	}

	public ItemStack getStack() {
		return dataManager.get(STACK);
	}

	public void setStack(ItemStack stack) {
		dataManager.set(STACK, stack);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		
		dataManager.set(RETURNING, compound.getBoolean(TAG_RETURNING));
		liveTime = compound.getInteger(TAG_LIVE_TIME);
		slot = compound.getInteger(TAG_RETURN_SLOT);
		
		setStack(new ItemStack(compound.getCompoundTag(TAG_ITEM_STACK)));
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		
		compound.setBoolean(TAG_RETURNING, dataManager.get(RETURNING));
		compound.setInteger(TAG_LIVE_TIME, liveTime);
		compound.setInteger(TAG_RETURN_SLOT, slot);

		compound.setTag(TAG_ITEM_STACK, getStack().serializeNBT());
	}
	
	@Override
	protected float getGravityVelocity() {
		return 0F;
	}

}
