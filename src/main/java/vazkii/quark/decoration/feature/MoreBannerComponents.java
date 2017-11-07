package vazkii.quark.decoration.feature;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.BannerPattern;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import vazkii.arl.util.ProxyRegistry;
import vazkii.quark.base.module.Feature;

public class MoreBannerComponents extends Feature {

	boolean firstQuarter, secondQuarter, thirdQuarter, fourthQuarter,
		chevron, chevronInverted, flaunche, barry, bendy, bendySinister,
		chevronny, chevronnyInverted, chequy;

	@Override
	public void setupConfig() {
		dragon = loadPropBool("Dragon", "", true);
		eye = loadPropBool("Eye", "", true);
		shield = loadPropBool("Shield", "", true);
		sword = loadPropBool("Sword", "", true);
	}

	@Override
	public void init(FMLInitializationEvent event) {
		addPattern(dragon, "dragon", "dr", ProxyRegistry.newStack(Items.SKULL, 1, 5));
		addPattern(eye, "eye", "ey", ProxyRegistry.newStack(Items.ENDER_EYE));
		addPattern(shield, "shield", "sh", ProxyRegistry.newStack(Items.IRON_CHESTPLATE));
		addPattern(sword, "sword", "sw", ProxyRegistry.newStack(Items.IRON_SWORD));
	}

	public static void addPattern(boolean doit, String name, String id, ItemStack craftingItem) {
		if(!doit)
			return;

		name = "quark_" + name;
		id = "q_" + id;
		EnumHelper.addEnum(BannerPattern.class, name.toUpperCase(), new Class[] { String.class, String.class, ItemStack.class }, new Object[] { name, id, craftingItem });
	}
	
	@Override
	public boolean requiresMinecraftRestartToEnable() {
		return true;
	}
	
}
