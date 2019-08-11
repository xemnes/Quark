package vazkii.quark.base.block;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import vazkii.arl.block.BasicBlock;
import vazkii.quark.base.moduleloader.Module;

public class QuarkBlock extends BasicBlock {
	
	private Module module;
	
	public QuarkBlock(String regname, Properties properties) {
		super(regname, properties);
	}
	
	public QuarkBlock setModule(Module module) {
		this.module = module;
		return this;
	}
	
	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
		if(module != null && module.enabled)
			super.fillItemGroup(group, items);
	}

}
