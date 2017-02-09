package vazkii.quark.building.block.slab;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import vazkii.quark.base.block.BlockQuarkSlab;

public class BlockPolishedNetherrackBricksSlab extends BlockQuarkSlab {

	public BlockPolishedNetherrackBricksSlab(boolean doubleSlab) {
		super("polished_netherrack_bricks_slab", Material.ROCK, doubleSlab);
		setHardness(1.5F);
		setResistance(10.0F);
		setSoundType(SoundType.STONE);
		setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
	}

}
