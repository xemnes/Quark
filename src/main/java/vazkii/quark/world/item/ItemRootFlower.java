package vazkii.quark.world.item;

import net.minecraft.creativetab.CreativeTabs;
import vazkii.arl.item.ItemMod;
import vazkii.quark.base.item.IQuarkItem;

public class ItemRootFlower extends ItemMod implements IQuarkItem {

	private static final String[] VARIANTS = {
			"root_blue_flower",
			"root_black_flower",
			"root_white_flower"
	};
	
	public ItemRootFlower() {
		super("root_flower", VARIANTS);
		setCreativeTab(CreativeTabs.MATERIALS);
	}

}
