/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 *
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 *
 * File Created @ [26/03/2016, 21:53:17 (GMT)]
 */
package vazkii.quark.vanity.feature;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.actors.threadpool.Arrays;
import vazkii.arl.network.NetworkHandler;
import vazkii.aurelienribon.tweenengine.Tween;
import vazkii.quark.base.client.ContributorRewardHandler;
import vazkii.quark.base.client.ModKeybinds;
import vazkii.quark.base.client.gui.GuiButtonTranslucent;
import vazkii.quark.base.module.Feature;
import vazkii.quark.base.module.ModuleLoader;
import vazkii.quark.base.network.message.MessageRequestEmote;
import vazkii.quark.vanity.client.emotes.CustomEmoteIconResourcePack;
import vazkii.quark.vanity.client.emotes.EmoteBase;
import vazkii.quark.vanity.client.emotes.EmoteDescriptor;
import vazkii.quark.vanity.client.emotes.EmoteHandler;
import vazkii.quark.vanity.client.emotes.ModelAccessor;
import vazkii.quark.vanity.client.gui.GuiButtonEmote;

public class EmoteSystem extends Feature {

	private static final String[] EMOTE_NAMES = new String[] {
			"no",
			"yes",
			"wave", 
			"salute", 
			"cheer",
			"clap", 
			"think",
			"point", 
			"shrug",
			"headbang",
			"weep", 
			"facepalm"
	};

	private static final Set<String> PATREON_EMOTES = ImmutableSet.of(
			"dance", 
			"tpose", 
			"dab",
			"exorcist",
			"zombie"
	); 

	private static List<String> EMOTE_NAME_LIST = new ArrayList(Arrays.asList(EMOTE_NAMES));

	private static final int EMOTE_BUTTON_START = 1800;
	static boolean emotesVisible = false;

	public static boolean customEmoteDebug, emoteCommands;
	public static File emotesDir;
	@SideOnly(Side.CLIENT)
	public static CustomEmoteIconResourcePack resourcePack;

	private String[] enabledEmotes;
	private String[] customEmotes;
	private boolean enableKeybinds;

	@Override
	public void setupConfig() {
		enableKeybinds = loadPropBool("Enable Keybinds", "Should keybinds for emotes be generated? (They're all unbound by default)", true);
		enabledEmotes = loadPropStringList("Enabled Emotes", "The enabled default emotes. Remove from this list to disable them. You can also re-order them, if you feel like it.", EMOTE_NAMES);
		customEmotes = loadPropStringList("Custom Emotes", "The list of Custom Emotes to be loaded.\nWatch the tutorial on Custom Emotes to learn how to make your own: https://youtu.be/ourHUkan6aQ", new String[0]);

		customEmoteDebug = loadPropBool("Custom Emote Dev Mode", "Enable this to make custom emotes read the file every time they're triggered so you can edit on the fly.\nDO NOT ship enabled this in a modpack, please.", false);
		emoteCommands = loadPropBool("Custom Emote Functions", "Allow custom emotes to run function files when a user prompts them.\n"
				+ "To attach a function file to any given emote, simply place a .mcfunction file with the same name as your .emote file (sans extension) in /quark_emotes.\n"
				+ "Command output from emote functions is enabled only if both \"Custom Emote Dev Mode\" and the \"commandBlockOutput\" gamerule are enabled. ", false);

		emotesDir = new File(ModuleLoader.configFile.getParent(), "quark_emotes");
		if(!emotesDir.exists())
			emotesDir.mkdir();
	}

