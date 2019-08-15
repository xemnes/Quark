package vazkii.quark.decoration.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import vazkii.quark.base.block.QuarkBlock;
import vazkii.quark.base.module.Module;
import vazkii.quark.decoration.module.CompressedBlocksModule;

public class CharcoalBlock extends QuarkBlock {

	public CharcoalBlock(Module module) {
		super("charcoal_block", module, ItemGroup.BUILDING_BLOCKS,
				Block.Properties.create(Material.ROCK, MaterialColor.BLACK)
				.hardnessAndResistance(5F, 10F)
				.sound(SoundType.STONE));
	}

	@Override
	public boolean isFireSource(BlockState state, IBlockReader world, BlockPos pos, Direction side) {
		return side == Direction.UP && CompressedBlocksModule.burnsForever;
	}
	
}
