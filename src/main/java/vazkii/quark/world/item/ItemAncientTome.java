/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 *
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 *
 * File Created @ [01/06/2016, 19:43:18 (GMT)]
 */
package vazkii.quark.world.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import vazkii.arl.item.ItemMod;
import vazkii.quark.base.item.IQuarkItem;
import vazkii.quark.world.feature.AncientTomes;

public class ItemAncientTome extends ItemMod implements IQuarkItem {

	public ItemAncientTome() {
		super("ancient_tome");
		setMaxStackSize(1);
		setCreativeTab(CreativeTabs.MISC);
	}

	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
		for(Enchantment e : AncientTomes.validEnchants) {
			ItemStack stack = new ItemStack(itemIn);
			stack.addEnchantment(e, e.getMaxLevel());
			subItems.add(stack);
		}
	}

}
