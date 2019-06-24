package vazkii.quark.client.feature;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.util.ItemNBTHelper;
import vazkii.quark.base.lib.LibMisc;
import vazkii.quark.base.module.Feature;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class VisualStatDisplay extends Feature {

	public static final ImmutableSet<String> VALID_ATTRIBUTES = ImmutableSet.of(
			"generic.attackDamage",
			"generic.attackSpeed",
			"generic.reachDistance",
			"generic.armor",
			"generic.armorToughness",
			"generic.knockbackResistance",
			"generic.maxHealth",
			"generic.movementSpeed",
			"generic.luck");

	private static final ImmutableSet<String> MULTIPLIER_ATTRIBUTES = ImmutableSet.of(
			"generic.movementSpeed");

	private static final ImmutableSet<String> PERCENT_ATTRIBUTES = ImmutableSet.of(
			"generic.knockbackResistance",
			"generic.luck");

	private String format(String attribute, double value) {
		if (PERCENT_ATTRIBUTES.contains(attribute))
			return (value > 0 ? "+" : "") + ItemStack.DECIMALFORMAT.format(value * 100) + "%";
		else if (MULTIPLIER_ATTRIBUTES.contains(attribute))
			return ItemStack.DECIMALFORMAT.format(value / baseValue(attribute)) + "x";
		else
			return ItemStack.DECIMALFORMAT.format(value);
	}

	private double baseValue(String attribute) {
		switch (attribute) {
			case "generic.movementSpeed":
				return 0.1;
			default:
				return 1;
		}
	}

	private int renderPosition(String attribute) {
		switch (attribute) {
			case "generic.attackDamage":
				return 238;
			case "generic.attackSpeed":
				return 247;
			case "generic.reachDistance":
				return 193;
			case "generic.armor":
				return 229;
			case "generic.armorToughness":
				return 220;
			case "generic.knockbackResistance":
				return 175;
			case "generic.maxHealth":
				return 211;
			case "generic.movementSpeed":
				return 184;
			case "generic.luck":
				return 202;
			default:
				return 211;
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void makeTooltip(ItemTooltipEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		ItemStack stack = event.getItemStack();

		if(!GuiScreen.isShiftKeyDown() && canStripAttributes(stack)) {
			List<String> tooltip = event.getToolTip();
			Map<EntityEquipmentSlot, StringBuilder> attributeTooltips = Maps.newEnumMap(EntityEquipmentSlot.class);

			boolean onlyInvalid = true;
			Multimap<String, AttributeModifier> baseCheck = null;
			boolean allAreSame = true;

			EntityEquipmentSlot[] slots = EntityEquipmentSlot.values();
			for(EntityEquipmentSlot slot : slots) {
				Multimap<String, AttributeModifier> slotAttributes = stack.getAttributeModifiers(slot);

				if (baseCheck == null)
					baseCheck = slotAttributes;
				else if (allAreSame && !slotAttributes.equals(baseCheck))
					allAreSame = false;

				if(!slotAttributes.isEmpty()) {
					String slotDesc = I18n.format("item.modifiers." + slot.getName());
					int index = tooltip.indexOf(slotDesc) - 1;
					if(index < 0)
						continue;
					
					tooltip.remove(index); // Remove blank space
					tooltip.remove(index); // Remove actual line
				}

				onlyInvalid = extractAttributeValues(event, stack, tooltip, attributeTooltips, onlyInvalid, slot, slotAttributes);
			}

			EntityEquipmentSlot primarySlot = EntityLiving.getSlotForItemStack(stack);
			boolean showSlots = !allAreSame && (onlyInvalid ||
					(attributeTooltips.size() == 1 && attributeTooltips.containsKey(primarySlot)));

			for (int i = 0; i < slots.length; i++) {
				EntityEquipmentSlot slot = slots[slots.length - (i + 1)];
				if (attributeTooltips.containsKey(slot)) {
					int len = mc.fontRenderer.getStringWidth(attributeTooltips.get(slot).toString()) + 32;
					if (showSlots)
						len += 20;

					int space = mc.fontRenderer.getCharWidth(' ');
					StringBuilder spaces = new StringBuilder();
					for (int j = 0; j < len / space; j++)
						spaces.append(' ');

					tooltip.add(1, spaces.toString());
					if (allAreSame)
						break;
				}
			}
		}
	}

	public boolean extractAttributeValues(ItemTooltipEvent event, ItemStack stack, List<String> tooltip, Map<EntityEquipmentSlot, StringBuilder> attributeTooltips, boolean onlyInvalid, EntityEquipmentSlot slot, Multimap<String, AttributeModifier> slotAttributes) {
		boolean anyInvalid = false;
		for(String s : slotAttributes.keys()) {
			if(VALID_ATTRIBUTES.contains(s)) {
				onlyInvalid = false;
				double attributeValue = getAttribute(event.getEntityPlayer(), stack, slotAttributes, s);
				if (attributeValue != 0) {
					if (!attributeTooltips.containsKey(slot))
						attributeTooltips.put(slot, new StringBuilder());
					attributeTooltips.get(slot).append(format(s, attributeValue));
				}
			} else if (!anyInvalid) {
				anyInvalid = true;
				if (!attributeTooltips.containsKey(slot))
					attributeTooltips.put(slot, new StringBuilder());
				attributeTooltips.get(slot).append("[+]");
			}

			for (int i = 1; i < tooltip.size(); i++) {
				if (isAttributeLine(tooltip.get(i), s)) {
					tooltip.remove(i);
					break;
				}
			}
		}
		return onlyInvalid;
	}

	private static final ImmutableSet<String> ATTRIBUTE_FORMATS = ImmutableSet.of("plus", "take", "equals");

	@SideOnly(Side.CLIENT)
	private static boolean isAttributeLine(String line, String attName) {
		String attNameLoc = I18n.format("attribute.name." + attName);

		for (String att : ATTRIBUTE_FORMATS) {
			for (int mod = 0; mod < 3; mod++) {
				String pattern = " " + I18n.format("attribute.modifier." + att + "." + mod, "\n", attNameLoc);
				String[] split = pattern.split("\n");
				if (split.length == 2 && line.startsWith(split[0]) && line.endsWith(split[1]))
					return true;
			}
		}

		return false;
	}

	@SideOnly(Side.CLIENT)
	private int renderAttribute(String attribute, int x, int y, ItemStack stack, Multimap<String, AttributeModifier> slotAttributes, Minecraft mc) {
		double value = getAttribute(mc.player, stack, slotAttributes, attribute);
		if (value != 0) {
			GlStateManager.color(1F, 1F, 1F);
			mc.getTextureManager().bindTexture(LibMisc.GENERAL_ICONS_RESOURCE);
			Gui.drawModalRectWithCustomSizedTexture(x, y, renderPosition(attribute), 0, 9, 9, 256, 256);

			String valueStr = format(attribute, value);

			int color = value < 0 ? 0xFF5555 : 0xFFFFFF;

			mc.fontRenderer.drawStringWithShadow(valueStr, x + 12, y + 1, color);
			x += mc.fontRenderer.getStringWidth(valueStr) + 20;
		}

		return x;
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void renderTooltip(RenderTooltipEvent.PostText event) {
		ItemStack stack = event.getStack();
		if(!GuiScreen.isShiftKeyDown() && canStripAttributes(stack)) {
			GlStateManager.pushMatrix();
			GlStateManager.color(1F, 1F, 1F);
			Minecraft mc = Minecraft.getMinecraft();
			GlStateManager.translate(0F, 0F, mc.getRenderItem().zLevel);

			int baseX = event.getX();
			int y = event.getY() + 10;
			
			for(int i = 1; i < event.getLines().size(); i++) {
				String s = event.getLines().get(i);
				s = TextFormatting.getTextWithoutFormattingCodes(s);
				if(s != null && s.trim().isEmpty()) {
					y += 10 * (i - 1) + 1;
					break;
				}
			}

			EntityEquipmentSlot primarySlot = EntityLiving.getSlotForItemStack(stack);
			boolean onlyInvalid = true;
			boolean showSlots = false;
			int attributeHash = 0;

			boolean allAreSame = true;

			shouldShow: for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
				Multimap<String, AttributeModifier> slotAttributes = stack.getAttributeModifiers(slot);
				if (slot == EntityEquipmentSlot.MAINHAND)
					attributeHash = slotAttributes.hashCode();
				else if (allAreSame && attributeHash != slotAttributes.hashCode())
					allAreSame = false;

				for (String s : slotAttributes.keys()) {
					if (VALID_ATTRIBUTES.contains(s)) {
						onlyInvalid = false;
						if (slot != primarySlot) {
							showSlots = true;
							break shouldShow;
						}
					}
				}
			}

			if (allAreSame)
				showSlots = false;
			else if (onlyInvalid)
				showSlots = true;


			for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
				int x = baseX;

				Multimap<String, AttributeModifier> slotAttributes = stack.getAttributeModifiers(slot);

				boolean anyToRender = false;
				for(String s : slotAttributes.keys()) {
					double value = getAttribute(mc.player, stack, slotAttributes, s);
					if (value != 0) {
						anyToRender = true;
						break;
					}
				}

				if (!anyToRender)
					continue;

				if (showSlots) {
					GlStateManager.color(1F, 1F, 1F);
					mc.getTextureManager().bindTexture(LibMisc.GENERAL_ICONS_RESOURCE);
					Gui.drawModalRectWithCustomSizedTexture(x, y, 202 + slot.ordinal() * 9, 35, 9, 9, 256, 256);
					x += 20;
				}

				for (String key : VALID_ATTRIBUTES)
					x = renderAttribute(key, x, y, stack, slotAttributes, mc);

				for (String key : slotAttributes.keys()) {
					if (!VALID_ATTRIBUTES.contains(key)) {
						mc.fontRenderer.drawStringWithShadow("[+]", x + 1, y + 1, 0xFFFF55);
						break;
					}
				}


				y += 10;

				if (allAreSame)
					break;
			}
			
			GlStateManager.popMatrix();
		}
	}

	private boolean canStripAttributes(ItemStack stack) {
		if (stack.isEmpty())
			return false;

		return (ItemNBTHelper.getInt(stack, "HideFlags", 0) & 2) == 0;
	}

	private double getAttribute(EntityPlayer player, ItemStack stack, Multimap<String, AttributeModifier> map, String key) {
		if(player == null) // apparently this can happen
			return 0;
		
		Collection<AttributeModifier> collection = map.get(key);
		if(collection.isEmpty())
			return 0;

		double value = 0;

		if (!PERCENT_ATTRIBUTES.contains(key)) {
			IAttributeInstance attribute = player.getAttributeMap().getAttributeInstanceByName(key);
			if (attribute != null)
				value = attribute.getBaseValue();
		}

		for (AttributeModifier modifier : collection) {
			if (modifier.getOperation() == 0)
				value += modifier.getAmount();
		}

		double rawValue = value;

		for (AttributeModifier modifier : collection) {
			if (modifier.getOperation() == 1)
				value += rawValue * modifier.getAmount();
		}

		for (AttributeModifier modifier : collection) {
			if (modifier.getOperation() == 2)
				value += value * modifier.getAmount();
		}


		if (key.equals(SharedMonsterAttributes.ATTACK_DAMAGE.getName()))
			value += EnchantmentHelper.getModifierForCreature(stack, EnumCreatureAttribute.UNDEFINED);

		return value;
	}
	
	@Override
	public boolean hasSubscriptions() {
		return isClient();
	}


}
