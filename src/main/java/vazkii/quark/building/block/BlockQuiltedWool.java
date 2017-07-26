package vazkii.quark.building.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import vazkii.arl.block.BlockMetaVariants;
import vazkii.quark.base.block.IQuarkBlock;
import vazkii.quark.experimental.features.ColoredLights;

public class BlockQuiltedWool extends BlockMetaVariants implements IQuarkBlock {

	public BlockQuiltedWool() {
		super("quilted_wool", Material.CLOTH, Variants.class);
		setHardness(0.8F);
		setSoundType(SoundType.CLOTH);
		setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
	}
	
	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		int val = super.getLightValue(state, world, pos);
		ColoredLights.addLightSource(world, pos, state, val);
		return val;
	}
	
	public static enum Variants implements EnumBase {
		WOOL_QUILTED_WHITE,
		WOOL_QUILTED_ORANGE,
		WOOL_QUILTED_MAGENTA,
		WOOL_QUILTED_LIGHT_BLUE,
		WOOL_QUILTED_YELLOW,
		WOOL_QUILTED_LIME,
		WOOL_QUILTED_PINK,
		WOOL_QUILTED_GRAY,
		WOOL_QUILTED_SILVER,
		WOOL_QUILTED_CYAN,
		WOOL_QUILTED_PURPLE,
		WOOL_QUILTED_BLUE,
		WOOL_QUILTED_BROWN,
		WOOL_QUILTED_GREEN,
		WOOL_QUILTED_RED,
		WOOL_QUILTED_BLACK
	}

}
