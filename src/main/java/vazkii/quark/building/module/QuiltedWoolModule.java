package vazkii.quark.building.module;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemGroup;
import vazkii.arl.util.RegistryHelper;
import vazkii.quark.base.block.QuarkBlock;
import vazkii.quark.base.moduleloader.LoadModule;
import vazkii.quark.base.moduleloader.Module;
import vazkii.quark.base.moduleloader.ModuleCategory;

@LoadModule(category = ModuleCategory.BUILDING)
public class QuiltedWoolModule extends Module {

	@Override
	public void start() {
		for(DyeColor dye : DyeColor.values()) {
			QuarkBlock block = new QuarkBlock(dye.getName() + "_quilted_wool", 
					Block.Properties.create(Material.WOOL, dye.getMapColor())
					.hardnessAndResistance(0.8F)
					.sound(SoundType.CLOTH));
			
			block.setModule(this);
			RegistryHelper.setCreativeTab(block, ItemGroup.BUILDING_BLOCKS);
		}
	}
	
}
