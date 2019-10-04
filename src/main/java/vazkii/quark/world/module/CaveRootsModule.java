package vazkii.quark.world.module;

import net.minecraft.block.Block;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.world.gen.GenerationStage.Decoration;
import vazkii.quark.base.item.QuarkItem;
import vazkii.quark.base.module.Config;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.base.world.WorldGenHandler;
import vazkii.quark.base.world.WorldGenWeights;
import vazkii.quark.base.world.config.DimensionConfig;
import vazkii.quark.world.block.RootBlock;
import vazkii.quark.world.gen.CaveRootGenerator;

@LoadModule(category = ModuleCategory.WORLD)
public class CaveRootsModule extends Module {

	@Config public static int chunkAttempts = 300;
	@Config public static int minY = 16;
	@Config public static int maxY = 52;
	@Config public static DimensionConfig dimensions = DimensionConfig.overworld(false);
	
	public static Block root;
	
	@Override
	public void construct() {
		root = new RootBlock(this);
		
		new QuarkItem("root_item", this, new Item.Properties()
				.food(new Food.Builder()
						.hunger(3)
						.saturation(0.4F)
						.build())
				.group(ItemGroup.FOOD));
	}
	
	@Override
	public void setup() {
		WorldGenHandler.addGenerator(new CaveRootGenerator(dimensions, this), Decoration.UNDERGROUND_DECORATION, WorldGenWeights.CAVE_ROOTS);
	}
	
}
