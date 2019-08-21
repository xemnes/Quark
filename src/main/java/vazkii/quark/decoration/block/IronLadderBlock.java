package vazkii.quark.decoration.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import vazkii.quark.base.module.Module;
import vazkii.quark.decoration.module.VariantLaddersModule;

public class IronLadderBlock extends VariantLadderBlock {

	private static final SoundType SOUND_TYPE = new SoundType(1.0F, 1.0F, SoundEvents.BLOCK_METAL_BREAK, SoundEvents.BLOCK_LADDER_STEP, SoundEvents.BLOCK_METAL_PLACE, SoundEvents.BLOCK_METAL_HIT, SoundEvents.BLOCK_LADDER_FALL);
	
	public IronLadderBlock(Module module) {
		super("iron", module, Block.Properties.create(Material.MISCELLANEOUS)
				.hardnessAndResistance(0.8F)
				.sound(SOUND_TYPE));
	}
	
	@Override
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		Direction facing = state.get(FACING);
		boolean solid = facing.getAxis() != Axis.Y && worldIn.getBlockState(pos.offset(facing.getOpposite())).func_224755_d(worldIn, pos.offset(facing.getOpposite()), facing);
		BlockState topState = worldIn.getBlockState(pos.up());
		return solid || (topState.getBlock() == this && (facing.getAxis() == Axis.Y || topState.get(FACING) == facing));
	}
	
	@Override
	public boolean isEnabled() {
		return super.isEnabled() && VariantLaddersModule.enableIronLadder;
	}

}
