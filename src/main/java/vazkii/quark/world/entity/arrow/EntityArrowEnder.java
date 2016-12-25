/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 * 
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [17/07/2016, 03:52:18 (GMT)]
 */
package vazkii.quark.world.entity.arrow;

import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import vazkii.quark.world.feature.ExtraArrows;

public class EntityArrowEnder extends EntityArrow {

	public EntityArrowEnder(World worldIn) {
		super(worldIn);
	}

	public EntityArrowEnder(World worldIn, EntityLivingBase shooter) {
		super(worldIn, shooter);
	}

	public EntityArrowEnder(World worldIn, IPosition pos) {
		super(worldIn, pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	protected ItemStack getArrowStack() {
		return new ItemStack(ExtraArrows.arrow_ender);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(worldObj.isRemote)
			if(inGround) {
				if(timeInGround % 5 == 0)
					spawnPotionParticles(1);
			} else spawnPotionParticles(2);
	}

	public void spawnPotionParticles(int particleCount) {
		if(particleCount > 0)
			for(int i = 0; i < particleCount; i++)
				worldObj.spawnParticle(EnumParticleTypes.PORTAL, posX + (rand.nextDouble() - 0.5D) * width, posY + rand.nextDouble() * height, posZ + (rand.nextDouble() - 0.5D) * width, 0.0D, 0.0D, 0.0D, new int[0]);
	}

	@Override
	protected void onHit(RayTraceResult raytraceResultIn) {
		super.onHit(raytraceResultIn);

		if(shootingEntity != null) {
			if(shootingEntity instanceof EntityPlayerMP) {
				EntityPlayerMP entityplayermp = (EntityPlayerMP)shootingEntity;

				if(entityplayermp.connection.getNetworkManager().isChannelOpen() && entityplayermp.worldObj == worldObj && !entityplayermp.isPlayerSleeping()) {
					EnderTeleportEvent event = new EnderTeleportEvent(entityplayermp, posX, posY, posZ, 5.0F);
					if(!MinecraftForge.EVENT_BUS.post(event) && rand.nextFloat() < 0.025F && worldObj.getGameRules().getBoolean("doMobSpawning")) {
						EntityEndermite entityendermite = new EntityEndermite(worldObj);
						entityendermite.setSpawnedByPlayer(true);
						entityendermite.setLocationAndAngles(shootingEntity.posX, shootingEntity.posY, shootingEntity.posZ, shootingEntity.rotationYaw, shootingEntity.rotationPitch);
						worldObj.spawnEntityInWorld(entityendermite);
					}

					if(shootingEntity.isRiding())
						shootingEntity.dismountRidingEntity();

					shootingEntity.setPositionAndUpdate(event.getTargetX(), event.getTargetY(), event.getTargetZ());
					shootingEntity.fallDistance = 0.0F;
					shootingEntity.attackEntityFrom(DamageSource.fall, event.getAttackDamage());
				}
			} else {
				shootingEntity.setPositionAndUpdate(this.posX, this.posY, this.posZ);
				shootingEntity.fallDistance = 0.0F;
			}

			// Full copypasta from EntityEnderPearl
			for(int i = 0; i < 32; ++i)
				this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, this.posX, this.posY + this.rand.nextDouble() * 2.0D, this.posZ, this.rand.nextGaussian(), 0.0D, this.rand.nextGaussian(), new int[0]);

			setDead();
		}
	}

}
