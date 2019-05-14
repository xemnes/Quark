package vazkii.quark.automation.feature;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.BlockPistonStructureHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import vazkii.arl.recipe.RecipeHandler;
import vazkii.arl.util.ProxyRegistry;
import vazkii.quark.automation.block.BlockIronRod;
import vazkii.quark.base.module.Feature;
import vazkii.quark.base.module.ModuleLoader;

import java.util.ArrayList;
import java.util.List;

public class PistonSpikes extends Feature {

	public static Block iron_rod;

	public static boolean ezRecipe;
	
	@Override
	public void setupConfig() {
		ezRecipe = loadPropBool("Enable Easy Recipe", "Replace the End Rod in the recipe with an Iron Ingot", false);
	}
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		iron_rod = new BlockIronRod();
		
		RecipeHandler.addOreDictRecipe(ProxyRegistry.newStack(iron_rod), 
				"I", "I", "R",
				'I', "ingotIron",
				'R', (ezRecipe ? "ingotIron" : Blocks.END_ROD));
	}
	
	public static void breakStuffWithSpikes(World world, BlockPos sourcePos, BlockPistonStructureHelper helper, EnumFacing facing, boolean extending) {
		if(!extending || !ModuleLoader.isFeatureEnabled(PistonSpikes.class))
			return;
		
		List<BlockPos> moveList = helper.getBlocksToMove();
		List<BlockPos> destroyList = helper.getBlocksToDestroy();
		
		boolean did = false;
		List<BlockPos> newMoveList = new ArrayList<>(moveList);
		List<BlockPos> newDestroyList = new ArrayList<>(destroyList);
		EnumFacing oppositeFacing = facing.getOpposite();
		
		for(BlockPos pos : moveList) {
			IBlockState state = world.getBlockState(pos);
			if(state.getBlock() == iron_rod && state.getValue(BlockDirectional.FACING) == facing) {
				BlockPos off = pos.offset(oppositeFacing);

				if(!off.equals(sourcePos))
					continue;
				
				for(int i = 0; i < 14; i++) {
					IBlockState stateAt = world.getBlockState(off);
					Block blockAt = stateAt.getBlock();
					if(blockAt.isAir(stateAt, world, off))
						break;
					
					if(blockAt == Blocks.SLIME_BLOCK)
						return;
					
					if(i >= 2) {
						if(i == 2) {
							newDestroyList.add(off);
							newMoveList.remove(off);
						} else {
							if(newMoveList.contains(off))
								newMoveList.remove(off);
							else newDestroyList.remove(off);
						}
					}

					off = off.offset(facing);
				}
				
				did = true;
			}
		}
		
		if(did) {
			moveList.clear();
			moveList.addAll(newMoveList);
			destroyList.clear();
			destroyList.addAll(newDestroyList);
		}
	}
	
	@Override
	public boolean requiresMinecraftRestartToEnable() {
		return true;
	}
	
}
