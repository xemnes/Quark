package vazkii.quark.tweaks.module;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleCategory;

@LoadModule(category = ModuleCategory.TWEAKS, hasSubscriptions = true)
public class CampfiresBoostElytraModule extends Module {

	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event) {
		PlayerEntity player = event.player;
		
		if(player.isElytraFlying()) {
			BlockPos pos = player.getPosition();
			World world = player.world;
			
			int moves = 0;
			while(world.isAirBlock(pos) && pos.getY() > 0 && moves < 20) {
				pos = pos.down();
				moves++;
			}
			
			BlockState state = world.getBlockState(pos);
			if(state.getBlock() == Blocks.CAMPFIRE && state.get(CampfireBlock.LIT) && state.get(CampfireBlock.SIGNAL_FIRE)) {
				double force = 0.5;
				if(moves > 16)
					force -= (force * (1.0 - ((double) moves - 16.0) / 4.0));
				
				double cap = 1.0;
				
				Vec3d motion = player.getMotion();
				double trueCap = Math.max(motion.getY(), cap);
				player.setMotion(motion.getX(), Math.min(trueCap, motion.getY() + force), motion.getZ());
			}
		}
	}
	
}
