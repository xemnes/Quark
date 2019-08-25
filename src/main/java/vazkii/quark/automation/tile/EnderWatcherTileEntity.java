package vazkii.quark.automation.tile;

import java.util.List;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext.BlockMode;
import net.minecraft.util.math.RayTraceContext.FluidMode;
import net.minecraft.util.math.RayTraceResult;
import vazkii.arl.block.tile.TileMod;
import vazkii.quark.automation.block.EnderWatcherBlock;
import vazkii.quark.automation.module.EnderWatcherModule;
import vazkii.quark.base.handler.RayTraceHandler;

public class EnderWatcherTileEntity extends TileMod implements ITickableTileEntity {
	
	public EnderWatcherTileEntity() {
		super(EnderWatcherModule.enderWatcherTEType);
	}

	@Override
	public void tick() {
		boolean wasLooking = getWorld().getBlockState(getPos()).get(EnderWatcherBlock.WATCHED);
		int range = 80;
		List<PlayerEntity> players = getWorld().getEntitiesWithinAABB(PlayerEntity.class, new AxisAlignedBB(pos.add(-range, -range, -range), pos.add(range, range, range)));
		
		boolean looking = false;
		for(PlayerEntity player : players) {
			ItemStack helm = player.getItemStackFromSlot(EquipmentSlotType.HEAD);
			if(!helm.isEmpty() && helm.getItem() == Items.PUMPKIN)
				continue;

			RayTraceResult result = RayTraceHandler.rayTrace(player, getWorld(), player, BlockMode.OUTLINE, FluidMode.NONE, 64);
			if(result != null && result instanceof BlockRayTraceResult && ((BlockRayTraceResult) result).getPos().equals(getPos())) {
				looking = true;
				break;
			}
		}
		
		if(looking != wasLooking && !getWorld().isRemote)
			getWorld().setBlockState(getPos(), getWorld().getBlockState(getPos()).with(EnderWatcherBlock.WATCHED, looking), 1 | 2);

		if(looking) {
			double x = getPos().getX() - 0.1 + Math.random() * 1.2;
			double y = getPos().getY() - 0.1 + Math.random() * 1.2;
			double z = getPos().getZ() - 0.1 + Math.random() * 1.2;

			getWorld().addParticle(new RedstoneParticleData(1.0F, 0.0F, 0.0F, 1.0F), x, y, z, 0.0D, 0.0D, 0.0D);
		}		
	}

}
