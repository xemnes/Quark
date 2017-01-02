package vazkii.quark.automation.feature;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.quark.base.module.Feature;
import vazkii.quark.base.module.ModuleLoader;

public class PistonSpikes extends Feature {

	public static boolean breakStuffWithSpikes(World world, List<BlockPos> moveList, List<BlockPos> destroyList, EnumFacing facing, boolean extending) {
		if(!extending || !ModuleLoader.isFeatureEnabled(PistonSpikes.class))
			return false;
		
		boolean did = false;
		List<BlockPos> newMoveList = new ArrayList(moveList);
		List<BlockPos> newDestroyList = new ArrayList(destroyList);
		
		for(BlockPos pos : moveList) {
			IBlockState state = world.getBlockState(pos);
			if(state.getBlock() == Blocks.END_ROD && state.getValue(BlockDirectional.FACING) == facing) {
				BlockPos off = pos.offset(facing.getOpposite());
				
				for(int i = 0; i < 14; i++) {
					IBlockState stateAt = world.getBlockState(off);
					Block blockAt = stateAt.getBlock();
					if(blockAt.isAir(stateAt, world, off))
						break;
					
					if(i == 0 && blockAt != Blocks.PISTON && blockAt != Blocks.STICKY_PISTON)
						return false;
					
					if(blockAt == Blocks.SLIME_BLOCK)
						return false;
					
					if(i >= 2) {
						if(i == 2) {
							newDestroyList.add(off);
							if(newMoveList.contains(off))
								newMoveList.remove(off);
						} else {
							if(newMoveList.contains(off))
								newMoveList.remove(off);
							else if(newDestroyList.contains(off))
								newDestroyList.remove(off);
						}
					}

					off = off.offset(facing);
				}
				
				did = true;
			}
		}
		
		if(did) {
			moveList.clear();
			moveList.addAll(newMoveList);
			destroyList.clear();
			destroyList.addAll(newDestroyList);
		}
		
		return did;
	}
	
}
