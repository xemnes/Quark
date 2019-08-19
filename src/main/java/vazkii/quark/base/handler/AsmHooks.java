package vazkii.quark.base.handler;

import net.minecraft.block.BlockState;
import net.minecraft.block.state.PistonBlockStructureHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.PistonTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.quark.vanity.module.ColorRunesModule;

/**
 * @author WireSegal
 * Created at 10:10 AM on 8/15/19.
 */
@SuppressWarnings("unused")
public class AsmHooks {

	// ==========================================================================
	// Color Runes
	// ==========================================================================

    public static void setColorRuneTargetStack(ItemStack stack) {
        ColorRunesModule.setTargetStack(stack);
    }

    public static int changeColor(int color) {
        if (color == 0xFF8040CC)
            return ColorRunesModule.changeColor(color);

        return color;
    }

	// ==========================================================================
	// Piston Logic Replacing
	// ==========================================================================

	public static PistonBlockStructureHelper transformStructureHelper(PistonBlockStructureHelper helper, World world, BlockPos sourcePos, Direction facing, boolean extending) {
		return new QuarkPistonStructureHelper(helper, world, sourcePos, facing, extending);
	}

	public static boolean setPistonBlock(World world, BlockPos pos, BlockState blockState, int flags) {
        return world.setBlockState(pos, blockState, flags); // todo
    }

    public static void onPistonUpdate(PistonTileEntity piston) {
        // todo
    }

    @OnlyIn(Dist.CLIENT)
    public static boolean renderPistonBlock(PistonTileEntity piston, double x, double y, double z, float pTicks) {
        return false; // todo
    }

}
