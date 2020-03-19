package vazkii.quark.experimental.entity;

import java.util.Random;

import net.minecraft.block.Blocks;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.BreedGoal;
import net.minecraft.entity.ai.goal.FollowParentGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import vazkii.quark.base.handler.MiscUtil;
import vazkii.quark.base.module.ModuleLoader;
import vazkii.quark.experimental.module.ToretoiseModule;
import vazkii.quark.mobs.entity.FoxhoundEntity;
import vazkii.quark.world.module.CaveRootsModule;

public class ToretoiseEntity extends AnimalEntity {

	private static final String TAG_TAMED = "tamed";
	private static final String TAG_ORE = "oreType";
	
	public int rideTime;
	private boolean isTamed;
	
	private Ingredient goodFood;

	private static final DataParameter<Integer> ORE_TYPE = EntityDataManager.createKey(ToretoiseEntity.class, DataSerializers.VARINT);

	public ToretoiseEntity(EntityType<? extends ToretoiseEntity> type, World world) {
		super(type, world);
		stepHeight = 1.0F;
		setPathPriority(PathNodeType.WATER, 1.0F);
	}
	
	@Override
	protected void registerData() {
		super.registerData();
		
		dataManager.register(ORE_TYPE, 0);
	}

	@Override
	protected void registerGoals() {
		goodFood = Ingredient.fromItems(ModuleLoader.INSTANCE.isModuleEnabled(CaveRootsModule.class) ? CaveRootsModule.rootItem : Items.CACTUS);
		
		goalSelector.addGoal(0, new SwimGoal(this));
		goalSelector.addGoal(1, new BreedGoal(this, 1.0));
		goalSelector.addGoal(2, new TemptGoal(this, 1.25, goodFood, false));
		goalSelector.addGoal(3, new FollowParentGoal(this, 1.25));
		goalSelector.addGoal(5, new RandomWalkingGoal(this, 1.0D));
		goalSelector.addGoal(4, new LookAtGoal(this, PlayerEntity.class, 6.0F));
		goalSelector.addGoal(5, new LookRandomlyGoal(this));
	}

	@Override
	public boolean isEntityInsideOpaqueBlock() {
		return MiscUtil.isEntityInsideOpaqueBlock(this);
	}

	@Override
	public void tick() {
		super.tick();

		Entity riding = getRidingEntity();
		if(riding != null)
			rideTime++;
		else rideTime = 0;

		
	}
	
	@Override
	public void setInLove(PlayerEntity player) {
		setInLove(0);
	}

	@Override
	public void setInLove(int ticks) {
		if(world.isRemote)
			return;
		
		if(!isTamed) {
			isTamed = true;
	        world.addParticle(ParticleTypes.HEART, getPosX(), getPosY(), getPosZ(), 0, 0, 0);
		}
	}
	
	@Override
	public boolean isBreedingItem(ItemStack stack) {
		return goodFood.test(stack);
	}
	
	@Override
	public boolean canDespawn(double distanceToClosestPlayer) {
		return !isTamed;
	}
	
	public static boolean canSpawnHere(IWorld world, BlockPos pos, Random rand) {
		if (world.getLightFor(LightType.SKY, pos) > rand.nextInt(32)) {
			return false;
		} else {
			int light = world.getWorld().isThundering() ? world.getNeighborAwareLightSubtracted(pos, 10) : world.getLight(pos);
			return light <= rand.nextInt(8);
		}
	}

	public static boolean spawnPredicate(EntityType<? extends FoxhoundEntity> type, IWorld world, SpawnReason reason, BlockPos pos, Random rand) {
		return canSpawnHere(world, pos, rand);
	}
	
	@Override
	protected void jump() {
		// NO-OP
	}

	@Override
	public boolean onLivingFall(float distance, float damageMultiplier) {
		return false;
	}

	@Override
	protected float getWaterSlowDown() {
		return 0.9F;
	}

	@Override
	public boolean canBeLeashedTo(PlayerEntity player) {
		return false;
	}

	@Override
	protected float getSoundPitch() {
		return (rand.nextFloat() - rand.nextFloat()) * 0.2F + 0.6F;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_TURTLE_AMBIENT_LAND;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_TURTLE_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_TURTLE_DEATH;
	}
	
	public int getOreType() {
		return dataManager.get(ORE_TYPE);
	}
	
	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		compound.putBoolean(TAG_TAMED, isTamed);
		compound.putInt(TAG_ORE, getOreType());
	}
	
	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		isTamed = compound.getBoolean(TAG_TAMED);
		dataManager.set(ORE_TYPE, compound.getInt(TAG_ORE));
	}

	protected void registerAttributes() {
		super.registerAttributes();
		getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(60);
		getAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1);
		getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.08);
	}

	@Override
	public AgeableEntity createChild(AgeableEntity arg0) {
		return new ToretoiseEntity(ToretoiseModule.toretoiseType, world); // TODO baby
	}

}
