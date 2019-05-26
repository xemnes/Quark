package vazkii.quark.experimental.entity;

import com.google.common.collect.Sets;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import vazkii.quark.base.sounds.QuarkSounds;
import vazkii.quark.experimental.features.Frogs;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Calendar;
import java.util.Set;

public class EntityFrog extends EntityAnimal {

	private static final DataParameter<Integer> TALK_TIME = EntityDataManager.createKey(EntityFrog.class, DataSerializers.VARINT);
	private static final Set<Item> TEMPTATION_ITEMS = Sets.newHashSet(Items.FISH, Items.EGG, Items.SPIDER_EYE);

	public int spawnCd = -1;
	public int spawnChain = 30;

	public EntityFrog(World worldIn) {
		super(worldIn);
		setSize(0.9f, 0.5f);
	}

	@Override
	protected void entityInit() {
		super.entityInit();

		dataManager.register(TALK_TIME, 0);
	}

	@Override
	protected void initEntityAI() {
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new EntityAIPanic(this, 1.25));
		tasks.addTask(3, new EntityAIMate(this, 1.0));
		tasks.addTask(4, new EntityAITempt(this, 1.2, false, TEMPTATION_ITEMS));
		tasks.addTask(5, new EntityAIFollowParent(this, 1.1));
		tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1));
		tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6));
		tasks.addTask(8, new EntityAILookIdle(this));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25);
	}

	public int getTalkTime() {
		return dataManager.get(TALK_TIME);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		int talkTime = getTalkTime();
		if (talkTime > 0)
			dataManager.set(TALK_TIME, talkTime - 1);

		if(Frogs.frogsDoTheFunny && spawnCd > 0 && spawnChain > 0) {
			spawnCd--;
			if(spawnCd == 0 && !world.isRemote) {
				float multiplier = 0.8F;
				EntityFrog newFrog = new EntityFrog(world);
				newFrog.setPosition(posX, posY, posZ);
				newFrog.motionX = (Math.random() - 0.5) * multiplier;
				newFrog.motionY = (Math.random() - 0.5) * multiplier;
				newFrog.motionZ = (Math.random() - 0.5) * multiplier;
				world.spawnEntity(newFrog);
				newFrog.spawnCd = 2;
				newFrog.spawnChain = spawnChain - 1;
				spawnChain = 0;
			}
		}

		this.prevRotationYaw = this.prevRotationYawHead;
		this.rotationYaw = this.rotationYawHead;
	}

	@Override
	protected boolean canDropLoot() {
		return spawnChain != 0;
	}

	@Override
	public boolean processInteract(EntityPlayer player, @Nonnull EnumHand hand) {
		Calendar calendar = world.getCurrentDate();
		if (Frogs.frogsDoTheFunny && calendar.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
			if (!world.isRemote) {
				if (spawnChain > 0) {
					spawnCd = 50;
					dataManager.set(TALK_TIME, 80);
					world.playSound(null, posX, posY, posZ, QuarkSounds.ENTITY_FROG_WEDNESDAY, SoundCategory.NEUTRAL, 1F, 1F);
				}
			}

			return true;
		}
		
		return super.processInteract(player, hand);
	}

	@Nullable
	@Override
	public EntityAgeable createChild(@Nonnull EntityAgeable otherParent) {
		return new EntityFrog(world);
	}

	@Override
	public boolean isBreedingItem(ItemStack stack) {
		return !stack.isEmpty() && TEMPTATION_ITEMS.contains(stack.getItem());

	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		spawnCd = compound.getInteger("Cooldown");
		spawnChain = compound.getInteger("Chain");
		dataManager.set(TALK_TIME, compound.getInteger("DudeAmount"));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setInteger("Cooldown", spawnCd);
		compound.setInteger("Chain", spawnChain);
		compound.setInteger("DudeAmount", getTalkTime());
	}
}
