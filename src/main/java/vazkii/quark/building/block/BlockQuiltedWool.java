package vazkii.quark.building.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import vazkii.arl.block.BlockMetaVariants;
import vazkii.quark.api.IColoredLightSource;
import vazkii.quark.base.block.IQuarkBlock;
import vazkii.quark.base.module.ModuleLoader;
import vazkii.quark.experimental.features.ColoredLights;

public class BlockQuiltedWool extends BlockMetaVariants implements IQuarkBlock, IColoredLightSource {

	public BlockQuiltedWool() {
		super("quilted_wool", Material.CLOTH, Variants.class);
		setHardness(0.8F);
		setSoundType(SoundType.CLOTH);
		setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		
		if(ModuleLoader.isFeatureEnabled(ColoredLights.class))
			setLightLevel(1F);
	}
	
	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		int val = super.getLightValue(state, world, pos);
		ColoredLights.addLightSource(world, pos, state, val);
		return val;
	}
	
	@Override
	public float[] getColoredLight(IBlockAccess access, BlockPos pos) {
		float r = 1F;
		float g = 1F;
		float b = 1F;
		
		IBlockState state = access.getBlockState(pos);
		if(state.getBlock() == this) {
			Variants variant = (Variants) state.getValue(getVariantProp());
			
			// TODO do the thing for all variants in a non shit way
			switch(variant) {
			case WOOL_QUILTED_RED:
				g = 0;
				b = 0;
				break;
			case WOOL_QUILTED_LIGHT_BLUE:
				r = 0;
				g = 0; 
				break;
			case WOOL_QUILTED_LIME:
				r = 0;
				b = 0;
				break;
			case WOOL_QUILTED_YELLOW:
				b = 0;
				break;
			case WOOL_QUILTED_PURPLE:
				g = 0;
				break;
			case WOOL_QUILTED_CYAN:
				r = 0;
				break;
			default:;
			}
		}
		
		return new float[] { r, g, b };
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
