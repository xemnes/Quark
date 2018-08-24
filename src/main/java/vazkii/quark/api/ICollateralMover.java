package vazkii.quark.api;

import java.util.Collection;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface ICollateralMover {

	public boolean addCollateralMovement(IBlockAccess world, BlockPos pos, Collection<BlockPos> positions, EnumFacing facing);
	
}
