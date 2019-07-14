package vazkii.quark.world.block;

import java.util.Locale;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.IStringSerializable;
import vazkii.arl.block.BlockMetaVariants;
import vazkii.quark.base.block.IQuarkBlock;

public class BlockSkylather extends BlockMetaVariants<BlockSkylather.Variants> implements IQuarkBlock {

	public BlockSkylather() {
		super("skylather", Material.ROCK, Variants.class);
		setHardness(1.5F);
		setResistance(10.0F);
		setSoundType(SoundType.STONE);
		setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
	}

	public enum Variants implements IStringSerializable {
		SKYLATHER,
		SKYLATHER_BRICK;

		@Override
		public String getName() {
			return name().toLowerCase(Locale.ROOT);
		}
	}

}
