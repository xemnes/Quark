package vazkii.quark.client.feature;

import betterwithmods.api.FeatureEnabledEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemFood;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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
	@SideOnly(Side.CLIENT)
	public void makeTooltip(RenderTooltipEvent.Pre event) {
		if(!event.getStack().isEmpty() && event.getStack().getItem() instanceof ItemFood) {
			int pips = ((ItemFood) event.getStack().getItem()).getHealAmount(event.getStack());
			int len = (int) Math.ceil((double) pips / divisor);
			
			StringBuilder s = new StringBuilder(" ");
			for(int i = 0; i < len; i++)
				s.append("  ");
			
			List<String> tooltip = event.getLines();
			if(tooltip.isEmpty())
				tooltip.add(s.toString());
			else tooltip.add(1, s.toString());
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
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
