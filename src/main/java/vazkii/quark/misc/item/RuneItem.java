package vazkii.quark.misc.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import vazkii.quark.base.item.QuarkItem;
import vazkii.quark.api.RuneColorProvider;
import vazkii.quark.base.module.Module;
import vazkii.quark.misc.module.ColorRunes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author WireSegal
 * Created at 2:27 PM on 8/17/19.
 */
public abstract class RuneItem extends QuarkItem implements RuneColorProvider {
    public RuneItem(String regname, Module module, Properties properties) {
        super(regname, module, properties);
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return true;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        final LazyOptional<RuneColorProvider> holder = LazyOptional.of(() -> this);

        return new ICapabilityProvider() {
            @Nonnull
            @Override
            @SuppressWarnings("ConstantConditions")
            public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
                return ColorRunes.CAPABILITY.orEmpty(cap, holder);
            }
        };
    }
}
