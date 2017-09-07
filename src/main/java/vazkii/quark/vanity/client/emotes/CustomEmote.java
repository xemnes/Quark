package vazkii.quark.vanity.client.emotes;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.player.EntityPlayer;
import vazkii.quark.vanity.feature.EmoteSystem;

public class CustomEmote extends TemplateSourcedEmote {

	public CustomEmote(EmoteDescriptor desc, EntityPlayer player, ModelBiped model, ModelBiped armorModel, ModelBiped armorLegsModel) {
		super(desc, player, model, armorModel, armorLegsModel);
	}
	
	@Override
	boolean shouldLoadTimelineOnLaunch() {
		return EmoteSystem.customEmoteDebug;
	}

}