	public static void addResourcePack(List<IResourcePack> packs) {
		packs.add(resourcePack = new CustomEmoteIconResourcePack());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void preInitClient(FMLPreInitializationEvent event) {
		Tween.registerAccessor(ModelBiped.class, new ModelAccessor());

		for(String s : enabledEmotes)
			if(EMOTE_NAME_LIST.contains(s))
				EmoteHandler.addEmote(s);

		for(String s : PATREON_EMOTES)
			EmoteHandler.addEmote(s);
		
		for(String s : customEmotes)
			EmoteHandler.addCustomEmote(s);

		if(enableKeybinds)
			ModKeybinds.initEmoteKeybinds();
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void initGui(GuiScreenEvent.InitGuiEvent.Post event) {
		GuiScreen gui = event.getGui();
		if(gui instanceof GuiChat) {
			List<GuiButton> list = event.getButtonList();
			list.add(new GuiButtonTranslucent(EMOTE_BUTTON_START, gui.width - 76, gui.height - 40, 75, 20, I18n.format("quark.gui.emotes")));

			int i = 0;
			int size = EmoteHandler.emoteMap.size() - 1;
			for(String key : EmoteHandler.emoteMap.keySet()) {
				EmoteDescriptor desc = EmoteHandler.emoteMap.get(key);
				int tier = desc.getTier();

				if(tier > ContributorRewardHandler.localPatronTier)
					continue;

				int x = gui.width - ((i % 3) + 1) * 25 - 1;
				int y = gui.height - 65 - 25 * ((size / 3) - i / 3);

				GuiButton button = new GuiButtonEmote(EMOTE_BUTTON_START + i + 1, x, y, desc);
				button.visible = emotesVisible;
				button.enabled = emotesVisible;
				list.add(button);
				i++;
			}
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void performAction(GuiScreenEvent.ActionPerformedEvent.Pre event) {
		GuiButton button = event.getButton();

		if(button.id == EMOTE_BUTTON_START) {
			event.getGui();
			List<GuiButton> list = event.getButtonList();

			for(GuiButton b : list)
				if(b instanceof GuiButtonEmote) {
					b.visible = !b.visible;
					b.enabled = !b.enabled;
				}

			emotesVisible = !emotesVisible;
		} else if(button instanceof GuiButtonEmote) {
			String name = ((GuiButtonEmote) button).desc.getRegistryName();
			NetworkHandler.INSTANCE.sendToServer(new MessageRequestEmote(name));
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onKeyInput(KeyInputEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		if(mc.inGameHasFocus && enableKeybinds) {
			for(KeyBinding key : ModKeybinds.emoteKeys.keySet())
				if(key.isKeyDown()) {
					String emote = ModKeybinds.emoteKeys.get(key);
					NetworkHandler.INSTANCE.sendToServer(new MessageRequestEmote(emote));
					return;
				}
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void drawHUD(RenderGameOverlayEvent.Post event) {
		if(event.getType() == ElementType.ALL) {
			Minecraft mc = Minecraft.getMinecraft();
			ScaledResolution res = event.getResolution();
			EmoteBase emote = EmoteHandler.getPlayerEmote(mc.player);
			if(emote != null && emote.timeDone < emote.totalTime) {
				ResourceLocation resource = emote.desc.texture;
				int x = res.getScaledWidth() / 2 - 16;
				int y = res.getScaledHeight() / 2 - 60;
				float transparency = 1F;
				float tween = 5F;

				if(emote.timeDone < tween)
					transparency = emote.timeDone / tween;
				else if(emote.timeDone > emote.totalTime - tween)
					transparency = (emote.totalTime - emote.timeDone) / tween;

				GlStateManager.pushMatrix();
				GlStateManager.disableLighting();
				GlStateManager.enableBlend();
				GlStateManager.disableAlpha();

				GlStateManager.color(1F, 1F, 1F, transparency);
				mc.getTextureManager().bindTexture(resource);
				GuiScreen.drawModalRectWithCustomSizedTexture(x, y, 0, 0, 32, 32, 32, 32);
				GlStateManager.enableBlend();

				String name = I18n.format(emote.desc.getUnlocalizedName());
				mc.fontRenderer.drawStringWithShadow(name, res.getScaledWidth() / 2 - mc.fontRenderer.getStringWidth(name) / 2, y + 34, 0xFFFFFF + (((int) (transparency * 255F)) << 24));
				GlStateManager.popMatrix();
			}
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void renderTick(RenderTickEvent event) {
		EmoteHandler.onRenderTick(Minecraft.getMinecraft(), event.phase == Phase.START);
	}

	@Override
	public boolean hasSubscriptions() {
		return isClient();
	}

	@Override
	public boolean requiresMinecraftRestartToEnable() {
		return true;
	}

}
