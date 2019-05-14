/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 *
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 *
 * File Created @ [03/07/2016, 04:35:10 (GMT)]
 */
package vazkii.quark.world.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import vazkii.arl.item.ItemMod;
import vazkii.quark.base.item.IQuarkItem;
import vazkii.quark.world.feature.Wraiths;

import javax.annotation.Nonnull;
import java.util.Collections;

public class ItemSoulBead extends ItemMod implements IQuarkItem {

	public ItemSoulBead() {
		super("soul_bead");
		setCreativeTab(CreativeTabs.COMBAT);
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand hand) {
		ItemStack stack = playerIn.getHeldItem(hand);
		if(Wraiths.enableCurse) {
			PotionEffect effect = new PotionEffect(Wraiths.curse, Wraiths.curseTime, 0, true, true);
			effect.setCurativeItems(Collections.emptyList());
			playerIn.addPotionEffect(effect);

			// TODO: 5/14/19 add new sound
			worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_BLAZE_AMBIENT, SoundCategory.PLAYERS, 0.5F, 1F);
			playerIn.renderBrokenItemStack(stack);
			stack.shrink(1);
		}

		return super.onItemRightClick(worldIn, playerIn, hand);
	}

}
