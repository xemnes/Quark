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
package vazkii.quark.base.potion;

import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.util.ProxyRegistry;
import vazkii.quark.base.lib.LibMisc;

public class PotionMod extends Potion {

	public static ResourceLocation TEXTURE = new ResourceLocation("quark", "textures/misc/potions.png");

	String bareName;

	public PotionMod(String name, boolean badEffect, int color, int iconIndex) {
		super(badEffect, color);
		setIconIndex(iconIndex % 8, iconIndex / 8);
		setPotionName(name);
		setRegistryName(new ResourceLocation(LibMisc.PREFIX_MOD + name));
		ProxyRegistry.register(this);
		bareName = name;
	}

	@Override
	public String getName() {
		return "quark.potion." + bareName + ".name";
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getStatusIconIndex() {
		Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);

		return super.getStatusIconIndex();
	}

}
