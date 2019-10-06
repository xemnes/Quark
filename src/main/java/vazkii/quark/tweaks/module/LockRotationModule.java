package vazkii.quark.tweaks.module;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.state.IProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.Half;
import net.minecraft.state.properties.SlabType;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.KeybindTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import vazkii.arl.network.MessageSerializer;
import vazkii.quark.base.handler.MiscUtil;
import vazkii.quark.base.handler.ModKeybindHandler;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.base.network.QuarkNetwork;
import vazkii.quark.base.network.message.SetLockProfileMessage;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.UUID;

@LoadModule(category = ModuleCategory.TWEAKS, hasSubscriptions = true)
public class LockRotationModule extends Module {

	private static final String TAG_LOCKED_ONCE = "quark:locked_once";

	private static final HashMap<UUID, LockProfile> lockProfiles = new HashMap<>();
	
	@OnlyIn(Dist.CLIENT)
	private LockProfile clientProfile;

	@OnlyIn(Dist.CLIENT)
	private KeyBinding keybind;

	@Override
	public void configChanged() {
		lockProfiles.clear();
	}
	
	@Override
	public void setup() {
		MessageSerializer.mapHandler(LockProfile.class, LockProfile::readProfile, LockProfile::writeProfile);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void clientSetup() {
		keybind = ModKeybindHandler.init("lock_rotation", "k", ModKeybindHandler.MISC_GROUP);
	}
	
	@SubscribeEvent
	public void onBlockPlaced(BlockEvent.EntityPlaceEvent event) {
		if(event.isCanceled() || event.getResult() == Result.DENY)
			return;

		Entity e = event.getEntity();
		if(e instanceof PlayerEntity)
			fixBlockRotation(event.getWorld(), (PlayerEntity) e, event.getPos());
	}

	public static void fixBlockRotation(IWorld world, PlayerEntity player, BlockPos pos) {
		BlockState state = world.getBlockState(pos);

		UUID uuid = player.getUniqueID();
		if(lockProfiles.containsKey(uuid)) {
			LockProfile profile = lockProfiles.get(uuid);
			setBlockRotated(world, state, pos, profile.facing.getOpposite(), true, profile.half);
		}
	}

	public static void setBlockRotated(IWorld world, BlockState state, BlockPos pos, Direction face) {
		setBlockRotated(world, state, pos, face, false, -1);
	}

	public static void setBlockRotated(IWorld world, BlockState state, BlockPos pos, Direction face, boolean stateCheck, int half) {
		BlockState setState = state;
		ImmutableMap<IProperty<?>, Comparable<?>> props = state.getValues();
		Block block = state.getBlock();

		// API hook TODO re-add
		//		if(block instanceof IRotationLockHandler)
		//			setState = ((IRotationLockHandler) block).setRotation(world, pos, setState, face, half != -1, half == 1);

		// Bed Special Case
		if(block.isIn(BlockTags.BEDS) && face.getAxis() != Axis.Y) {
			Direction prevFace = state.get(BlockStateProperties.HORIZONTAL_FACING);
			Direction opposite = face.getOpposite();
			if (prevFace != opposite) {
				BlockPos prevPos = pos.offset(prevFace);
				setState = state.with(BlockStateProperties.HORIZONTAL_FACING, opposite);
				BlockState inWorld = world.getBlockState(prevPos);
				if (inWorld.getBlock().isIn(BlockTags.BEDS)) {
					world.removeBlock(prevPos, false);
					world.setBlockState(pos.offset(opposite), inWorld.with(BlockStateProperties.HORIZONTAL_FACING, opposite), 1 | 2);
				}
			}
		}
		
		// General Facing
		else if(props.containsKey(BlockStateProperties.FACING))
			setState = state.with(BlockStateProperties.FACING, face);

		// Horizontal Facing
		else if(props.containsKey(BlockStateProperties.HORIZONTAL_FACING) && face.getAxis() != Axis.Y) {
			if(block instanceof StairsBlock)
				setState = state.with(BlockStateProperties.HORIZONTAL_FACING, face.getOpposite());
			else setState = state.with(BlockStateProperties.HORIZONTAL_FACING, face);
		} 

		// Pillar Axis
		else if(props.containsKey(BlockStateProperties.AXIS))
			setState = state.with(BlockStateProperties.AXIS, face.getAxis());

		// Hopper Facing
		else if(props.containsKey(BlockStateProperties.FACING_EXCEPT_UP))
			setState = state.with(BlockStateProperties.FACING_EXCEPT_UP, face == Direction.DOWN ? face : face.getOpposite());

		// Half
		if(half != -1) {
			// Slab type
			if(props.containsKey(BlockStateProperties.SLAB_TYPE) && props.get(BlockStateProperties.SLAB_TYPE) != SlabType.DOUBLE)
				setState = setState.with(BlockStateProperties.SLAB_TYPE, half == 1 ? SlabType.TOP : SlabType.BOTTOM);
			
			// Half (stairs)
			else if(props.containsKey(BlockStateProperties.HALF))
				setState = setState.with(BlockStateProperties.HALF, half == 1 ? Half.TOP : Half.BOTTOM);
		}
			

		if(!stateCheck || setState != state) {
			world.setBlockState(pos, setState, 1 | 2);
			if(world instanceof World)
				((World) world).neighborChanged(pos, setState.getBlock(), pos);
		}
	}

	@SubscribeEvent
	public void onPlayerLogoff(PlayerLoggedOutEvent event) {
		lockProfiles.remove(event.getPlayer().getUniqueID());
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void onKeyInput(KeyInputEvent event) {
		Minecraft mc = Minecraft.getInstance();
		boolean down = keybind.isKeyDown();
		if(mc.isGameFocused() && down) {
			LockProfile newProfile;
			RayTraceResult result = mc.objectMouseOver;

			if(result instanceof BlockRayTraceResult && result.getType() == Type.BLOCK) {
				BlockRayTraceResult bresult = (BlockRayTraceResult) result;
				Vec3d hitVec = bresult.getHitVec();
				Direction face = bresult.getFace();

				int half = (int) ((hitVec.y - (int) hitVec.y) * 2);
				if(face.getAxis() == Axis.Y)
					half = -1;

				newProfile = new LockProfile(face.getOpposite(), half);

			} else {
				Vec3d look = mc.player.getLookVec();
				newProfile = new LockProfile(Direction.getFacingFromVector((float) look.x, (float) look.y, (float) look.z), -1);
			}

			if(clientProfile != null && clientProfile.equals(newProfile))
				clientProfile = null;
			else clientProfile = newProfile;
			QuarkNetwork.sendToServer(new SetLockProfileMessage(clientProfile));
		}
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void onHUDRender(RenderGameOverlayEvent.Post event) {
		if(event.getType() == ElementType.ALL && clientProfile != null) {
			Minecraft mc = Minecraft.getInstance();
			GlStateManager.pushMatrix();
			GlStateManager.enableBlend();
			GlStateManager.enableAlphaTest();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GlStateManager.color4f(1F, 1F, 1F, 0.5F);

			mc.textureManager.bindTexture(MiscUtil.GENERAL_ICONS);

			MainWindow window = event.getWindow();
			int x = window.getScaledWidth() / 2 + 20;
			int y = window.getScaledHeight() / 2 - 8;
			Screen.blit(x, y, clientProfile.facing.ordinal() * 16, 65, 16, 16, 256, 256);

			if(clientProfile.half > -1)
				Screen.blit(x + 16, y, clientProfile.half * 16, 81, 16, 16, 256, 256);

			GlStateManager.popMatrix();
		}
	}

	public static void setProfile(PlayerEntity player, LockProfile profile) {
		UUID uuid = player.getUniqueID();
		
		if(profile == null)
			lockProfiles.remove(uuid);
		else {
			boolean locked = player.getPersistentData().getBoolean(TAG_LOCKED_ONCE);
			if(!locked) {
				ITextComponent keybind = new KeybindTextComponent("quark.keybind.lock_rotation");
				keybind.getStyle().setColor(TextFormatting.AQUA);
				ITextComponent text = new TranslationTextComponent("quark.misc.rotation_lock", keybind);
				player.sendMessage(text);

				player.getPersistentData().putBoolean(TAG_LOCKED_ONCE, true);
			}

			lockProfiles.put(uuid, profile);
		}
	}

	public static class LockProfile {

		public final Direction facing;
		public final int half;

		public LockProfile(Direction facing, int half) {
			this.facing = facing;
			this.half = half;
		}

		public static LockProfile readProfile(PacketBuffer buf, Field field) {
			boolean valid = buf.readBoolean();
			if(!valid)
				return null;

			int face = buf.readInt();
			int half = buf.readInt();
			return new LockProfile(Direction.byIndex(face), half);
		}

		public static void writeProfile(PacketBuffer buf, Field field, LockProfile p) {
			if(p == null)
				buf.writeBoolean(false);
			else {
				buf.writeBoolean(true);
				buf.writeInt(p.facing.getIndex());
				buf.writeInt(p.half);
			}
		}

		@Override
		public boolean equals(Object other) {
			if(other == this)
				return true;
			if(!(other instanceof LockProfile))
				return false;

			LockProfile otherProfile = (LockProfile) other;
			return otherProfile.facing == facing && otherProfile.half == half;
		}

	}

}
