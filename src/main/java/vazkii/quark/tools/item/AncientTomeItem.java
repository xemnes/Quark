package vazkii.quark.tools.item;

import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.arl.util.ItemNBTHelper;
import vazkii.quark.base.item.QuarkItem;
import vazkii.quark.base.module.Module;
import vazkii.quark.tools.module.AncientTomesModule;

public class AncientTomeItem extends QuarkItem {

	public AncientTomeItem(Module module) {
		super("ancient_tome", module, 
				new Item.Properties().maxStackSize(1));
	}
	
	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		ListNBT ench = ItemNBTHelper.getList(stack, "ench", Constants.NBT.TAG_COMPOUND, true);
		if (ench != null && !ItemNBTHelper.verifyExistence(stack, "StoredEnchantments")) {
			ItemNBTHelper.setList(stack, "StoredEnchantments", ench.copy());
			ItemNBTHelper.getNBT(stack).remove("ench");
		}
	}
	
	@Override
	public boolean isEnchantable(ItemStack stack) {
		return true;
	}
	
	@Override
	public boolean hasEffect(ItemStack stack) {
		return true;
	}
	
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
		if (tab.getRelevantEnchantmentTypes().length != 0 || tab == ItemGroup.SEARCH) {
			for (Enchantment ench : ForgeRegistries.ENCHANTMENTS.getValues()) {
				if (AncientTomesModule.validEnchants.contains(ench) && (tab == ItemGroup.SEARCH ? ench.type != null : tab.hasRelevantEnchantmentType(ench.type)))
					subItems.add(getEnchantedItemStack(new EnchantmentData(ench, ench.getMaxLevel())));
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
