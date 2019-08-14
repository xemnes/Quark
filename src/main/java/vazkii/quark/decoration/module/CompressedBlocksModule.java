package vazkii.quark.decoration.module;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import vazkii.quark.base.block.QuarkBlock;
import vazkii.quark.base.block.QuarkPillarBlock;
import vazkii.quark.base.moduleloader.Config;
import vazkii.quark.base.moduleloader.LoadModule;
import vazkii.quark.base.moduleloader.Module;
import vazkii.quark.base.moduleloader.ModuleCategory;
import vazkii.quark.base.moduleloader.SubscriptionTarget;
import vazkii.quark.decoration.block.CharcoalBlock;

@LoadModule(category = ModuleCategory.DECORATION, subscriptions = SubscriptionTarget.BOTH_SIDES)
public class CompressedBlocksModule extends Module {

	@Config(name = "Charcoal Block Burns Forever") 
	public static boolean burnsForever = true;

	@Config(name = "Charcoal Block Fuel Time")
	public static int fuelTime = 16000;

	@Config(flag = "charcoal_block") public static boolean enableCharcoalBlock = true;
	@Config(flag = "blaze_lantern") public static boolean enableBlazeLantern = true;
	@Config(flag = "sugar_cane_block") public static boolean enableSugarCaneBlock = true;

	private Block charcoal_block;

	@Override
	public void start() {
		charcoal_block = new CharcoalBlock(this)
				.setCondition(() -> enableCharcoalBlock);

		new QuarkBlock("blaze_lantern", this, ItemGroup.BUILDING_BLOCKS, 
				Block.Properties.create(Material.GLASS, DyeColor.YELLOW)
				.hardnessAndResistance(0.3F)
				.sound(SoundType.GLASS)
				.lightValue(15))
		.setCondition(() -> enableBlazeLantern);

		new QuarkPillarBlock("sugar_cane_block", this, ItemGroup.BUILDING_BLOCKS,
				Block.Properties.create(Material.WOOD, DyeColor.LIME)
				.hardnessAndResistance(0.5F)
				.sound(SoundType.WOOD))
		.setCondition(() -> enableSugarCaneBlock);
	}

	@SubscribeEvent
	public void onFurnaceFuelEvent(FurnaceFuelBurnTimeEvent event) {
		if(event.getItemStack().getItem() == charcoal_block.asItem())
			event.setBurnTime(fuelTime);
	}

}
