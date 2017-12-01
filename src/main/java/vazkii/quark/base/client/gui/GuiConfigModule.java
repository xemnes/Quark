package vazkii.quark.base.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.translation.I18n;
import vazkii.quark.base.module.ConfigHelper;
import vazkii.quark.base.module.Feature;
import vazkii.quark.base.module.GlobalConfig;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleLoader;

public class GuiConfigModule extends GuiConfigBase {

	final Module module;
	final List<Feature> features;
	final String moduleTitle;
	int index;
	
	
	public GuiConfigModule(GuiScreen parent, Module module) {
		super(parent);
		this.module = module;
		
		moduleTitle = "";
		
		features = new ArrayList();
		module.forEachFeature(features::add);
		Collections.sort(features);
		
		index = 0;
	}
	
	public void initGui() {
		super.initGui();
		
		title += " - " + I18n.translateToLocal("quark.config.module." + module.name);

		int startX = width / 2 - 195;
		int startY = height / 6 - 12;
		
		int x = 0, y = 0;
		
		for(int i = 0; i < features.size(); i++) {
			x = startX + i % 2 * 200;
			y = startY + i / 2 * 22;
			
			Feature feature = features.get(i);
			
			buttonList.add(new GuiButtonConfigSetting(0, x + 150, y, feature.prop, true, feature.getFeatureIngameConfigName()));
			
			if(ModuleLoader.config.hasCategory(feature.configCategory))
				buttonList.add(new GuiButtonFeatureSettings(x + 170, y, feature.configCategory));

		}
		x = width / 2;
		
		buttonList.add(backButton = new GuiButton(0, x - 100, y + 74, 200, 20, I18n.translateToLocal("gui.done")));
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		
		if(button instanceof GuiButtonFeatureSettings) {
			GuiButtonFeatureSettings featureButton = (GuiButtonFeatureSettings) button;
			mc.displayGuiScreen(new GuiConfigCategory(this, featureButton.category));
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		drawCenteredString(mc.fontRenderer, moduleTitle, width / 2, 28, 0xFFFFFF);
	}
	
}
