package vazkii.quark.client.feature;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.quark.base.module.Feature;

public class ShowInvalidSlots extends Feature {

	boolean skip = false;
	boolean requiresShift;
	
	@Override
	public void setupConfig() {
		requiresShift = loadPropBool("Requires Shift", "Set this to true to only display the reds boxes when Shift is held", true);
	}
	
	@Override
	public boolean hasSubscriptions() {
		return isClient();
	}
	
	@SubscribeEvent
	public void onRender(GuiScreenEvent.DrawScreenEvent.Post event) {
		if(!skip)
			renderElements(event.getGui());
		skip = false;
	}
	
	@SubscribeEvent
	public void drawTooltipEvent(RenderTooltipEvent.Pre event) {
		renderElements(Minecraft.getMinecraft().currentScreen);
		skip = true;
	}
	
	private void renderElements(GuiScreen gui) {
		if(gui instanceof GuiContainer && (!requiresShift || GuiScreen.isShiftKeyDown())) {
			GuiContainer guiContainer = (GuiContainer) gui;
			Container container = guiContainer.inventorySlots;
			
			ItemStack stack = Minecraft.getMinecraft().player.inventory.getItemStack();
			if(stack.isEmpty()) {
				Slot slotUnder = guiContainer.getSlotUnderMouse();
				if(slotUnder != null)
					stack = slotUnder.getStack();
			}
			
			if(stack.isEmpty())
				return;
			
			int guiLeft = guiContainer.getGuiLeft();
			int guiTop = guiContainer.getGuiTop();
			
			for(Slot s : container.inventorySlots) {
				if(!s.isItemValid(stack)) {
					int x = guiLeft + s.xPos;
					int y = guiTop + s.yPos;
					
					GlStateManager.disableDepth();
					Gui.drawRect(x, y, x + 16, y + 16, 0x55FF0000);
				}
			}
		}
	}
	
}
