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
import vazkii.arl.block.BasicBlock;
import vazkii.arl.util.RegistryHelper;
import vazkii.quark.decoration.feature.CharcoalBlockFeature;

public class CharcoalBlock extends BasicBlock {

	public CharcoalBlock() {
		super("charcoal_block", 
				Block.Properties.create(Material.ROCK, MaterialColor.BLACK)
				.hardnessAndResistance(5F, 10F)
				.sound(SoundType.STONE));
		RegistryHelper.setCreativeTab(this, ItemGroup.BUILDING_BLOCKS);
	}

	@Override
	public boolean isFireSource(BlockState state, IBlockReader world, BlockPos pos, Direction side) {
		return side == Direction.UP && CharcoalBlockFeature.burnsForever;
	}
	
}
