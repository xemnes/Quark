package vazkii.quark.management.client.gui;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.recipebook.IRecipeShownListener;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import vazkii.quark.base.handler.MiscUtil;
import vazkii.quark.base.handler.TopLayerTooltipHandler;

public class MiniInventoryButton extends Button {

	private final Consumer<List<String>> tooltip;
	private final int type;
	private final ContainerScreen<?> parent;
	private final int startX;

	private Supplier<Boolean> shiftTexture = () -> false;

	public MiniInventoryButton(ContainerScreen<?> parent, int type, int x, int y, Consumer<List<String>> tooltip, IPressable onPress) {
		super(parent.getGuiLeft() + x, parent.getGuiTop() + y, 10, 10, "", onPress);
		this.parent = parent;
		this.type = type;
		this.tooltip = tooltip;
		this.startX = x;
	}

	public MiniInventoryButton(ContainerScreen<?> parent, int type, int x, int y, String tooltip, IPressable onPress) {
		this(parent, type, x, y, (t) -> t.add(I18n.format(tooltip)), onPress);
	}

	public MiniInventoryButton setTextureShift(Supplier<Boolean> func) {
		shiftTexture = func;
		return this;
	}

	@Override
	public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
		if(parent instanceof IRecipeShownListener)
			x = parent.getGuiLeft() + startX;

		super.render(p_render_1_, p_render_2_, p_render_3_);
	}

	@Override
	public void renderButton(int mouseX, int mouseY, float pticks) {
		Minecraft mc = Minecraft.getInstance();
		mc.getTextureManager().bindTexture(MiscUtil.GENERAL_ICONS);
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, alpha);
		GlStateManager.disableLighting();
		GlStateManager.enableBlend();
		GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

		int u = type * width;
		int v = 25 + (isHovered ? height : 0);
		if(shiftTexture.get())
			v += (height * 2);

		blit(x, y, u, v, width, height);

		if(isHovered)
			TopLayerTooltipHandler.setTooltip(getTooltip(), mouseX, mouseY);
	}

	@Override
	protected String getNarrationMessage() {
		List<String> tooltip = getTooltip();
		return tooltip.isEmpty() ? "" : I18n.format("gui.narrate.button", getTooltip().get(0));
	}

	public List<String> getTooltip() {
		List<String> list = new LinkedList<>();
		tooltip.accept(list);
		return list;
	}

}
