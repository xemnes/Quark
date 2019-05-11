package vazkii.quark.world.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import vazkii.arl.item.ItemMod;
import vazkii.quark.base.item.IQuarkItem;

public class ItemDiamondHeart extends ItemMod implements IQuarkItem {

	public ItemDiamondHeart() {
		super("diamond_heart");
		setCreativeTab(CreativeTabs.MISC);
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return EnumRarity.UNCOMMON;
	}
	
	@Override
	public boolean hasEffect(ItemStack stack) {
		return true;
	}

}
