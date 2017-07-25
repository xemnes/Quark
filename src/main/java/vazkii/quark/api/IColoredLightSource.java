package vazkii.quark.api;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

// TODO document me
public interface IColoredLightSource {

	public float[] getColoredLight(IBlockAccess world, BlockPos pos);
	
}
