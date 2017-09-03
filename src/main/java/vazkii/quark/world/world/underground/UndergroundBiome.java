package vazkii.quark.world.world.underground;

import java.util.List;
import java.util.Map;

import com.google.common.base.Predicate;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import vazkii.quark.base.module.ModuleLoader;
import vazkii.quark.world.feature.RevampStoneGen;

public abstract class UndergroundBiome {

	int dungeonChance, maxDungeons, minDungeons;
	
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

	public List<BlockPos> floorList, ceilingList, insideList;
	public Map<BlockPos, EnumFacing> wallMap;

	public void fill(World world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		if(state.getBlock().getBlockHardness(state, world, pos) == -1 || world.canBlockSeeSky(pos))
			return;

		if(isFloor(world, pos, state)) {
			floorList.add(pos);
			fillFloor(world, pos, state);
		} else if(isCeiling(world, pos, state)) {
			ceilingList.add(pos);
			fillCeiling(world, pos, state);
		} else if(isWall(world, pos, state)) {
			wallMap.put(pos, getBorderSide(world, pos));
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
	
	public final void setupBaseConfig(String category) {
		if(hasDungeon()) {
			int[] settings = getDefaultDungeonSettings();
			dungeonChance = ModuleLoader.config.getInt("Dungeon Chance", category, settings[0], 0, Integer.MAX_VALUE, "The chance that dungeons will spawn in this biome. 1 is 100%, 2 is 50%, the higher, the less dungeons will spawn.");
			maxDungeons = ModuleLoader.config.getInt("Max Dungeons", category, settings[1], 0, Integer.MAX_VALUE, "The max amount of dungeons that can spawn.");
			minDungeons = ModuleLoader.config.getInt("Min Dungeons", category, settings[2], 0, Integer.MAX_VALUE, "The minimum amount of dungeons that will spawn.");
		}
		
		setupConfig(category);
	}

	public void setupConfig(String category) {
		// NO-OP
	}
	
	public boolean isValidBiome(Biome biome) {
		return true;
	}
	
	public boolean hasDungeon() {
		return false;
	}
	
	public int getDungeonDistance() {
		return 0;
	}
	
	/**
	 * 0: Dungeon Chance
	 * 1: Max Dungeons
	 * 2: Min Dungeons 
	 */
	public int[] getDefaultDungeonSettings() {
		return new int[] { 2, 2, 0 };
	}
 
	public void spawnDungeon(WorldServer world, BlockPos pos, EnumFacing face) {
		// NO-OP
	}
	
	boolean isFloor(World world, BlockPos pos, IBlockState state) {
		if(!state.isFullBlock() || !state.isOpaqueCube())
			return false;

		BlockPos upPos = pos.up();
		return world.isAirBlock(upPos) || world.getBlockState(upPos).getBlock().isReplaceable(world, upPos);
	}

	boolean isCeiling(World world, BlockPos pos, IBlockState state) {
		if(!state.isFullBlock() || !state.isOpaqueCube())
			return false;

		BlockPos downPos = pos.down();
		return world.isAirBlock(downPos) || world.getBlockState(downPos).getBlock().isReplaceable(world, downPos);
	}

	boolean isWall(World world, BlockPos pos, IBlockState state) {
		if(!state.isFullBlock() || !state.isOpaqueCube() || !STONE_PREDICATE.apply(state))
			return false;

		return isBorder(world, pos, state);
	}
	
	EnumFacing getBorderSide(World world, BlockPos pos) {
		for(EnumFacing facing : EnumFacing.HORIZONTALS) {
			BlockPos offsetPos = pos.offset(facing);
			IBlockState stateAt = world.getBlockState(offsetPos);
			
			if(world.isAirBlock(offsetPos) || stateAt.getBlock().isReplaceable(world, offsetPos))
				return facing;
		}

		return null;
	}
	
	boolean isBorder(World world, BlockPos pos, IBlockState state) {
		return getBorderSide(world, pos) != null;
	}
	
	boolean isInside(World world, BlockPos pos, IBlockState state) {
		return STONE_PREDICATE.apply(state);
	}
	
	public static Rotation rotationFromFacing(EnumFacing facing) {
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
