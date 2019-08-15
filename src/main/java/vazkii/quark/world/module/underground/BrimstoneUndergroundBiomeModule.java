package vazkii.quark.world.module.underground;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.BiomeDictionary.Type;
import vazkii.quark.base.block.QuarkBlock;
import vazkii.quark.base.moduleloader.LoadModule;
import vazkii.quark.base.moduleloader.ModuleCategory;
import vazkii.quark.world.config.UndergroundBiomeConfig;
import vazkii.quark.world.gen.underground.BrimstoneUndergroundBiome;

@LoadModule(category = ModuleCategory.WORLD)
public class BrimstoneUndergroundBiomeModule extends UndergroundBiomeModule {

	public static Block brimstone;
	
	@Override
	public void start() {
		brimstone = new QuarkBlock("brimstone", this, ItemGroup.BUILDING_BLOCKS, 
				Block.Properties.create(Material.ROCK, MaterialColor.RED)
				.hardnessAndResistance(1.5F, 10F)
				.sound(SoundType.STONE));
		
		super.start();
	}
	
	@Override
	protected UndergroundBiomeConfig getBiomeConfig() {
		return new UndergroundBiomeConfig(new BrimstoneUndergroundBiome(), 80, Type.MESA);
	}

}
