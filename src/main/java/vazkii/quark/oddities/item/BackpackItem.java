package vazkii.quark.oddities.item;

import java.util.Map;

import javax.annotation.Nonnull;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.DyeableArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import vazkii.arl.interf.IItemColorProvider;
import vazkii.arl.util.ItemNBTHelper;
import vazkii.arl.util.RegistryHelper;
import vazkii.quark.base.handler.ProxiedItemStackHandler;
import vazkii.quark.base.module.Module;
import vazkii.quark.oddities.container.BackpackContainer;
import vazkii.quark.oddities.module.BackpackModule;

public class BackpackItem extends DyeableArmorItem implements IItemColorProvider, INamedContainerProvider {

	private final Module module;

	public BackpackItem(Module module) {
		super(ArmorMaterial.LEATHER, EquipmentSlotType.CHEST, 
				new Item.Properties()
				.maxStackSize(1)
				.maxDamage(0)
				.group(ItemGroup.TOOLS)
				.rarity(Rarity.RARE));
		
		RegistryHelper.registerItem(this, "backpack");
		this.module = module;
	}
	
	public static boolean doesBackpackHaveItems(ItemStack stack) {
		LazyOptional<IItemHandler> handlerOpt  = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		
		if (!handlerOpt.isPresent())
			return false;
		
		IItemHandler handler = handlerOpt.orElse(null); 
		for(int i = 0; i < handler.getSlots(); i++)
			if(!handler.getStackInSlot(i).isEmpty())
				return true;
		
		return false;
	}
	
	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if(worldIn.isRemote)
			return;
		
		boolean hasItems = !BackpackModule.superOpMode && doesBackpackHaveItems(stack);
		
		Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(stack);
		boolean isCursed = enchants.containsKey(Enchantments.BINDING_CURSE);
		boolean changedEnchants = false;
		
		if(hasItems) {
			if(BackpackModule.isEntityWearingBackpack(entityIn, stack)) {
				if(!isCursed) {
					enchants.put(Enchantments.BINDING_CURSE, 1);
					changedEnchants = true;
				}
			} else {
				ItemStack copy = stack.copy();
				stack.setCount(0);
				entityIn.entityDropItem(copy, 0);
			}
		} else if(isCursed) {
			enchants.remove(Enchantments.BINDING_CURSE);
			changedEnchants = true;
		}
		
		if(changedEnchants)
			EnchantmentHelper.setEnchantments(enchants, stack);
	}
	
	@Override
	public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entityItem) {
		if(BackpackModule.superOpMode || entityItem.world.isRemote)
			return false;
		
		if (!ItemNBTHelper.detectNBT(stack))
			return false;
		
		LazyOptional<IItemHandler> handlerOpt  = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		
		if(!handlerOpt.isPresent())
			return false;
		
		IItemHandler handler = handlerOpt.orElse(null); 

		for(int i = 0; i < handler.getSlots(); i++) {
			ItemStack stackAt = handler.getStackInSlot(i);
			if(!stackAt.isEmpty()) {
				ItemStack copy = stackAt.copy();
				InventoryHelper.spawnItemStack(entityItem.world, entityItem.getPosX(), entityItem.getPosY(), entityItem.getPosZ(), copy);
			}
		}

		CompoundNBT comp = ItemNBTHelper.getNBT(stack);
		comp.remove("Inventory");
		if (comp.size() == 0)
			stack.setTag(null);
		
		return false;
	}
	
	@Nonnull
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT oldCapNbt) {
		ProxiedItemStackHandler handler = new ProxiedItemStackHandler(stack, 27);

		if (oldCapNbt != null && oldCapNbt.contains("Parent")) {
			CompoundNBT itemData = oldCapNbt.getCompound("Parent");
			ItemStackHandler stacks = new ItemStackHandler();
			stacks.deserializeNBT(itemData);

			for (int i = 0; i < stacks.getSlots(); i++)
				handler.setStackInSlot(i, stacks.getStackInSlot(i));

			oldCapNbt.remove("Parent");
		}	

		return handler;
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
		return HashMultimap.create();
	}
	
	@Override
	public boolean hasEffect(ItemStack stack) {
		return false;
	}
	
	@Override
	public boolean isEnchantable(@Nonnull ItemStack stack) {
		return false;
	}

	@Override
	public void fillItemGroup(@Nonnull ItemGroup group, @Nonnull NonNullList<ItemStack> items) {
		if(isEnabled() || group == ItemGroup.SEARCH)
			super.fillItemGroup(group, items);
	}

	public boolean isEnabled() {
		return module != null && module.enabled;
	}
	
	@Override
	public IItemColor getItemColor() {
		return (stack, i) -> i > 0 ? -1 : ((DyeableArmorItem) stack.getItem()).getColor(stack);
	}

	@Override
	public Container createMenu(int arg0, PlayerInventory arg1, PlayerEntity arg2) {
		return new BackpackContainer(arg2);
	}

	@Override
	public ITextComponent getDisplayName() {
		return getName();
	}

}
