package vazkii.quark.base.handler;

import net.minecraft.item.ItemStack;

/**
 * @author WireSegal
 * Created at 10:10 AM on 8/15/19.
 */
@SuppressWarnings("unused")
public class AsmHooks {
    public static void setColorRuneTargetStack(ItemStack stack) {
        //TODO
    }

    public static int changeColor(int color) {
        // TODO
        if (color == 0xFF8040CC) {
            return -1;
        }

        return color;
    }
}
