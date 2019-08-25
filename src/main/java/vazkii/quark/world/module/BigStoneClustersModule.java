package vazkii.quark.world.module;

import com.google.common.base.Supplier;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraftforge.common.BiomeDictionary.Type;
import vazkii.quark.base.module.Config;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.base.world.WorldGenHandler;
import vazkii.quark.base.world.WorldGenWeights;
import vazkii.quark.world.config.BigStoneClusterConfig;
import vazkii.quark.world.gen.BigStoneClusterGenerator;

@LoadModule(category = ModuleCategory.WORLD)
public class BigStoneClustersModule extends Module {

	@Config public static BigStoneClusterConfig granite = new BigStoneClusterConfig(false, Type.MOUNTAIN, Type.HILLS);
	@Config public static BigStoneClusterConfig diorite = new BigStoneClusterConfig(false, Type.SAVANNA, Type.JUNGLE, Type.MUSHROOM);
	@Config public static BigStoneClusterConfig andesite = new BigStoneClusterConfig(false, Type.FOREST);
	@Config public static BigStoneClusterConfig marble = new BigStoneClusterConfig(false, Type.PLAINS);
	@Config public static BigStoneClusterConfig limestone = new BigStoneClusterConfig(false, Type.SWAMP, Type.OCEAN);
	@Config public static BigStoneClusterConfig jasper = new BigStoneClusterConfig(false, Type.MESA, Type.SANDY);
	@Config public static BigStoneClusterConfig slate = new BigStoneClusterConfig(false, Type.COLD);
	@Config public static BigStoneClusterConfig basalt = new BigStoneClusterConfig(true, Type.NETHER); // TODO change values
	
	@Override
	public void setup() {
		Supplier<Boolean> alwaysTrue = () -> true;
		add(granite, Blocks.GRANITE, alwaysTrue);
		add(diorite, Blocks.DIORITE, alwaysTrue);
		add(andesite, Blocks.ANDESITE, alwaysTrue);
		
		add(marble, NewStoneTypesModule.marbleBlock, () -> NewStoneTypesModule.enableMarble);
		add(limestone, NewStoneTypesModule.limestoneBlock, () -> NewStoneTypesModule.enableLimestone);
		add(jasper, NewStoneTypesModule.jasperBlock, () -> NewStoneTypesModule.enableJasper);
		add(slate, NewStoneTypesModule.slateBlock, () -> NewStoneTypesModule.enableSlate);
		add(basalt, NewStoneTypesModule.basaltBlock, () -> NewStoneTypesModule.enableBasalt);
	}
	
	private void add(BigStoneClusterConfig config, Block block, Supplier<Boolean> condition) {
		WorldGenHandler.addGenerator(new BigStoneClusterGenerator(config, block.getDefaultState(), condition), Decoration.UNDERGROUND_DECORATION, WorldGenWeights.BIG_STONE_CLUSTERS);
	}
	
}
