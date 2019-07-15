package vazkii.quark.world.world.tree;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.BlockVine;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

// mostly a copy of WorldGenSwamp but for our purposes
public class WorldGenSwampTree extends WorldGenAbstractTree {

	private static final IBlockState TRUNK = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.OAK);
	private final IBlockState leaf;
	private final boolean addVines;

	public WorldGenSwampTree(boolean addVines) {
		super(false);

		this.addVines = addVines;
		leaf = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK).withProperty(BlockOldLeaf.CHECK_DECAY, false); // TODO replace
	}

	public boolean generate(World worldIn, Random rand, BlockPos pos) {
		int i;
		for(i = rand.nextInt(4) + 5; worldIn.getBlockState(pos.down()).getMaterial() == Material.WATER; pos = pos.down());

		boolean flag = true;

		if(pos.getY() >= 1 && pos.getY() + i + 1 <= 256) {
			for(int j = pos.getY(); j <= pos.getY() + 1 + i; ++j) {
				int k = 1;

				if(j == pos.getY())
					k = 0;

				if(j >= pos.getY() + 1 + i - 2)
					k = 3;

				BlockPos.MutableBlockPos mpos = new BlockPos.MutableBlockPos();

				for(int l = pos.getX() - k; l <= pos.getX() + k && flag; ++l)
					for(int i1 = pos.getZ() - k; i1 <= pos.getZ() + k && flag; ++i1) {
						if(j >= 0 && j < 256) {
							IBlockState iblockstate = worldIn.getBlockState(mpos.setPos(l, j, i1));
							Block block = iblockstate.getBlock();

							if(!iblockstate.getBlock().isAir(iblockstate, worldIn, mpos.setPos(l, j, i1)) && !iblockstate.getBlock().isLeaves(iblockstate, worldIn, mpos.setPos(l, j, i1))) {
								if(block != Blocks.WATER && block != Blocks.FLOWING_WATER)
									flag = false;
								else if(j > pos.getY())
									flag = false;
							}
						}
						else flag = false;
					}
			}

			if(!flag)
				return false;
			else {
				BlockPos down = pos.down();
				IBlockState state = worldIn.getBlockState(down);
				boolean isSoil = state.getBlock().canSustainPlant(state, worldIn, down, EnumFacing.UP, ((BlockSapling) Blocks.SAPLING));

				if(isSoil && pos.getY() < worldIn.getHeight() - i - 1) {
					state.getBlock().onPlantGrow(state, worldIn, pos.down(),pos);

					for(int k1 = pos.getY() - 3 + i; k1 <= pos.getY() + i; ++k1) {
						int j2 = k1 - (pos.getY() + i);
						int l2 = 2 - j2 / 2;

						for(int j3 = pos.getX() - l2; j3 <= pos.getX() + l2; ++j3) {
							int k3 = j3 - pos.getX();

							for(int i4 = pos.getZ() - l2; i4 <= pos.getZ() + l2; ++i4) {
								int j1 = i4 - pos.getZ();

								if(Math.abs(k3) != l2 || Math.abs(j1) != l2 || rand.nextInt(2) != 0 && j2 != 0) {
									BlockPos blockpos = new BlockPos(j3, k1, i4);
									state = worldIn.getBlockState(blockpos);

									if(state.getBlock().canBeReplacedByLeaves(state, worldIn, blockpos))
										setBlockAndNotifyAdequately(worldIn, blockpos, leaf);
								}
							}
						}
					}

					for(int l1 = 0; l1 < i; ++l1) {
						BlockPos upN = pos.up(l1);
						IBlockState iblockstate1 = worldIn.getBlockState(upN);
						Block block2 = iblockstate1.getBlock();

						if(block2.isAir(iblockstate1, worldIn, upN) || block2.isLeaves(iblockstate1, worldIn, upN) || block2 == Blocks.FLOWING_WATER || block2 == Blocks.WATER)
							setBlockAndNotifyAdequately(worldIn, pos.up(l1), TRUNK);
					}

					if(addVines)
						for(int i2 = pos.getY() - 3 + i; i2 <= pos.getY() + i; ++i2) {
							int k2 = i2 - (pos.getY() + i);
							int i3 = 2 - k2 / 2;
							BlockPos.MutableBlockPos mpos = new BlockPos.MutableBlockPos();

							for(int l3 = pos.getX() - i3; l3 <= pos.getX() + i3; ++l3)
								for(int j4 = pos.getZ() - i3; j4 <= pos.getZ() + i3; ++j4) {
									mpos.setPos(l3, i2, j4);

									if(worldIn.getBlockState(mpos).getMaterial() == Material.LEAVES) {
										BlockPos blockpos3 = mpos.west();
										BlockPos blockpos4 = mpos.east();
										BlockPos blockpos1 = mpos.north();
										BlockPos blockpos2 = mpos.south();

										if(rand.nextInt(4) == 0 && worldIn.isAirBlock(blockpos3))
											addVine(worldIn, blockpos3, BlockVine.EAST);

										if(rand.nextInt(4) == 0 && worldIn.isAirBlock(blockpos4))
											addVine(worldIn, blockpos4, BlockVine.WEST);

										if(rand.nextInt(4) == 0 && worldIn.isAirBlock(blockpos1))
											addVine(worldIn, blockpos1, BlockVine.SOUTH);

										if(rand.nextInt(4) == 0 && worldIn.isAirBlock(blockpos2))
											addVine(worldIn, blockpos2, BlockVine.NORTH);
									}
								}
							}

					return true;
				}
				else return false;
			}
		}
		else return false;
	}

	private void addVine(World worldIn, BlockPos pos, PropertyBool prop) {
		IBlockState iblockstate = Blocks.VINE.getDefaultState().withProperty(prop, true);
		setBlockAndNotifyAdequately(worldIn, pos, iblockstate);
		int i = 4;

		for (BlockPos blockpos = pos.down(); worldIn.isAirBlock(blockpos) && i > 0; --i) {
			setBlockAndNotifyAdequately(worldIn, blockpos, iblockstate);
			blockpos = blockpos.down();
		}
	}

}
