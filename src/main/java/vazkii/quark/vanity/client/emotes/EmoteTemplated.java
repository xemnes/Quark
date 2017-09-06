package vazkii.quark.vanity.client.emotes;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.player.EntityPlayer;
import vazkii.aurelienribon.tweenengine.Timeline;
import vazkii.quark.base.module.ModuleLoader;

public class EmoteTemplated extends EmoteBase {
	
	public EmoteTemplated(EmoteDescriptor desc, EntityPlayer player, ModelBiped model, ModelBiped armorModel, ModelBiped armorLegsModel) {
		super(desc, player, model, armorModel, armorLegsModel);
		
		if(ModuleLoader.DEBUG_MODE)
			desc.template.readAndMakeTimeline(player, model);
	}

	@Override
	public Timeline getTimeline(EntityPlayer player, ModelBiped model) {
		return desc.template.getTimeline(player, model);
	}

	@Override
	public boolean usesBodyPart(int part) {
		return desc.template.usesBodyPart(part);
	}

}
