package vazkii.quark.tools.module;

import java.sql.Date;
import java.text.SimpleDateFormat;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ScreenShotHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.client.event.ScreenshotEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.RenderTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import vazkii.quark.base.Quark;
import vazkii.quark.base.handler.QuarkSounds;
import vazkii.quark.base.item.QuarkItem;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleCategory;

@LoadModule(category = ModuleCategory.TOOLS, hasSubscriptions = true, subscribeOn = Dist.CLIENT)
public class CameraModule extends Module {

	private static final int RULER_COLOR = 0x33000000;

	private static final int RULERS = 4;
	private static final int BORERS = 6;
	private static final int OVERLAYS = 5;

	private static final ResourceLocation[] SHADERS = new ResourceLocation[] {
			null,
			new ResourceLocation(Quark.MOD_ID, "shaders/post/grayscale.json"),
			new ResourceLocation(Quark.MOD_ID, "shaders/post/sepia.json"),
			new ResourceLocation(Quark.MOD_ID, "shaders/post/desaturate.json"),
			new ResourceLocation(Quark.MOD_ID, "shaders/post/oversaturate.json"),
			new ResourceLocation(Quark.MOD_ID, "shaders/post/cool.json"),
			new ResourceLocation(Quark.MOD_ID, "shaders/post/warm.json"),
			new ResourceLocation(Quark.MOD_ID, "shaders/post/conjugate.json"),

			new ResourceLocation(Quark.MOD_ID, "shaders/post/redfocus.json"),
			new ResourceLocation(Quark.MOD_ID, "shaders/post/greenfocus.json"),
			new ResourceLocation(Quark.MOD_ID, "shaders/post/bluefocus.json"),
			new ResourceLocation(Quark.MOD_ID, "shaders/post/yellowfocus.json"),

			new ResourceLocation("shaders/post/bumpy.json"),
			new ResourceLocation("shaders/post/notch.json"),
			new ResourceLocation("shaders/post/creeper.json"),
			new ResourceLocation(Quark.MOD_ID, "shaders/post/enderman.json"),

			new ResourceLocation(Quark.MOD_ID, "shaders/post/bits.json"),
			new ResourceLocation("shaders/post/blobs.json"),
			new ResourceLocation("shaders/post/pencil.json"),
			new ResourceLocation(Quark.MOD_ID, "shaders/post/watercolor.json"),
			new ResourceLocation("shaders/post/sobel.json")
	};

	private static int currentHeldItem = -1;
	private static int currShader = 0;
	private static int currRulers = 0;
	private static int currBorders = 0;
	private static int currOverlay = 0;
	private static boolean queuedRefresh = false;
	private static boolean queueScreenshot = false;
	private static boolean screenshotting = false;

	private static Item camera;
	private static boolean currentlyHoldingCamera;

