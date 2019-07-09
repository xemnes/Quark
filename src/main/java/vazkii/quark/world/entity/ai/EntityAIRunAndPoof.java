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
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import vazkii.quark.base.sounds.QuarkSounds;
import vazkii.quark.world.entity.EntityStoneling;

public class EntityAIRunAndPoof<T extends Entity> extends EntityAIAvoidEntity<T> {
	
	private final EntityStoneling stoneling;
	
	public EntityAIRunAndPoof(EntityStoneling stoneling, Class<T> classToAvoidIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn) {
		super(stoneling, classToAvoidIn, avoidDistanceIn, farSpeedIn, nearSpeedIn);
		this.stoneling = stoneling;
	}

	@Override
	public boolean shouldExecute() {
		return !stoneling.isPlayerMade() && super.shouldExecute();
	}
	
	@Override
	public void startExecuting() {
		super.startExecuting();
		entity.world.playSound(null, entity.posX, entity.posY, entity.posZ, QuarkSounds.ENTITY_STONELING_MEEP, SoundCategory.NEUTRAL, 1.0F, 1.0F);
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
		for (Entity passenger : entity.getRecursivePassengers()) {
			if (!(passenger instanceof EntityPlayer))
				passenger.setDead();
		}
		entity.setDead();
	}
}
