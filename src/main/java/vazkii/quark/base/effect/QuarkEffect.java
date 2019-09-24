/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 *
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 *
 * File Created @ [03/07/2016, 17:24:22 (GMT)]
 */
package vazkii.quark.base.effect;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.arl.util.RegistryHelper;

import javax.annotation.Nonnull;

public class QuarkEffect extends Effect {

	public static final ResourceLocation TEXTURE = new ResourceLocation("quark", "textures/misc/potions.png");

	protected final String bareName;

	private final int iconX;
	private final int iconY;

	public QuarkEffect(String name, EffectType type, int color, int iconIndex) {
		super(type, color);
		iconX = iconIndex % 8;
		iconY = iconIndex / 8;
		RegistryHelper.register(this, name);
		bareName = name;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void renderHUDEffect(@Nonnull EffectInstance effect, AbstractGui gui, int x, int y, float z, float alpha) {
		GlStateManager.color4f(1f, 1f, 1f, alpha);
		Minecraft.getInstance().textureManager.bindTexture(TEXTURE);
		gui.blit(x + 3, y + 3, iconX * 18, 198 + iconY * 18, 18, 18);
		GlStateManager.color3f(1f, 1f, 1f);
	}

	@Override
	public void renderInventoryEffect(EffectInstance effect, DisplayEffectsScreen<?> gui, int x, int y, float z) {
		Minecraft.getInstance().textureManager.bindTexture(TEXTURE);
		gui.blit(x + 6, y + 7, iconX * 18, 198 + iconY * 18, 18, 18);
	}
}
