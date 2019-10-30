package vazkii.quark.tools.entity;

import com.google.common.collect.Multimap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.Hand;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.*;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import vazkii.quark.base.handler.QuarkSounds;
import vazkii.quark.tools.module.PickarangModule;

import javax.annotation.Nonnull;
import java.util.List;

public class PickarangEntity extends ThrowableEntity {

	private static final DataParameter<ItemStack> STACK = EntityDataManager.createKey(PickarangEntity.class, DataSerializers.ITEMSTACK);
	private static final DataParameter<Boolean> RETURNING = EntityDataManager.createKey(PickarangEntity.class, DataSerializers.BOOLEAN);

	private int liveTime;
	private int slot;
	private int hitCount;

	private static final ThreadLocal<Boolean> IS_PICKARANG_UPDATING = ThreadLocal.withInitial(() -> false);

	private static final String TAG_RETURNING = "returning";
	private static final String TAG_LIVE_TIME = "liveTime";
	private static final String TAG_BLOCKS_BROKEN = "hitCount";
	private static final String TAG_RETURN_SLOT = "returnSlot";
	private static final String TAG_ITEM_STACK = "itemStack";

	public PickarangEntity(EntityType<? extends PickarangEntity> type, World worldIn) {
		super(type, worldIn);
	}
	
    public PickarangEntity(World worldIn, LivingEntity throwerIn) {
    	super(PickarangModule.pickarangType, throwerIn, worldIn);
    	this.setPosition(posX, throwerIn.posY + throwerIn.getEyeHeight(), posZ);
    }
    
    public void setThrowData(int slot, ItemStack stack) {
    	this.slot = slot;
    	setStack(stack.copy());
    }

	@Override
	protected void registerData() {
		dataManager.register(STACK, new ItemStack(PickarangModule.pickarang));
    	dataManager.register(RETURNING, false);
    }

	@Override
	protected void onImpact(@Nonnull RayTraceResult result) {
		if(dataManager.get(RETURNING) || world.isRemote)
			return;
		
		LivingEntity owner = getThrower();

		if(result.getType() == Type.BLOCK && result instanceof BlockRayTraceResult) {
			BlockPos hit = ((BlockRayTraceResult) result).getPos();
			BlockState state = world.getBlockState(hit);
			
			if(getPiercingModifier() == 0 || !state.getMaterial().isOpaque())
				addHit();

			if(!(owner instanceof ServerPlayerEntity))
				return;
			
			ServerPlayerEntity player = (ServerPlayerEntity) owner;
			
			float hardness = state.getBlockHardness(world, hit);
			if (hardness <= PickarangModule.maxHardness && hardness >= 0) {
				ItemStack prev = player.getHeldItemMainhand();
				player.setHeldItem(Hand.MAIN_HAND, getStack());

				if (player.interactionManager.tryHarvestBlock(hit))
					world.playEvent(null, 2001, hit, Block.getStateId(state));
				else
					clank();

				setStack(player.getHeldItemMainhand());

				player.setHeldItem(Hand.MAIN_HAND, prev);
			} else
				clank();

		} else if(result.getType() == Type.ENTITY && result instanceof EntityRayTraceResult) {
			Entity hit = ((EntityRayTraceResult) result).getEntity();
			if(hit != owner) {
				addHit();
				if (hit instanceof PickarangEntity) {
					((PickarangEntity) hit).setReturning();
					clank();
				} else {
					ItemStack pickarang = getStack();
					Multimap<String, AttributeModifier> modifiers = pickarang.getAttributeModifiers(EquipmentSlotType.MAINHAND);

					if (owner != null) {
						ItemStack prev = owner.getHeldItemMainhand();
						owner.setHeldItem(Hand.MAIN_HAND, pickarang);
						owner.getAttributes().applyAttributeModifiers(modifiers);

						int ticksSinceLastSwing = owner.ticksSinceLastSwing;
						owner.ticksSinceLastSwing = (int) (1.0 / owner.getAttribute(SharedMonsterAttributes.ATTACK_SPEED).getValue() * 20.0) + 1;

						float prevHealth = hit instanceof LivingEntity ? ((LivingEntity) hit).getHealth() : 0;

						PickarangModule.setActivePickarang(this);

						if (owner instanceof PlayerEntity)
							((PlayerEntity) owner).attackTargetEntityWithCurrentItem(hit);
						else
							owner.attackEntityAsMob(hit);

						if (hit instanceof LivingEntity && ((LivingEntity) hit).getHealth() == prevHealth)
							clank();

						PickarangModule.setActivePickarang(null);

						owner.ticksSinceLastSwing = ticksSinceLastSwing;

						setStack(owner.getHeldItemMainhand());
						owner.setHeldItem(Hand.MAIN_HAND, prev);
						owner.getAttributes().removeAttributeModifiers(modifiers);
					} else {
						AttributeMap map = new AttributeMap();
						IAttributeInstance attack = map.registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
						attack.setBaseValue(1);
						map.applyAttributeModifiers(modifiers);
						ItemStack stack = getStack();
						stack.attemptDamageItem(1, world.rand, null);
						setStack(stack);
						hit.attackEntityFrom(new IndirectEntityDamageSource("player", this, this).setProjectile(),
								(float) attack.getValue());
					}
				}
			}
		}
	}

