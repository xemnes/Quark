package vazkii.quark.tweaks.feature;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import net.minecraft.client.resources.AbstractResourcePack;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import vazkii.quark.base.Quark;
import vazkii.quark.base.module.Feature;

public class BetterVanillaTextures extends Feature {

	boolean granite, andesite, diorite, bricks, glass, pumpkinFace;
	
	@Override
	public void setupConfig() {
		granite = loadPropBool("Override Granite", "", true);
		andesite = loadPropBool("Override Andesite", "", true);
		diorite = loadPropBool("Override Diorite", "", true);
		bricks = loadPropBool("Override Bricks", "", true);
		glass = loadPropBool("Override Glass", "", true);
		pumpkinFace = loadPropBool("Override Pumpkin Face", "", true);
	}
	
	@Override
	public void preInitClient(FMLPreInitializationEvent event) {
		overrideBlock("stone_granite", granite);
		overrideBlock("stone_andesite", andesite);
		overrideBlock("stone_diorite", diorite);
		overrideBlock("brick", bricks);
		overrideBlock("glass", glass);
		overrideBlock("pumpkin_face_off", pumpkinFace);
	}
	
	private void overrideBlock(String str, boolean flag) {
		if(flag)
			Quark.proxy.addResourceOverride("textures", "blocks", str, "png");
	}
	
	@Override
	public boolean requiresMinecraftRestartToEnable() {
		return true;
	}
	
}
