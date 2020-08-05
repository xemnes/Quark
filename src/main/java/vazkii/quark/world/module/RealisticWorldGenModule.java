package vazkii.quark.world.module;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.world.gen.RealisticChunkGenerator;
import vazkii.quark.world.gen.RealisticGenScreen;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

@LoadModule(category = ModuleCategory.WORLD)
public class RealisticWorldGenModule extends Module {

	@Override
	public void construct() {
		Registry.register(Registry.field_239690_aB_, new ResourceLocation("quark", "realistic"), RealisticChunkGenerator.CODEC);
	}

	@OnlyIn(Dist.CLIENT)
	public void constructClient() {
		new RealisticGenScreen();
	}
}
