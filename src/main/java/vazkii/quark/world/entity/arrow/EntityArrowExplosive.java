/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 * 
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [17/07/2016, 03:52:03 (GMT)]
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

public class EntityArrowExplosive extends EntityArrow {

	public EntityArrowExplosive(World worldIn) {
		super(worldIn);
	}
	
	public EntityArrowExplosive(World worldIn, EntityLivingBase shooter) {
		super(worldIn, shooter);
	}
	
	public EntityArrowExplosive(World worldIn, IPosition pos) {
		super(worldIn, pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	protected ItemStack getArrowStack() {
		return new ItemStack(ExtraArrows.arrow_explosive);
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();

		if(worldObj.isRemote)
			spawnPotionParticles(4);
	}

	public void spawnPotionParticles(int particleCount) {
		if(particleCount > 0)
			for(int i = 0; i < particleCount; i++)
				worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, posX + (rand.nextDouble() - 0.5D) * width, posY + rand.nextDouble() * height, posZ + (rand.nextDouble() - 0.5D) * width, 0.0D, 0.0D, 0.0D, new int[0]);
	}

	@Override
	protected void onHit(RayTraceResult raytraceResultIn) {
		super.onHit(raytraceResultIn);

		if(!worldObj.isRemote)
			worldObj.createExplosion(this, posX, posY, posZ, (float) ExtraArrows.explosiveArrowPower, ExtraArrows.explosiveArrowDestroysBlocks);
		setDead();
	}

}
