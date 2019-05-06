package vazkii.quark.vanity.client.emotes;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.player.EntityPlayer;
import vazkii.aurelienribon.tweenengine.Timeline;
import vazkii.quark.base.Quark;
import vazkii.quark.base.module.ModuleLoader;

public class TemplateSourcedEmote extends EmoteBase {
	
	public TemplateSourcedEmote(EmoteDescriptor desc, EntityPlayer player, ModelBiped model, ModelBiped armorModel, ModelBiped armorLegsModel) {
		super(desc, player, model, armorModel, armorLegsModel);

		if(shouldLoadTimelineOnLaunch()) {
			Quark.LOG.debug("Loading emote " + desc.getTranslationKey());
			desc.template.readAndMakeTimeline(model);
		}
	}
	
	boolean shouldLoadTimelineOnLaunch() {
		return ModuleLoader.DEBUG_MODE;
	}

	@Override
	public Timeline getTimeline(EntityPlayer player, ModelBiped model) {
		return desc.template.getTimeline(model);
	}

	@Override
	public boolean usesBodyPart(int part) {
		return desc.template.usesBodyPart(part);
	}

}
