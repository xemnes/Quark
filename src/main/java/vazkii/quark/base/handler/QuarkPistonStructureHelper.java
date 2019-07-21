/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 * <p>
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * <p>
 * File Created @ [Jul 20, 2019, 15:50 AM (EST)]
 */
package vazkii.quark.base.handler;

import java.util.List;
import java.util.function.Function;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.state.BlockPistonStructureHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.quark.api.ICollateralMover.MoveResult;
import vazkii.quark.api.ICollateralMover;
import vazkii.quark.api.IPistonHelper;
import vazkii.quark.base.module.GlobalConfig;

public class QuarkPistonStructureHelper extends BlockPistonStructureHelper implements IPistonHelper {

	private final BlockPistonStructureHelper parent;

	private final World world;
	private final BlockPos pistonPos;
	private final BlockPos blockToMove;
	private final EnumFacing moveDirection;
	private final boolean isPulling;
	private final List<BlockPos> toMove = Lists.<BlockPos>newArrayList();
	private final List<BlockPos> toDestroy = Lists.<BlockPos>newArrayList();

	public QuarkPistonStructureHelper(BlockPistonStructureHelper parent, World worldIn, BlockPos posIn, EnumFacing pistonFacing, boolean extending) {
		super(worldIn, posIn, pistonFacing, extending);
		this.parent = parent;

		this.world = worldIn;
		this.pistonPos = posIn;
		if(extending) {
			this.moveDirection = pistonFacing;
			this.blockToMove = posIn.offset(pistonFacing);
		} else {
			this.moveDirection = pistonFacing.getOpposite();
			this.blockToMove = posIn.offset(pistonFacing, 2);
		}
		isPulling = !extending;
	}

	// 		EnumFacing realFacing = extending ? facing : facing.getOpposite();
	//
	//		PistonSpikes.breakStuffWithSpikes(world, sourcePos, helper, realFacing, extending);
	//		CollateralPistonMovement.applyCollateralMovements(world, helper, realFacing, extending);
	//		PistonsMoveTEs.detachTileEntities(world, helper, realFacing);

	@Override
	public boolean canMove() {
		if(!GlobalConfig.usePistonLogicRepl)
			return parent.canMove();

		toMove.clear();
		toDestroy.clear();
		IBlockState iblockstate = world.getBlockState(blockToMove);

		if(!isBlockPushable(iblockstate, world, blockToMove, moveDirection, false, moveDirection)) {
			if(iblockstate.getPushReaction() == EnumPushReaction.DESTROY) {
				toDestroy.add(blockToMove);
				return true;
			} else return false;
		}
		else if(!addBlockLine(blockToMove, moveDirection))
			return false;
		else {
			for(int i = 0; i < toMove.size(); ++i) {
				BlockPos blockpos = toMove.get(i);

				if(isBlockBranching(world, blockpos) && addBranchingBlocks(world, blockpos) == MoveResult.PREVENT)
					return false;
			}

			return true;
		}
	}

	private boolean addBlockLine(BlockPos origin, EnumFacing face) {
		final int max = 12;

		BlockPos target = origin;
		IBlockState iblockstate = world.getBlockState(target);
		Block block = iblockstate.getBlock();

		System.out.println("Adding Block Line for " + iblockstate);
		
		if(iblockstate.getBlock().isAir(iblockstate, world, origin) 
				|| !isBlockPushable(iblockstate, world, origin, moveDirection, false, face)
				|| origin.equals(pistonPos)
				|| toMove.contains(origin))
			return true;

		else {
			int lineLen = 1;

			if(lineLen + toMove.size() > max) 
				return false;
			else {
				boolean branched = false;
				while(true) {
					MoveResult res = getBranchResult(world, target);
					if(res == MoveResult.PREVENT)
						return false;
					else if(res != MoveResult.MOVE)
						break;
					
					target = origin.offset(moveDirection.getOpposite(), lineLen);
					iblockstate = world.getBlockState(target);
					block = iblockstate.getBlock();

					if(iblockstate.getBlock().isAir(iblockstate, world, target) || !isBlockPushable(iblockstate, world, target, moveDirection, false, moveDirection.getOpposite()) || target.equals(pistonPos))
						break;

					branched = true;
					lineLen++;
					if(lineLen + toMove.size() > max)
						return false;
				}

				int i1 = 0;

				for(int j = lineLen - 1; j >= 0; --j) {
					BlockPos finalPos = origin.offset(moveDirection.getOpposite(), j);
					toMove.add(finalPos);
					++i1;
				}

				int j1 = 1;

				while(true) {
					BlockPos blockpos1 = origin.offset(moveDirection, j1);
					int k = toMove.indexOf(blockpos1);

					if(k > -1) {
						reorderListAtCollision(i1, k);

						for(int l = 0; l <= k + i1; ++l) {
							BlockPos blockpos2 = toMove.get(l);

							if(isBlockBranching(world, blockpos2) && addBranchingBlocks(world, blockpos2) == MoveResult.PREVENT)
								return false;
						}

						return true;
					}

					iblockstate = world.getBlockState(blockpos1);

					if(iblockstate.getBlock().isAir(iblockstate, world, blockpos1))
						return true;

					if(!isBlockPushable(iblockstate, world, blockpos1, moveDirection, true, moveDirection) || blockpos1.equals(pistonPos))
						return false;

					if(iblockstate.getPushReaction() == EnumPushReaction.DESTROY) {
						toDestroy.add(blockpos1);
						return true;
					}

					if(toMove.size() >= max)
						return false;

					System.out.println("Adding " + iblockstate);
					System.out.println("toDestroy: " + toDestroy);
					toMove.add(blockpos1);

					++i1;
					++j1;
				}
			}
		}
	}


