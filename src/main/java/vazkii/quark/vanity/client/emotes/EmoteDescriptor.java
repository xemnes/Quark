package vazkii.quark.vanity.client.emotes;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EmoteDescriptor {

	public final Class<? extends EmoteBase> clazz;
	public final int index;
	public final String name;
	public final String regName;
	public final ResourceLocation texture;
	public final EmoteTemplate template;

	private int tier;
	
	public EmoteDescriptor(Class<? extends EmoteBase> clazz, String name, String regName, int index) {
		this(clazz, name, regName, index, new ResourceLocation("quark", "textures/emotes/" + name + ".png"), new EmoteTemplate(name + ".emote"));
	}
	
	public EmoteDescriptor(Class<? extends EmoteBase> clazz, String name, String regName, int index, ResourceLocation texture, EmoteTemplate template) {
		this.clazz = clazz;
		this.index = index;
		this.name = name;
		this.regName = regName;
		this.texture = texture;
		this.template = template;
		this.tier = template.tier;
	}
	
	public String getTranslationKey() {
		return "quark.emote." + name;
	}
	
	@SideOnly(Side.CLIENT)
	public String getLocalizedName() {
		return I18n.format(getTranslationKey());
	}
	
	public String getRegistryName() {
		return regName;
	}
	
	public int getTier() {
		return tier;
	}
	
	public EmoteBase instantiate(EntityPlayer player, ModelBiped model, ModelBiped armorModel, ModelBiped armorLegModel) {
		try {
			return clazz.getConstructor(EmoteDescriptor.class, EntityPlayer.class, ModelBiped.class, ModelBiped.class, ModelBiped.class).newInstance(this, player, model, armorModel, armorLegModel);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
