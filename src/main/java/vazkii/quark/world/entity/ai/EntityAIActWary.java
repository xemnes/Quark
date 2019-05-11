/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 * <p>
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * <p>
 * File Created @ [May 11, 2019, 17:44 AM (EST)]
 */
package vazkii.quark.world.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class EntityAIActWary extends EntityAIWanderAvoidWater {

	private final EntityCreature creature;

	private final boolean scaredBySuddenMovement;

	private final double range;

	private boolean startled;

	public EntityAIActWary(EntityCreature creature, double speed, double range, boolean scaredBySuddenMovement) {
		super(creature, speed, 1F);
		this.creature = creature;
		this.range = range;
		this.scaredBySuddenMovement = scaredBySuddenMovement;
	}

	public void startle() {
		startled = true;
	}

	protected boolean shouldApplyPath() {
		return super.shouldExecute();
	}

	@Override
	public void updateTask() {
		if (creature.getNavigator().noPath() && shouldApplyPath())
			startExecuting();
	}

	@Override
	public boolean shouldContinueExecuting() {
		return shouldExecute();
	}

	@Override
	public void resetTask() {
		creature.getNavigator().clearPath();
	}

	@Override
	public boolean shouldExecute() {
		if (startled)
			return false;

		List<EntityPlayer> playersAround = creature.world.getEntitiesWithinAABB(EntityPlayer.class, creature.getEntityBoundingBox().grow(range),
				(player) -> player != null && !player.capabilities.isCreativeMode && player.getDistanceSq(creature) < range * range);

		if (playersAround.isEmpty())
			return false;

		for (EntityPlayer player : playersAround) {
			if (player.isSneaking()) {
				if (scaredBySuddenMovement) {
					double dX = player.posX - player.lastTickPosX;
					double dY = player.posY - player.lastTickPosY;
					double dZ = player.posZ - player.lastTickPosZ;
					double displacementSq = dX * dX + dY * dY + dZ * dZ;
					if (displacementSq < 0.01)
						return true;

					startled = true;
					return false;
				}
			} else {
				startled = true;
				return false;
			}
		}

		return true;
	}
}
