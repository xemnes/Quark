package vazkii.quark.world.block;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import vazkii.quark.world.feature.CaveRoots;

public class BlockRootsFlower extends BlockRoots {

	private int meta;
	
	public BlockRootsFlower(String name, int meta) {	
		super(name);
		this.meta = meta;
	}
	
	@Override
	protected ItemStack getRootDrop() {
		return new ItemStack(CaveRoots.root_flower, 1, meta);
	}
	
	@Override
	protected float getDropChance() {
		return CaveRoots.rootFlowerDropChance;
	}
	
}
