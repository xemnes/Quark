package vazkii.quark.tweaks.module;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LadderBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.MovementInput;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import vazkii.quark.base.module.Config;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleCategory;

@LoadModule(category = ModuleCategory.TWEAKS, hasSubscriptions = true)
public class EnchancedLaddersModule extends Module {

	@Config public double fallSpeed = -0.2;
	
	private static boolean canAttachTo(Block ladder, IBlockReader world, BlockPos pos, Direction facing) {
		if (ladder instanceof LadderBlock) {
			BlockPos offset = pos.offset(facing);
			BlockState blockstate = world.getBlockState(offset);
			return !blockstate.canProvidePower() && blockstate.func_224755_d(world, offset, facing); 
		}

		return false;
	}

	@SubscribeEvent
	public void onInteract(PlayerInteractEvent.RightClickBlock event) {
		PlayerEntity player = event.getPlayer();
		Hand hand = event.getHand();
		ItemStack stack = player.getHeldItem(hand);

		if(!stack.isEmpty() && stack.getItem() == Items.LADDER) {
			Block block = Block.getBlockFromItem(stack.getItem());
			World world = event.getWorld();
			BlockPos pos = event.getPos();
			while(world.getBlockState(pos).getBlock() == block) {
				event.setCanceled(true);
				BlockPos posDown = pos.down();

				if(World.isOutsideBuildHeight(posDown))
					break;

				BlockState stateDown = world.getBlockState(posDown);

				if(stateDown.getBlock() == block)
					pos = posDown;
				else {
					if(stateDown.getBlock().isAir(stateDown, world, posDown)) {
						BlockState copyState = world.getBlockState(pos);

						Direction facing = copyState.get(LadderBlock.FACING);
						if(canAttachTo(block, world, posDown, facing.getOpposite())) {
							world.setBlockState(posDown, copyState);
							world.playSound(null, posDown.getX(), posDown.getY(), posDown.getZ(), SoundEvents.BLOCK_LADDER_PLACE, SoundCategory.BLOCKS, 1F, 1F);

							if(world.isRemote)
								player.swingArm(hand);

							if(!player.isCreative()) {
								stack.shrink(1);

								if(stack.getCount() <= 0)
									player.setHeldItem(hand, ItemStack.EMPTY);
							}
						}
					}
					break;
				} 
			}
		}
	}

	@SubscribeEvent
	public void onPlayerTick(LivingUpdateEvent event) {
		if(event.getEntityLiving() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getEntityLiving();
			if(player.isOnLadder() && !player.isSneaking() && player.moveForward == 0 && player.rotationPitch > 70 && !player.world.getBlockState(player.getPosition().down()).isSolid()) {
				Vec3d move = new Vec3d(0, fallSpeed, 0);
				player.setBoundingBox(player.getBoundingBox().offset(move));
				player.move(MoverType.SELF, Vec3d.ZERO); // update all the things
			}
		}
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void onInput(InputUpdateEvent event) {
		PlayerEntity player = event.getPlayer();
		if(player.isOnLadder() && Minecraft.getInstance().currentScreen != null && !(player.moveForward == 0 && player.rotationPitch > 70)) {
			MovementInput input = event.getMovementInput();
			if(input != null)
				input.sneak = true;
		}
	}

}
