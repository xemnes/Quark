/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 * <p>
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * <p>
 * File Created @ [May 11, 2019, 18:35 AM (EST)]
 */
package vazkii.quark.world.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.function.Predicate;

public class EntityAIRunAndPoof<T extends Entity> extends EntityAIAvoidEntity<T> {
	public EntityAIRunAndPoof(EntityCreature entityIn, Class<T> classToAvoidIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn) {
		super(entityIn, classToAvoidIn, avoidDistanceIn, farSpeedIn, nearSpeedIn);
	}

	public EntityAIRunAndPoof(EntityCreature entityIn, Class<T> classToAvoidIn, Predicate<? super T> avoidTargetSelectorIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn) {
		super(entityIn, classToAvoidIn, avoidTargetSelectorIn::test, avoidDistanceIn, farSpeedIn, nearSpeedIn);
	}

	@Override
	public void startExecuting() {
		super.startExecuting();
		entity.world.playSound(null, entity.posX, entity.posY, entity.posZ, SoundEvents.ENTITY_GHAST_SCREAM, SoundCategory.NEUTRAL, 1.0F, 1.0F);
	}

	@Override
	public void resetTask() {
		super.resetTask();

		World world = entity.world;

		if(world instanceof WorldServer) {
			WorldServer ws = (WorldServer) world;
			ws.spawnParticle(EnumParticleTypes.CLOUD, entity.posX, entity.posY, entity.posZ, 40, 0.5, 0.5, 0.5, 0.1);
			ws.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, entity.posX, entity.posY, entity.posZ, 20, 0.5, 0.5, 0.5, 0);
		}
		entity.setDead();
	}
}
