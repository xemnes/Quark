/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 *
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 *
 * File Created @ [26/03/2016, 21:37:17 (GMT)]
 */
package vazkii.quark.vanity.client.emotes;

import com.google.common.collect.ImmutableSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import vazkii.quark.base.lib.LibObfuscation;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class EmoteHandler {

	public static final String CUSTOM_EMOTE_NAMESPACE = "quark_custom";
	public static final String CUSTOM_PREFIX = "custom:";
	
	public static Map<String, EmoteDescriptor> emoteMap = new LinkedHashMap<>();
	private static Map<String, EmoteBase> playerEmotes = new HashMap<>();

	private static int count;
	
	public static void addEmote(String name, Class<? extends EmoteBase> clazz) {
		EmoteDescriptor desc = new EmoteDescriptor(clazz, name, name, count++);
		emoteMap.put(name, desc);
	}
	
	public static void addEmote(String name) {
		addEmote(name, TemplateSourcedEmote.class);
	}

	public static void addCustomEmote(String name) {
		String reg = CUSTOM_PREFIX + name;
		EmoteDescriptor desc = new CustomEmoteDescriptor(name, reg, count++);
		emoteMap.put(reg, desc);
	}
	
	public static void putEmote(AbstractClientPlayer player, String emoteName) {
		if(emoteMap.containsKey(emoteName))
			putEmote(player, emoteMap.get(emoteName));
	}
	
	public static void putEmote(AbstractClientPlayer player, EmoteDescriptor desc) {
		String name = player.getName();
		if(desc == null)
			return;

		ModelBiped model = getPlayerModel(player);
		ModelBiped armorModel = getPlayerArmorModel(player);
		ModelBiped armorLegModel = getPlayerArmorLegModel(player);

		if(model != null && armorModel != null && armorLegModel != null) {
			EmoteBase emote = desc.instantiate(player, model, armorModel, armorLegModel);
			emote.startAllTimelines();
			playerEmotes.put(name, emote);
		}
	}

	public static void updateEmotes(Entity e) {
		if(e instanceof AbstractClientPlayer) {
			AbstractClientPlayer player = (AbstractClientPlayer) e;
			String name = player.getName();
			
			resetPlayer(player);
			
			if(playerEmotes.containsKey(name)) {
				EmoteBase emote = playerEmotes.get(name);
				boolean done = emote.isDone();

				if(!done)
					emote.update();
			}
		}
	}
	
	public static void onRenderTick(Minecraft mc, boolean start) {
		World world = mc.world;
		if(world == null)
			return;
		
		for(EntityPlayer player : world.playerEntities)
			updatePlayerStatus(player);
	}
	
	private static void updatePlayerStatus(EntityPlayer e) {
		if(e instanceof AbstractClientPlayer) {
			AbstractClientPlayer player = (AbstractClientPlayer) e;
			String name = player.getName();

			if(playerEmotes.containsKey(name)) {
				EmoteBase emote = playerEmotes.get(name);
				boolean done = emote.isDone();
				if(done) {
					playerEmotes.remove(name);
					resetPlayer(player);
				}
			} else resetPlayer(player);
		}
	}
	
	public static EmoteBase getPlayerEmote(EntityPlayer player) {
		return playerEmotes.get(player.getName());
	}

	private static RenderPlayer getRenderPlayer(AbstractClientPlayer player) {
		Minecraft mc = Minecraft.getMinecraft();
		RenderManager manager = mc.getRenderManager();
		return manager.getSkinMap().get(player.getSkinType());
	}

	private static ModelBiped getPlayerModel(AbstractClientPlayer player) {
		RenderPlayer render = getRenderPlayer(player);
		if(render != null)
			return render.getMainModel();
		
		return null;
	}

	private static ModelBiped getPlayerArmorModel(AbstractClientPlayer player) {
		RenderPlayer render = getRenderPlayer(player);
		if(render == null)
			return null;
		
		List list = ObfuscationReflectionHelper.getPrivateValue(RenderLivingBase.class, render, LibObfuscation.LAYER_RENDERERS);
		for(int i = 0; i < list.size(); i++)
			if(list.get(i) instanceof LayerBipedArmor)
				return ObfuscationReflectionHelper.getPrivateValue(LayerArmorBase.class, (LayerArmorBase) list.get(i), LibObfuscation.MODEL_ARMOR);

		return null;
	}

	private static ModelBiped getPlayerArmorLegModel(AbstractClientPlayer player) {
		RenderPlayer render = getRenderPlayer(player);
		if(render == null)
			return null;
		
		List list = ObfuscationReflectionHelper.getPrivateValue(RenderLivingBase.class, render, LibObfuscation.LAYER_RENDERERS);
		for(int i = 0; i < list.size(); i++)
			if(list.get(i) instanceof LayerBipedArmor)
				return ObfuscationReflectionHelper.getPrivateValue(LayerArmorBase.class, (LayerArmorBase) list.get(i), LibObfuscation.MODEL_LEGGINGS);
		
		return null;
	}
	
	private static void resetPlayer(AbstractClientPlayer player) {
		resetModel(getPlayerModel(player));
		resetModel(getPlayerArmorModel(player));
		resetModel(getPlayerArmorLegModel(player));
	}

	private static void resetModel(ModelBiped model) {
		if(model != null)
			ImmutableSet.of(model.bipedHead, model.bipedHeadwear, model.bipedBody, model.bipedLeftArm, model.bipedRightArm, model.bipedLeftLeg, model.bipedRightLeg).forEach(EmoteHandler::resetPart);
	}
	
	private static void resetPart(ModelRenderer part) {
		if(part != null)
			part.rotateAngleZ = part.offsetX = part.offsetY = part.offsetZ = 0F;
	}

}
