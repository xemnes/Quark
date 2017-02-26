package vazkii.quark.world.world.underground;

import java.util.ArrayList;
import java.util.List;

import javax.jws.soap.SOAPBinding;

import com.google.common.base.Predicate;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.quark.world.feature.RevampStoneGen;

public abstract class UndergroundBiome {

	public static final Predicate<IBlockState> STONE_PREDICATE = state -> {
		if(state != null) {
			Block block = state.getBlock();
			if(block == Blocks.STONE) {
				BlockStone.EnumType blockstone$enumtype = (BlockStone.EnumType) state.getValue(BlockStone.VARIANT);
				return blockstone$enumtype.isNatural();
			}
			
			return block == RevampStoneGen.limestone || block == RevampStoneGen.marble;
		}

		return false;
	};

	List<BlockPos> floorList, ceilingList, wallList, insideList;

	public boolean apply(World world, BlockPos center, int radiusX, int radiusY, int radiusZ) {
		int centerX = center.getX();
		int centerY = center.getY();
		int centerZ = center.getZ();

		double radiusX2 = radiusX * radiusX;
		double radiusY2 = radiusY * radiusY;
		double radiusZ2 = radiusZ * radiusZ;

		floorList = new ArrayList();
		ceilingList = new ArrayList();
		wallList = new ArrayList();
		insideList = new ArrayList();

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
		insideList.forEach(pos -> finalInsidePass(world, pos));
		
		return true;
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
		} else if(isInside(world, pos, state)) {
			insideList.add(pos);
			fillInside(world, pos, state);
		}
	}

	public abstract void fillFloor(World world, BlockPos pos, IBlockState state);
	public abstract void fillCeiling(World world, BlockPos pos, IBlockState state);
	public abstract void fillWall(World world, BlockPos pos, IBlockState state);
	public abstract void fillInside(World world, BlockPos pos, IBlockState state);
	
	public void finalFloorPass(World world, BlockPos pos) {
		// NO-OP
	}

	public void finalCeilingPass(World world, BlockPos pos) {
		// NO-OP
	}

	public void finalWallPass(World world, BlockPos pos) {
		// NO-OP
	}
	
	public void finalInsidePass(World world, BlockPos pos) {
		// NO-OP
	}

	public void setupConfig(String category) {
		// NO-OP
	}

	boolean isFloor(World world, BlockPos pos, IBlockState state) {
		if(!state.isFullBlock())
			return false;

		BlockPos upPos = pos.up();
		return world.isAirBlock(upPos) || world.getBlockState(upPos).getBlock().isReplaceable(world, upPos);
	}

	boolean isCeiling(World world, BlockPos pos, IBlockState state) {
		if(!state.isFullBlock())
			return false;

		BlockPos downPos = pos.down();
		return world.isAirBlock(downPos) || world.getBlockState(downPos).getBlock().isReplaceable(world, downPos);
	}

	boolean isWall(World world, BlockPos pos, IBlockState state) {
		if(!state.isFullBlock() || !STONE_PREDICATE.apply(state))
			return false;

		return isBorder(world, pos, state);
	}
	
	boolean isBorder(World world, BlockPos pos, IBlockState state) {
		for(EnumFacing facing : EnumFacing.HORIZONTALS) {
			BlockPos offsetPos = pos.offset(facing);
			if(world.isAirBlock(offsetPos) || state.getBlock().isReplaceable(world, offsetPos))
				return true;
		}

		return false;
	}
	
	boolean isInside(World world, BlockPos pos, IBlockState state) {
		return STONE_PREDICATE.apply(state);
	}

}
