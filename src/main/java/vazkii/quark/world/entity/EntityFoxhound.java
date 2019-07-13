/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 * <p>
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * <p>
 * File Created @ [Jul 13, 2019, 12:04 AM (EST)]
 */
package vazkii.quark.world.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.quark.tweaks.ai.EntityAIWantLove;
import vazkii.quark.world.entity.ai.EntityAIFoxhoundSleep;
import vazkii.quark.world.entity.ai.EntityAIIfNoSleep;

import javax.annotation.Nonnull;
import java.util.UUID;

public class EntityFoxhound extends EntityWolf {

	private static final DataParameter<Integer> TEMPTATION = EntityDataManager.createKey(EntityFoxhound.class, DataSerializers.VARINT);
	private static final DataParameter<Boolean> SLEEPING = EntityDataManager.createKey(EntityFoxhound.class, DataSerializers.BOOLEAN);

	public EntityFoxhound(World worldIn) {
		super(worldIn);
		this.setPathPriority(PathNodeType.WATER, -1.0F);
		this.setPathPriority(PathNodeType.LAVA, 1.0F);
		this.setPathPriority(PathNodeType.DANGER_FIRE, 1.0F);
		this.setPathPriority(PathNodeType.DAMAGE_FIRE, 1.0F);
		this.isImmuneToFire = true;
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(TEMPTATION, 0);
		dataManager.register(SLEEPING, false);
	}

	@Override
	public boolean isNoDespawnRequired() {
		return getTemptation() > 0 || super.isNoDespawnRequired();
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		if (EntityAIWantLove.needsPets(this)) {
			Entity owner = getOwner();
			if (owner != null && owner.getDistanceSq(this) < 1 && !owner.isInWater() && !owner.isImmuneToFire() && (!(owner instanceof EntityPlayer) || !((EntityPlayer) owner).isCreative()))
				owner.setFire(5);
		}

		if (this.world.isRemote) {
			for (int i = 0; i < 2; ++i)
				this.world.spawnParticle(EnumParticleTypes.FLAME, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + this.rand.nextDouble() * this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, 0.0D, 0.0D, 0.0D);
		} else if (isSleeping()) {
			BlockPos below = getPosition().down();
			TileEntity tile = world.getTileEntity(below);
			if (tile instanceof TileEntityFurnace) {
				int cookTime = ((TileEntityFurnace) tile).getField(2);
				if (cookTime > 0)
					((TileEntityFurnace) tile).setField(2, Math.min(199, cookTime + 1));
			}
		}
	}

	@Override
	protected void initEntityAI() {
		this.aiSit = new EntityAISit(this);
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, this.aiSit);
		this.tasks.addTask(3, new EntityAILeapAtTarget(this, 0.4F));
		this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.0D, true));
		this.tasks.addTask(5, new EntityAIFoxhoundSleep(this, 0.8D, true));
		this.tasks.addTask(6, new EntityAIFoxhoundSleep(this, 0.8D, false));
		this.tasks.addTask(7, new EntityAIFollowOwner(this, 1.0D, 10.0F, 2.0F));
		this.tasks.addTask(8, new EntityAIIfNoSleep(this, new EntityAIMate(this, 1.0D)));
		this.tasks.addTask(9, new EntityAIWanderAvoidWater(this, 1.0D));
		this.tasks.addTask(10, new EntityAIIfNoSleep(this, new EntityAIBeg(this, 8.0F)));
		this.tasks.addTask(11, new EntityAIIfNoSleep(this, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F)));
		this.tasks.addTask(11, new EntityAIIfNoSleep(this, new EntityAILookIdle(this)));
		this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
		this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
		this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true));
		this.targetTasks.addTask(4, new EntityAITargetNonTamed<>(this, EntityAnimal.class, false,
				target -> target instanceof EntitySheep || target instanceof EntityRabbit));
		this.targetTasks.addTask(4, new EntityAITargetNonTamed<>(this, EntityPlayer.class, false,
				target -> getTemptation() < 15 && rand.nextInt(getTemptation()) == 0));
		this.targetTasks.addTask(5, new EntityAINearestAttackableTarget<>(this, AbstractSkeleton.class, false));
	}

	@Override
	public boolean isAngry() {
		return getTemptation() < 15 || super.isAngry();
	}

	@Override
	public boolean attackEntityAsMob(Entity entityIn) {
		if (entityIn.isImmuneToFire())
			return false;

		boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this).setFireDamage(),
				((int)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue()));

		if (flag) {
			entityIn.setFire(5);
			this.applyEnchantments(this, entityIn);
		}

		return flag;
	}

	@Override
	public boolean processInteract(EntityPlayer player, @Nonnull EnumHand hand) {
		ItemStack itemstack = player.getHeldItem(hand);

		if (!this.isTamed() && getTemptation() < 15 && !itemstack.isEmpty()) {
			if (itemstack.getItem() == Items.COAL && (player.isCreative() || player.getActivePotionEffect(MobEffects.FIRE_RESISTANCE) != null)) {
				this.setTemptation(getTemptation() + 1);
				this.navigator.clearPath();
				this.setAttackTarget(null);
				this.playSound(SoundEvents.ENTITY_WOLF_WHINE, 1F, 0.5F + (float) Math.random() * 0.5F);
				this.playTameEffect(true);
				return true;
			}
		}

		if (!world.isRemote)
			setSleeping(false);

		return super.processInteract(player, hand);
	}

	@Override
	public EntityWolf createChild(EntityAgeable otherParent) {
		EntityWolf entitywolf = new EntityFoxhound(this.world);
		UUID uuid = this.getOwnerId();

		if (uuid != null) {
			entitywolf.setOwnerId(uuid);
			entitywolf.setTamed(true);
		}

		return entitywolf;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setInteger("Temptation", getTemptation());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		setTemptation(compound.getInteger("Temptation"));
	}

	@Override
	public void setTamed(boolean tamed) {
		super.setTamed(tamed);
		if (tamed)
			setTemptation(15);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return isSleeping() ? null : super.getAmbientSound();
	}

	public int getTemptation() {
		return dataManager.get(TEMPTATION);
	}

	public void setTemptation(int temptation) {
		dataManager.set(TEMPTATION, temptation);
	}

	public boolean isSleeping() {
		return dataManager.get(SLEEPING);
	}

	public void setSleeping(boolean sleeping) {
		dataManager.set(SLEEPING, sleeping);
	}

}
