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
import vazkii.quark.world.gen.underground.ElderPrismarineUndergroundBiome;

@LoadModule(category = ModuleCategory.WORLD)
public class ElderPrismarineUndergroundBiomeModule extends UndergroundBiomeModule {

	public static QuarkBlock elder_prismarine;
	public static Block elder_sea_lantern;
	
	@Override
	public void start() {
		elder_prismarine = new QuarkBlock("elder_prismarine", this, ItemGroup.BUILDING_BLOCKS, 
				Block.Properties.create(Material.ROCK, MaterialColor.ADOBE)
				.hardnessAndResistance(1.5F, 10F)
				.sound(SoundType.STONE));
		
		VariantHandler.addSlabStairsWall(elder_prismarine);
		VariantHandler.addSlabAndStairs(new QuarkBlock("elder_prismarine_bricks", this, ItemGroup.BUILDING_BLOCKS, Block.Properties.from(elder_prismarine)));
		VariantHandler.addSlabAndStairs(new QuarkBlock("dark_elder_prismarine", this, ItemGroup.BUILDING_BLOCKS, Block.Properties.from(elder_prismarine)));
		
		elder_sea_lantern = new QuarkBlock("elder_sea_lantern", this, ItemGroup.BUILDING_BLOCKS, 
				Block.Properties.create(Material.GLASS, MaterialColor.ADOBE)
				.hardnessAndResistance(0.3F)
				.lightValue(15)
				.sound(SoundType.GLASS));
		
		super.start();
	}
	
	@Override
	protected UndergroundBiomeConfig getBiomeConfig() {
		return new UndergroundBiomeConfig(new ElderPrismarineUndergroundBiome(), 200, Type.OCEAN);
	}

}
