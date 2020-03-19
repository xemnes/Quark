package vazkii.quark.experimental.entity;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.BreedGoal;
import net.minecraft.entity.ai.goal.FollowParentGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import vazkii.quark.experimental.module.ToretoiseModule;
import vazkii.quark.world.module.CaveRootsModule;

public class ToretoiseEntity extends AnimalEntity {

	public int rideTime;
	
	public ToretoiseEntity(EntityType<? extends ToretoiseEntity> type, World world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(0, new SwimGoal(this));
		goalSelector.addGoal(1, new BreedGoal(this, 1.0));
		goalSelector.addGoal(2, new TemptGoal(this, 1.25, Ingredient.fromItems(CaveRootsModule.rootItem), false));
		goalSelector.addGoal(3, new FollowParentGoal(this, 1.25));
		goalSelector.addGoal(5, new RandomWalkingGoal(this, 1.0D));
		goalSelector.addGoal(4, new LookAtGoal(this, PlayerEntity.class, 6.0F));
		goalSelector.addGoal(5, new LookRandomlyGoal(this));
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
	protected float getWaterSlowDown() {
		return 0.9F;
	}

	@Override
	public boolean canBeLeashedTo(PlayerEntity player) {
		return false;
	}

	protected void registerAttributes() {
		super.registerAttributes();
		getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(60);
		getAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1);
		getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.08);
	}

	@Override
	public AgeableEntity createChild(AgeableEntity arg0) {
		return new ToretoiseEntity(ToretoiseModule.toretoiseType, world);
	}

}
