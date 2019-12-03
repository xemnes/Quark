package vazkii.quark.client.tooltip;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.item.Food;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeIngameGui;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import org.apache.commons.lang3.tuple.Pair;
import vazkii.quark.client.module.ImprovedTooltipsModule;

import java.util.List;

public class FoodTooltips {

	@OnlyIn(Dist.CLIENT)
	public static void makeTooltip(ItemTooltipEvent event) {
		if(event.getItemStack().isFood()) {
			Food food = event.getItemStack().getItem().getFood();
			if (food != null) {
				int pips = food.getHealing();
				int len = (int) Math.ceil((double) pips / ImprovedTooltipsModule.foodDivisor);

				StringBuilder s = new StringBuilder(" ");
				for (int i = 0; i < len; i++)
					s.append("  ");


				ITextComponent spaces = new StringTextComponent(s.toString());
				List<ITextComponent> tooltip = event.getToolTip();
				if (tooltip.isEmpty())
					tooltip.add(spaces);
				else tooltip.add(1, spaces);
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static void renderTooltip(RenderTooltipEvent.PostText event) {
		if(event.getStack().isFood()) {
			Food food = event.getStack().getItem().getFood();
			if (food != null) {
				GlStateManager.pushMatrix();
				GlStateManager.color3f(1F, 1F, 1F);
				Minecraft mc = Minecraft.getInstance();
				mc.getTextureManager().bindTexture(ForgeIngameGui.GUI_ICONS_LOCATION);
				int pips = food.getHealing();

				boolean poison = false;
				for (Pair<EffectInstance, Float> effect : food.getEffects()) {
					if (effect.getLeft() != null && effect.getLeft().getPotion() != null && effect.getLeft().getPotion().getEffectType() == EffectType.HARMFUL) {
						poison = true;
						break;
					}
				}

				int count = (int) Math.ceil((double) pips / ImprovedTooltipsModule.foodDivisor);
				int y = TooltipUtils.shiftTextByLines(event.getLines(), event.getY() + 10);

				for (int i = 0; i < count; i++) {
					int x = event.getX() + i * 9 - 1;

					int u = 16;
					if (poison)
						u += 117;
					int v = 27;

					AbstractGui.blit(x, y, u, v, 9, 9, 256, 256);

					u = 52;
					if (pips % 2 != 0 && i == 0)
						u += 9;
					if (poison)
						u += 36;

					AbstractGui.blit(x, y, u, v, 9, 9, 256, 256);
				}

				GlStateManager.popMatrix();
			}
		}
	}

}
