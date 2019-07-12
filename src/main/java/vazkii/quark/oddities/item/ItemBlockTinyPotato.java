/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 27, 2015, 3:44:10 PM (GMT)]
 */
package vazkii.quark.oddities.item;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import vazkii.arl.item.ItemModBlock;
import vazkii.arl.util.ItemNBTHelper;

import java.util.Arrays;
import java.util.List;

public class ItemBlockTinyPotato extends ItemModBlock {

	private static final List<String> TYPOS = Arrays.asList("vaskii", "vazki", "voskii", "vazkkii", "vazkki", "vazzki", "vaskki", "vozkii", "vazkil", "vaskil", "vazkill", "vaskill", "vaski");

	private static final int NOT_MY_NAME = 17;

	private static final String TAG_TICKS = "notMyNameTicks";

	public ItemBlockTinyPotato(Block block, ResourceLocation loc) {
		super(block, loc);
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity holder, int itemSlot, boolean isSelected) {
		if(!world.isRemote && holder instanceof EntityPlayer && holder.ticksExisted % 30 == 0 && TYPOS.contains(stack.getDisplayName().toLowerCase())) {
			EntityPlayer player = (EntityPlayer) holder;
			int ticks = ItemNBTHelper.getInt(stack, TAG_TICKS, 0);
			if(ticks < NOT_MY_NAME) {
				player.sendMessage(new TextComponentTranslation("quarkmisc.you_came_to_the_wrong_neighborhood." + ticks).setStyle(new Style().setColor(TextFormatting.RED)));
				ItemNBTHelper.setInt(stack, TAG_TICKS, ticks + 1);
			}
		}
	}

}
