package vazkii.quark.world.gen.underground;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import vazkii.quark.base.handler.MiscUtil;
import vazkii.quark.base.world.generator.OreGenerator;
import vazkii.quark.world.gen.UndergroundBiomeGenerator.UndergroundBiomeGenerationContext;

public abstract class UndergroundBiome {

	public double dungeonChance;

	public final void fill(UndergroundBiomeGenerationContext context, BlockPos pos) {
		IWorld world = context.world;
		BlockState state = world.getBlockState(pos);
		if(state.getBlockHardness(world, pos) == -1 || world.canBlockSeeSky(pos))
			return;

		if(isFloor(world, pos, state)) {
			context.floorList.add(pos);
			fillFloor(context, pos, state);
		} else if(isCeiling(world, pos, state)) {
			context.ceilingList.add(pos);
			fillCeiling(context, pos, state);
		} else if(isWall(world, pos, state)) {
			context.wallMap.put(pos, getBorderSide(world, pos));
			fillWall(context, pos, state);
		} else if(isInside(state)) {
			context.insideList.add(pos);
			fillInside(context, pos, state);
		}
	}

	public abstract void fillFloor(UndergroundBiomeGenerationContext context, BlockPos pos, BlockState state);
	public abstract void fillCeiling(UndergroundBiomeGenerationContext context, BlockPos pos, BlockState state);
	public abstract void fillWall(UndergroundBiomeGenerationContext context, BlockPos pos, BlockState state);
	public abstract void fillInside(UndergroundBiomeGenerationContext context, BlockPos pos, BlockState state);
	
	public void finalFloorPass(UndergroundBiomeGenerationContext context, BlockPos pos) {
		// NO-OP
	}

	public void finalCeilingPass(UndergroundBiomeGenerationContext context, BlockPos pos) {
		// NO-OP
	}

	public void finalWallPass(UndergroundBiomeGenerationContext context, BlockPos pos) {
		// NO-OP
	}

	public void finalInsidePass(UndergroundBiomeGenerationContext context, BlockPos pos) {
		// NO-OP
	}

	public boolean isFloor(IWorld world, BlockPos pos, BlockState state) {
		if(!state.isOpaqueCube(world, pos))
			return false;

		BlockPos upPos = pos.up();
		return world.isAirBlock(upPos) || world.getBlockState(upPos).getMaterial().isReplaceable();
	}

	public boolean isCeiling(IWorld world, BlockPos pos, BlockState state) {
		if(!state.isOpaqueCube(world, pos))
			return false;

		BlockPos downPos = pos.down();
		return world.isAirBlock(downPos) || world.getBlockState(downPos).getMaterial().isReplaceable();
	}

	public boolean isWall(IWorld world, BlockPos pos, BlockState state) {
		if( !state.isOpaqueCube(world, pos) || !OreGenerator.STONE_MATCHER.test(state))
			return false;

		return isBorder(world, pos);
	}

	public Direction getBorderSide(IWorld world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		for(Direction facing : MiscUtil.HORIZONTALS) {
			BlockPos offsetPos = pos.offset(facing);
			BlockState stateAt = world.getBlockState(offsetPos);
			
			if(state != stateAt && world.isAirBlock(offsetPos) || stateAt.getMaterial().isReplaceable())
				return facing;
		}

		return null;
	}
	
	public boolean isBorder(IWorld world, BlockPos pos) {
		return getBorderSide(world, pos) != null;
	}

	public boolean isInside(BlockState state) {
		return OreGenerator.STONE_MATCHER.test(state);
	}
	
	public static Rotation rotationFromFacing(Direction facing) {
		switch(facing) {
		case SOUTH:
			return Rotation.CLOCKWISE_180;
		case WEST:
			return Rotation.COUNTERCLOCKWISE_90;
		case EAST:
			return Rotation.CLOCKWISE_90;
		default:
			return Rotation.NONE;
		}
	}

}
