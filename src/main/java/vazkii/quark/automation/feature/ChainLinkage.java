/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 * <p>
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * <p>
 * File Created @ [Jul 17, 2019, 12:37 AM (EST)]
 */
package vazkii.quark.automation.feature;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.minecart.MinecartUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import vazkii.arl.network.NetworkHandler;
import vazkii.quark.base.client.ClientReflectiveAccessor;
import vazkii.quark.base.module.Feature;
import vazkii.quark.base.network.message.MessageSyncChain;

import java.util.UUID;

public class ChainLinkage extends Feature {
	public static final String LINKED_TO = "Quark:VehicleLink";
	public static final double DRAG = 0.95;
	public static final float CHAIN_LENGTH = 3F;
	public static final float MAX_DISTANCE = 8F;
	private static final float STIFFNESS = 0.7F;
	private static final float DAMPING = 0.4F;
	private static final float MAX_FORCE = 6F;

	public static Item chain;

	private static final TIntObjectMap<Entity> RENDER_MAP = new TIntObjectHashMap<>();
	private static final TIntObjectMap<UUID> AWAIT_MAP = new TIntObjectHashMap<>();

	private static <T extends Entity> void adjustVelocity(T master, T follower) {
		if (master == follower || master.world.isRemote)
			return;

		double dist = master.getDistance(follower);

		Vec3d masterPosition = master.getPositionVector();
		Vec3d followerPosition = follower.getPositionVector();

		Vec3d direction = followerPosition.subtract(masterPosition);
		direction = direction.subtract(0, direction.y, 0).normalize();

		double stretch = dist - CHAIN_LENGTH;

		double springX = STIFFNESS * stretch * direction.x;
		double springZ = STIFFNESS * stretch * direction.z;

		springX = MathHelper.clamp(springX, -MAX_FORCE, MAX_FORCE);
		springZ = MathHelper.clamp(springZ, -MAX_FORCE, MAX_FORCE);

		master.motionX += springX;
		master.motionZ += springZ;

		follower.motionX -= springX;
		follower.motionZ -= springZ;

		Vec3d newMasterVelocity = new Vec3d(master.motionX, 0, master.motionZ);
		Vec3d newFollowerVelocity = new Vec3d(follower.motionX, 0, follower.motionZ);

		double deviation = newFollowerVelocity.subtract(newMasterVelocity).dotProduct(direction);

		double dampX = DAMPING * deviation * direction.x;
		double dampZ = DAMPING * deviation * direction.z;

		dampX = MathHelper.clamp(dampX, -MAX_FORCE, MAX_FORCE);
		dampZ = MathHelper.clamp(dampZ, -MAX_FORCE, MAX_FORCE);

		master.motionX += dampX;
		master.motionZ += dampZ;

		follower.motionX -= dampX;
		follower.motionZ -= dampZ;
	}

	private static UUID getLink(Entity cart) {
		if (!cart.getEntityData().hasUniqueId(LINKED_TO))
			return null;

		return cart.getEntityData().getUniqueId(LINKED_TO);
	}

	private static <T extends Entity> T getLinked(T cart, Class<T> vehicleClass) {
		UUID uuid = getLink(cart);
		if (uuid == null)
			return null;

		for (T entity : cart.world.getEntitiesWithinAABB(vehicleClass,
				cart.getEntityBoundingBox().grow(MAX_DISTANCE))) {
			if (entity.getUniqueID().equals(uuid)) {
				return entity;
			}
		}

		return null;
	}

	private static <T extends Entity> void adjustVehicle(T cart, Class<T> vehicleClass) {
		T other = getLinked(cart, vehicleClass);

		if (other == null) {
			breakChain(cart);
			return;
		}

		adjustVelocity(cart, other);

		cart.motionX *= DRAG;
		cart.motionZ *= DRAG;
	}

	public static void renderChain(Render render, double x, double y, double z, Entity entity, float partTicks) {
		if (!ClientReflectiveAccessor.renderOutlines(render)) {
			renderChain(entity, x, y, z, partTicks);
		}
	}

	private static double interp(double start, double end, double pct)
	{
		return start + (end - start) * pct;
	}

