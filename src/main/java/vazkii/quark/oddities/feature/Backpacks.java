package vazkii.quark.oddities.feature;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityVillager.PriceInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerCareer;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.quark.base.module.Feature;
import vazkii.quark.oddities.client.gui.GuiBackpackInventory;
import vazkii.quark.oddities.item.ItemBackpack;

public class Backpacks extends Feature {

	public static Item backpack;
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		backpack = new ItemBackpack();
	}
	
	@SubscribeEvent
	public void onRegisterVillagers(RegistryEvent.Register<VillagerProfession> event) {
		VillagerProfession butcher = event.getRegistry().getValue(new ResourceLocation("minecraft:butcher"));
		VillagerCareer leatherworker = butcher.getCareer(1);
		
		leatherworker.addTrade(1, new BackpackTrade());
 	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onOpenGUI(GuiOpenEvent event) {
		EntityPlayer player = Minecraft.getMinecraft().player;
		if(player != null && isInventoryGUI(event.getGui()) && !player.isCreative() && isEntityWearingBackpack(player))
			event.setGui(new GuiBackpackInventory(player));
	}
	
	@SubscribeEvent
	public void clientTick(ClientTickEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		if(isInventoryGUI(mc.currentScreen) && isEntityWearingBackpack(mc.player))
			mc.displayGuiScreen(new GuiBackpackInventory(mc.player));
	}
	
	@SubscribeEvent
	public void removeCurseTooltip(ItemTooltipEvent event) {
		if(event.getItemStack().getItem() instanceof ItemBackpack)
			for(String s : event.getToolTip())
				if(s.equals(Enchantments.BINDING_CURSE.getTranslatedName(1))) {
					event.getToolTip().remove(s);
					return;
				}
	}
	
	private static boolean isInventoryGUI(GuiScreen gui) {
		return gui != null && gui.getClass() == GuiInventory.class;
	}
	
	public static boolean isEntityWearingBackpack(Entity e) {
		if(e instanceof EntityLivingBase) {
			EntityLivingBase living = (EntityLivingBase) e;
			ItemStack chestArmor = living.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
			return chestArmor.getItem() instanceof ItemBackpack;
		}
		
		return false;
	}
	
	public static boolean isEntityWearingBackpack(Entity e, ItemStack stack) {
		if(e instanceof EntityLivingBase) {
			EntityLivingBase living = (EntityLivingBase) e;
			ItemStack chestArmor = living.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
			return chestArmor == stack;
		}
		
		return false;
	}
	
	@Override
	public boolean requiresMinecraftRestartToEnable() {
		return true;
	}
	
	@Override
	public boolean hasSubscriptions() {
		return true;
	}
	
    public static class BackpackTrade implements EntityVillager.ITradeList {

    	@Override
        public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random) {
        	int count = random.nextInt(6) + 12;
        	recipeList.add(new MerchantRecipe(new ItemStack(Items.LEATHER, 12), new ItemStack(Items.EMERALD, count), new ItemStack(backpack)));
        }
    }

}
