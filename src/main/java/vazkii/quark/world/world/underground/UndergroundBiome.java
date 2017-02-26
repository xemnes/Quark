package vazkii.quark.world.world.underground;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class UndergroundBiome {
	
	List<BlockPos> floorList, ceilingList, wallList;
	
	public void apply(World world, BlockPos center, int radiusX, int radiusY, int radiusZ) {
		int centerX = center.getX();
		int centerY = center.getY();
		int centerZ = center.getZ();
		
		double radiusX2 = radiusX * radiusX;
		double radiusY2 = radiusY * radiusY;
		double radiusZ2 = radiusZ * radiusZ;
		
		floorList = new ArrayList();
		ceilingList = new ArrayList();
		wallList = new ArrayList();
		
		for(int x = -radiusX; x < radiusX + 1; x++)
			for(int y = -radiusY; y < radiusY + 1; y++)
				for(int z = -radiusZ; z < radiusZ + 1; z++) {
					double distX = x * x;
					double distY = y * y;
					double distZ = z * z;
					boolean inside = distX / radiusX2 + distY / radiusY2 + distZ / radiusZ2 <= 1;
					
					if(inside)
						fill(world, center.add(x, y, z));
				}
		
		
		floorList.forEach(pos -> finalFloorPass(world, pos));
		ceilingList.forEach(pos -> finalCeilingPass(world, pos));
		wallList.forEach(pos -> finalWallPass(world, pos));
	}
	
	public void fill(World world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		if(state.getBlock().getBlockHardness(state, world, pos) == -1)
			return;
		
		if(isFloor(world, pos, state)) {
			floorList.add(pos);
			fillFloor(world, pos, state);
		} else if(isCeiling(world, pos, state)) {
			ceilingList.add(pos);
			fillCeiling(world, pos, state);
		} else if(isWall(world, pos, state)) {
			wallList.add(pos);
			fillWall(world, pos, state);
		}
	}
	
	public abstract void fillFloor(World world, BlockPos pos, IBlockState state);
	public abstract void fillCeiling(World world, BlockPos pos, IBlockState state);
	public abstract void fillWall(World world, BlockPos pos, IBlockState state);
	
	public void finalFloorPass(World world, BlockPos pos) {}
	public void finalCeilingPass(World world, BlockPos pos) {}
	public void finalWallPass(World world, BlockPos pos) {}

	boolean isFloor(World world, BlockPos pos, IBlockState state) {
		if(!state.isFullBlock())
			return false;
		
		return world.isAirBlock(pos.up());
	}
	
	boolean isCeiling(World world, BlockPos pos, IBlockState state) {
		if(!state.isFullBlock())
			return false;
		
		return world.isAirBlock(pos.down());
	}
	
	boolean isWall(World world, BlockPos pos, IBlockState state) {
		if(!state.isFullBlock())
			return false;
		
		for(EnumFacing facing : EnumFacing.HORIZONTALS)
			if(world.isAirBlock(pos.offset(facing)))
				return true;
		
		return false;
	}
	
}
