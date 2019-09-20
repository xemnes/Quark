package vazkii.quark.tools.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.item.*;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.quark.base.item.QuarkItem;
import vazkii.quark.base.module.Module;
import vazkii.quark.tools.module.AncientTomesModule;

import javax.annotation.Nonnull;
import java.util.List;

public class AncientTomeItem extends QuarkItem {

	public AncientTomeItem(Module module) {
		super("ancient_tome", module, 
				new Item.Properties().maxStackSize(1));
	}
	
	@Override
	public boolean isEnchantable(@Nonnull ItemStack stack) {
		return false;
	}
	
	@Override
	public boolean hasEffect(ItemStack stack) {
		return true;
	}
	
	@Nonnull
	@Override
	public Rarity getRarity(ItemStack stack) {
		return EnchantedBookItem.getEnchantments(stack).isEmpty() ? super.getRarity(stack) : Rarity.UNCOMMON;
	}
	
	public static ItemStack getEnchantedItemStack(EnchantmentData ench) {
		ItemStack newStack = new ItemStack(AncientTomesModule.ancient_tome);
		EnchantedBookItem.addEnchantment(newStack, ench);
		return newStack;
	}
	
	@Override
	public void fillItemGroup(@Nonnull ItemGroup tab, @Nonnull NonNullList<ItemStack> subItems) {
		if (isEnabled()) {
			if (tab == ItemGroup.SEARCH || tab.getRelevantEnchantmentTypes().length != 0) {
				for (Enchantment ench : ForgeRegistries.ENCHANTMENTS) {
					if (AncientTomesModule.validEnchants.contains(ench)) {
						if ((tab == ItemGroup.SEARCH && ench.type != null) || tab.hasRelevantEnchantmentType(ench.type)) {
							subItems.add(getEnchantedItemStack(new EnchantmentData(ench, ench.getMaxLevel())));
						}
					}
				}
			}
		}
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
	    ItemStack.addEnchantmentTooltips(tooltip, EnchantedBookItem.getEnchantments(stack));
	}

}
