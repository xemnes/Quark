package vazkii.quark.world.entity;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
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

	public static final int VARIANTS = 5;

	private static final String TAG_CARRYING_ITEM = "carryingItem";
	private static final String TAG_VARIANT = "variant";
	private static final String TAG_HOLD_ANGLE = "itemAngle";

	private EntityAIActWary waryTask;

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
		tasks.addTask(2, new EntityAIWanderAvoidWater(this, 0.2, 1F));
		tasks.addTask(1, new EntityAIRunAndPoof<>(this, EntityPlayer.class, 4, 0.5, 0.5));
		tasks.addTask(0, waryTask = new EntityAIActWary(this, 0.1, 6, false));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0D);
		getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		this.prevRenderYawOffset = this.prevRotationYaw;
		this.renderYawOffset = this.rotationYaw;
	}

	@Nullable
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData data) {
		dataManager.set(VARIANT, (byte) world.rand.nextInt(VARIANTS));
		dataManager.set(HOLD_ANGLE, world.rand.nextFloat() * 90 - 45);
		List<ItemStack> items = world.getLootTableManager().getLootTableFromLocation(CARRY_LOOT_TABLE).generateLootForPools(rand, new LootContext.Builder((WorldServer) world).build());
		if(!items.isEmpty())
			dataManager.set(CARRYING_ITEM, items.get(0));
		return super.onInitialSpawn(difficulty, data);
	}

	@Override
	protected void damageEntity(@Nonnull DamageSource damageSrc, float damageAmount) {
		super.damageEntity(damageSrc, damageAmount);

		if(damageSrc.getTrueSource() instanceof EntityPlayer)
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

	public ItemStack getCarryingItem() {
		return dataManager.get(CARRYING_ITEM);
	}

	public byte getVariant() {
		return dataManager.get(VARIANT);
	}

	public float getItemAngle() {
		return dataManager.get(HOLD_ANGLE);
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
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);

		compound.setTag(TAG_CARRYING_ITEM, getCarryingItem().writeToNBT(new NBTTagCompound()));

		compound.setByte(TAG_VARIANT, getVariant());
		compound.setFloat(TAG_HOLD_ANGLE, getItemAngle());
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
