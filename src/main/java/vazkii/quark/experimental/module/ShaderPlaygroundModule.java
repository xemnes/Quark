package vazkii.quark.experimental.module;

import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.RenderTickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import vazkii.quark.base.Quark;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleCategory;

@LoadModule(category = ModuleCategory.EXPERIMENTAL, enabledByDefault = false, hasSubscriptions = true, subscribeOn = Dist.CLIENT)
public class ShaderPlaygroundModule extends Module {

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
			
			new ResourceLocation("shaders/post/invert.json"),
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
	private static int currShader = 0;
	
	@SubscribeEvent	
	public void itemUse(PlayerInteractEvent.RightClickItem event) {
		if(event.getItemStack().getItem() == Items.SPIDER_EYE && event.getHand() == Hand.MAIN_HAND && Thread.currentThread().toString().contains("Render thread")) {
			if(event.getPlayer().isDiscrete())
				currShader--;
			else currShader++;
			
			if(currShader >= SHADERS.length)
				currShader = 0;
			else if(currShader < 0)
				currShader = SHADERS.length - 1;
			
			ResourceLocation shader = SHADERS[currShader];
			GameRenderer render = Minecraft.getInstance().gameRenderer;
			
			if(shader == null)
				render.loadEntityShader(null);
			else render.loadShader(shader);
			
			event.setResult(Result.ALLOW);
		}
	}
	
	@SubscribeEvent
	public void renderTick(RenderTickEvent event) {
		if(event.phase == Phase.END) {
			Minecraft mc = Minecraft.getInstance();
			
			if(mc.world != null && mc.currentScreen == null && mc.player != null && mc.player.getHeldItemMainhand().getItem() == Items.SPIDER_EYE) {
				ResourceLocation shader = SHADERS[currShader];
				String text = "none";
				if(shader != null)
					text = shader.getPath().replaceAll(".+/(.+)\\.json", "$1");
				text = I18n.format("quark.filter." + text);
				
				mc.fontRenderer.drawStringWithShadow(text, 20, 20, 0xFFFFFF);
				
				MainWindow mw = mc.getMainWindow();
				int width = mw.getScaledWidth();
				int height = mw.getScaledHeight();
				
				Screen.fill(width / 3, 0, width / 3 + 1, height, 0x33000000);
				Screen.fill(width / 3 * 2, 0, width / 3 * 2 + 1, height, 0x33000000);
				
				Screen.fill(0, height / 3, width, height / 3 + 1, 0x33000000);
				Screen.fill(0, height / 3 * 2, width, height / 3 * 2 + 1, 0x33000000);
			}
		}
	}

}