	@Override
	public void construct() {
		camera = new QuarkItem("camera", this, new Item.Properties().maxStackSize(1).group(ItemGroup.TOOLS).rarity(Rarity.UNCOMMON));
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void screenshotTaken(ScreenshotEvent event) {
		screenshotting = false;
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void keystroke(KeyInputEvent event) {
		Minecraft mc = Minecraft.getInstance();
		if(isHoldingCamera(mc) && event.getAction() == GLFW.GLFW_PRESS) {
			int key = event.getKey();
			boolean affected = false;
			boolean sneak = mc.player.isDiscrete();
			System.out.println(key);
			switch(key) {
			case 49: // 1
				currShader = cycle(currShader, SHADERS.length, sneak);
				affected = true;
				break;
			case 50: // 2
				currRulers = cycle(currRulers, RULERS, sneak);
				affected = true;
				break;
			case 51: // 3
				currBorders = cycle(currBorders, BORERS, sneak);
				affected = true;
				break;
			case 52: // 4
				currOverlay = cycle(currOverlay, OVERLAYS, sneak);
				affected = true;
				break;
			case 53: // 5
				if(sneak) {
					currShader = 0;
					currRulers = 0;
					currBorders = 0;
					currOverlay = 0;
					affected = true;
				}
				break;
			case 257: // ENTER
				if(!queueScreenshot && !screenshotting)
					mc.getSoundHandler().play(SimpleSound.master(QuarkSounds.ITEM_CAMERA_SHUTTER, 1.0F));

				queueScreenshot = true;
			}

			if(affected) {
				queuedRefresh = true;
				currentHeldItem = mc.player.inventory.currentItem;
			}
		}
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void renderTick(RenderTickEvent event) {
		Minecraft mc = Minecraft.getInstance();
		
		if(queueScreenshot) {
			screenshotting = true;
			queueScreenshot = false;
			ScreenShotHelper.saveScreenshot(mc.gameDir, mc.getMainWindow().getFramebufferWidth(), mc.getMainWindow().getFramebufferHeight(), mc.getFramebuffer(), (msg) -> {
				mc.execute(() -> {
					mc.ingameGUI.getChatGUI().printChatMessage(msg);
				});
			});
		}
		
		PlayerEntity player = mc.player;
		if(player != null && currentHeldItem != -1 && player.inventory.currentItem != currentHeldItem) {
			player.inventory.currentItem = currentHeldItem;
			currentHeldItem = -1;	
		}

		boolean wasHolding = currentlyHoldingCamera;
		currentlyHoldingCamera = isHoldingCamera(mc);

		if(wasHolding != currentlyHoldingCamera || queuedRefresh)
			refreshShader();

		if(event.phase == Phase.END && currentlyHoldingCamera)
			renderCameraHUD(mc);
	}

	private static void renderCameraHUD(Minecraft mc) {
		MainWindow mw = mc.getMainWindow();
		int twidth = mw.getScaledWidth();
		int theight = mw.getScaledHeight();
		int width = twidth;
		int height = theight;

		int paddingHoriz = 0;
		int paddingVert = 0;
		int paddingColor = 0xFF000000;

		double targetAspect = -1;

		switch(currBorders) {
		case 1: // Square
			targetAspect = 1;
			break;
		case 2: // 4:3
			targetAspect = 4.0 / 3.0;
			break;
		case 3: // 16:9
			targetAspect = 16.0 / 9.0;
			break;
		case 4: // 21:9
			targetAspect = 21.0 / 9.0;
			break;
		case 5: // Polaroid
			int border = (int) (20.0 * ((double) (twidth * theight) / 518400));
			paddingHoriz = border;
			paddingVert = border;
			paddingColor = 0xFFFFFFFF;
			break;
		}

		if(targetAspect > 0) {
			double currAspect = (double) width / (double) height;

			if(currAspect > targetAspect) {
				int desiredWidth = (int) ((double) height * targetAspect);
				paddingHoriz = (width - desiredWidth) / 2;
			} else if (currAspect < targetAspect) {
				int desiredHeight = (int) ((double) width / targetAspect);
				paddingVert = (height - desiredHeight) / 2;
			}
		}

		width -= (paddingHoriz * 2);
		height -= (paddingVert * 2);

		// =============================================== DRAW BORDERS ===============================================
		if(paddingHoriz > 0) {
			Screen.fill(0, 0, paddingHoriz, theight, paddingColor);
			Screen.fill(twidth - paddingHoriz, 0, twidth, theight, paddingColor);
		}

		if(paddingVert > 0) {
			Screen.fill(0, 0, twidth, paddingVert, paddingColor);
			Screen.fill(0, theight - paddingVert, twidth, theight, paddingColor);
		}

		// =============================================== DRAW OVERLAYS ===============================================
		String overlayText = "";
		boolean overlayShadow = true;
		double overlayScale = 2.0;
		int overlayColor = 0xFFFFFFFF;
		int overlayX = -1;
		int overlayY = -1;

		switch(currOverlay) {
		case 1: // Date
			overlayText = new SimpleDateFormat("MM/dd/yyyy").format(new Date(System.currentTimeMillis()));
			overlayColor = 0xf77700;
			break;
		case 2: // Postcard
			overlayText = I18n.format("quark.camera.greetings", mc.getIntegratedServer().getWorldName());
			overlayX = paddingHoriz + 20;
			overlayY = paddingVert + 20;
			overlayScale = 3;
			overlayColor = 0xef5425;
			break;
		case 3: // Watermark
			overlayText = mc.player.getGameProfile().getName();
			overlayScale = 6;
			overlayShadow = false;
			overlayColor = 0x44000000;
			break;
		case 4: // Offhand Item
			overlayText = mc.player.getHeldItemOffhand().getDisplayName().getFormattedText();
			overlayX = twidth / 2 - mc.fontRenderer.getStringWidth(overlayText);
			overlayY = paddingVert + 40;
			break;
		}

		if(overlayX == -1)
			overlayX = twidth - paddingHoriz - mc.fontRenderer.getStringWidth(overlayText) * (int) overlayScale - 40;
		if(overlayY == -1)
			overlayY = theight - paddingVert - 10 - (10 * (int) overlayScale);


		if(!overlayText.isEmpty()) {
			RenderSystem.pushMatrix();
			RenderSystem.translatef(overlayX, overlayY, 0);
			RenderSystem.scaled(overlayScale, overlayScale, 1.0);
			if(overlayShadow)
				mc.fontRenderer.drawStringWithShadow(overlayText, 0, 0, overlayColor);
			else mc.fontRenderer.drawString(overlayText, 0, 0, overlayColor);
			RenderSystem.popMatrix();
		}

		if(!screenshotting) {
			// =============================================== DRAW RULERS ===============================================
			RenderSystem.pushMatrix();
			RenderSystem.translatef(paddingHoriz, paddingVert, 0);
			switch(currRulers) {
			case 1: // Rule of Thirds
				vruler(width / 3, height);
				vruler(width / 3 * 2, height);
				hruler(height / 3, width);
				hruler(height / 3 * 2, width);
				break;
			case 2: // Golden Ratio
				double phi1 = 1 / 2.61;
				double phi2 = 1.61 / 2.61;
				vruler((int) (width * phi1), height);
				vruler((int) (width * phi2), height);
				hruler((int) (height * phi1), width);
				hruler((int) (height * phi2), width);
				break;
			case 3: // Center
				vruler(width / 2, height);
				hruler(height / 2, width);
				break;
			}
			RenderSystem.popMatrix();

			int left = 30;
			int top = theight - 80;

			// =============================================== DRAW SETTINGS ===============================================
			ResourceLocation shader = SHADERS[currShader];
			String text = "none";
			if(shader != null)
				text = shader.getPath().replaceAll(".+/(.+)\\.json", "$1");
			text = TextFormatting.BOLD + "[1] " + TextFormatting.RESET + I18n.format("quark.camera.filter") + TextFormatting.GOLD + I18n.format("quark.camera.filter." + text);
			mc.fontRenderer.drawStringWithShadow(text, left, top, 0xFFFFFF);

			text = TextFormatting.BOLD + "[2] " + TextFormatting.RESET + I18n.format("quark.camera.rulers") + TextFormatting.GOLD + I18n.format("quark.camera.rulers" + currRulers);
			mc.fontRenderer.drawStringWithShadow(text, left, top + 12, 0xFFFFFF);

			text = TextFormatting.BOLD + "[3] " + TextFormatting.RESET + I18n.format("quark.camera.borders") + TextFormatting.GOLD + I18n.format("quark.camera.borders" + currBorders);
			mc.fontRenderer.drawStringWithShadow(text, left, top + 24, 0xFFFFFF);

			text = TextFormatting.BOLD + "[4] " + TextFormatting.RESET + I18n.format("quark.camera.overlay") + TextFormatting.GOLD + I18n.format("quark.camera.overlay" + currOverlay);
			mc.fontRenderer.drawStringWithShadow(text, left, top + 36, 0xFFFFFF);

			text = TextFormatting.BOLD + "[5] " + TextFormatting.RESET + I18n.format("quark.camera.reset");
			mc.fontRenderer.drawStringWithShadow(text, left, top + 48, 0xFFFFFF);

			text = TextFormatting.BOLD + "[ENTER] " + TextFormatting.RESET + I18n.format("quark.camera.shoot");
			mc.fontRenderer.drawStringWithShadow(text, left, top + 60, 0xFFFFFF);

			mc.getItemRenderer().renderItemIntoGUI(mc.player.getHeldItemMainhand(), left - 25, top + 20);
		}
	}

	private static void refreshShader() {
		if(queuedRefresh)
			queuedRefresh = false;

		Minecraft mc = Minecraft.getInstance();
		GameRenderer render = mc.gameRenderer;
		mc.gameSettings.hideGUI = currentlyHoldingCamera;

		if(currentlyHoldingCamera) {
			ResourceLocation shader = SHADERS[currShader];

			if(shader != null) {
				render.loadShader(shader);
				return;
			}
		} 

		render.loadEntityShader(null);
	}

	private static void vruler(int x, int height) {
		Screen.fill(x, 0, x + 1, height, RULER_COLOR);
	}

	private static void hruler(int y, int width) {
		Screen.fill(0, y, width, y + 1, RULER_COLOR);
	}

	private static int cycle(int curr, int max, boolean neg) {
		int val = curr + (neg ? -1 : 1);
		if(val < 0)
			val = max - 1;
		else if(val >= max)
			val = 0;

		return val;
	}

	private static boolean isHoldingCamera(Minecraft mc) {
		return mc.world != null && mc.currentScreen == null && mc.player != null && mc.player.getHeldItemMainhand().getItem() == camera;
	}

}
