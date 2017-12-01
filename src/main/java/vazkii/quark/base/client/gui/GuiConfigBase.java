package vazkii.quark.base.client.gui;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.translation.I18n;
import vazkii.quark.base.module.ModuleLoader;

public class GuiConfigBase extends GuiScreen {

	String title;
	GuiScreen parent;
	
	GuiButton backButton;
	
	public GuiConfigBase(GuiScreen parent) {
		this.parent = parent;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		title = I18n.translateToLocal("quark.config.title");
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawCenteredString(fontRenderer, title, width / 2, 15, 0xFFFFFF);
        
        super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override 
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if(keyCode == 1) // Esc
        	returnToParent();
    }
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		
		if(backButton != null && button == backButton)
			returnToParent();
		
		if(button instanceof GuiButtonConfigSetting) {
			GuiButtonConfigSetting configButton = (GuiButtonConfigSetting) button;
			configButton.prop.set(!configButton.prop.getBoolean());
			ModuleLoader.loadConfig();
		}
	}
	
	void returnToParent() {
        mc.displayGuiScreen(parent);

        if(mc.currentScreen == null)
            mc.setIngameFocus();
	}
	
}
