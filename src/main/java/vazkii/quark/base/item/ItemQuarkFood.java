package vazkii.quark.base.item;

import javax.annotation.Nonnull;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import vazkii.arl.util.ProxyRegistry;

public class ItemQuarkFood extends ItemFood implements IQuarkItem {

	private final String bareName;

	public ItemQuarkFood(String name, int amount, float saturation, boolean isWolfFood) {
		super(amount, saturation, isWolfFood);
		setTranslationKey(name);
		bareName = name;
		setCreativeTab(CreativeTabs.FOOD);
	}
	
	public ItemQuarkFood(String name, int amount, float saturation) {
		this(name, amount, saturation, false);
	}
	
	@Nonnull
	@Override
	public Item setTranslationKey(@Nonnull String name) {
		super.setTranslationKey(name);
		setRegistryName(new ResourceLocation(getPrefix() + name));
		ProxyRegistry.register(this);

		return this;
	}

	@Nonnull
	@Override
	public String getTranslationKey(ItemStack par1ItemStack) {
		return "item." + getPrefix() + bareName;
	}

	@Override
	public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> subItems) {
		if(isInCreativeTab(tab))
			subItems.add(new ItemStack(this, 1, 0));
	}

	@Override
	public String[] getVariants() {
		return new String[] { bareName };
	}

}
