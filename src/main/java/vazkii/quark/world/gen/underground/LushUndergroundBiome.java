package vazkii.quark.world.gen.underground;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IGrowable;
import net.minecraft.block.VineBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import vazkii.quark.base.handler.MiscUtil;
import vazkii.quark.world.gen.UndergroundBiomeGenerator.UndergroundBiomeGenerationContext;

public class LushUndergroundBiome extends BasicUndergroundBiome {

	public LushUndergroundBiome() {
		super(Blocks.GRASS_BLOCK.getDefaultState(), null, null);
	}

	@Override
	public void finalFloorPass(UndergroundBiomeGenerationContext context, BlockPos pos) {
		if(context.random.nextDouble() < 0.05) {
			BlockState stateAt = context.world.getBlockState(pos);
			if(stateAt.getBlock() instanceof IGrowable)
				((IGrowable) stateAt.getBlock()).grow(context.world.getWorld(), context.random, pos, stateAt);
		}
	}

	@Override
	public void finalWallPass(UndergroundBiomeGenerationContext context, BlockPos pos) {
		IWorld world = context.world;
		for(Direction facing : MiscUtil.HORIZONTALS) {
			BlockPos off = pos.offset(facing);
			BlockPos up = off.up();
			if(isCeiling(world, up, world.getBlockState(up)) && context.random.nextDouble() < 0.125) {
				BlockState stateAt = world.getBlockState(off); 
				boolean did = false;
				while(stateAt.getBlock().isAir(stateAt, world, off) && off.getY() > 0) {
					world.setBlockState(off, Blocks.VINE.getDefaultState().with(VineBlock.getPropertyFor(facing.getOpposite()), true), 2);
					off = off.down();
					stateAt = world.getBlockState(off);
					did = true;
				}

				if(did)
					return;
			}
		}
	}



}
