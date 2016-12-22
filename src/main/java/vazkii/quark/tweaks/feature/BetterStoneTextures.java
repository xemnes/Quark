package vazkii.quark.tweaks.feature;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import net.minecraft.client.resources.AbstractResourcePack;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import vazkii.quark.base.Quark;
import vazkii.quark.base.module.Feature;

public class BetterStoneTextures extends Feature {

	@Override
	public void preInitClient(FMLPreInitializationEvent event) {
		Quark.proxy.addResourceOverride("textures", "blocks", "stone_granite", "png");
		Quark.proxy.addResourceOverride("textures", "blocks", "stone_andesite", "png");
		Quark.proxy.addResourceOverride("textures", "blocks", "stone_diorite", "png");
	}
	
	@Override
	public boolean requiresMinecraftRestartToEnable() {
		return true;
	}
	
}
