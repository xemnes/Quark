package vazkii.quark.world.module;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.world.gen.GenerationStage.Decoration;
import vazkii.quark.base.module.Config;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.base.world.WorldGenHandler;
import vazkii.quark.base.world.WorldGenWeights;
import vazkii.quark.base.world.config.DimensionConfig;
import vazkii.quark.base.world.generator.Generator;
import vazkii.quark.world.block.SpeleothemBlock;
import vazkii.quark.world.gen.SpeleothemGenerator;

@LoadModule(category = ModuleCategory.WORLD)
public class SpeleothemsModule extends Module {
	
	@Config public static DimensionConfig dimensions = DimensionConfig.all();
	
	@Config public static int triesPerChunk = 60;
	@Config public static int speleothemsPerChunk = 12;
	@Config public static int triesPerChunkInNether = 4;
	@Config public static int speleothemsPerChunkInNether = 12;
	@Config public static int maxYlevel = 55;
	
	public static Map<Block, Block> speleothemMapping = new HashMap<>();
	
	@Override
	public void modulesStarted() {
		make("stone", Blocks.STONE, MaterialColor.STONE, false);
		make("netherrack", Blocks.NETHERRACK, MaterialColor.NETHERRACK, true);
		make("granite", Blocks.GRANITE, MaterialColor.DIRT, false);
		make("diorite", Blocks.DIORITE, MaterialColor.QUARTZ, false);
		make("andesite", Blocks.ANDESITE, MaterialColor.STONE, false);
		
		make("marble", NewStoneTypesModule.marbleBlock, MaterialColor.QUARTZ, false).setCondition(() -> NewStoneTypesModule.enableMarble);
		make("limestone", NewStoneTypesModule.limestoneBlock, MaterialColor.STONE, false).setCondition(() -> NewStoneTypesModule.enableLimestone);
		make("jasper", NewStoneTypesModule.jasperBlock, MaterialColor.RED_TERRACOTTA, false).setCondition(() -> NewStoneTypesModule.enableJasper);
		make("slate", NewStoneTypesModule.slateBlock, MaterialColor.ICE, false).setCondition(() -> NewStoneTypesModule.enableSlate);
		make("basalt", NewStoneTypesModule.basaltBlock, MaterialColor.BLACK, false).setCondition(() -> NewStoneTypesModule.enableBasalt);
	}
	
	private SpeleothemBlock make(String name, Block parent, MaterialColor color, boolean nether) {
		SpeleothemBlock block = new SpeleothemBlock(name, this, color, nether); 
		speleothemMapping.put(parent, block);
		return block;
	}
	
	@Override
	public void setup() {
		WorldGenHandler.addGenerator(this, new SpeleothemGenerator(dimensions, Generator.NO_COND), Decoration.UNDERGROUND_DECORATION, WorldGenWeights.SPELEOTHEMS);
	}

}
