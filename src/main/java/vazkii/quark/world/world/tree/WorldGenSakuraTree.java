package vazkii.quark.world.world.tree;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import vazkii.quark.world.block.BlockVariantLeaves;
import vazkii.quark.world.feature.OakVariants;

public class WorldGenSakuraTree extends WorldGenBigTree {

	private final IBlockState leaf;
	
	public WorldGenSakuraTree(boolean notify) {
		super(notify);
		
		leaf = OakVariants.variant_leaves.getDefaultState().withProperty(BlockVariantLeaves.VARIANT, BlockVariantLeaves.Variant.SAKURA_LEAVES).withProperty(BlockLeaves.CHECK_DECAY, false);
	}

	@Override
	protected void setBlockAndNotifyAdequately(World worldIn, BlockPos pos, IBlockState state) {
		if(state.getBlock() == Blocks.LEAVES)
			state = leaf;
		
		super.setBlockAndNotifyAdequately(worldIn, pos, state);
	}
	
}
