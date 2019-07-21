package vazkii.quark.api;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IPistonHelper {

	World getWorld();
	
	BlockPos getSource();
	
	EnumFacing getMoveDirection();
	
	boolean isPulling();
	
	boolean isBlockPushable(IBlockState state, World worldIn, BlockPos pos, EnumFacing facing, boolean destroyBlocks, EnumFacing targetFacing);
	
}
