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

import net.minecraft.block.state.BlockPistonStructureHelper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

public class QuarkPistonStructureHelper extends BlockPistonStructureHelper {
	private final BlockPistonStructureHelper parent;

	public QuarkPistonStructureHelper(BlockPistonStructureHelper parent, World world, BlockPos sourcePos, EnumFacing facing, boolean extending) {
		super(world, sourcePos, facing, extending);
		this.parent = parent;
	}

	// 		EnumFacing realFacing = extending ? facing : facing.getOpposite();
	//
	//		PistonSpikes.breakStuffWithSpikes(world, sourcePos, helper, realFacing, extending);
	//		CollateralPistonMovement.applyCollateralMovements(world, helper, realFacing, extending);
	//		PistonsMoveTEs.detachTileEntities(world, helper, realFacing);

	@Override
	public boolean canMove() {
		return parent.canMove();
	}

	@Nonnull
	@Override
	public List<BlockPos> getBlocksToMove() {
		return parent.getBlocksToMove();
	}

	@Nonnull
	@Override
	public List<BlockPos> getBlocksToDestroy() {
		return parent.getBlocksToDestroy();
	}
}
