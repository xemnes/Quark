package vazkii.quark.api;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public interface ICollateralMover {

	default boolean isCollateralMover(IPistonHelper helper, BlockPos pos) {
		return true;
	}
	
	MoveResult getCollateralMovement(IPistonHelper helper, EnumFacing side, BlockPos pos); 
	
	public static enum MoveResult {
		
		MOVE,
		BREAK,
		SKIP,
		PREVENT
		
	}
	
	
}
