package vazkii.quark.oddities.item;

import net.minecraft.creativetab.CreativeTabs;
import vazkii.arl.item.ItemMod;
import vazkii.quark.base.item.IQuarkItem;

public class ItemTotemOfHolding extends ItemMod implements IQuarkItem {
    public ItemTotemOfHolding(String name) {
		super(name);
		setCreativeTab(CreativeTabs.MISC);
		setMaxStackSize(1);
	}
}
