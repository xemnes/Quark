package vazkii.quark.base.block;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import vazkii.arl.block.BasicBlock;
import vazkii.quark.base.moduleloader.Module;

public class QuarkBlock extends BasicBlock {
	
	private final Module module;
	
	public QuarkBlock(String regname, Module module, Properties properties) {
		super(regname, properties);
		this.module = module;
	}
	
	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
		if(module.enabled)
			super.fillItemGroup(group, items);
	}

}
