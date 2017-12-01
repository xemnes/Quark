package vazkii.quark.base.client.gui;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.translation.I18n;
import vazkii.quark.base.module.GlobalConfig;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleLoader;

public class GuiConfigRoot extends GuiConfigBase {

	public GuiConfigRoot(GuiScreen parent) {
		super(parent);
	}

	public void initGui() {
		super.initGui();
		
		int startX = width / 2 - 175;
		int startY = height / 6 - 12;

		int x = 0, y = 0, i = 0;
		
		Set<Module> modules = new TreeSet();
		modules.addAll(ModuleLoader.moduleInstances.values());

		for(Module module : modules) {
			x = startX + i % 2 * 180;
			y = startY + i / 2 * 22;

			buttonList.add(new GuiButtonModule(i, x, y, module));
			buttonList.add(new GuiButtonConfigSetting(i, x + 150, y, module.prop, false));

			i++;
		}

		x = width / 2;
		y = startY + 140;
		buttonList.add(new GuiButtonConfigSetting(0, x + 80, y, GlobalConfig.qButtonProp, true));
		buttonList.add(new GuiButton(0, x - 100, y + 22, 200, 20, I18n.translateToLocal("quark.config.general")));
		buttonList.add(new GuiButton(0, x - 100, y + 44, 98, 20, I18n.translateToLocal("quark.config.import")));
		buttonList.add(new GuiButton(0, x + 2, y + 44, 98, 20, I18n.translateToLocal("quark.config.opensite")));

		buttonList.add(backButton = new GuiButton(i, x - 100, y + 74, 200, 20, I18n.translateToLocal("gui.done")));
	}



}
