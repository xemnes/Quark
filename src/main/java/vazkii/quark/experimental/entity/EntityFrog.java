package vazkii.quark.experimental.entity;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import vazkii.quark.base.sounds.QuarkSounds;

import java.util.Calendar;

public class EntityFrog extends EntityCreature {

	private static final DataParameter<Integer> TALK_TIME = EntityDataManager.createKey(EntityFrog.class, DataSerializers.VARINT);

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
		tasks.addTask(1, new EntityAIWander(this, 0.001));
		tasks.addTask(2, new EntityAIWatchClosest(this, EntityPlayer.class, 6));
		tasks.addTask(3, new EntityAILookIdle(this));
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

		if(spawnCd > 0 && spawnChain > 0) {
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
	protected float getJumpUpwardsMotion() {
		return 0.65f;
	}

	@Override
	protected boolean canDropLoot() {
		return spawnChain != 0;
	}

	@Override
	protected boolean processInteract(EntityPlayer player, EnumHand hand) {
		if(!world.isRemote) {
			Calendar calendar = world.getCurrentDate();

			if (spawnChain > 0 && calendar.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
				spawnCd = 50;
				dataManager.set(TALK_TIME, 80);
				world.playSound(null, posX, posY, posZ, QuarkSounds.ENTITY_FROG_WEDNESDAY, SoundCategory.NEUTRAL, 1F, 1F);
			}
		}
		
		return true;
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
