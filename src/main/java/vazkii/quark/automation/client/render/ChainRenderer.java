/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 * <p>
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * <p>
 * File Created @ [Jul 17, 2019, 20:06 AM (EST)]
 */
package vazkii.quark.automation.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.HangingEntity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LightType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.quark.automation.base.ChainHandler;

@OnlyIn(Dist.CLIENT)
public class ChainRenderer {
	private static final IntObjectMap<Entity> RENDER_MAP = new IntObjectHashMap<>();

	@SuppressWarnings("unchecked")
	private static void renderLeash(EntityRenderer renderer, Entity entity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderBuffer, Entity holder) {
		matrixStack.push();
		double yaw = (MathHelper.lerp(partialTicks * 0.5F, holder.rotationYaw, holder.prevRotationYaw) * Math.PI / 180);
		double pitch = (MathHelper.lerp(partialTicks * 0.5F, holder.rotationPitch, holder.prevRotationPitch) * Math.PI / 180);
		double xRot = Math.cos(yaw);
		double zRot = Math.sin(yaw);
		double yRot = Math.sin(pitch);
		if (holder instanceof HangingEntity) {
			xRot = 0.0D;
			zRot = 0.0D;
			yRot = -1.0D;
		}

		double yMod = Math.cos(pitch);
		double xLerp = MathHelper.lerp(partialTicks, prevX(holder), holder.getPosX()) - xRot * 0.7D - zRot * 0.5D * yMod;
		double yLerp = MathHelper.lerp(partialTicks, prevY(holder) + holder.getEyeHeight() * 0.7D, holder.getPosY() + holder.getEyeHeight() * 0.7D) - yRot * 0.5D - 0.25D;
		double zLerp = MathHelper.lerp(partialTicks, prevZ(holder), holder.getPosZ()) - zRot * 0.7D + xRot * 0.5D * yMod;
		double yawOffset = (MathHelper.lerp(partialTicks, renderYawOffset(entity), prevRenderYawOffset(entity)) * Math.PI / 180) + Math.PI / 2;
		xRot = Math.cos(yawOffset) * entity.getWidth() * 0.4D;
		zRot = Math.sin(yawOffset) * entity.getWidth() * 0.4D;
		double adjustedX = MathHelper.lerp(partialTicks, prevX(entity), entity.getPosX()) + xRot;
		double adjustedY = MathHelper.lerp(partialTicks, prevY(entity), entity.getPosY());
		double adjustedZ = MathHelper.lerp(partialTicks, prevZ(entity), entity.getPosZ()) + zRot;
		matrixStack.translate(xRot, -(1.6D - entity.getHeight()) * 0.5D, zRot);
		float dX = (float)(xLerp - adjustedX);
		float dY = (float)(yLerp - adjustedY);
		float dZ = (float)(zLerp - adjustedZ);
		IVertexBuilder vertexBuilder = renderBuffer.getBuffer(RenderType.getLeash());
		Matrix4f matrix = matrixStack.getLast().getMatrix();
		float mag = MathHelper.fastInvSqrt(dX * dX + dZ * dZ) * 0.025F / 2.0F;
		float zMag = dZ * mag;
		float xMag = dX * mag;
		int lightAtEntity = renderer.getBlockLight(entity, partialTicks);
		int lightAtOther = renderer.getRenderManager().getRenderer(holder).getBlockLight(holder, partialTicks);
		int skyLightAtEntity = entity.world.getLightFor(LightType.SKY, new BlockPos(entity.getEyePosition(partialTicks)));
		int skyLightAtOther = entity.world.getLightFor(LightType.SKY, new BlockPos(holder.getEyePosition(partialTicks)));
		renderSide(vertexBuilder, matrix, dX, dY, dZ, lightAtEntity, lightAtOther, skyLightAtEntity, skyLightAtOther, 0.025F, 0.025F, zMag, xMag);
		renderSide(vertexBuilder, matrix, dX, dY, dZ, lightAtEntity, lightAtOther, skyLightAtEntity, skyLightAtOther, 0.025F, 0.0F, zMag, xMag);
		matrixStack.pop();
	}

	public static void renderSide(IVertexBuilder vertexBuilder, Matrix4f matrix, float dX, float dY, float dZ, int lightAtEntity, int lightAtOther, int skyLightAtEntity, int skyLightAtOther, float p_229119_9_, float p_229119_10_, float xMag, float zMag) {
		for(int stepIdx = 0; stepIdx < 24; ++stepIdx) {
			float step = stepIdx / 23.0F;
			int brightness = (int)MathHelper.lerp(step, lightAtEntity, lightAtOther);
			int skyBrightness = (int)MathHelper.lerp(step, skyLightAtEntity, skyLightAtOther);
			int light = LightTexture.packLight(brightness, skyBrightness);
			addVertexPair(vertexBuilder, matrix, light, dX, dY, dZ, p_229119_9_, p_229119_10_, 24, stepIdx, false, xMag, zMag);
			addVertexPair(vertexBuilder, matrix, light, dX, dY, dZ, p_229119_9_, p_229119_10_, 24, stepIdx + 1, true, xMag, zMag);
		}

	}

