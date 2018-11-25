package vazkii.quark.oddities.feature;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
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

}
