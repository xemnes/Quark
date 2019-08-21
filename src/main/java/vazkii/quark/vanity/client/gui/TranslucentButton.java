package vazkii.quark.vanity.client.gui;

import net.minecraft.client.gui.widget.button.Button;

public class TranslucentButton extends Button {

	public TranslucentButton(int xIn, int yIn, int widthIn, int heightIn, String text, IPressable onPress) {
		super(xIn, yIn, widthIn, heightIn, text, onPress);
	}
	
	@Override
	public void blit(int x, int y, int textureX, int textureY, int width, int height) {
		fill(x, y, x + width, y + height, Integer.MIN_VALUE);
	}

}
