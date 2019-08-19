package vazkii.quark.base.handler;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceContext.BlockMode;
import net.minecraft.util.math.RayTraceContext.FluidMode;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class RayTraceHandler {

	public static RayTraceResult rayTrace(Entity entity, World world, PlayerEntity player, BlockMode blockMode, FluidMode fluidMode) {
		return rayTrace(entity, world, player, blockMode, fluidMode, getEntityRange(player));
	}
	
	public static RayTraceResult rayTrace(Entity entity, World world, Entity player, BlockMode blockMode, FluidMode fluidMode, double range) {
		 Pair<Vec3d, Vec3d> params = getEntityParams(player);
		
		return rayTrace(entity, world, params.getLeft(), params.getRight(), blockMode, fluidMode, range);
	}
	
	public static RayTraceResult rayTrace(Entity entity, World world, Vec3d startPos, Vec3d ray, BlockMode blockMode, FluidMode fluidMode, double range) {
		return rayTrace(entity, world, startPos, ray.scale(range), blockMode, fluidMode);
	}

	public static RayTraceResult rayTrace(Entity entity, World world, Vec3d startPos, Vec3d ray, BlockMode blockMode, FluidMode fluidMode) {
		Vec3d end = startPos.add(ray);
		RayTraceContext context = new RayTraceContext(startPos, end, blockMode, fluidMode, entity);
		return world.rayTraceBlocks(context);
	}
	
	public static double getEntityRange(LivingEntity player) {
		return player.getAttribute(PlayerEntity.REACH_DISTANCE).getValue();
	}
	
	public static Pair<Vec3d, Vec3d> getEntityParams(Entity player) {
		float scale = 1.0F;
		float pitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * scale;
		float yaw = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * scale;
		double posX = player.prevPosX + (player.posX - player.prevPosX) * scale;
		double posY = player.prevPosY + (player.posY - player.prevPosY) * scale;
		if (player instanceof PlayerEntity)
			posY += ((PlayerEntity) player).getEyeHeight();
		double posZ = player.prevPosZ + (player.posZ - player.prevPosZ) * scale;
		Vec3d rayPos = new Vec3d(posX, posY, posZ);

		float zYaw = -MathHelper.cos(yaw * (float) Math.PI / 180);
		float xYaw = MathHelper.sin(yaw * (float) Math.PI / 180);
		float pitchMod = -MathHelper.cos(pitch * (float) Math.PI / 180);
		float azimuth = -MathHelper.sin(pitch * (float) Math.PI / 180);
		float xLen = xYaw * pitchMod;
		float yLen = zYaw * pitchMod;
		Vec3d ray = new Vec3d(xLen, azimuth, yLen);
		
		return Pair.of(rayPos, ray);
	}
	
}
