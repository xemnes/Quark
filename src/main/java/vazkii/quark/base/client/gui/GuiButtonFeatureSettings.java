package vazkii.quark.base.client.gui;

import net.minecraft.client.gui.GuiButton;

public class GuiButtonFeatureSettings extends GuiButton {

	public final String category;
	
	public GuiButtonFeatureSettings(int x, int y, String category) {
		super(0, x, y, 20, 20, "C");
		this.category = category;
	}

}