	private void reorderListAtCollision(int p_177255_1_, int p_177255_2_) {
		List<BlockPos> list = Lists.<BlockPos>newArrayList();
		List<BlockPos> list1 = Lists.<BlockPos>newArrayList();
		List<BlockPos> list2 = Lists.<BlockPos>newArrayList();
		list.addAll(toMove.subList(0, p_177255_2_));
		list1.addAll(toMove.subList(toMove.size() - p_177255_1_, toMove.size()));
		list2.addAll(toMove.subList(p_177255_2_, toMove.size() - p_177255_1_));
		toMove.clear();
		toMove.addAll(list);
		toMove.addAll(list1);
		toMove.addAll(list2);
	}

	@SuppressWarnings("incomplete-switch")
	private MoveResult addBranchingBlocks(World world, BlockPos fromPos) {
		IBlockState state = world.getBlockState(fromPos);
		Block block = state.getBlock();
		
		EnumFacing opposite = moveDirection.getOpposite();
		MoveResult retResult = MoveResult.SKIP;
		for(EnumFacing enumfacing : EnumFacing.values()) {
				MoveResult res = block instanceof ICollateralMover ? ((ICollateralMover) block).getCollateralMovement(this, enumfacing, fromPos) : MoveResult.MOVE;
				
				switch(res) {
				case PREVENT:
					return MoveResult.PREVENT;
				case MOVE:
					if(!addBlockLine(fromPos.offset(enumfacing), enumfacing))
						return MoveResult.PREVENT;
					break;
				case BREAK:
					toDestroy.add(fromPos.offset(enumfacing));
					break;
				}
				
				if(enumfacing == moveDirection)
					retResult = res;
			}

		return retResult;
	}

	private boolean isBlockBranching(World world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();

		return block instanceof ICollateralMover ? ((ICollateralMover) block).isCollateralMover(this, pos) : state.getBlock().isStickyBlock(state);
	}
	
	private MoveResult getBranchResult(World world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();

		if(!isBlockBranching(world, pos))
			return MoveResult.SKIP;
		
		if(block instanceof ICollateralMover)
			return ((ICollateralMover) block).getCollateralMovement(this, moveDirection, pos);
		
		return MoveResult.MOVE;
	}

	@Nonnull
	@Override
	public List<BlockPos> getBlocksToMove() {
		if(!GlobalConfig.usePistonLogicRepl)
			return parent.getBlocksToMove();

		return toMove;
	}

	@Nonnull
	@Override
	public List<BlockPos> getBlocksToDestroy() {
		if(!GlobalConfig.usePistonLogicRepl)
			return parent.getBlocksToDestroy();

		return toDestroy;
	}

	@Override
	public World getWorld() {
		return world;
	}

	@Override
	public BlockPos getSource() {
		return pistonPos;
	}

	@Override
	public EnumFacing getMoveDirection() {
		return moveDirection;
	}

	@Override
	public boolean isPulling() {
		return isPulling;
	}

	@Override
	public boolean isBlockPushable(IBlockState state, World worldIn, BlockPos pos, EnumFacing facing, boolean destroyBlocks, EnumFacing targetFacing) {
		return BlockPistonBase.canPush(state, worldIn, pos, facing, destroyBlocks, targetFacing);
	}

}
