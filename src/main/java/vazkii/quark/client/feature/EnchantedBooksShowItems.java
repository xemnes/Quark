package vazkii.quark.client.feature;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.quark.base.module.Feature;

public class EnchantedBooksShowItems extends Feature {
	
	private static List<ItemStack> testItems;
	
	@Override
	public void setupConfig() {
		String[] testItemsArr = loadPropStringList("Items to Test", "", new String[] {
				"minecraft:diamond_sword", "minecraft:diamond_pickaxe", "minecraft:diamond_shovel", "minecraft:diamond_axe", "minecraft:diamond_hoe",
				"minecraft:diamond_helmet", "minecraft:diamond_chestplate", "minecraft:diamond_leggings", "minecraft:diamond_boots",
				"minecraft:shears", "minecraft:bow", "minecraft:fishing_rod", "minecraft:elytra"
		});
		
		testItems = new LinkedList();
		for(String s : testItemsArr) {
			Item item = Item.REGISTRY.getObject(new ResourceLocation(s));
			testItems.add(new ItemStack(item));
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void makeTooltip(ItemTooltipEvent event) {
		if(Minecraft.getMinecraft().player == null)
			return;
		
		ItemStack stack = event.getItemStack();
		if(stack.getItem() == Items.ENCHANTED_BOOK) {
			Minecraft mc = Minecraft.getMinecraft();
			List<String> tooltip = event.getToolTip();
			int tooltipIndex = 0;
			
			List<EnchantmentData> enchants = getEnchantedBookEnchantments(stack);
			for(EnchantmentData ed : enchants) {
				String match = ed.enchantment.getTranslatedName(ed.enchantmentLevel);
				
				for(; tooltipIndex < tooltip.size(); tooltipIndex++)
					if(tooltip.get(tooltipIndex).equals(match)) {
						List<ItemStack> items = getItemsForEnchantment(ed.enchantment);
						if(!items.isEmpty()) {
							int len = 3 + items.size() * 9;
							String spaces = "";
							while(mc.fontRenderer.getStringWidth(spaces) < len)
								spaces += " ";
							
							tooltip.add(tooltipIndex + 1, spaces);
						}
						
						break;
					}
			}
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void renderTooltip(RenderTooltipEvent.PostText event) {
		ItemStack stack = event.getStack();
		
		if(stack.getItem() == Items.ENCHANTED_BOOK) {
			Minecraft mc = Minecraft.getMinecraft();
			List<String> tooltip = event.getLines();
			int tooltipIndex = 0;

			GlStateManager.pushMatrix();
			GlStateManager.translate(event.getX(), event.getY() + 12, 0);
			GlStateManager.scale(0.5, 0.5, 1.0);
			
			List<EnchantmentData> enchants = getEnchantedBookEnchantments(stack);
			for(EnchantmentData ed : enchants) {
				String match = TextFormatting.getTextWithoutFormattingCodes(ed.enchantment.getTranslatedName(ed.enchantmentLevel));
				for(; tooltipIndex < tooltip.size(); tooltipIndex++) {
					String line = TextFormatting.getTextWithoutFormattingCodes(tooltip.get(tooltipIndex));
					if(line.equals(match)) {
						int drawn = 0;
						
						List<ItemStack> items = getItemsForEnchantment(ed.enchantment);
						for(ItemStack testStack : items)
							if(ed.enchantment.canApply(testStack)) {
								mc.getRenderItem().renderItemIntoGUI(testStack, 6 + drawn * 18, tooltipIndex * 20 - 2);
								drawn++;
							}
						
						break;
					}
				}
			}
			GlStateManager.popMatrix();
		}
	}
	
	@Override
	public boolean hasSubscriptions() {
		return true;
	}
	
	public static List<ItemStack> getItemsForEnchantment(Enchantment e) {
		List<ItemStack> list = new LinkedList();
		for(ItemStack stack : testItems)
			if(e.canApply(stack))
				list.add(stack);
		
		return list;
	}
	
	public static List<EnchantmentData> getEnchantedBookEnchantments(ItemStack stack) {
		NBTTagList nbttaglist = ItemEnchantedBook.getEnchantments(stack);
		List retList = new ArrayList(nbttaglist.tagCount() + 1);
		
        for(int i = 0; i < nbttaglist.tagCount(); i++) {
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound.getShort("id");
            Enchantment enchantment = Enchantment.getEnchantmentByID(j);
            short level = nbttagcompound.getShort("lvl");
            
            retList.add(new EnchantmentData(enchantment, level));
        }
        
        return retList;
	}
	
}
