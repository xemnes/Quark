package vazkii.quark.client.module;

import com.google.common.base.Supplier;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import vazkii.quark.base.block.QuarkBlock;
import vazkii.quark.base.block.QuarkPillarBlock;
import vazkii.quark.base.module.Config;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.building.block.CharcoalBlock;

@LoadModule(category = ModuleCategory.BUILDING, hasSubscriptions = true)
public class CompressedBlocksModule extends Module {

	@Config(name = "Charcoal Block Burns Forever") 
	public static boolean burnsForever = true;

	@Config(name = "Charcoal Block Fuel Time")
	public static int fuelTime = 16000;

	@Config(flag = "charcoal_block") public static boolean enableCharcoalBlock = true;
	@Config(flag = "sugar_cane_block") public static boolean enableSugarCaneBlock = true;
	@Config(flag = "bamboo_block") public static boolean enableBambooBlock = true;
	@Config(flag = "cactus_block") public static boolean enableCactusBlock = true;
	@Config(flag = "chorus_fruit_block") public static boolean enableChorusFruitBlock = true;

	@Config(flag = "apple_crate") public static boolean enableAppleCrate = true;
	@Config(flag = "golden_apple_crate") public static boolean enableGoldenAppleCrate = true;
	@Config(flag = "potato_crate") public static boolean enablePotatoCrate = true;
	@Config(flag = "carrot_crate") public static boolean enableCarrotCrate = true;
	@Config(flag = "beetroot_crate") public static boolean enableBeetrootCrate = true;

	@Config(flag = "cocoa_beans_sack") public static boolean enableCocoaBeanSack = true;
	@Config(flag = "nether_wart_sack") public static boolean enableNetherWartSack = true;
	@Config(flag = "gunpowder_sack") public static boolean enableGunpowderSack = true;
	
	@Config(flag = "blaze_lantern") public static boolean enableBlazeLantern = true;

	private Block charcoal_block;

	@Override
	public void start() {
		charcoal_block = new CharcoalBlock(this)
				.setCondition(() -> enableCharcoalBlock);
		
		pillar("sugar_cane", MaterialColor.LIME, () -> enableSugarCaneBlock);
		pillar("bamboo", MaterialColor.YELLOW, () -> enableBambooBlock);
		pillar("cactus", MaterialColor.GREEN, () -> enableCactusBlock);
		pillar("chorus_fruit", MaterialColor.PURPLE, () -> enableChorusFruitBlock);
		
		crate("apple", MaterialColor.RED, () -> enableAppleCrate);
		crate("golden_apple", MaterialColor.GOLD, () -> enableGoldenAppleCrate);
		crate("potato", MaterialColor.ADOBE, () -> enablePotatoCrate);
		crate("carrot", MaterialColor.ORANGE_TERRACOTTA, () -> enableCarrotCrate);
		crate("beetroot", MaterialColor.RED, () -> enableBeetrootCrate);

		sack("cocoa_beans", MaterialColor.BROWN, () -> enableCocoaBeanSack);
		sack("nether_wart", MaterialColor.RED, () -> enableNetherWartSack);
		sack("gunpowder", MaterialColor.GRAY, () -> enableGunpowderSack);
		
		new QuarkBlock("blaze_lantern", this, ItemGroup.BUILDING_BLOCKS, 
				Block.Properties.create(Material.GLASS, DyeColor.YELLOW)
				.hardnessAndResistance(0.3F)
				.sound(SoundType.GLASS)
				.lightValue(15))
		.setCondition(() -> enableBlazeLantern);
	}
	
	private void pillar(String name, MaterialColor color, Supplier<Boolean> cond) {
		new QuarkPillarBlock(name + "_block", this, ItemGroup.BUILDING_BLOCKS,
				Block.Properties.create(Material.WOOD, color)
				.hardnessAndResistance(0.5F)
				.sound(SoundType.WOOD))
		.setCondition(cond);
	}
	
	private void crate(String name, MaterialColor color, Supplier<Boolean> cond) {
		new QuarkBlock(name + "_crate", this, ItemGroup.DECORATIONS, 
				Block.Properties.create(Material.WOOD, color)
				.hardnessAndResistance(1.5F)
				.sound(SoundType.WOOD))
		.setCondition(cond);
	}

	private void sack(String name, MaterialColor color, Supplier<Boolean> cond) {
		new QuarkBlock(name + "_sack", this, ItemGroup.DECORATIONS, 
				Block.Properties.create(Material.WOOL, color)
				.hardnessAndResistance(0.5F)
				.sound(SoundType.CLOTH))
		.setCondition(cond);
	}
	
	@SubscribeEvent
	public void onFurnaceFuelEvent(FurnaceFuelBurnTimeEvent event) {
		if(event.getItemStack().getItem() == charcoal_block.asItem())
			event.setBurnTime(fuelTime);
	}

}
