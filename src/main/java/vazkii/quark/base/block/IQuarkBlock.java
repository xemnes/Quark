package vazkii.quark.base.block;

import net.minecraftforge.common.extensions.IForgeBlock;
import vazkii.quark.base.module.Module;

import javax.annotation.Nullable;
import java.util.function.BooleanSupplier;

/**
 * @author WireSegal
 * Created at 1:14 PM on 9/19/19.
 */
public interface IQuarkBlock extends IForgeBlock {

    @Nullable
    Module getModule();

    IQuarkBlock setCondition(BooleanSupplier condition);

    boolean doesConditionApply();

    default boolean isEnabled() {
        Module module = getModule();
        return module != null && module.enabled && doesConditionApply();
    }
}
