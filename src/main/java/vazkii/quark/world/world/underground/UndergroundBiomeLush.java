package vazkii.quark.world.world.underground;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenShrub;

public class UndergroundBiomeLush extends BasicUndergroundBiome {

	public UndergroundBiomeLush() {
		super(Blocks.GRASS.getDefaultState(), null, null);
	}
	
	@Override
	public void finalFloorPass(World world, BlockPos pos) {
		if(world.rand.nextInt(20) == 0)
			ItemDye.applyBonemeal(new ItemStack(Items.DYE, 1, 14), world, pos);
		
		if(world.rand.nextInt(100) == 0)
			new WorldGenShrub(Blocks.LOG.getDefaultState(), Blocks.LEAVES.getDefaultState()).generate(world, world.rand, pos.up());
	}

}
