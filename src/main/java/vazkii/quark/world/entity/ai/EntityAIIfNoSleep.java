/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 * <p>
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * <p>
 * File Created @ [Jul 13, 2019, 12:17 AM (EST)]
 */
package vazkii.quark.world.entity.ai;

import net.minecraft.entity.ai.EntityAIBase;
import vazkii.quark.world.entity.EntityFoxhound;

public class EntityAIIfNoSleep extends EntityAIBase {
	private final EntityFoxhound foxhound;
	private final EntityAIBase parent;

	public EntityAIIfNoSleep(EntityFoxhound foxhound, EntityAIBase parent) {
		this.foxhound = foxhound;
		this.parent = parent;
	}

	@Override
	public boolean shouldExecute() {
		return !this.foxhound.isSleeping() && parent.shouldExecute();
	}

	@Override
	public boolean shouldContinueExecuting() {
		return !this.foxhound.isSleeping() && parent.shouldContinueExecuting();
	}

	@Override
	public boolean isInterruptible() {
		return parent.isInterruptible();
	}

	@Override
	public void startExecuting() {
		parent.startExecuting();
	}

	@Override
	public void resetTask() {
		parent.resetTask();
	}

	@Override
	public void updateTask() {
		parent.updateTask();
	}

	@Override
	public void setMutexBits(int mutexBitsIn) {
		parent.setMutexBits(mutexBitsIn);
	}

	@Override
	public int getMutexBits() {
		return parent.getMutexBits();
	}
}
