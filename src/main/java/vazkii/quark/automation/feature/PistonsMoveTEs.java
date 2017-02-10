package vazkii.quark.automation.feature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import scala.actors.threadpool.Arrays;
import vazkii.quark.base.module.Feature;
import vazkii.quark.base.module.ModuleLoader;

public class PistonsMoveTEs extends Feature {

	private static WeakHashMap<World, Map<BlockPos, TileEntity>> movements = new WeakHashMap();
	
	public static List<String> blacklist;
	
	@Override
	public void setupConfig() {
		String[] blacklistArray = loadPropStringList("Tile Entity Render Blacklist", "Some mod blocks with complex renders will break everything if moved. Add them here if you find any.", 
				new String[] { "psi:programmer" });
		
		blacklist = new ArrayList(Arrays.asList(blacklistArray));
	}
	
	// This is called from injected code and subsequently flipped, so to make it move, we return false
	public static boolean shouldMoveTE(boolean te) {
		if(!ModuleLoader.isFeatureEnabled(PistonsMoveTEs.class))
			return te;
		
		return false;
	}
	
	public static void detachTileEntities(World world, BlockPos sourcePos, List<BlockPos> moveList, List<BlockPos> destroyList, EnumFacing facing, boolean extending) {
		if(!ModuleLoader.isFeatureEnabled(PistonsMoveTEs.class))
			return;
		
		for(BlockPos pos : moveList) {
			IBlockState state = world.getBlockState(pos);
			if(state.getBlock().hasTileEntity(state)) {
				TileEntity tile = world.getTileEntity(pos);
				world.removeTileEntity(pos);
				
				registerMovement(world, pos.offset(facing), tile);
			}
		}
	}
	
	public static boolean setPistonBlock(World world, BlockPos pos, IBlockState state, int flags) {
		if(!ModuleLoader.isFeatureEnabled(PistonsMoveTEs.class)) {
			world.setBlockState(pos, state, flags);
			return false;
		}
		
		Block block = state.getBlock();
		TileEntity tile = getAndClearMovement(world, pos);
		boolean destroyed = false;
		
		if(tile != null) {
			IBlockState currState = world.getBlockState(pos);
			TileEntity currTile = world.getTileEntity(pos);
			
			world.setBlockToAir(pos);
			if(!block.canPlaceBlockAt(world, pos)) {
				world.setBlockState(pos, state, flags);
				world.setTileEntity(pos, tile);
				block.dropBlockAsItem(world, pos, state, 0);
				world.setBlockToAir(pos);
				destroyed = true;
			}
			
			if(!destroyed) {
				world.setBlockState(pos, currState);
				world.setTileEntity(pos, currTile);
			}
		}
		
		if(!destroyed) {
			world.setBlockState(pos, state, flags);
			if(tile != null) {
				world.setTileEntity(pos, tile);
				tile.updateContainingBlockInfo();
			}
		}
		
		return false; // the value is popped, doesn't matter what we return
	}
	
	private static void registerMovement(World world, BlockPos pos, TileEntity tile) {
		if(!movements.containsKey(world))
			movements.put(world, new HashMap());
		
		movements.get(world).put(pos, tile);
	}
	
	public static TileEntity getMovement(World world, BlockPos pos) {
		return getMovement(world, pos, false);
	}
	
	private static TileEntity getMovement(World world, BlockPos pos, boolean remove) {
		if(!movements.containsKey(world))
			return null;
		
		Map<BlockPos, TileEntity> worldMovements = movements.get(world);
		if(!worldMovements.containsKey(pos))
			return null;
		
		TileEntity ret = worldMovements.get(pos);
		if(remove)
			worldMovements.remove(pos);
		
		return ret; 
	}
	
	private static TileEntity getAndClearMovement(World world, BlockPos pos) {
		TileEntity tile = getMovement(world, pos, true);
		if(tile != null)
			tile.validate();
		
		return tile;
	}
	
}
