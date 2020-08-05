package vazkii.quark.world.client;

import vazkii.quark.world.gen.RealisticChunkGenerator;

import net.minecraft.client.gui.screen.BiomeGeneratorTypeScreens;
import net.minecraft.world.biome.provider.OverworldBiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.DimensionSettings;

public class RealisticGenScreen extends BiomeGeneratorTypeScreens {

	public RealisticGenScreen() {
		super("quark.realistic");
		field_239068_c_.add(this);
	}

	@Override
	protected ChunkGenerator func_230484_a_(long seed) {
		return new RealisticChunkGenerator(new OverworldBiomeProvider(seed, false, false), seed, DimensionSettings.Preset.field_236122_b_.func_236137_b_());
	}
}
