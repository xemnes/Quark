package vazkii.quark.world.module;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.ItemGroup;
import net.minecraft.world.gen.GenerationStage.Decoration;
import vazkii.quark.base.block.QuarkBlock;
import vazkii.quark.base.handler.VariantHandler;
import vazkii.quark.base.module.Config;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.base.world.WorldGenHandler;
import vazkii.quark.base.world.WorldGenWeights;
import vazkii.quark.base.world.generator.OreGenerator;
import vazkii.quark.world.config.BigStoneClusterConfig;
import vazkii.quark.world.config.StoneTypeConfig;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;
import java.util.function.BooleanSupplier;

@LoadModule(category = ModuleCategory.WORLD)
public class NewStoneTypesModule extends Module {

	@Config(flag = "marble") public static boolean enableMarble = true;
	@Config(flag = "limestone") public static boolean enableLimestone = true;
	@Config(flag = "jasper") public static boolean enableJasper = true;
	@Config(flag = "slate") public static boolean enableSlate = true;
	@Config(flag = "basalt") public static boolean enableBasalt = true;
	
	@Config public static StoneTypeConfig marble = new StoneTypeConfig(false);
	@Config public static StoneTypeConfig limestone = new StoneTypeConfig(false);
	@Config public static StoneTypeConfig jasper = new StoneTypeConfig(false);
	@Config public static StoneTypeConfig slate = new StoneTypeConfig(false);
	@Config public static StoneTypeConfig basalt = new StoneTypeConfig(true);
	
	public static Block marbleBlock, limestoneBlock, jasperBlock, slateBlock, basaltBlock;

	public static Map<Block, Block> polishedBlocks = Maps.newHashMap();
	
	private Queue<Runnable> defers = new ArrayDeque<>();
	
	@Override
	public void construct() {
		marbleBlock = makeStone("marble", marble, BigStoneClustersModule.marble, () -> enableMarble, MaterialColor.QUARTZ);
		limestoneBlock = makeStone("limestone", limestone, BigStoneClustersModule.limestone, () -> enableLimestone, MaterialColor.STONE);
		jasperBlock = makeStone("jasper", jasper, BigStoneClustersModule.jasper, () -> enableJasper, MaterialColor.RED_TERRACOTTA);
		slateBlock = makeStone("slate", slate, BigStoneClustersModule.slate, () -> enableSlate, MaterialColor.ICE);
		basaltBlock = makeStone("basalt", basalt, BigStoneClustersModule.basalt, () -> enableBasalt, MaterialColor.BLACK);
	}
	
	private Block makeStone(String name, StoneTypeConfig config, BigStoneClusterConfig bigConfig, BooleanSupplier enabledCond, MaterialColor color) {
		BooleanSupplier trueEnabledCond = () -> !bigConfig.enabled && enabledCond.getAsBoolean();
		Block.Properties props = Block.Properties.create(Material.ROCK, color).hardnessAndResistance(1.5F, 6.0F);
		
		QuarkBlock normal = new QuarkBlock(name, this, ItemGroup.BUILDING_BLOCKS, props).setCondition(enabledCond);
		QuarkBlock polished = new QuarkBlock("polished_" + name, this, ItemGroup.BUILDING_BLOCKS, props).setCondition(enabledCond);
		polishedBlocks.put(normal, polished);

		VariantHandler.addSlabStairsWall(normal);
		VariantHandler.addSlabAndStairs(polished);
		
		defers.add(() ->
			WorldGenHandler.addGenerator(this, new OreGenerator(config.dimensions, config.oregen, normal.getDefaultState(), OreGenerator.ALL_DIMS_STONE_MATCHER, trueEnabledCond), Decoration.UNDERGROUND_ORES, WorldGenWeights.NEW_STONES)
		);
		
		return normal;
	}
	
	@Override
	public void setup() {
		while(!defers.isEmpty())
			defers.poll().run();
	}
	
}
