package vazkii.quark.automation.feature;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.quark.base.module.Feature;
import vazkii.quark.base.module.ModuleLoader;

public class PistonsMoveTEs extends Feature {

	private static WeakHashMap<World, Map<BlockPos, TileEntity>> movements = new WeakHashMap();
	
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
		world.setBlockState(pos, state, flags);
		
		TileEntity tile = getAndClearMovement(world, pos);
		if(tile != null)
			world.setTileEntity(pos, tile);
		
		return false; // the value is popped, doesn't matter what we return
	}
	
	private static void registerMovement(World world, BlockPos pos, TileEntity tile) {
		if(!movements.containsKey(world))
			movements.put(world, new HashMap());
		
		movements.get(world).put(pos, tile);
	}
	
	private static TileEntity getAndClearMovement(World world, BlockPos pos) {
		if(!movements.containsKey(world))
			return null;
		
		Map<BlockPos, TileEntity> worldMovements = movements.get(world);
		if(!worldMovements.containsKey(pos))
			return null;
		
		TileEntity tile = worldMovements.get(pos);
		if(tile != null) {
			tile.setPos(pos);
			tile.validate();
		}
		
		worldMovements.remove(pos);
		return tile;
	}
	
}
