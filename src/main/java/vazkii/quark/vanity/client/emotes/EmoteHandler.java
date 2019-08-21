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

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public final class EmoteHandler {

	public static final String CUSTOM_EMOTE_NAMESPACE = "quark_custom";
	public static final String CUSTOM_PREFIX = "custom:";
	
	public static final Map<String, EmoteDescriptor> emoteMap = new LinkedHashMap<>();
	private static final Map<String, EmoteBase> playerEmotes = new HashMap<>();

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
	
	public static void putEmote(AbstractClientPlayerEntity player, String emoteName, int tier) {
		if(emoteMap.containsKey(emoteName)) {
			putEmote(player, emoteMap.get(emoteName), tier);
		}
	}
	
	public static void putEmote(AbstractClientPlayerEntity player, EmoteDescriptor desc, int tier) {
		String name = player.getGameProfile().getName();
		if(desc == null)
			return;

		if (desc.getTier() > tier)
			return;

		BipedModel model = getPlayerModel(player);
		BipedModel armorModel = getPlayerArmorModel(player);
		BipedModel armorLegModel = getPlayerArmorLegModel(player);

		if(model != null && armorModel != null && armorLegModel != null) {
			resetPlayer(player);
			EmoteBase emote = desc.instantiate(player, model, armorModel, armorLegModel);
			emote.startAllTimelines();
			playerEmotes.put(name, emote);
		}
	}

	public static void updateEmotes(Entity e) {
		if(e instanceof AbstractClientPlayerEntity) {
			AbstractClientPlayerEntity player = (AbstractClientPlayerEntity) e;
			String name = player.getGameProfile().getName();
			
			resetPlayer(player);
			
			if(playerEmotes.containsKey(name)) {
				EmoteBase emote = playerEmotes.get(name);
				boolean done = emote.isDone();

				if(!done)
					emote.update();
			}
		}
	}

	public static void preRender(PlayerEntity player) {
		EmoteBase emote = getPlayerEmote(player);
		if (emote != null) {
			GlStateManager.pushMatrix();
			emote.rotateAndOffset();
		}
	}

	public static void postRender(PlayerEntity player) {
		EmoteBase emote = getPlayerEmote(player);
		if (emote != null) {
			GlStateManager.popMatrix();
		}
	}
	
	public static void onRenderTick(Minecraft mc) {
		World world = mc.world;
		if(world == null)
			return;
		
		for(PlayerEntity player : world.getPlayers())
			updatePlayerStatus(player);
	}
	
	private static void updatePlayerStatus(PlayerEntity e) {
		if(e instanceof AbstractClientPlayerEntity) {
			AbstractClientPlayerEntity player = (AbstractClientPlayerEntity) e;
			String name = player.getGameProfile().getName();

			if(playerEmotes.containsKey(name)) {
				EmoteBase emote = playerEmotes.get(name);
				boolean done = emote.isDone();
				if(done) {
					playerEmotes.remove(name);
					resetPlayer(player);
				} else
					emote.update();
			} else resetPlayer(player);
		}
	}
	
	public static EmoteBase getPlayerEmote(PlayerEntity player) {
		return playerEmotes.get(player.getGameProfile().getName());
	}

	private static PlayerRenderer getRenderPlayer(AbstractClientPlayerEntity player) {
		Minecraft mc = Minecraft.getInstance();
		EntityRendererManager manager = mc.getRenderManager();
		return manager.getSkinMap().get(player.getSkinType());
	}

	private static BipedModel getPlayerModel(AbstractClientPlayerEntity player) {
		PlayerRenderer render = getRenderPlayer(player);
		if(render != null)
			return render.getEntityModel();
		
		return null;
	}

	private static BipedModel getPlayerArmorModel(AbstractClientPlayerEntity player) {
		PlayerRenderer render = getRenderPlayer(player);
		if(render == null)
			return null;
		
//		List list = ObfuscationReflectionHelper.getPrivateValue(LivingRenderer.class, render, LibObfuscation.LAYER_RENDERERS);
//		for (Object aList : list)
//			if (aList instanceof BipedArmorLayer)
//				return ObfuscationReflectionHelper.getPrivateValue(ArmorLayer.class, (ArmorLayer) aList, LibObfuscation.MODEL_ARMOR);

		return null;
	}

	private static BipedModel getPlayerArmorLegModel(AbstractClientPlayerEntity player) {
		PlayerRenderer render = getRenderPlayer(player);
		if(render == null)
			return null;
		
//		List list = ObfuscationReflectionHelper.getPrivateValue(LivingRenderer.class, render, LibObfuscation.LAYER_RENDERERS);
//		for (Object aList : list)
//			if (aList instanceof BipedArmorLayer)
//				return ObfuscationReflectionHelper.getPrivateValue(ArmorLayer.class, (ArmorLayer) aList, LibObfuscation.MODEL_LEGGINGS);
		
		return null;
	}
	
	private static void resetPlayer(AbstractClientPlayerEntity player) {
		resetModel(getPlayerModel(player));
		resetModel(getPlayerArmorModel(player));
		resetModel(getPlayerArmorLegModel(player));
	}

	private static void resetModel(BipedModel model) {
		if (model != null) {
			resetPart(model.bipedHead);
			resetPart(model.bipedHeadwear);
			resetPart(model.bipedBody);
			resetPart(model.bipedLeftArm);
			resetPart(model.bipedRightArm);
			resetPart(model.bipedLeftLeg);
			resetPart(model.bipedRightLeg);
			if (model instanceof PlayerModel) {
				PlayerModel playerModel = (PlayerModel) model;
				resetPart(playerModel.bipedBodyWear);
				resetPart(playerModel.bipedLeftArmwear);
				resetPart(playerModel.bipedRightArmwear);
				resetPart(playerModel.bipedLeftLegwear);
				resetPart(playerModel.bipedRightLegwear);
				resetPart(ModelAccessor.getEarsModel(playerModel));
			}

			ModelAccessor.INSTANCE.resetModel(model);
		}
	}
	
	private static void resetPart(RendererModel part) {
		if(part != null)
			part.rotateAngleZ = part.offsetX = part.offsetY = part.offsetZ = 0F;
	}

}
