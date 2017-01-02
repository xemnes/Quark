/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 *
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 *
 * File Created @ [02/06/2016, 00:52:11 (GMT)]
 */
package vazkii.quark.tweaks.feature;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.quark.base.module.Feature;

public class EndermenTeleportYou extends Feature {

	int minimumDifficulty = 2;

	@Override
	public void setupConfig() {
		minimumDifficulty = loadPropInt("Minimum Difficulty", "The minimum difficulty in which this effect should take place. (1: easy, 2: normal, 3: hard)", 2);
	}

	@SubscribeEvent
	public void onUpdate(LivingUpdateEvent event) {
		if(event.getEntityLiving() instanceof EntityEnderman && event.getEntityLiving().getEntityWorld().getDifficulty().getDifficultyId() >= minimumDifficulty) {
			EntityEnderman entity = (EntityEnderman) event.getEntityLiving();

			BlockPos ourPos = entity.getPosition().up(2);
			IBlockState ourState = entity.getEntityWorld().getBlockState(ourPos);
			Block ourBlock = ourState.getBlock();
			if(ourBlock.getCollisionBoundingBox(ourState, entity.getEntityWorld(), ourPos) != null)
				return;

			EntityLivingBase target = entity.getAttackTarget();
			if(target != null && target instanceof EntityPlayer && target.onGround) {
				BlockPos pos = target.getPosition().up(2);
				if(pos.getDistance(ourPos.getX(), ourPos.getY(), ourPos.getZ()) > 5)
					return;

				IBlockState state = entity.getEntityWorld().getBlockState(pos);
				Block block = state.getBlock();

				if(block.getCollisionBoundingBox(state, entity.getEntityWorld(), pos) != null) {
					for(int i = 0; i < 16; i++)
						if(target.attemptTeleport(entity.posX + (Math.random() - 0.5) * 2, entity.posY + 0.5, entity.posZ + (Math.random() - 0.5) * 2))
							break;

					target.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 30, 0));
					target.getEntityWorld().playSound(null, entity.posX, entity.posY, entity.posZ, SoundEvents.ENTITY_ENDERMEN_SCREAM, SoundCategory.HOSTILE, 1F, 1F);
					target.getEntityWorld().playSound(null, entity.posX, entity.posY, entity.posZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.HOSTILE, 1F, 1F);
				}
			}
		}
	}

	@Override
	public boolean hasSubscriptions() {
		return true;
	}

}