	public static void queueChainUpdate(int vehicle, UUID other) {
		if (other != null)
			AWAIT_MAP.put(vehicle, other);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void clientUpdateTick(TickEvent.ClientTickEvent event) {
		if (event.side == Side.CLIENT && event.phase == TickEvent.Phase.START) {
			RENDER_MAP.clear();

			World world = Minecraft.getMinecraft().world;
			if (world == null)
				return;

			for (EntityBoat entity : world.getEntities(EntityBoat.class,
					(entity) -> entity != null && entity.getEntityData().hasUniqueId(LINKED_TO))) {
				EntityBoat other = getLinked(entity, EntityBoat.class);
				if (other != null)
					RENDER_MAP.put(entity.getEntityId(), other);
			}

			for (EntityMinecart entity : world.getEntities(EntityMinecart.class,
					(entity) -> entity != null && entity.getEntityData().hasUniqueId(LINKED_TO))) {
				EntityMinecart other = getLinked(entity, EntityMinecart.class);
				if (other != null)
					RENDER_MAP.put(entity.getEntityId(), other);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	private static void renderChain(Entity cart, double x, double y, double z, float partialTicks) {
		Entity entity = RENDER_MAP.get(cart.getEntityId());

		if (entity != null) {
//			y -= (1.6D - cart.height) * 0.5D;
			Tessellator tess = Tessellator.getInstance();
			BufferBuilder buf = tess.getBuffer();
			double yaw = interp(entity.prevRotationYaw, entity.rotationYaw, (partialTicks * 0.5F)) * 0.01745329238474369D;
			double pitch = interp(entity.prevRotationPitch, entity.rotationPitch, (partialTicks * 0.5F)) * 0.01745329238474369D;
			double rotX = Math.cos(yaw);
			double rotZ = Math.sin(yaw);
			double rotY = Math.sin(pitch);

			double pitchMod = Math.cos(pitch);
			double xLocus = interp(entity.prevPosX, entity.posX, partialTicks);
			double yLocus = interp(entity.prevPosY, entity.posY, partialTicks);
			double zLocus = interp(entity.prevPosZ, entity.posZ, partialTicks);
			double targetX = interp(cart.prevPosX, cart.posX, partialTicks);
			double targetY = interp(cart.prevPosY, cart.posY, partialTicks);
			double targetZ = interp(cart.prevPosZ, cart.posZ, partialTicks);
//			x += rotX;
//			z += rotZ;
			double offsetX = ((float) (xLocus - targetX));
			double offsetY = ((float) (yLocus - targetY));
			double offsetZ = ((float) (zLocus - targetZ));
			GlStateManager.disableTexture2D();
			GlStateManager.disableLighting();
			GlStateManager.disableCull();
			buf.begin(GL11.GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION_COLOR);

			drawChainSegment(x, y, z, buf, offsetX, offsetY, offsetZ, 0.025, 0, 0.3f, 0.3f, 0.3f);

			tess.draw();
			buf.begin(GL11.GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION_COLOR);

			drawChainSegment(x, y, z, buf, offsetX, offsetY, offsetZ, 0, 0.025, 0.3f, 0.3f, 0.3f);

			tess.draw();
			GlStateManager.enableLighting();
			GlStateManager.enableTexture2D();
			GlStateManager.enableCull();
		}
	}

	@SideOnly(Side.CLIENT)
	public static void drawChainSegment(double x, double y, double z, BufferBuilder bufferbuilder, double offsetX, double offsetY, double offsetZ, double xOff, double zOff, float baseR, float baseG, float baseB) {
		for (int seg = 0; seg <= 24; ++seg) {
			float r = baseR;
			float g = baseG;
			float b = baseB;

			if (seg % 2 == 0) {
				r *= 0.7F;
				g *= 0.7F;
				b *= 0.7F;
			}

			float amount = seg / 24.0F;
			bufferbuilder.pos(x + offsetX * amount + 0.0D, y + offsetY * (amount * amount + amount) * 0.5D + ((24.0F - seg) / 18.0F + 0.125F) * offsetY + xOff, z + offsetZ * amount).color(r, g, b, 1.0F).endVertex();
			bufferbuilder.pos(x + offsetX * amount + 0.025D, y + offsetY * (amount * amount + amount) * 0.5D + ((24.0F - seg) / 18.0F + 0.125F) * offsetY + zOff, z + offsetZ * amount + xOff).color(r, g, b, 1.0F).endVertex();
		}
	}

	private static <T extends Entity> void breakChain(T cart) {
		cart.getEntityData().removeTag(LINKED_TO + "Most");
		cart.getEntityData().removeTag(LINKED_TO + "Least");

		if (cart.world.getGameRules().getBoolean("doEntityDrops"))
			cart.entityDropItem(new ItemStack(chain), 0f);
	}

	public static void onBoatUpdate(EntityBoat boat) {
		adjustVehicle(boat, EntityBoat.class);
	}

	public static void drop(EntityMinecart vehicle) {
		if (getLinked(vehicle, EntityMinecart.class) != null)
			vehicle.entityDropItem(new ItemStack(chain), 0f);
	}

	public static void drop(EntityBoat vehicle) {
		if (getLinked(vehicle, EntityBoat.class) != null)
			vehicle.entityDropItem(new ItemStack(chain), 0f);
	}

	public static void updateLinkState(Entity entity, UUID uuid, EntityPlayer player) {
		if (player instanceof EntityPlayerMP)
			NetworkHandler.INSTANCE.sendTo(new MessageSyncChain(entity.getEntityId(), uuid), (EntityPlayerMP) player);
	}

	@SubscribeEvent
	public void onVehicleSeen(PlayerEvent.StartTracking event) {
		updateLinkState(event.getTarget(), getLink(event.getTarget()), event.getEntityPlayer());
	}

	public static void setLink(Entity entity, UUID uuid, boolean sync) {
		if (entity instanceof EntityBoat || entity instanceof EntityMinecart) {
			if (uuid != null)
				entity.getEntityData().setUniqueId(LINKED_TO, uuid);
			else {
				entity.getEntityData().removeTag(LINKED_TO + "Most");
				entity.getEntityData().removeTag(LINKED_TO + "Least");
			}

			if (sync) {
				if (entity.world instanceof WorldServer) {
					WorldServer world = (WorldServer) entity.world;
					for (EntityPlayer target : world.getEntityTracker().getTrackingPlayers(entity))
						updateLinkState(entity, uuid, target);
				}
			}
		}
	}

	@SubscribeEvent
	public void onVehicleArrive(EntityJoinWorldEvent event) {
		if (event.getWorld().isRemote) {
			Entity target = event.getEntity();
			int id = target.getEntityId();
			if (AWAIT_MAP.containsKey(id))
				target.getEntityData().setUniqueId(LINKED_TO, AWAIT_MAP.get(id));
			AWAIT_MAP.remove(id);
		}
	}

	@SubscribeEvent
	public void onMinecartUpdate(MinecartUpdateEvent event) {
		EntityMinecart cart = event.getMinecart();
		adjustVehicle(cart, EntityMinecart.class);
	}

	@Override
	public boolean hasSubscriptions() {
		return true;
	}
}
