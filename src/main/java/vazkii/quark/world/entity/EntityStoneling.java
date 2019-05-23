package vazkii.quark.world.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.quark.base.sounds.QuarkSounds;
import vazkii.quark.world.base.EnumStonelingVariant;
import vazkii.quark.world.entity.ai.EntityAIActWary;
import vazkii.quark.world.entity.ai.EntityAIRunAndPoof;
import vazkii.quark.world.feature.Stonelings;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class EntityStoneling extends EntityCreature {

	public static final ResourceLocation CARRY_LOOT_TABLE = new ResourceLocation("quark", "entities/stoneling_carry");
	public static final ResourceLocation LOOT_TABLE = new ResourceLocation("quark", "entities/stoneling");

	private static final DataParameter<ItemStack> CARRYING_ITEM = EntityDataManager.createKey(EntityStoneling.class, DataSerializers.ITEM_STACK);
	private static final DataParameter<Byte> VARIANT = EntityDataManager.createKey(EntityStoneling.class, DataSerializers.BYTE);
	private static final DataParameter<Float> HOLD_ANGLE = EntityDataManager.createKey(EntityStoneling.class, DataSerializers.FLOAT);

	private static final String TAG_CARRYING_ITEM = "carryingItem";
	private static final String TAG_VARIANT = "variant";
	private static final String TAG_HOLD_ANGLE = "itemAngle";
	private static final String TAG_PLAYER_MADE = "playerMade";

	private EntityAIActWary waryTask;

	private boolean isTame;

	public EntityStoneling(World worldIn) {
		super(worldIn);
		setSize(0.5F, 1F);
	}

	@Override
	protected void entityInit() {
		super.entityInit();

		dataManager.register(CARRYING_ITEM, ItemStack.EMPTY);
		dataManager.register(VARIANT, (byte) 0);
		dataManager.register(HOLD_ANGLE, 0F);
	}

	@Override
	protected void initEntityAI() {
		tasks.addTask(4, new EntityAIWanderAvoidWater(this, 0.2, 1F));

		if(Stonelings.enableDiamondHeart)
			tasks.addTask(3, new EntityAITempt(this, 0.6, Items.DIAMOND, false));

		tasks.addTask(2, new EntityAIRunAndPoof<>(this, EntityPlayer.class, 4, 0.5, 0.5));
		tasks.addTask(1, waryTask = new EntityAIActWary(this, 0.1, 6, false));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8);
		getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1);
	}


	@Override
	public void onUpdate() {
		super.onUpdate();
		this.prevRenderYawOffset = this.prevRotationYaw;
		this.renderYawOffset = this.rotationYaw;
	}

	@Nonnull
	@Override
	public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, EnumHand hand) {
		if(isPlayerMade() && hand == EnumHand.MAIN_HAND) {
			ItemStack playerItem = player.getHeldItem(hand);

			if(!world.isRemote) {
				if(!player.isSneaking() && !playerItem.isEmpty()) {
					int[] ids = OreDictionary.getOreIDs(playerItem);
					EnumStonelingVariant currentVariant = getVariant();
					EnumStonelingVariant targetVariant = null;
					for (EnumStonelingVariant variant : EnumStonelingVariant.values()) {
						int oreKey = OreDictionary.getOreID(variant.getOreKey());
						for (int id : ids) {
							if (id == oreKey) {
								targetVariant = variant;
								break;
							}
						}
					}

					if (targetVariant != null) {
						if (world instanceof WorldServer) {
							((WorldServer) world).spawnParticle(EnumParticleTypes.HEART, posX, posY + height, posZ, 1, 0.1, 0.1, 0.1, 0.1);
							if (targetVariant != currentVariant)
								((WorldServer) world).spawnParticle(EnumParticleTypes.BLOCK_CRACK, posX, posY + height / 2, posZ, 16, 0.1, 0.1, 0.1, 0.25,
										Block.getStateId(targetVariant.getDisplayState()));
						}

						if (targetVariant != currentVariant) {
							playSound(QuarkSounds.ENTITY_STONELING_EAT, 1F, 0.8F);
							dataManager.set(VARIANT, targetVariant.getIndex());
						}

						playSound(QuarkSounds.ENTITY_STONELING_PURR, 0.25F, 0.25F + world.rand.nextFloat() * 0.25F);

						heal(1);

						if (!player.capabilities.isCreativeMode)
							playerItem.shrink(1);

						return EnumActionResult.SUCCESS;
					}
				}

				ItemStack stonelingItem = dataManager.get(CARRYING_ITEM);

				if (!stonelingItem.isEmpty() || !playerItem.isEmpty()) {
					player.setHeldItem(hand, stonelingItem.copy());
					dataManager.set(CARRYING_ITEM, playerItem.copy());

					if (playerItem.isEmpty())
						playSound(QuarkSounds.ENTITY_STONELING_GIVE, 1.0F, 1.0F);
					else playSound(QuarkSounds.ENTITY_STONELING_TAKE, 1.0F, 1.0F);
				}
			}


			return EnumActionResult.SUCCESS;
		}

		return EnumActionResult.PASS;
	}

	@Nullable
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData data) {
		byte variant;
		if (data instanceof EnumStonelingVariant)
			variant = ((EnumStonelingVariant) data).getIndex();
		else
			variant = (byte) world.rand.nextInt(EnumStonelingVariant.values().length);

		dataManager.set(VARIANT, variant);
		dataManager.set(HOLD_ANGLE, world.rand.nextFloat() * 90 - 45);

		if(!isTame && !world.isRemote) {
			List<ItemStack> items = world.getLootTableManager().getLootTableFromLocation(CARRY_LOOT_TABLE).generateLootForPools(rand, new LootContext.Builder((WorldServer) world).build());
			if(!items.isEmpty())
				dataManager.set(CARRYING_ITEM, items.get(0));	
		}

		return super.onInitialSpawn(difficulty, data);
	}

	@Override
	public boolean isEntityInvulnerable(@Nonnull DamageSource source) {
		return source == DamageSource.CACTUS || source.isProjectile();
	}

	@Override
	protected int decreaseAirSupply(int air) {
		return air;
	}

	@Override
	public void fall(float distance, float damageMultiplier) {
		// NO-OP
	}

	@Override
	protected void damageEntity(@Nonnull DamageSource damageSrc, float damageAmount) {
		super.damageEntity(damageSrc, damageAmount);

		if(!isPlayerMade() && damageSrc.getTrueSource() instanceof EntityPlayer)
			waryTask.startle();
	}

	@Override
	protected void dropEquipment(boolean wasRecentlyHit, int lootingModifier) {
		super.dropEquipment(wasRecentlyHit, lootingModifier);

		ItemStack stack = getCarryingItem();
		if(!stack.isEmpty())
			entityDropItem(stack, 0F);
	}

	@Override
	protected ResourceLocation getLootTable() {
		return Stonelings.enableDiamondHeart ? LOOT_TABLE : null;
	}

	public void setPlayerMade(boolean value) {
		isTame = value;
	}

	public ItemStack getCarryingItem() {
		return dataManager.get(CARRYING_ITEM);
	}

	public EnumStonelingVariant getVariant() {
		return EnumStonelingVariant.byIndex(dataManager.get(VARIANT));
	}

	public float getItemAngle() {
		return dataManager.get(HOLD_ANGLE);
	}

	public boolean isPlayerMade() {
		return isTame;
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);

		if(compound.hasKey(TAG_CARRYING_ITEM, 10)) {
			NBTTagCompound itemCmp = compound.getCompoundTag(TAG_CARRYING_ITEM);
			ItemStack stack = new ItemStack(itemCmp);
			dataManager.set(CARRYING_ITEM, stack);
		}

		dataManager.set(VARIANT, compound.getByte(TAG_VARIANT));
		dataManager.set(HOLD_ANGLE, compound.getFloat(TAG_HOLD_ANGLE));
		setPlayerMade(compound.getBoolean(TAG_PLAYER_MADE));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);

		compound.setTag(TAG_CARRYING_ITEM, getCarryingItem().serializeNBT());

		compound.setByte(TAG_VARIANT, getVariant().getIndex());
		compound.setFloat(TAG_HOLD_ANGLE, getItemAngle());
		compound.setBoolean(TAG_PLAYER_MADE, isPlayerMade());
	}

	@Override
	public boolean getCanSpawnHere() {
		return Stonelings.dimensions.canSpawnHere(world) && posY < Stonelings.maxYLevel && isValidLightLevel() && super.getCanSpawnHere();
	}

	// Vanilla copy pasta from EntityMob
	protected boolean isValidLightLevel() {
		BlockPos blockpos = new BlockPos(posX, getEntityBoundingBox().minY, posZ);

		if(world.getLightFor(EnumSkyBlock.SKY, blockpos) > rand.nextInt(32))
			return false;
		else {
			int i = world.getLightFromNeighbors(blockpos);

			if (world.isThundering()) {
				int j = world.getSkylightSubtracted();
				world.setSkylightSubtracted(10);
				i = world.getLightFromNeighbors(blockpos);
				world.setSkylightSubtracted(j);
			}

			return i <= rand.nextInt(8);
		}
	}

}
