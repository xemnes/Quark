package vazkii.quark.building.module;

import java.util.function.BooleanSupplier;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.ItemGroup;
import vazkii.quark.base.block.QuarkBlock;
import vazkii.quark.base.handler.VariantHandler;
import vazkii.quark.base.module.Config;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.base.module.ModuleLoader;
import vazkii.quark.building.block.MagmaBrickBlock;

@LoadModule(category = ModuleCategory.BUILDING)
public class MoreBrickTypesModule extends Module {

	@Config(flag = "sandy_bricks") public boolean enableSandyBricks = true;
	@Config(flag = "snow_bricks") public boolean enableSnowBricks = true;
	@Config(flag = "magma_bricks") public boolean enableMagmaBricks = true;
	@Config(flag = "charred_nether_bricks") public boolean enableCharredNetherBricks = true;
	
	@Config(flag = "sandstone_bricks",
			description = "This also includes Red Sandstone Bricks and Soul Sandstone Bricks") 
	public boolean enableSandstoneBricks = true;
	
	@Override
	public void construct() {
		add("sandy", MaterialColor.SAND, () -> enableSandyBricks);
		add("snow", MaterialColor.SNOW, () -> enableSnowBricks);
		add("charred_nether", MaterialColor.BLACK, () -> enableCharredNetherBricks);
		add("sandstone", MaterialColor.SAND, () -> enableSandstoneBricks);
		add("red_sandstone", MaterialColor.ADOBE, () -> enableSandstoneBricks);
		add("soul_sandstone", MaterialColor.BROWN, () -> enableSandstoneBricks && ModuleLoader.INSTANCE.isModuleEnabled(SoulSandstoneModule.class));
		
		VariantHandler.addSlabStairsWall(new MagmaBrickBlock(this).setCondition(() -> enableMagmaBricks));
	}
	
	private void add(String name, MaterialColor color, BooleanSupplier cond) {
		VariantHandler.addSlabStairsWall(new QuarkBlock(name + "_bricks", this, ItemGroup.BUILDING_BLOCKS, 
				Block.Properties.create(Material.ROCK, color)
				.hardnessAndResistance(2F, 6F))
				.setCondition(cond));
	}
	
}

