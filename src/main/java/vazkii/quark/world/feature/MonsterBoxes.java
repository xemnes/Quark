package vazkii.quark.world.feature;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import vazkii.quark.base.module.Feature;
import vazkii.quark.world.block.BlockMonsterBox;
import vazkii.quark.world.tile.TileMonsterBox;

public class MonsterBoxes extends Feature {

	public static Block monster_box;
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		monster_box = new BlockMonsterBox();
		
		registerTile(TileMonsterBox.class, "monster_box");
	}
	
}
