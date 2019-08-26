package vazkii.quark.world.module;

import net.minecraft.block.material.MaterialColor;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.world.block.SpeleothemBlock;

@LoadModule(category = ModuleCategory.WORLD)
public class SpeleothemsModule extends Module {
	
	@Override
	public void start() {
		new SpeleothemBlock("stone", this, MaterialColor.STONE, false);
		new SpeleothemBlock("netherrack", this, MaterialColor.NETHERRACK, true);
		new SpeleothemBlock("granite", this, MaterialColor.DIRT, false);
		new SpeleothemBlock("diorite", this, MaterialColor.QUARTZ, false);
		new SpeleothemBlock("andesite", this, MaterialColor.STONE, false);
		
		new SpeleothemBlock("marble", this, MaterialColor.QUARTZ, false).setCondition(() -> NewStoneTypesModule.enableMarble);
		new SpeleothemBlock("limestone", this, MaterialColor.STONE, false).setCondition(() -> NewStoneTypesModule.enableLimestone);
		new SpeleothemBlock("jasper", this, MaterialColor.RED_TERRACOTTA, false).setCondition(() -> NewStoneTypesModule.enableJasper);
		new SpeleothemBlock("slate", this, MaterialColor.ICE, false).setCondition(() -> NewStoneTypesModule.enableSlate);
		new SpeleothemBlock("basalt", this, MaterialColor.BLACK, false).setCondition(() -> NewStoneTypesModule.enableBasalt);
	}

}
