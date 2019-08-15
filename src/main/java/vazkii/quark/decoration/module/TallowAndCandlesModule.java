package vazkii.quark.decoration.module;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import vazkii.quark.base.item.QuarkItem;
import vazkii.quark.base.module.Config;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.decoration.block.CandleBlock;

@LoadModule(category = ModuleCategory.DECORATION, hasSubscriptions = true)
public class TallowAndCandlesModule extends Module {

	@Config public static boolean candlesFall = true;
	@Config public static int minDrop = 1;
	@Config public static int maxDrop = 3;
	@Config public static int tallowBurnTime = 200;
	@Config public static float enchantPower = 0.5F;
	
	private Item tallow;
	
	@Override
	public void start() {
		tallow = new QuarkItem("tallow", this, new Item.Properties().group(ItemGroup.MATERIALS));
		
		for(DyeColor dye : DyeColor.values())
			new CandleBlock(dye.getName() + "_candle", this, dye);
	}
	
	@SubscribeEvent
	public void onDrops(LivingDropsEvent event) {
		LivingEntity e = event.getEntityLiving();
		if(e instanceof PigEntity && maxDrop > 0) {
			int drops = minDrop + e.world.rand.nextInt(maxDrop - minDrop + 1);
			if(drops > 0)
				event.getDrops().add(new ItemEntity(e.world, e.posX, e.posY, e.posZ, new ItemStack(tallow, drops)));
		}
	}
	
	@SubscribeEvent
	public void onFurnaceTimeCheck(FurnaceFuelBurnTimeEvent event) {
		if(event.getItemStack().getItem() == tallow && tallowBurnTime > 0)
			event.setBurnTime(tallowBurnTime);
	}
	
}
