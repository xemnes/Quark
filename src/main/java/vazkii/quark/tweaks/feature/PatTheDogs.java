package vazkii.quark.tweaks.feature;

import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.quark.base.module.Feature;

public class PatTheDogs extends Feature {

	@SubscribeEvent
	public void onInteract(PlayerInteractEvent.EntityInteract event) {
		if(event.getTarget() instanceof EntityWolf) {
			EntityWolf wolf = (EntityWolf) event.getTarget();
			EntityPlayer player = event.getEntityPlayer();
			
			if(player.isSneaking() && player.getHeldItemMainhand().isEmpty() && wolf.isTamed()) {
				if(event.getHand() == EnumHand.MAIN_HAND) {
					if(player.world instanceof WorldServer) {
						((WorldServer) player.world).spawnParticle(EnumParticleTypes.HEART, wolf.posX, wolf.posY + 0.5, wolf.posZ, 1, 0, 0, 0, 0.1);
						wolf.playSound(SoundEvents.ENTITY_WOLF_WHINE, 1F, 0.5F + (float) Math.random() * 0.5F);
					} else player.swingArm(EnumHand.MAIN_HAND);
				}
				
				event.setCanceled(true);
			}
		}
	}
	
	@Override
	public boolean hasSubscriptions() {
		return true;
	}
	
}
