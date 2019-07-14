package vazkii.quark.world.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import vazkii.arl.block.BlockMod;
import vazkii.quark.base.block.IQuarkBlock;

public class BlockSkyfoam extends BlockMod implements IQuarkBlock {

	public BlockSkyfoam() {
		super("skyfoam", Material.SAND);
		setHardness(0.1F);
		setSoundType(SoundType.SAND);
		setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
	}
	
}
