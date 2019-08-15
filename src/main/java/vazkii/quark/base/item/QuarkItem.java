package vazkii.quark.base.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import vazkii.arl.item.BasicItem;
import vazkii.quark.base.module.Module;

public class QuarkItem extends BasicItem {

	private final Module module;
	
	public QuarkItem(String regname, Module module, Properties properties) {
		super(regname, properties);
		this.module = module;
	}

	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
		if(isEnabled() || group == ItemGroup.SEARCH)
			super.fillItemGroup(group, items);
	}
	
	public boolean isEnabled() {
		return module != null && module.enabled;
	}

}
