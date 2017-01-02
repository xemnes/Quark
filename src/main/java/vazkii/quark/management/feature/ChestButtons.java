/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 *
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 *
 * File Created @ [02/04/2016, 17:04:11 (GMT)]
 */
package vazkii.quark.management.feature;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.network.NetworkHandler;
import vazkii.quark.base.handler.DropoffHandler;
import vazkii.quark.base.lib.LibObfuscation;
import vazkii.quark.base.module.Feature;
import vazkii.quark.base.module.ModuleLoader;
import vazkii.quark.base.network.message.MessageDropoff;
import vazkii.quark.base.network.message.MessageRestock;
import vazkii.quark.management.client.gui.GuiButtonChest;
import vazkii.quark.management.client.gui.GuiButtonChest.Action;

public class ChestButtons extends Feature {

	boolean deposit, smartDeposit, restock, sort;
	int xShift, yShift;

	@Override
	public void setupConfig() {
		deposit = loadPropBool("Enable Deposit Button", "", true);
		smartDeposit = loadPropBool("Enable Smart Deposit Button", "", true);
		restock = loadPropBool("Enable Restock Button", "", true);
		sort = loadPropBool("Enable Sort Button", "The Sort button is only available if the Inventory Sorting feature is enabled", true);
		xShift = loadPropInt("Horizontal Icon Shift", "", -18);
		yShift = loadPropInt("Vertical Icon Shift", "", 0);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void initGui(GuiScreenEvent.InitGuiEvent.Post event) {
		if(event.getGui() instanceof GuiContainer) {
			GuiContainer guiInv = (GuiContainer) event.getGui();
			Container container = guiInv.inventorySlots;
			EntityPlayer player = Minecraft.getMinecraft().player;

			boolean accept = guiInv instanceof GuiChest;

			for(Slot s : container.inventorySlots) {
				IInventory inv = s.inventory;
				if(inv != null && DropoffHandler.isValidChest(player, inv)) {
					accept = true;
					break;
				}
			}

			if(!accept)
				return;

			int guiLeft = ReflectionHelper.getPrivateValue(GuiContainer.class, guiInv, LibObfuscation.GUI_LEFT);
			int guiTop = ReflectionHelper.getPrivateValue(GuiContainer.class, guiInv, LibObfuscation.GUI_TOP);

			for(Slot s : container.inventorySlots)
				if(s.inventory == player.inventory && s.getSlotIndex() == 9) {
					if(sort && ModuleLoader.isFeatureEnabled(InventorySorting.class))
						event.getButtonList().add(new GuiButtonChest(guiInv, Action.SORT, 13210, guiLeft + xShift, guiTop + s.yPos - 60 + yShift));
					if(restock)
						event.getButtonList().add(new GuiButtonChest(guiInv, Action.RESTOCK, 13211, guiLeft + xShift, guiTop + s.yPos - 40 + yShift));
					if(deposit)
						event.getButtonList().add(new GuiButtonChest(guiInv, Action.DEPOSIT, 13212, guiLeft + xShift, guiTop + s.yPos + 18 + yShift));
					if(smartDeposit)
						event.getButtonList().add(new GuiButtonChest(guiInv, Action.SMART_DEPOSIT, 13213, guiLeft + xShift, guiTop + s.yPos + 36 + yShift));
					
					break;
				}
		}
	}

	@SuppressWarnings("incomplete-switch")
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void performAction(GuiScreenEvent.ActionPerformedEvent.Pre event) {
		if(event.getButton() instanceof GuiButtonChest) {
			GuiButtonChest buttonChest = (GuiButtonChest) event.getButton();
			Action action = buttonChest.action;

			switch(action) {
			case SMART_DEPOSIT:
				NetworkHandler.INSTANCE.sendToServer(new MessageDropoff(true, true));
				event.setCanceled(true);
				break;
			case DEPOSIT:
				NetworkHandler.INSTANCE.sendToServer(new MessageDropoff(false, true));
				event.setCanceled(true);
				break;
			case RESTOCK:
				NetworkHandler.INSTANCE.sendToServer(new MessageRestock());
				event.setCanceled(true);
				break;
			}
		}
	}

	@Override
	public boolean hasSubscriptions() {
		return isClient();
	}

}
