package vazkii.quark.base.block;

import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import vazkii.arl.util.RegistryHelper;
import vazkii.quark.base.module.Module;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class QuarkPillarBlock extends RotatedPillarBlock implements IQuarkBlock {

	private final Module module;
	private Supplier<Boolean> enabledSupplier = () -> true; 

	public QuarkPillarBlock(String regname, Module module, ItemGroup creativeTab, Properties properties) {
		super(properties);
		this.module = module;

		RegistryHelper.registerBlock(this, regname);
		if(creativeTab != null)
			RegistryHelper.setCreativeTab(this, creativeTab);
	}

	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
		if(isEnabled() || group == ItemGroup.SEARCH)
			super.fillItemGroup(group, items);
	}

	@Nullable
	@Override
	public Module getModule() {
		return module;
	}

	@Override
	public QuarkPillarBlock setCondition(Supplier<Boolean> enabledSupplier) {
		this.enabledSupplier = enabledSupplier;
		return this;
	}

	@Override
	public boolean doesConditionApply() {
		return enabledSupplier.get();
	}
}