	public static void addVertexPair(IVertexBuilder vertexBuilder, Matrix4f matrix, int light, float dX, float dY, float dZ, float width, float rotation, int steps, int stepIdx, boolean leading, float xMag, float zMag) {
		float r = 0.3F;
		float g = 0.3F;
		float b = 0.3F;
		if (stepIdx % 2 == 0) {
			r *= 0.7F;
			g *= 0.7F;
			b *= 0.7F;
		}

		float step = (float)stepIdx / steps;
		float x = dX * step;
		float y = dY * (step * step + step) * 0.5F + ((float)steps - stepIdx) / (steps * 0.75F) + 0.125F;
		float z = dZ * step;
		if (!leading) {
			vertexBuilder.pos(matrix, x + xMag, y + width - rotation, z - zMag).color(r, g, b, 1.0F).lightmap(light).endVertex();
		}

		vertexBuilder.pos(matrix, x - xMag, y + rotation, z + zMag).color(r, g, b, 1.0F).lightmap(light).endVertex();
		if (leading) {
			vertexBuilder.pos(matrix, x + xMag, y + width - rotation, z - zMag).color(r, g, b, 1.0F).lightmap(light).endVertex();
		}

	}
	
	// TODO WIRE: does not render

	public static void renderChain(EntityRenderer render, Entity entity, MatrixStack matrixStack, IRenderTypeBuffer renderBuffer, float partTicks) {
		if (ChainHandler.canBeLinked(entity)) {
			Entity holder = RENDER_MAP.get(entity.getEntityId());

			if (holder != null) {
				renderLeash(render, entity, partTicks, matrixStack, renderBuffer, holder);
			}
		}
	}

//	private static double interp(double start, double end, double pct)
//	{
//		return start + (end - start) * pct;
//	}

	private static double prevX(Entity entity) {
		if (entity instanceof AbstractMinecartEntity)
			return entity.lastTickPosX;
		return entity.prevPosX;
	}
	private static double prevY(Entity entity) {
		if (entity instanceof AbstractMinecartEntity)
			return entity.lastTickPosY;
		return entity.prevPosY;
	}
	private static double prevZ(Entity entity) {
		if (entity instanceof AbstractMinecartEntity)
			return entity.lastTickPosZ;
		return entity.prevPosZ;
	}
	private static double renderYawOffset(Entity entity) {
		if (entity instanceof LivingEntity)
			return ((LivingEntity) entity).renderYawOffset;
		return 0;
	}
	private static double prevRenderYawOffset(Entity entity) {
		if (entity instanceof LivingEntity)
			return ((LivingEntity) entity).prevRenderYawOffset;
		return 0;
	}

//	private static void renderChain(Entity cart, double x, double y, double z, float partialTicks) {
//		Entity entity = RENDER_MAP.get(cart.getEntityId());
//
//		if (entity != null) {
//			boolean player = entity instanceof PlayerEntity;
//
//			if(player)
//				y -= 1.3;
//			else y += 0.1;
//
//			Tessellator tess = Tessellator.getInstance();
//			BufferBuilder buf = tess.getBuffer();
//			double yaw = interp(entity.prevRotationYaw, entity.rotationYaw, (partialTicks * 0.5F)) * Math.PI / 180;
//			double pitch = interp(entity.prevRotationPitch, entity.rotationPitch, (partialTicks * 0.5F)) * Math.PI / 180;
//			double rotX = Math.cos(yaw);
//			double rotZ = Math.sin(yaw);
//			double rotY = Math.sin(pitch);
//
//			double height = player ? entity.getEyeHeight() * 0.7 : 0;
//
//			Vec3d entityPos = entity.getPositionVec();
//			Vec3d cartPos = cart.getPositionVec();
//
//			double pitchMod = Math.cos(pitch);
//			double xLocus = interp(prevX(entity), entityPos.x, partialTicks);
//			double yLocus = interp(prevY(entity), entityPos.y, partialTicks) + height;
//			double zLocus = interp(prevZ(entity), entityPos.z, partialTicks);
//
//			if (player) {
//				xLocus += -rotX * 0.7D - rotZ * 0.5D * pitchMod;
//				yLocus += -rotY * 0.5D - 0.25D;
//				zLocus += -rotZ * 0.7D + rotX * 0.5D * pitchMod;
//
//				zLocus -= 1;
//				yLocus += 2;
//			}
//
//			double targetX = interp(prevX(cart), entityPos.x, partialTicks);
//			double targetY = interp(prevY(cart), entityPos.y, partialTicks);
//			double targetZ = interp(prevZ(cart), entityPos.z, partialTicks);
//			if (player) {
//				xLocus -= rotX;
//				zLocus -= rotZ;
//			}
//
//			double offsetX = ((float) (xLocus - targetX));
//			double offsetY = ((float) (yLocus - targetY));
//			double offsetZ = ((float) (zLocus - targetZ));
//			RenderSystem.disableTexture();
//			RenderSystem.disableLighting();
//			RenderSystem.disableCull();
//
////			drawChainSegment(x, y, z, buf, offsetX, offsetY, offsetZ, 0.025, 0, 0.3f, 0.3f, 0.3f, height);
////
////			drawChainSegment(x, y, z, buf, offsetX, offsetY, offsetZ, 0, 0.025, 0.3f, 0.3f, 0.3f, height);
//
//			RenderSystem.enableLighting();
//			RenderSystem.enableTexture();
//			RenderSystem.enableCull();
//		}
//	}

	public static void updateTick() {
		RENDER_MAP.clear();

		ClientWorld world = Minecraft.getInstance().world;
		if (world == null)
			return;

		for (Entity entity : world.getAllEntities()) {
			if (ChainHandler.canBeLinked(entity)) {
				Entity other = ChainHandler.getLinked(entity);
				if (other != null)
					RENDER_MAP.put(entity.getEntityId(), other);
			}
		}
	}
}
