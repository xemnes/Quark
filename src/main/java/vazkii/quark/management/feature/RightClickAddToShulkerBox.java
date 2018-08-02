package vazkii.quark.management.feature;

import java.util.Arrays;

import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.InvWrapper;
import vazkii.arl.network.NetworkHandler;
import vazkii.arl.util.RenderHelper;
import vazkii.quark.base.module.Feature;
import vazkii.quark.base.module.ModuleLoader;
import vazkii.quark.base.network.message.MessageAddToShulkerBox;

public class RightClickAddToShulkerBox extends Feature {

	@SubscribeEvent
	public void onDrawScreen(GuiScreenEvent.DrawScreenEvent.Post event) {
		Minecraft mc = Minecraft.getMinecraft();
		GuiScreen gui = mc.currentScreen;
		if(gui instanceof GuiContainer) {
			GuiContainer container = (GuiContainer) gui;
			ItemStack held = mc.player.inventory.getItemStack();
			if(!held.isEmpty()) {
				Slot under = container.getSlotUnderMouse();
				for(Slot s : container.inventorySlots.inventorySlots) {
					if(s.inventory != mc.player.inventory)
						continue;

					ItemStack stack = s.getStack();
					if(stack.getItem() instanceof ItemShulkerBox && canAddToShulkerBox(held, stack)) {
						if(s == under) {
							int x = event.getMouseX();
							int y = event.getMouseY();
							RenderHelper.renderTooltip(x, y, Arrays.asList(I18n.translateToLocal("quarkmisc.rightClickAdd")));
						} else {
							int x = container.getGuiLeft() + s.xPos;
							int y = container.getGuiTop() + s.yPos;

							GlStateManager.disableDepth();
							mc.fontRenderer.drawStringWithShadow("+", x + 10, y + 8, 0xFFFF00);
							GlStateManager.enableDepth();
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onRightClick(GuiScreenEvent.MouseInputEvent.Pre event) {
		Minecraft mc = Minecraft.getMinecraft();
		GuiScreen gui = mc.currentScreen;
		if(gui instanceof GuiContainer && Mouse.getEventButton() == 1) {
			GuiContainer container = (GuiContainer) gui;
			Slot under = container.getSlotUnderMouse();
			ItemStack held = mc.player.inventory.getItemStack();

			if(under != null && !held.isEmpty() && under.inventory == mc.player.inventory) {
				ItemStack stack = under.getStack();
				if(stack.getItem() instanceof ItemShulkerBox && canAddToShulkerBox(held, stack)) {
					mc.player.inventory.setItemStack(ItemStack.EMPTY);
					NetworkHandler.INSTANCE.sendToServer(new MessageAddToShulkerBox(under.getSlotIndex(), held));
					event.setCanceled(true);
				}
			}
		}
	}

	public static boolean canAddToShulkerBox(ItemStack stack, ItemStack shulkerBox) {
		if(stack.getItem() instanceof ItemShulkerBox)
			return false;
		
		return tryAddToShulkerBox(stack, shulkerBox, false);
	}

	public static void addToShulkerBox(EntityPlayer player, int slot, ItemStack stack) {
		if(!ModuleLoader.isFeatureEnabled(RightClickAddToShulkerBox.class))
			return;

		ItemStack shulkerBox = player.inventory.getStackInSlot(Math.min(player.inventory.getSizeInventory() - 1, slot));
		if(shulkerBox.getItem() instanceof ItemShulkerBox && canAddToShulkerBox(stack, shulkerBox)) {
			ItemStack held = player.inventory.getItemStack();

			if(player.isCreative() && !stack.isEmpty())
				held = stack;

			tryAddToShulkerBox(stack, shulkerBox, true);
			player.inventory.setItemStack(ItemStack.EMPTY);
		}
	}
	
	private static boolean tryAddToShulkerBox(ItemStack stack, ItemStack shulkerBox, boolean doit) {
		TileEntityShulkerBox tile = new TileEntityShulkerBox();
		NBTTagCompound blockCmp = shulkerBox.getTagCompound();
		if(blockCmp == null || !blockCmp.hasKey("BlockEntityTag"))
			return false;
		blockCmp = blockCmp.getCompoundTag("BlockEntityTag");
		
		tile.readFromNBT(blockCmp);
		IItemHandler handler = new InvWrapper(tile);
		ItemStack result = ItemHandlerHelper.insertItem(handler, stack, !doit);
		boolean did = result.isEmpty();
		
		if(doit && did)
			tile.writeToNBT(blockCmp);
		
		return did;
	}
	
	@Override
	public boolean hasSubscriptions() {
		return true;
	}
	
}
