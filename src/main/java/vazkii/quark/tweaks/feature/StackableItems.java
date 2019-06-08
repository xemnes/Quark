/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 *
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 *
 * File Created @ [18/03/2016, 23:01:43 (GMT)]
 */
package vazkii.quark.tweaks.feature;

import com.google.common.collect.ImmutableSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.quark.base.module.Feature;

public class StackableItems extends Feature {

	public static int minecarts, soups, saddle;

	@Override
	public void setupConfig() {
		minecarts = Math.min(64, loadPropInt("Minecarts", "", 16));
		soups = Math.min(64, loadPropInt("Soups", "", 64));
		saddle = Math.min(64, loadPropInt("Saddle", "", 8));
	}

	@Override
	public void init() {
		ImmutableSet.of(Items.MINECART, Items.CHEST_MINECART, Items.COMMAND_BLOCK_MINECART, Items.FURNACE_MINECART, Items.HOPPER_MINECART, Items.TNT_MINECART)
			.forEach(item -> item.setMaxStackSize(minecarts));
		
		ImmutableSet.of(Items.MUSHROOM_STEW, Items.RABBIT_STEW, Items.BEETROOT_SOUP)
			.forEach(item -> item.setMaxStackSize(soups));

		Items.SADDLE.setMaxStackSize(saddle);
	}

	@SubscribeEvent
	public void finishEvent(LivingEntityUseItemEvent.Finish event) {
		if(event.getEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntity();
			ItemStack original = event.getItem();
			ItemStack result = event.getResultStack();
			if(original.getCount() > 1 && (result.getItem() == Items.BOWL || result.getItem() == Items.BUCKET)) {
				ItemStack newResult = original.copy();
				newResult.setCount(original.getCount() - 1);
				event.setResultStack(newResult);
				player.addItemStackToInventory(result);
			}
		}
			
	}

	@Override
	public boolean hasSubscriptions() {
		return true;
	}

	@Override
	public boolean requiresMinecraftRestartToEnable() {
		return true;
	}

}
