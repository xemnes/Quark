package vazkii.quark.management.feature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.quark.base.module.Feature;

public class AutomaticToolRestock extends Feature {

	public List<Enchantment> importantEnchants = new ArrayList();
	private String[] enchantNames;
	private boolean enableLooseMatching;
	private boolean enableEnchantMatching;

	@Override
	public void setupConfig() {
		enchantNames = loadPropStringList("Important Enchantments", "Enchantments deemed important enough to have special priority when finding a replacement", generateDefaultEnchantmentList());
		enableLooseMatching = loadPropBool("Enable Loose Matching", "Enable replacing your tools with tools of the same type but not the same item", true);
		enableEnchantMatching = loadPropBool("Enable Enchantment Matching", "Enable comparing enchantments to find a replacement", true);
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		importantEnchants.clear();
		for(String s : enchantNames) {
			ResourceLocation r = new ResourceLocation(s);
			Enchantment e = Enchantment.REGISTRY.getObject(r);
			if(e != null)
				importantEnchants.add(e);
		}
	}

	@SubscribeEvent
	public void onToolBreak(PlayerDestroyItemEvent event) {
		EntityPlayer player = event.getEntityPlayer();
		ItemStack stack = event.getOriginal();
		Item item = stack.getItem();

		if(!player.world.isRemote && !stack.isEmpty() && stack.isItemStackDamageable() && stack.getMaxStackSize() == 1 && !(item instanceof ItemArmor)) {
			int currSlot = player.inventory.currentItem;
			if(event.getHand() == EnumHand.OFF_HAND)
				currSlot = player.inventory.getSizeInventory() - 1;

			List<Enchantment> enchantmentsOnStack = getImportantEnchantments(stack);
			Predicate<ItemStack> itemPredicate = (other) -> other.getItem() == item;
			Predicate<ItemStack> enchantmentPredicate = (other) -> !(new ArrayList(enchantmentsOnStack)).retainAll(getImportantEnchantments(other));

			if(enableEnchantMatching && findReplacement(player, currSlot, itemPredicate.and(enchantmentPredicate)))
				return;

			if(findReplacement(player, currSlot, itemPredicate))
				return;

			if(enableLooseMatching) {
				Set<String> classes = getItemClasses(stack);
				
				if(!classes.isEmpty()) {
					Predicate<ItemStack> toolPredicate = (other) -> getItemClasses(other).retainAll(classes);

					if(enableEnchantMatching && findReplacement(player, currSlot, toolPredicate.and(enchantmentPredicate)))
						return;

					findReplacement(player, currSlot, toolPredicate);
				}
			}
		}
	}

	private Set<String> getItemClasses(ItemStack stack) {
		Item item = stack.getItem();
		if(item instanceof ItemTool)
			return new HashSet(((ItemTool) item).getToolClasses(stack));
		else if(item instanceof ItemSword)
			return new HashSet(Arrays.asList("sword"));
		else if(item instanceof ItemBow)
			return new HashSet(Arrays.asList("bow"));
		else if(item instanceof ItemFishingRod)
			return new HashSet(Arrays.asList("fishing_rod"));
		
		return new HashSet();
	}

	private boolean findReplacement(EntityPlayer player, int currSlot, Predicate<ItemStack> match) {
		for(int i = 0; i < player.inventory.mainInventory.size(); i++) {
			if(i == currSlot)
				continue;

			ItemStack stackAt = player.inventory.getStackInSlot(i);
			if(match.test(stackAt)) {
				switchItems(player, i, currSlot);
				return true;
			}
		}

		return false;
	}

	private void switchItems(EntityPlayer player, int slot1, int slot2) {
		int size = player.inventory.mainInventory.size();
		if(slot1 >= size || slot2 >= size)
			return;

		ItemStack stackAtSlot1 = player.inventory.getStackInSlot(slot1);
		ItemStack stackAtSlot2 = player.inventory.getStackInSlot(slot2);

		player.inventory.setInventorySlotContents(slot2, stackAtSlot1);
		player.inventory.setInventorySlotContents(slot1, stackAtSlot2);
	}

	private List<Enchantment> getImportantEnchantments(ItemStack stack) {
		List<Enchantment> enchantsOnStack = new ArrayList();
		for(Enchantment ench : importantEnchants)
			if(EnchantmentHelper.getEnchantmentLevel(ench, stack) > 0)
				enchantsOnStack.add(ench);

		return enchantsOnStack;
	}

	@Override
	public boolean hasSubscriptions() {
		return true;
	}

	@Override
	public String[] getIncompatibleMods() {
		return new String[] { "invtweaks" };
	}

	private String[] generateDefaultEnchantmentList() {
		Enchantment[] enchants = new Enchantment[] {
				Enchantments.SILK_TOUCH,
				Enchantments.FORTUNE,
				Enchantments.INFINITY,
				Enchantments.LUCK_OF_THE_SEA,
				Enchantments.LOOTING
		};

		List<String> strings = new ArrayList();
		for(Enchantment e : enchants)
			if(e != null && e.getRegistryName() != null)
				strings.add(e.getRegistryName().toString());

		return strings.toArray(new String[strings.size()]);
	}

}
