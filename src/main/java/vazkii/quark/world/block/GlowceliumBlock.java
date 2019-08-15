package vazkii.quark.world.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.ItemGroup;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.lighting.LightEngine;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.quark.base.block.QuarkBlock;
import vazkii.quark.base.module.Module;

public class GlowceliumBlock extends QuarkBlock {

	public GlowceliumBlock(Module module) {
		super("glowcelium", module, ItemGroup.BUILDING_BLOCKS, 
				Block.Properties.create(Material.ORGANIC, MaterialColor.LIGHT_BLUE)
				.tickRandomly()
				.hardnessAndResistance(0.2F)
				.lightValue(7)
				.sound(SoundType.PLANT));
	}

	@Override
	public void tick(BlockState state, World worldIn, BlockPos pos, Random random) {
		if(!worldIn.isRemote) {
			if(!canExist(state, worldIn, pos))
				worldIn.setBlockState(pos, Blocks.DIRT.getDefaultState());
			else for(int i = 0; i < 4; ++i) {
				BlockPos blockpos = pos.add(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
                if(worldIn.getBlockState(blockpos).getBlock() == Blocks.DIRT && canGrowTo(state, worldIn, blockpos)) 
					worldIn.setBlockState(blockpos, getDefaultState());
			}
		}
	}

	// Some vanilla copypasta from SpreadableSnowyDirtBlock
	
	private static boolean canExist(BlockState p_220257_0_, IWorldReader p_220257_1_, BlockPos p_220257_2_) {
		BlockPos blockpos = p_220257_2_.up();
		BlockState blockstate = p_220257_1_.getBlockState(blockpos);
		int i = LightEngine.func_215613_a(p_220257_1_, p_220257_0_, p_220257_2_, blockstate, blockpos, Direction.UP, blockstate.getOpacity(p_220257_1_, blockpos));
		return i < p_220257_1_.getMaxLightLevel();
	}

	private static boolean canGrowTo(BlockState p_220256_0_, IWorldReader p_220256_1_, BlockPos p_220256_2_) {
		BlockPos blockpos = p_220256_2_.up();
		return canExist(p_220256_0_, p_220256_1_, p_220256_2_) && !p_220256_1_.getFluidState(blockpos).isTagged(FluidTags.WATER);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	   public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		super.animateTick(stateIn, worldIn, pos, rand);

		if(rand.nextInt(40) == 0)
			worldIn.addParticle(ParticleTypes.END_ROD, pos.getX() + rand.nextFloat(), pos.getY() + 1.15F, pos.getZ() + rand.nextFloat(), 0, 0, 0);
	}

}
