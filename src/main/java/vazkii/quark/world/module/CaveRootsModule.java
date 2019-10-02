package vazkii.quark.world.module;

import net.minecraft.block.Block;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import vazkii.quark.base.item.QuarkItem;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.world.block.RootBlock;

@LoadModule(category = ModuleCategory.WORLD)
public class CaveRootsModule extends Module {

	public static Block root;
	
	@Override
	public void start() {
		root = new RootBlock(this);
		
		new QuarkItem("root_item", this, new Item.Properties()
				.food(new Food.Builder()
						.hunger(3)
						.saturation(0.4F)
						.build())
				.group(ItemGroup.FOOD));
	}
	
}
