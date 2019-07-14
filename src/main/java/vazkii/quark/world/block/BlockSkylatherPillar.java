package vazkii.quark.world.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import vazkii.quark.base.block.BlockQuarkPillar;

public class BlockSkylatherPillar extends BlockQuarkPillar {

	public BlockSkylatherPillar() {
		super("skylather_pillar", Material.ROCK);
		setHardness(1.5F);
		setResistance(10.0F);
		setSoundType(SoundType.STONE);
		setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
	}

}
