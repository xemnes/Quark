package vazkii.quark.base.client;

import java.util.List;
import java.util.stream.Collectors;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.RenderTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import vazkii.arl.util.RenderHelper;
import vazkii.quark.base.Quark;

@EventBusSubscriber(modid = Quark.MOD_ID, value = Dist.CLIENT)
public class TopLayerTooltipHandler {

	private static List<ITextProperties> tooltip;
	private static int tooltipX, tooltipY;
	
	@SubscribeEvent
	public static void renderTick(RenderTickEvent event) {
		if(event.phase == Phase.END && tooltip != null) {
			RenderHelper.renderTooltip(new MatrixStack(), tooltipX, tooltipY, tooltip);
			tooltip = null;
		}
	}
	
	public static void setTooltip(List<String> tooltip, int tooltipX, int tooltipY) {
		TopLayerTooltipHandler.tooltip = tooltip.stream().map(StringTextComponent::new).collect(Collectors.toList());
		TopLayerTooltipHandler.tooltipX = tooltipX;
		TopLayerTooltipHandler.tooltipY = tooltipY;
	}
	
}
