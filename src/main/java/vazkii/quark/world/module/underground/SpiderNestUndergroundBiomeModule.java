package vazkii.quark.world.module.underground;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.BiomeDictionary.Type;
import vazkii.quark.base.block.QuarkBlock;
import vazkii.quark.base.handler.VariantHandler;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.world.config.UndergroundBiomeConfig;
import vazkii.quark.world.gen.underground.SpiderNestUndergroundBiome;

@LoadModule(category = ModuleCategory.WORLD)
public class SpiderNestUndergroundBiomeModule extends UndergroundBiomeModule {

	public static QuarkBlock cobbedstone;
	
	@Override
	public void start() {
		cobbedstone = new QuarkBlock("cobbedstone", this, ItemGroup.BUILDING_BLOCKS, 
				Block.Properties.create(Material.ROCK, MaterialColor.GRAY)
				.hardnessAndResistance(1.5F, 10F)
				.sound(SoundType.STONE));
		
		VariantHandler.addSlabStairsWall(cobbedstone);
		
		super.start();
	}
	
	@Override
	protected UndergroundBiomeConfig getBiomeConfig() {
		return new UndergroundBiomeConfig(new SpiderNestUndergroundBiome(), 80, Type.PLAINS);
	}

}
