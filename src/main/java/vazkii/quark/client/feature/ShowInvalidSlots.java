package vazkii.quark.client.feature;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.quark.base.module.Feature;
import vazkii.quark.base.module.ModuleLoader;

public class ShowInvalidSlots extends Feature {

	public static boolean requiresShift;
	
	@Override
	public void setupConfig() {
		requiresShift = loadPropBool("Requires Shift", "Set this to true to only display the reds boxes when Shift is held", true);
	}

	@SideOnly(Side.CLIENT)
	public static void renderElements(GuiContainer gui) {
		if (!ModuleLoader.isFeatureEnabled(ShowInvalidSlots.class))
			return;

		if(!requiresShift || GuiScreen.isShiftKeyDown()) {
			Minecraft mc = Minecraft.getMinecraft();

			ItemStack stack = mc.player.inventory.getItemStack();
			Slot slotUnder = gui.getSlotUnderMouse();

			if(stack.isEmpty()) {
				if(slotUnder != null)
					stack = slotUnder.getStack();
			}

			if(stack.isEmpty())
				return;

			GlStateManager.disableLighting();
			GlStateManager.disableBlend();

			for (Slot slot : gui.inventorySlots.inventorySlots) {
				if (slot != slotUnder && !slot.isItemValid(stack) && slot.isEnabled()) {
					GlStateManager.pushMatrix();

					GlStateManager.translate(0, 0, mc.getRenderItem().zLevel + 100.125f);
					int x = slot.xPos;
					int y = slot.yPos;

					GlStateManager.colorMask(true, true, true, false);
					Gui.drawRect(x, y, x + 16, y + 16, 0x55FF0000);
					GlStateManager.colorMask(true, true, true, true);

					GlStateManager.popMatrix();
				}
			}

			GlStateManager.enableBlend();
			GlStateManager.enableLighting();
		}
	}
	
}
