package vazkii.quark.content;

import vazkii.quark.base.module.Module;
import vazkii.quark.content.feature.AncientTomes;
import vazkii.quark.content.feature.ColorRunes;
import vazkii.quark.content.feature.ExtraArrows;
import vazkii.quark.content.feature.SlimeBucket;

public class QuarkContent extends Module {
	
	@Override
	public void addFeatures() {
		registerFeature(new AncientTomes());
		registerFeature(new SlimeBucket());
		registerFeature(new ColorRunes());
		registerFeature(new ExtraArrows());
	}

}
