package vazkii.quark.vanity.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import vazkii.quark.base.handler.MiscUtil;
import vazkii.quark.vanity.client.emote.EmoteDescriptor;
import vazkii.quark.vanity.module.EmotesModule;

public class EmoteButton extends TranslucentButton {

	public final EmoteDescriptor desc;

	public EmoteButton(int x, int y, EmoteDescriptor desc, IPressable onPress) {
		super(x, y, EmotesModule.EMOTE_BUTTON_WIDTH - 1, EmotesModule.EMOTE_BUTTON_WIDTH - 1, "", onPress);
		this.desc = desc;
	}

	@Override
	public void renderButton(int mouseX, int mouseY, float partial) {
		super.renderButton(mouseX, mouseY, partial);

		if(visible) {
			Minecraft mc = Minecraft.getInstance();
			mc.getTextureManager().bindTexture(desc.texture);
			GlStateManager.color3f(1F, 1F, 1F);
			blit(x + 4, y + 4, 0, 0, 16, 16, 16, 16);

			ResourceLocation tierTexture = desc.getTierTexture();
			if(tierTexture != null) {
				mc.getTextureManager().bindTexture(tierTexture);
				blit(x + 4, y + 4, 0, 0, 16, 16, 16, 16);
			}
			
			boolean hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
			if(hovered) {
				String name = desc.getLocalizedName();
				
				mc.getTextureManager().bindTexture(MiscUtil.GENERAL_ICONS);
				int w = mc.fontRenderer.getStringWidth(name);
				int left = x - w;
				int top = y - 8;
				
				GlStateManager.pushMatrix();
				GlStateManager.color3f(1F, 1F, 1F);
				GlStateManager.translated(0, 0, 100);
				blit(left, top, 242, 9, 5, 17, 256, 256);
				for(int i = 0; i < w; i++)
					blit(left + i + 5, top, 248, 9, 1, 17, 256, 256);
				blit(left + w + 5, top, 250, 9, 6, 17, 256, 256);

				mc.fontRenderer.drawString(name, left + 5, top + 3, 0);
				GlStateManager.popMatrix();
			}
		}
	}
	
}
