package vazkii.quark.building.block;

import java.util.function.Supplier;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import vazkii.arl.block.BlockMetaVariants;
import vazkii.arl.block.BlockMetaVariants.EnumBase;
import vazkii.quark.base.block.IQuarkBlock;
import vazkii.quark.base.module.Feature;
import vazkii.quark.base.module.ModuleLoader;
import vazkii.quark.building.block.BlockWorldStoneBricks.Variants;
import vazkii.quark.building.feature.WorldStoneBricks;
import vazkii.quark.world.feature.Basalt;
import vazkii.quark.world.feature.RevampStoneGen;

public class BlockPolishedNetherrack extends BlockMetaVariants implements IQuarkBlock {

	public BlockPolishedNetherrack() {
		super("polished_netherrack", Material.ROCK, Variants.class);
		setHardness(1.5F);
		setResistance(10.0F);
		setSoundType(SoundType.STONE);
		setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
	}

	public enum Variants implements EnumBase {
		
		POLISHED_NETHERRACK,
		POLISHED_NETHERRACK_BRICKS
		
	}

}