	public void clank() {
		playSound(QuarkSounds.ENTITY_PICKARANG_CLANK, 1, 1);
		setReturning();
	}

	public void addHit() {
		hitCount++;
		if(hitCount > getPiercingModifier())
			setReturning();
	}
	
	protected void setReturning() {
		int piercing = getPiercingModifier();
		if (hitCount <= piercing)
			hitCount = piercing + 1;
		dataManager.set(RETURNING, true);
	}

	@Override
	public boolean canBeCollidedWith() {
		return IS_PICKARANG_UPDATING.get();
	}

	@Override
	public boolean isPushedByWater() {
		return false;
	}

	@Override
	public void tick() {
		IS_PICKARANG_UPDATING.set(true);
		super.tick();
		this.ignoreTime = 0;
		IS_PICKARANG_UPDATING.set(false);
		
		if(!isAlive())
			return;
		
		boolean returning = dataManager.get(RETURNING);
		liveTime++;
		
		if(!returning) {
			if(liveTime > PickarangModule.timeout)
				setReturning();
		} else {
			noClip = true;

			ItemStack stack = getStack();
			int eff = getEfficiencyModifier();
			
			List<ItemEntity> items = world.getEntitiesWithinAABB(ItemEntity.class, getBoundingBox().grow(2));
			List<ExperienceOrbEntity> xp = world.getEntitiesWithinAABB(ExperienceOrbEntity.class, getBoundingBox().grow(2));

			Vec3d ourPos = getPositionVector();
			for(ItemEntity item : items) {
				if (item.isPassenger())
					continue;
				item.startRiding(this);
				
				item.setPickupDelay(2);
			}
			for(ExperienceOrbEntity xpOrb : xp) {
				if (xpOrb.isPassenger())
					continue;
				xpOrb.startRiding(this);

				xpOrb.delayBeforeCanPickup = 2;
			}


			LivingEntity owner = getThrower();
			if(owner == null || !owner.isAlive() || !(owner instanceof PlayerEntity)) {
				if(!world.isRemote) {
					entityDropItem(stack, 0);
					remove();
				}

				return;
			}
			
			Vec3d ownerPos = owner.getPositionVector().add(0, 1, 0);
			Vec3d motion = ownerPos.subtract(ourPos);
			double motionMag = 3.25 + eff * 0.25;

			if(motion.lengthSquared() < motionMag) {
				PlayerEntity player = (PlayerEntity) owner;
				ItemStack stackInSlot = player.inventory.getStackInSlot(slot);
				
		        if(!world.isRemote) {
		        	playSound(QuarkSounds.ENTITY_PICKARANG_PICKUP, 1, 1);

			        if(!stack.isEmpty()) {
						if(player.isAlive() && stackInSlot.isEmpty())
							player.inventory.setInventorySlotContents(slot, stack);
						else if(!player.isAlive() || !player.inventory.addItemStackToInventory(stack))
							player.dropItem(stack, false);
			        }

			        if (player.isAlive()) {
						for (ItemEntity item : items) {
							ItemStack drop = item.getItem();
							if (!player.addItemStackToInventory(drop))
								player.dropItem(drop, false);
							item.remove();
						}

						for (ExperienceOrbEntity xpOrb : xp) {
							xpOrb.onCollideWithPlayer(player);
						}

						for (Entity riding : getPassengers()) {
							if (!riding.isAlive())
								continue;

							if (riding instanceof ItemEntity) {
								ItemStack drop = ((ItemEntity) riding).getItem();
								if (!player.addItemStackToInventory(drop))
									player.dropItem(drop, false);
								riding.remove();
							} else if (riding instanceof ExperienceOrbEntity)
								riding.onCollideWithPlayer(player);
						}
					}

					remove();
		        }
			} else
				setMotion(motion.normalize().scale(0.7 + eff * 0.325F));
		}
	}

	@Override
	protected boolean canFitPassenger(Entity passenger) {
		return super.canFitPassenger(passenger) || passenger instanceof ItemEntity || passenger instanceof ExperienceOrbEntity;
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
	
	public int getPiercingModifier() {
		return EnchantmentHelper.getEnchantmentLevel(Enchantments.PIERCING, getStack());
	}

	public ItemStack getStack() {
		return dataManager.get(STACK);
	}

	public void setStack(ItemStack stack) {
		dataManager.set(STACK, stack);
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		
		dataManager.set(RETURNING, compound.getBoolean(TAG_RETURNING));
		liveTime = compound.getInt(TAG_LIVE_TIME);
		hitCount = compound.getInt(TAG_BLOCKS_BROKEN);
		slot = compound.getInt(TAG_RETURN_SLOT);

		if (compound.contains(TAG_ITEM_STACK))
			setStack(ItemStack.read(compound.getCompound(TAG_ITEM_STACK)));
		else
			setStack(new ItemStack(PickarangModule.pickarang));
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		
		compound.putBoolean(TAG_RETURNING, dataManager.get(RETURNING));
		compound.putInt(TAG_LIVE_TIME, liveTime);
		compound.putInt(TAG_BLOCKS_BROKEN, hitCount);
		compound.putInt(TAG_RETURN_SLOT, slot);

		compound.put(TAG_ITEM_STACK, getStack().serializeNBT());
	}

	@Nonnull
	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected float getGravityVelocity() {
		return 0F;
	}

}
