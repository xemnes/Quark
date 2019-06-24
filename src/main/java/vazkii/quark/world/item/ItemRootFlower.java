package vazkii.quark.world.item;

import net.minecraft.creativetab.CreativeTabs;
import vazkii.quark.base.item.IQuarkItem;
import vazkii.quark.base.item.ItemQuarkFood;

public class ItemRootFlower extends ItemQuarkFood implements IQuarkItem {

	private static final String[] VARIANTS = {
			"root_blue_flower",
			"root_black_flower",
			"root_white_flower"
	};
	
	public ItemRootFlower() {
		super("root_flower", 3, 0.4F, VARIANTS);
		setCreativeTab(CreativeTabs.MATERIALS);
	}

}
