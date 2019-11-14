package vazkii.quark.world.module;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.gen.GenerationStage.Decoration;
import vazkii.arl.util.RegistryHelper;
import vazkii.quark.base.module.Config;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.base.world.WorldGenHandler;
import vazkii.quark.base.world.WorldGenWeights;
import vazkii.quark.base.world.config.DimensionConfig;
import vazkii.quark.world.block.MonsterBoxBlock;
import vazkii.quark.world.gen.MonsterBoxGenerator;
import vazkii.quark.world.tile.MonsterBoxTileEntity;
	
@LoadModule(category = ModuleCategory.WORLD)
public class MonsterBoxModule extends Module {

    public static TileEntityType<MonsterBoxTileEntity> tileEntityType;
	
	@Config public static double chancePerChunk = 0.5;
	@Config public static int minY = 5;
	@Config public static int maxY = 30;
	@Config public static int minMobCount = 5;
	@Config public static int maxMobCount = 8;
	@Config public static DimensionConfig dimensions = DimensionConfig.overworld(false);
	
	public static Block monster_box = null;
	
	@Override
	public void construct() {
		monster_box = new MonsterBoxBlock(this);
		
        tileEntityType = TileEntityType.Builder.create(MonsterBoxTileEntity::new, monster_box).build(null);
        RegistryHelper.register(tileEntityType, "monster_box");
	}
	
	@Override
	public void setup() {
		WorldGenHandler.addGenerator(this, new MonsterBoxGenerator(dimensions), Decoration.UNDERGROUND_DECORATION, WorldGenWeights.MONSTER_BOXES);
	}
	
}
