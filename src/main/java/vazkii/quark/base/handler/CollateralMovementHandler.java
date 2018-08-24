package vazkii.quark.base.handler;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.quark.api.ICollateralMover;

public class CollateralMovementHandler {

	public static boolean addCollateralMovements(World world, BlockPos sourcePos, List<BlockPos> moveList, List<BlockPos> destroyList, EnumFacing facing, boolean extending) {
		final int max = 13;
		
		Set<BlockPos> addedBlocks = new LinkedHashSet<>();
		Set<BlockPos> totalAddedBlocks = new LinkedHashSet<>();
		for(BlockPos pos : moveList) {
			System.out.println(pos);
			boolean res = addBlockCollateralMovement(world, pos, addedBlocks, facing);
			if(!res)
				return true;
		}
		
		while(!addedBlocks.isEmpty() && addedBlocks.size() <= max) {
			Set<BlockPos> lastItrBlocks = new LinkedHashSet<>(addedBlocks);
			totalAddedBlocks.addAll(addedBlocks);
			addedBlocks.clear();
			
			for(BlockPos pos : lastItrBlocks) {
				boolean res = addBlockCollateralMovement(world, pos, addedBlocks, facing);
				if(!res)
					return true;
			}
			
			addedBlocks.removeAll(totalAddedBlocks);
		}
		
		System.out.println("TOTAL: " + totalAddedBlocks);
		if(totalAddedBlocks.size() < max) {
			moveList.addAll(totalAddedBlocks);
			return false;
		}
		
		return true;
	}
	
	public static boolean addBlockCollateralMovement(World world, BlockPos pos, Collection<BlockPos> positions, EnumFacing facing) { 
		IBlockState state = world.getBlockState(pos);
		if(state.getBlock() instanceof ICollateralMover)
			return ((ICollateralMover) state.getBlock()).addCollateralMovement(world, pos, positions, facing);
		
		return true;
	}

	
}
