package vazkii.quark.world.module;

import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.quark.base.module.Config;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.base.world.WorldGenHandler;
import vazkii.quark.base.world.WorldGenWeights;
import vazkii.quark.world.config.BigStoneClusterConfig;
import vazkii.quark.world.gen.BigStoneClusterGenerator;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

@LoadModule(category = ModuleCategory.WORLD)
public class BigStoneClustersModule extends Module {

	@Config public static BigStoneClusterConfig granite = new BigStoneClusterConfig(false, Type.MOUNTAIN, Type.HILLS);
	@Config public static BigStoneClusterConfig diorite = new BigStoneClusterConfig(false, Type.SAVANNA, Type.JUNGLE, Type.MUSHROOM);
	@Config public static BigStoneClusterConfig andesite = new BigStoneClusterConfig(false, Type.FOREST);
	@Config public static BigStoneClusterConfig marble = new BigStoneClusterConfig(false, Type.PLAINS);
	@Config public static BigStoneClusterConfig limestone = new BigStoneClusterConfig(false, Type.SWAMP, Type.OCEAN);
	@Config public static BigStoneClusterConfig jasper = new BigStoneClusterConfig(false, Type.MESA, Type.SANDY);
	@Config public static BigStoneClusterConfig slate = new BigStoneClusterConfig(false, Type.COLD);
	@Config public static BigStoneClusterConfig basalt = new BigStoneClusterConfig(true, 19, 6, 20, 20, 120, Type.NETHER);
	
	@Config public static List<String> blocksToReplace = Lists.newArrayList(
			"minecraft:stone", "minecraft:andesite", "minecraft:diorite", "minecraft:granite", "minecraft:netherrack", "minecraft:end_stone",
			"quark:marble", "quark:limestone", "quark:jasper", "quark:slate", "quark:basalt");
	
	public static Predicate<Block> blockReplacePredicate = Predicates.alwaysFalse();
	
	@Override
	public void setup() {
		BooleanSupplier alwaysTrue = () -> true;
		add(granite, Blocks.GRANITE, alwaysTrue);
		add(diorite, Blocks.DIORITE, alwaysTrue);
		add(andesite, Blocks.ANDESITE, alwaysTrue);
		
		add(marble, NewStoneTypesModule.marbleBlock, () -> NewStoneTypesModule.enableMarble);
		add(limestone, NewStoneTypesModule.limestoneBlock, () -> NewStoneTypesModule.enableLimestone);
		add(jasper, NewStoneTypesModule.jasperBlock, () -> NewStoneTypesModule.enableJasper);
		add(slate, NewStoneTypesModule.slateBlock, () -> NewStoneTypesModule.enableSlate);
		add(basalt, NewStoneTypesModule.basaltBlock, () -> NewStoneTypesModule.enableBasalt);
		
		conditionalize(Blocks.GRANITE, () -> (!enabled || !granite.enabled));
		conditionalize(Blocks.DIORITE, () -> (!enabled || !diorite.enabled));
		conditionalize(Blocks.ANDESITE, () -> (!enabled || !andesite.enabled));
	}
	
	private void add(BigStoneClusterConfig config, Block block, BooleanSupplier condition) {
		WorldGenHandler.addGenerator(this, new BigStoneClusterGenerator(config, block.getDefaultState(), condition), Decoration.UNDERGROUND_DECORATION, WorldGenWeights.BIG_STONE_CLUSTERS);
	}
	
	private void conditionalize(Block block, BooleanSupplier condition) {
		BiPredicate<Feature<? extends IFeatureConfig>, IFeatureConfig> pred = (feature, config) -> {
			if(config instanceof OreFeatureConfig) {
				OreFeatureConfig oconfig = (OreFeatureConfig) config;
				return oconfig.state.getBlock() == block;
			}
			
			return false;
		};
		
		WorldGenHandler.conditionalizeFeatures(GenerationStage.Decoration.UNDERGROUND_ORES, pred, condition);
	}
	
	@Override
	public void configChanged() {
		blockReplacePredicate = Predicates.alwaysFalse();
		for(String s : blocksToReplace) {
			Block b = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(s));
			if(b != null && b != Blocks.AIR)
				blockReplacePredicate = blockReplacePredicate.or(Predicates.equalTo(b));
		}
	}
	
}
