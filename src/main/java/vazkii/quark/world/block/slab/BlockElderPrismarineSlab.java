package vazkii.quark.world.block.slab;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import vazkii.quark.base.block.BlockQuarkSlab;
import vazkii.quark.world.block.BlockElderPrismarine;

public class BlockElderPrismarineSlab extends BlockQuarkSlab {

	public BlockElderPrismarineSlab(BlockElderPrismarine.Variants variant, boolean doubleSlab) {
		super(variant.getName() + "_slab", Material.ROCK, doubleSlab);
		setHardness(1.5F);
		setResistance(10.0F);
		setSoundType(SoundType.STONE);
		setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
	}

}
