//package vazkii.quark.world.block;
//
//import java.util.Random;
//
//import javax.annotation.Nonnull;
//
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.Blocks;
//import net.minecraft.block.HugeMushroomBlock;
//import net.minecraft.item.ItemGroup;
//import net.minecraft.item.ItemStack;
//import net.minecraft.particles.ParticleTypes;
//import net.minecraft.util.BlockRenderLayer;
//import net.minecraft.util.Direction;
//import net.minecraft.util.NonNullList;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.IBlockReader;
//import net.minecraft.world.World;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import vazkii.arl.util.RegistryHelper;
//import vazkii.quark.base.module.Module;
//import vazkii.quark.world.module.underground.GlowshroomUndergroundBiomeModule;
//
//public class HugeGlowshroomBlock extends HugeMushroomBlock {
//
//	private final Module module;
//
//	public HugeGlowshroomBlock(Module module) {
//		super(Block.Properties.from(Blocks.RED_MUSHROOM_BLOCK).lightValue(14).tickRandomly());
//
//		this.module = module;
//		RegistryHelper.registerBlock(this, "glowshroom");
//		RegistryHelper.setCreativeTab(this, ItemGroup.DECORATIONS);
//	}
//
//	@Override
//	@OnlyIn(Dist.CLIENT)
//	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
//		super.animateTick(stateIn, worldIn, pos, rand);
//
//		if(rand.nextInt(10) == 0)
//			worldIn.addParticle(ParticleTypes.END_ROD, pos.getX() + rand.nextFloat(), pos.getY() + rand.nextFloat(), pos.getZ() + rand.nextFloat(), 0, 0, 0);
//	}
//
//	@Nonnull
//	@Override
//	@OnlyIn(Dist.CLIENT)
//	public BlockRenderLayer getRenderLayer() {
//		return BlockRenderLayer.TRANSLUCENT;
//	}
//
//	@Override
//	@OnlyIn(Dist.CLIENT)
//	@SuppressWarnings("deprecation")
//	public boolean isSideInvisible(BlockState state, BlockState adjacentBlockState, Direction side) {
//		return adjacentBlockState.getBlock() == this ? true : super.isSideInvisible(state, adjacentBlockState, side);
//	}
//	
//	@Override
//	public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos) {
//		return false;
//	}
//
//	public static boolean setInPosition(World worldIn, Random rand, BlockPos position, boolean update) {
//		Block block = GlowshroomUndergroundBiomeModule.glowshroom;
//
//		int i = rand.nextInt(3) + 4;
//
//		if(rand.nextInt(12) == 0)
//			i *= 2;
//
//		boolean canPlace = true;
//		int flags = update ? 3 : 0;
//
//		if(position.getY() >= 1 && position.getY() + i + 1 < 256) {
//			for(int j = position.getY(); j <= position.getY() + 1 + i; ++j) {
//				int k = 3;
//
//				if(j <= position.getY() + 3)
//					k = 0;
//
//				BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
//
//				for(int l = position.getX() - k; l <= position.getX() + k && canPlace; ++l)
//					for(int i1 = position.getZ() - k; i1 <= position.getZ() + k && canPlace; ++i1) {
//						if(j >= 0 && j < 256) {
//							BlockState state = worldIn.getBlockState(pos.setPos(l, j, i1));
//
//							if (!state.getBlock().isAir(state, worldIn, pos) && !state.getBlock().isLeaves(state, worldIn, pos)) 
//								canPlace = false;
//						}
//						else
//							canPlace = false;
//					}
//			}
//
//			if (!canPlace)
//				return false;
//			else {
//				Block block1 = worldIn.getBlockState(position.down()).getBlock();
//
//				if (block1 != GlowshroomUndergroundBiomeModule.glowcelium)
//					return false;
//				else {
//					int k2 = position.getY() + i - 3;
//
//					for(int l2 = k2; l2 <= position.getY() + i; ++l2) {
//						int j3 = 1;
//
//						if(l2 < position.getY() + i)
//							++j3;
//
//						int k3 = position.getX() - j3;
//						int l3 = position.getX() + j3;
//						int j1 = position.getZ() - j3;
//						int k1 = position.getZ() + j3;
//
//						for(int l1 = k3; l1 <= l3; ++l1) {
//							for(int i2 = j1; i2 <= k1; ++i2) {
//								int j2 = 5;
//
//								if (l1 == k3)
//									--j2;
//								else if (l1 == l3)
//									++j2;
//
//								if (i2 == j1)
//									j2 -= 3;
//								else if (i2 == k1)
//									j2 += 3;
//
//								HugeMushroomBlock.EnumType type = HugeMushroomBlock.EnumType.byMetadata(j2);
//
//								if(type == HugeMushroomBlock.EnumType.CENTER && l2 < position.getY() + i)
//									type = HugeMushroomBlock.EnumType.ALL_INSIDE;
//
//								if (position.getY() >= position.getY() + i - 1 || type != HugeMushroomBlock.EnumType.ALL_INSIDE) {
//									BlockPos blockpos = new BlockPos(l1, l2, i2);
//									BlockState state = worldIn.getBlockState(blockpos);
//
//									if (state.getBlock().canBeReplacedByLeaves(state, worldIn, blockpos))
//										worldIn.setBlockState(blockpos, block.getDefaultState().withProperty(HugeMushroomBlock.VARIANT, type), flags);
//								}
//							}
//						}
//					}
//
//					for (int i3 = 0; i3 < i; ++i3) {
//						BlockState iblockstate = worldIn.getBlockState(position.up(i3));
//
//						if(iblockstate.getBlock().canBeReplacedByLeaves(iblockstate, worldIn, position.up(i3)))
//							worldIn.setBlockState(position.up(i3), block.getDefaultState().withProperty(HugeMushroomBlock.VARIANT, HugeMushroomBlock.EnumType.STEM), flags);
//					}
//
//					return true;
//				}
//			}
//		}
//
//		return false;
//	}
//
//	@Override
//	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
//		if(isEnabled() || group == ItemGroup.SEARCH)
//			super.fillItemGroup(group, items);
//	}
//
//	public boolean isEnabled() {
//		return module != null && module.enabled;
//	}
//
//}
