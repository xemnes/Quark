/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 * <p>
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * <p>
 * File Created @ [Jul 13, 2019, 13:34 AM (EST)]
 */
package vazkii.quark.world.feature;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.quark.base.Quark;
import vazkii.quark.base.lib.LibEntityIDs;
import vazkii.quark.base.module.Feature;
import vazkii.quark.world.client.render.RenderFoxhound;
import vazkii.quark.world.entity.EntityFoxhound;

public class Foxhounds extends Feature {
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		String foxName = "quark:foxhound";

		EntityRegistry.registerModEntity(new ResourceLocation(foxName), EntityFoxhound.class, foxName, LibEntityIDs.FOXHOUND, Quark.instance, 80, 3, true, 0x890d0d, 0xf2af4b);
	}


	@Override
	@SideOnly(Side.CLIENT)
	public void preInitClient() {
		RenderingRegistry.registerEntityRenderingHandler(EntityFoxhound.class, RenderFoxhound::new);
	}

}
