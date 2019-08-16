package vazkii.quark.base.handler;

import net.minecraft.block.state.PistonBlockStructureHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
        //TODO
    }

    public static int changeColor(int color) {
    	// TODO
        return color;
    }
    
	// ==========================================================================
	// Piston Logic Replacing
	// ==========================================================================
    
	public static PistonBlockStructureHelper transformStructureHelper(PistonBlockStructureHelper helper, World world, BlockPos sourcePos, Direction facing, boolean extending) {
		return new QuarkPistonStructureHelper(helper, world, sourcePos, facing, extending);
	}
    
}
