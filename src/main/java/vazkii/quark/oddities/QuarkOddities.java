package vazkii.quark.oddities;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vazkii.quark.base.module.Module;
import vazkii.quark.oddities.feature.Backpacks;
import vazkii.quark.oddities.feature.MatrixEnchanting;
import vazkii.quark.oddities.feature.Pipes;
import vazkii.quark.oddities.feature.TotemOfHolding;

public class QuarkOddities extends Module {

	@Override
	public void addFeatures() {
		registerFeature(new Pipes());
		registerFeature(new Backpacks());
		registerFeature(new TotemOfHolding());
		registerFeature(new MatrixEnchanting());
	}
	
	@Override
	public ItemStack getIconStack() {
		return new ItemStack(Items.CHORUS_FRUIT);
	}
	
}
