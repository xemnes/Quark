/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 * <p>
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * <p>
 * File Created @ [May 20, 2019, 01:58 AM (EST)]
 */
package vazkii.quark.tweaks.feature;

import net.minecraft.block.BlockSlime;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import vazkii.quark.base.handler.OverrideRegistryHandler;
import vazkii.quark.base.module.Feature;
import vazkii.quark.base.module.ModuleLoader;
import vazkii.quark.base.util.MutableVectorHolder;
import vazkii.quark.tweaks.block.BlockSpringySlime;

public class SpringySlime extends Feature {

	public static BlockSpringySlime springySlime;

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		springySlime = new BlockSpringySlime();
		springySlime.setTranslationKey("slime");
		OverrideRegistryHandler.registerBlock(springySlime, "slime");
	}

	private static final ThreadLocal<MutableVectorHolder> motionRecorder = ThreadLocal.withInitial(MutableVectorHolder::new);

	public static void recordMotion(Entity entity) {
		motionRecorder.get().x = entity.motionX;
		motionRecorder.get().y = entity.motionY;
		motionRecorder.get().z = entity.motionZ;
	}

	public static void onEntityCollision(Entity entity, double attemptedX, double attemptedY, double attemptedZ, double dX, double dY, double dZ) {
		if (!ModuleLoader.isFeatureEnabled(SpringySlime.class))
			return;

		if (entity.isSneaking() || (entity instanceof EntityPlayer && ((EntityPlayer) entity).capabilities.isFlying))
			return;

		double height = entity.height;
		double width = entity.width;

		double minX = entity.posX - width / 2;
		double minY = entity.posY;
		double minZ = entity.posZ - width / 2;
		double maxX = entity.posX + width / 2;
		double maxY = entity.posY + height;
		double maxZ = entity.posZ + width / 2;

		if (attemptedX != dX) {
			double xBase = dX < 0 ? minX : maxX;
			double x1 = xBase + dX;
			double x2 = xBase + attemptedX;
			EnumFacing impactedSide = dX < 0 ? EnumFacing.EAST : EnumFacing.WEST;

			int lowXBound = (int) Math.floor(Math.min(x1, x2));
			int highXBound = (int) Math.floor(Math.max(x1, x2));
			int lowYBound = (int) Math.floor(minY);
			int highYBound = (int) Math.floor(maxY);
			int lowZBound = (int) Math.floor(minZ);
			int highZBound = (int) Math.floor(maxZ);

			boolean restoredX = false;
			for (BlockPos position : BlockPos.getAllInBoxMutable(lowXBound, lowYBound, lowZBound,
					highXBound, highYBound, highZBound)) {
				restoredX = applyCollision(entity, position, impactedSide, restoredX);
			}
		}

		if (attemptedY != dY) {
			double yBase = dY < 0 ? minY : maxY;
			double y1 = yBase + dY;
			double y2 = yBase + attemptedY;
			EnumFacing impactedSide = dY < 0 ? EnumFacing.UP : EnumFacing.DOWN;

			int lowXBound = (int) Math.floor(minX);
			int highXBound = (int) Math.floor(maxX);
			int lowYBound = (int) Math.floor(Math.min(y1, y2));
			int highYBound = (int) Math.floor(Math.max(y1, y2));
			int lowZBound = (int) Math.floor(minZ);
			int highZBound = (int) Math.floor(maxZ);

			boolean restoredY = false;
			for (BlockPos position : BlockPos.getAllInBoxMutable(lowXBound, lowYBound, lowZBound,
					highXBound, highYBound, highZBound)) {
				restoredY = applyCollision(entity, position, impactedSide, restoredY);
			}
		}

		if (attemptedZ != dZ) {
			double zBase = dZ < 0 ? minZ : maxZ;
			double z1 = zBase + dZ;
			double z2 = zBase + attemptedZ;
			EnumFacing impactedSide = dZ < 0 ? EnumFacing.SOUTH : EnumFacing.NORTH;

			int lowXBound = (int) Math.floor(minX);
			int highXBound = (int) Math.floor(maxX);
			int lowYBound = (int) Math.floor(minY);
			int highYBound = (int) Math.floor(maxY);
			int lowZBound = (int) Math.floor(Math.min(z1, z2));
			int highZBound = (int) Math.floor(Math.max(z1, z2));

			boolean restoredZ = false;
			for (BlockPos position : BlockPos.getAllInBoxMutable(lowXBound, lowYBound, lowZBound,
					highXBound, highYBound, highZBound)) {
				restoredZ = applyCollision(entity, position, impactedSide, restoredZ);
			}
		}
	}

	private static boolean applyCollision(Entity entity, BlockPos position, EnumFacing impacted, boolean restoredMotion) {
		IBlockState state = entity.world.getBlockState(position);
		if (state.getBlock() instanceof BlockSlime) {
			if (impacted == EnumFacing.UP && entity instanceof EntityItem)
				entity.onGround = false;

			switch (impacted.getAxis()) {
				case X:
					if (!restoredMotion) {
						restoredMotion = true;
						entity.motionX = motionRecorder.get().x;
					}
					entity.motionX = Math.abs(entity.motionX) * impacted.getXOffset();
					if (!(entity instanceof EntityLivingBase))
						entity.motionX *= 0.8;
					break;
				case Y:
					if (!restoredMotion) {
						restoredMotion = true;
						entity.motionY = motionRecorder.get().y;
					}
					entity.motionY = Math.abs(entity.motionY) * impacted.getYOffset();
					if (!(entity instanceof EntityLivingBase))
						entity.motionY *= 0.8;
					break;
				case Z:
					if (!restoredMotion) {
						restoredMotion = true;
						entity.motionZ = motionRecorder.get().z;
					}
					entity.motionZ = Math.abs(entity.motionZ) * impacted.getZOffset();
					if (!(entity instanceof EntityLivingBase))
						entity.motionZ *= 0.8;
					break;
			}
		}

		return restoredMotion;
	}
}
