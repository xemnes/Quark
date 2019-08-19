package vazkii.quark.tweaks.module;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;

import net.minecraft.block.BlockState;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext.BlockMode;
import net.minecraft.util.math.RayTraceContext.FluidMode;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import vazkii.quark.base.handler.RayTraceHandler;
import vazkii.quark.base.module.Config;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleCategory;

@LoadModule(category = ModuleCategory.TWEAKS, hasSubscriptions = true)
public class ReacharoundPlacingModule extends Module {

	@Config public float leniency = 0.5F;
	@Config public List<String> whitelist = Lists.newArrayList();
	@Config public String display = "[  ]";
	
	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void onRender(RenderGameOverlayEvent.Post event) {
		if(event.getType() != RenderGameOverlayEvent.ElementType.CROSSHAIRS)
			return;
		
		Minecraft mc = Minecraft.getInstance();
		PlayerEntity player = mc.player;
		
		if(player != null) {
			BlockPos pos = getPlayerReacharoundTarget(player);
			if(pos != null) {
				MainWindow res = event.getWindow();
				mc.fontRenderer.drawString(display, res.getScaledWidth() / 2 - mc.fontRenderer.getStringWidth(display) / 2 + 1, res.getScaledHeight() / 2 - 3, 0xFFFFFF);
			}
		}
	}
	
	@SubscribeEvent
	public void onRightClick(PlayerInteractEvent.RightClickItem event) {
		ItemStack stack = event.getItemStack();
	
		PlayerEntity player = event.getPlayer();
		BlockPos pos = getPlayerReacharoundTarget(player);
		
		if(pos != null) {
			int count = stack.getCount();
			Hand hand = event.getHand();
			
//			BlockState currState = player.world.getBlockState(pos);
			ItemUseContext context = new ItemUseContext(player, hand, new BlockRayTraceResult(new Vec3d(0.5F, 1F, 0.5F), Direction.DOWN, pos, false));
			ActionResultType res = stack.getItem().onItemUse(context);
			
			if(res != ActionResultType.PASS) {
				event.setCanceled(true);
				event.setCancellationResult(res);
				
				if(res == ActionResultType.SUCCESS) {
//					if(!player.world.getBlockState(pos).equals(currState)) TODO add back after lock direction
//						LockDirectionHotkey.fixBlockRotation(player.world, player, pos);
						
					player.swingArm(hand);
				}

				if(player.isCreative() && stack.getCount() < count)
					stack.setCount(count);
			}
		}
	}
	
	private BlockPos getPlayerReacharoundTarget(PlayerEntity player) {
		if(player.rotationPitch < 0 || !(validateReacharoundStack(player.getHeldItemMainhand()) || validateReacharoundStack(player.getHeldItemOffhand())))
			return null;
		
		World world = player.world;
		
		Pair<Vec3d, Vec3d> params = RayTraceHandler.getEntityParams(player);
		double range = RayTraceHandler.getEntityRange(player);
		Vec3d rayPos = params.getLeft();
		Vec3d ray = params.getRight().scale(range);

		RayTraceResult normalRes = RayTraceHandler.rayTrace(player, world, rayPos, ray, BlockMode.OUTLINE, FluidMode.NONE);
		
		if(normalRes == null || normalRes.getType() == RayTraceResult.Type.MISS) {
			float leniency = 0.5F;
			
			rayPos = rayPos.add(0, leniency, 0);
			RayTraceResult take2Res = RayTraceHandler.rayTrace(player, world, rayPos, ray, BlockMode.OUTLINE, FluidMode.NONE);
			
			if(take2Res != null && take2Res.getType() == RayTraceResult.Type.BLOCK && take2Res instanceof BlockRayTraceResult) {
				BlockPos pos = ((BlockRayTraceResult) take2Res).getPos().down();
				BlockState state = world.getBlockState(pos);

				if(player.posY - pos.getY() > 1 && (world.isAirBlock(pos) || state.getMaterial().isReplaceable()))
					return pos;
			}
		}
		
		return null;
	}
	
	private boolean validateReacharoundStack(ItemStack stack) {
		Item item = stack.getItem();
		return item instanceof BlockItem || whitelist.contains(Objects.toString(item.getRegistryName()).toString());
	}
	
}
