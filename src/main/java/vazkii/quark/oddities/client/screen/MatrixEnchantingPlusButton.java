package vazkii.quark.oddities.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;

public class MatrixEnchantingPlusButton extends Button {

	public MatrixEnchantingPlusButton(int x, int y, IPressable onPress) {
		super(x, y, 50, 12, "", onPress);
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		boolean hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
		if(!visible)
			return;
		
		Minecraft.getInstance().textureManager.bindTexture(MatrixEnchantingScreen.BACKGROUND);
		int u = 0;
		int v = 177;
		
		if(!active)
			v += 12;
		else if(hovered)
			v += 24;

		RenderSystem.color3f(1F, 1F, 1F);
		blit(x, y, u, v, width, height);
	}

}
