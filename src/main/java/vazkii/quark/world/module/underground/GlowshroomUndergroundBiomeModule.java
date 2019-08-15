package vazkii.quark.world.module.underground;

import net.minecraft.block.Block;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.BiomeDictionary.Type;
import vazkii.arl.util.RegistryHelper;
import vazkii.quark.base.module.Config;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.world.block.GlowceliumBlock;
import vazkii.quark.world.block.GlowshroomBlock;
import vazkii.quark.world.block.HugeGlowshroomBlock;
import vazkii.quark.world.config.UndergroundBiomeConfig;
import vazkii.quark.world.gen.underground.GlowshroomUndergroundBiome;

@LoadModule(category = ModuleCategory.WORLD)
public class GlowshroomUndergroundBiomeModule extends UndergroundBiomeModule {

	@Config public static int glowshroomGrowthRate = 20;
	@Config public static boolean enableHugeGlowshrooms = true;
	
	public static Block glowcelium, glowshroom, glowshroom_block, glowshroom_stem;
	
	@Override
	public void start() {
		glowcelium = new GlowceliumBlock(this);
		glowshroom = new GlowshroomBlock(this);
		glowshroom_block = new HugeGlowshroomBlock("glowshroom_block", this);
		glowshroom_stem = new HugeGlowshroomBlock("glowshroom_stem", this);
		
		RegistryHelper.registerBlock(new FlowerPotBlock(glowshroom,  Block.Properties.create(Material.MISCELLANEOUS).hardnessAndResistance(0F).lightValue(14)), "potted_glowshroom", false);
		
		super.start();
	}
	
	@Override
	protected UndergroundBiomeConfig getBiomeConfig() {
		return new UndergroundBiomeConfig(new GlowshroomUndergroundBiome(), 80, Type.MOUNTAIN, Type.MUSHROOM);
	}
	
}
