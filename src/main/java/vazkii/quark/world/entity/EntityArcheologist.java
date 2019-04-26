package vazkii.quark.world.entity;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIFollowGolem;
import net.minecraft.entity.ai.EntityAILookAtTradePlayer;
import net.minecraft.entity.ai.EntityAIMoveIndoors;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITradePlayer;
import net.minecraft.entity.ai.EntityAIVillagerInteract;
import net.minecraft.entity.ai.EntityAIVillagerMate;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;

public class EntityArcheologist extends EntityLiving implements IMerchant {

	private MerchantRecipeList buyingList;
	private EntityPlayer buyingPlayer;
	private boolean needsInitilization;
	private int timeUntilReset;
	private int randomTickDivider;

	public EntityArcheologist(World worldIn) {
		super(worldIn);
		setSize(0.6F, 1.95F);
	}
	
	private void populateBuyingList() {
        if(buyingList == null)
            buyingList = new MerchantRecipeList();
		
		// TODO
		buyingList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, 3), new ItemStack(Items.BONE, 4)));
		buyingList.add(new MerchantRecipe(new ItemStack(Items.BONE, 20), new ItemStack(Items.EMERALD, 1)));
	}


	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
	}

	@Override
	protected void updateAITasks() {
		if(--randomTickDivider <= 0) {
			randomTickDivider = 70 + rand.nextInt(50);

			if(!isTrading() && timeUntilReset > 0) {
				--timeUntilReset;

				if(timeUntilReset <= 0 && needsInitilization)
					for(MerchantRecipe merchantrecipe : buyingList)
						if(merchantrecipe.isRecipeDisabled())
							merchantrecipe.increaseMaxTradeUses(rand.nextInt(6) + rand.nextInt(6) + 2);
			}
		}
	}

	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand) {
		ItemStack itemstack = player.getHeldItem(hand);
		boolean flag = itemstack.getItem() == Items.NAME_TAG;

		if(flag) {
			itemstack.interactWithEntity(player, this, hand);
			return true;
		}
		else if(isEntityAlive() && !isTrading() && !player.isSneaking()) {
			if(buyingList == null)
				populateBuyingList();

			if(hand == EnumHand.MAIN_HAND)
				player.addStat(StatList.TALKED_TO_VILLAGER);

			if(!world.isRemote && !buyingList.isEmpty()) {
				setCustomer(player);
				player.displayVillagerTradeGui(this);
			}
			else if(buyingList.isEmpty())
				return super.processInteract(player, hand);

			return true;
		}
		else return super.processInteract(player, hand);
	}

	@Override
	public void setCustomer(EntityPlayer player) {
		buyingPlayer = player;
	}

	@Override
	public EntityPlayer getCustomer() {
		return buyingPlayer;
	}

	@Override
	public MerchantRecipeList getRecipes(EntityPlayer player) {
		if (buyingList == null)
			populateBuyingList();

		return buyingList;
	}

	@Override
	public void setRecipes(MerchantRecipeList recipeList) {
		buyingList = recipeList;
		livingSoundTime = -getTalkInterval();
		playSound(SoundEvents.ENTITY_VILLAGER_YES, getSoundVolume(), getSoundPitch());
	}

	@Override
	public void useRecipe(MerchantRecipe recipe) {
		recipe.incrementToolUses();

		if(recipe.getToolUses() == 1 || rand.nextInt(5) == 0)
			timeUntilReset = 40;
	}

	@Override
	public void verifySellingItem(ItemStack stack) {
		if(!world.isRemote && livingSoundTime > -getTalkInterval() + 20) {
			livingSoundTime = -getTalkInterval();
			playSound(stack.isEmpty() ? SoundEvents.ENTITY_VILLAGER_NO : SoundEvents.ENTITY_VILLAGER_YES, getSoundVolume(), getSoundPitch());
		}
	}

	@Override
	public World getWorld() {
		return world;
	}

	@Override
	public BlockPos getPos() {
		return getPosition();
	}

	public boolean isTrading() {
		return buyingPlayer != null;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);

		if(buyingList != null)
			compound.setTag("Offers", buyingList.getRecipiesAsTags());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);

		if(compound.hasKey("Offers", 10)) {
			NBTTagCompound nbttagcompound = compound.getCompoundTag("Offers");
			buyingList = new MerchantRecipeList(nbttagcompound);
		}
	}

	@Override
	protected boolean canDespawn() {
		return false;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return isTrading() ? SoundEvents.ENTITY_VILLAGER_TRADING : SoundEvents.ENTITY_VILLAGER_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_VILLAGER_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_VILLAGER_DEATH;
	}
	
	@Override
	protected float getSoundVolume() {
		return 10F;
	}

}
