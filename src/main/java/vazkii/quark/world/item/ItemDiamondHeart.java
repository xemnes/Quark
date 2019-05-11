package vazkii.quark.world.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.item.ItemMod;
import vazkii.quark.base.item.IQuarkItem;

import javax.annotation.Nonnull;

public class ItemDiamondHeart extends ItemMod implements IQuarkItem {

	public ItemDiamondHeart() {
		super("diamond_heart");
		setCreativeTab(CreativeTabs.MISC);
	}

	@Nonnull
	@Override
	public IRarity getForgeRarity(@Nonnull ItemStack stack) {
		return EnumRarity.UNCOMMON;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack) {
		return true;
	}

}
