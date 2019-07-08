/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 * <p>
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * <p>
 * File Created @ [Jul 08, 2019, 17:52 AM (EST)]
 */
package vazkii.quark.world.effects;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.quark.base.lib.LibMisc;
import vazkii.quark.base.potion.PotionMod;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = LibMisc.MOD_ID)
public class PotionColorizer extends PotionMod {

	public final int color;

	public final float r;
	public final float g;
	public final float b;

	public PotionColorizer(String name, int color, int iconIndex) {
		super(name, true, color, iconIndex);
		this.color = color;
		this.r = ((color & 0xff0000) >> 16) / 255f;
		this.g = ((color & 0xff00) >> 8) / 255f;
		this.b = (color & 0xff) / 255f;
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void colorize(RenderLivingEvent.Pre event) {
		colorEntity(event.getEntity());
	}

	public static void colorEntity(EntityLivingBase entity) {
		float rMix = 0, gMix = 0, bMix = 0;
		int total = 0;
		for (PotionEffect effect : entity.getActivePotionEffects()) {
			if (effect.getPotion() instanceof PotionColorizer) {
				rMix += ((PotionColorizer) effect.getPotion()).r;
				gMix += ((PotionColorizer) effect.getPotion()).g;
				bMix += ((PotionColorizer) effect.getPotion()).b;
				total++;
			}
		}

		if (total > 0) {
			rMix /= total;
			gMix /= total;
			bMix /= total;
			GlStateManager.color(rMix, gMix, bMix);
		}
	}


	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void colorize(RenderLivingEvent.Post event) {
		for (PotionEffect effect : event.getEntity().getActivePotionEffects()) {
			if (effect.getPotion() instanceof PotionColorizer) {
				GlStateManager.color(1f, 1f, 1f);
				return;
			}
		}
	}

}
