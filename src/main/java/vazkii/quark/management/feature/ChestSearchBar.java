package vazkii.quark.management.feature;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiShulkerBox;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.quark.api.IItemSearchBar;
import vazkii.quark.base.handler.GuiFactory;
import vazkii.quark.base.module.Feature;
import vazkii.quark.management.client.gui.GuiButtonChest;

public class ChestSearchBar extends Feature {

	static String text = "";
	GuiTextField searchBar;
	boolean skip;
	
	@SubscribeEvent
	public void initGui(GuiScreenEvent.InitGuiEvent.Post event) {
		GuiScreen gui = event.getGui();
		boolean callback = gui instanceof IItemSearchBar;
		if(callback || gui instanceof GuiChest || gui instanceof GuiShulkerBox) {
			GuiContainer chest = (GuiContainer) gui;
			searchBar = new GuiTextField(12831, gui.mc.fontRenderer, chest.getGuiLeft() + 80, chest.getGuiTop() + 5, 88, 10);
			searchBar.setText(text);
			searchBar.setFocused(false);
			searchBar.setMaxStringLength(32);
			searchBar.setEnableBackgroundDrawing(false);
			
			if(callback)
				((IItemSearchBar) gui).onSearchBarAdded(searchBar);
		} else searchBar = null;
	}
	
	@SubscribeEvent
	public void onKeypress(GuiScreenEvent.KeyboardInputEvent.Pre event) {
		if(searchBar != null && searchBar.isFocused()) {
	        char eventChar = Keyboard.getEventCharacter();
	        int eventCode = Keyboard.getEventKey();
	        
			searchBar.textboxKeyTyped(eventChar, eventCode);
			text = searchBar.getText();
			
			event.setCanceled(eventCode != 1);
		}
	}
	
	@SubscribeEvent
	public void onMouseclick(GuiScreenEvent.MouseInputEvent.Pre event) {
		if(searchBar != null && Mouse.getEventButtonState()) {
			Minecraft mc = Minecraft.getMinecraft();
			GuiScreen gui = event.getGui();
			
	        int x = Mouse.getEventX() * gui.width / mc.displayWidth;
	        int y = gui.height - Mouse.getEventY() * gui.height / mc.displayHeight - 1;
			int button = Mouse.getEventButton();
			
			searchBar.mouseClicked(x, y, button);
		}
	}
	
	@SubscribeEvent
	public void onRender(GuiScreenEvent.DrawScreenEvent.Post event) {
		if(searchBar != null && !skip)
			renderElements(event.getGui());
		skip = false;
	}
	
	@SubscribeEvent
	public void drawTooltipEvent(RenderTooltipEvent.Pre event) {
		if(searchBar != null) {
			renderElements(Minecraft.getMinecraft().currentScreen);
			skip = true;
		}
	}
	
	private void renderElements(GuiScreen gui) {
		drawBackground(gui, searchBar.x - 1, searchBar.y - 1);
		searchBar.drawTextBox();
		
		if(!text.isEmpty()) {
			if(gui instanceof GuiContainer) {
				GuiContainer guiContainer = (GuiContainer) gui;
				Container container = guiContainer.inventorySlots;
				
				int guiLeft = guiContainer.getGuiLeft();
				int guiTop = guiContainer.getGuiTop();
				
				for(Slot s : container.inventorySlots) {
					ItemStack stack = s.getStack();
					if(stack.isEmpty() || !namesMatch(stack.getDisplayName(), text)) {
						int x = guiLeft + s.xPos;
						int y = guiTop + s.yPos;
						
						GlStateManager.disableDepth();
						guiContainer.drawRect(x, y, x + 16, y + 16, 0xAA000000);
					}
				}
			}
		}
	}
	
	private void drawBackground(GuiScreen gui, int x, int y) {
		if(gui instanceof IItemSearchBar && ((IItemSearchBar) gui).renderBackground(x, y))
			return;
		
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.disableLighting();
		gui.mc.getTextureManager().bindTexture(GuiButtonChest.GENERAL_ICONS_RESOURCE);
		Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 244, 90, 12, 256, 256);
	}
	
	private boolean namesMatch(String name, String search) {
		search = TextFormatting.getTextWithoutFormattingCodes(search.trim().toLowerCase());
		name = TextFormatting.getTextWithoutFormattingCodes(name.trim().toLowerCase());
		if(search.startsWith("\"") && search.endsWith("\""))
			return name.equals(search);
		return name.contains(search);
	}
	
	@Override
	public boolean hasSubscriptions() {
		return isClient();
	}
	
}
