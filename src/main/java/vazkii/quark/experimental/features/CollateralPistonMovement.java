package vazkii.quark.experimental.features;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockObserver;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.state.BlockPistonStructureHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.quark.base.module.Feature;
import vazkii.quark.base.module.ModuleLoader;

public class CollateralPistonMovement extends Feature {

	public static void applyCollateralMovements(World world, BlockPos sourcePos, BlockPistonStructureHelper helper, EnumFacing facing, boolean extending) {
		if(!ModuleLoader.isFeatureEnabled(CollateralPistonMovement.class))
			return;
		
		List<BlockPos> moveList = helper.getBlocksToMove();
		List<BlockPos> additions = new LinkedList();
		
		for(BlockPos pos : moveList) {
			IBlockState state = world.getBlockState(pos);
			if(state.getBlock() == Blocks.STICKY_PISTON) {
				EnumFacing face = state.getValue(BlockDirectional.FACING);
				BlockPos offPos = pos.offset(face);
				IBlockState offState = world.getBlockState(offPos);
				System.out.println(offState);
				
				if(!offState.getBlock().isAir(offState, world, offPos) && offState.getMobilityFlag() == EnumPushReaction.NORMAL)
					additions.add(offPos);
			} else if(state.getBlock() == Blocks.OAK_FENCE) {
				BlockPos tpos = pos;
				do {
					additions.add(tpos);
					tpos = tpos.up();
					state = world.getBlockState(tpos);
				} while(state.getBlock() == Blocks.OAK_FENCE);
			}
		}
		
		moveList.addAll(additions);
	}

	
}
