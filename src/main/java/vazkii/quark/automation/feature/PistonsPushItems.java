package vazkii.quark.automation.feature;

import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.BlockPistonExtension.EnumPistonType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.arl.util.ItemTickHandler.EntityItemTickEvent;
import vazkii.quark.base.module.Feature;

public class PistonsPushItems extends Feature {

	@SubscribeEvent
	public void onEntityTick(EntityItemTickEvent event) {
		EntityItem entity = event.getEntityItem();
		World world = entity.getEntityWorld();
		BlockPos pos = entity.getPosition();
		for(EnumFacing face : EnumFacing.VALUES) {
			BlockPos offPos = pos.offset(face);
			if(world.isBlockLoaded(offPos)) {
				IBlockState state = world.getBlockState(offPos);
				if(state.getBlock() == Blocks.PISTON_EXTENSION) {
					EnumPistonType type = state.getValue(BlockPistonExtension.TYPE);
					if(type == EnumPistonType.DEFAULT) {
						EnumFacing facing = state.getValue(BlockDirectional.FACING);
						if(facing == face.getOpposite()) {
							float force = 0.48F;
							float x = force * facing.getFrontOffsetX();
							float y = force * facing.getFrontOffsetY();
							float z = force * facing.getFrontOffsetZ();
							float px = x == 0 ? 0.4F : 0;
							float py = y == 0 ? 0.4F : 0;
							float pz = z == 0 ? 0.4F : 0;
							entity.addVelocity(x, y, z);
							if(world instanceof WorldServer)
								((WorldServer) world).spawnParticle(EnumParticleTypes.CRIT, entity.posX, entity.posY, entity.posZ, 12, px, py, pz, 0);
						}
					}
				}
			}
		}
	}

	@Override
	public boolean hasSubscriptions() {
		return true;
	}

}
