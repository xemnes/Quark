package vazkii.quark.client.feature;

import betterwithmods.api.FeatureEnabledEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemFood;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.quark.base.lib.LibObfuscation;
import vazkii.quark.base.module.Feature;

import java.util.List;

public class FoodTooltip extends Feature {

	public static int divisor = 2;
	
	@SubscribeEvent
	@Optional.Method(modid = "betterwithmods")
	public void bwmFeatureEnabled(FeatureEnabledEvent event) {
		if(event.getFeature().equals("hchunger") && event.isEnabled())
			divisor = 12;
	}
	
	@SubscribeEvent
	public void makeTooltip(ItemTooltipEvent event) {
		if(!event.getItemStack().isEmpty() && event.getItemStack().getItem() instanceof ItemFood) {
			int pips = ((ItemFood) event.getItemStack().getItem()).getHealAmount(event.getItemStack());
			int len = (int) Math.ceil((double) pips / divisor);
			
			StringBuilder s = new StringBuilder(" ");
			for(int i = 0; i < len; i++)
				s.append("  ");
			
			List<String> tooltip = event.getToolTip();
			if(tooltip.isEmpty())
				tooltip.add(s.toString());
			else tooltip.add(1, s.toString());
		}
	}

	@SubscribeEvent
	public void renderTooltip(RenderTooltipEvent.PostText event) {
		if(!event.getStack().isEmpty() && event.getStack().getItem() instanceof ItemFood) {
			GlStateManager.pushMatrix();
			GlStateManager.color(1F, 1F, 1F);
			Minecraft mc = Minecraft.getMinecraft();
			mc.getTextureManager().bindTexture(GuiIngameForge.ICONS);
			ItemFood food = ((ItemFood) event.getStack().getItem()); 
			int pips = food.getHealAmount(event.getStack());
			
			PotionEffect eff = ObfuscationReflectionHelper.getPrivateValue(ItemFood.class, food, LibObfuscation.POTION_ID);
			boolean poison = eff != null && eff.getPotion().isBadEffect();

			for(int i = 0; i < Math.ceil((double) pips / divisor); i++) {
				int x = event.getX() + i * 9 - 2;
				int y = event.getY() + 12;
				
				if(mc.currentScreen instanceof GuiContainerCreative && ((GuiContainerCreative) mc.currentScreen).getSelectedTabIndex() == CreativeTabs.SEARCH.getIndex())
						y += 10;
				
				int u = 16;
				if(poison)
					u += 117;
				int v = 27;
				
				Gui.drawModalRectWithCustomSizedTexture(x, y, u, v, 9, 9, 256, 256);
				
				u = 52;
				if(pips % 2 != 0 && i == 0)
					u += 9;
				if(poison)
					u += 36;
				
				Gui.drawModalRectWithCustomSizedTexture(x, y, u, v, 9, 9, 256, 256);
			}

			GlStateManager.popMatrix();
		}
	}
	
	@Override
	public String[] getIncompatibleMods() {
		return new String[] { "appleskin" };
	}

	@Override
	public boolean hasSubscriptions() {
		return isClient();
	}

}
