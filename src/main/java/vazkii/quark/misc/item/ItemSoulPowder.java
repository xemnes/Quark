package vazkii.quark.misc.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import vazkii.arl.item.ItemMod;
import vazkii.quark.base.item.IQuarkItem;
import vazkii.quark.misc.entity.EntitySoulPowder;

import javax.annotation.Nonnull;

public class ItemSoulPowder extends ItemMod implements IQuarkItem {

	public ItemSoulPowder() {
		super("soul_powder");
		setCreativeTab(CreativeTabs.MISC);
	}
	
	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);

		if(!playerIn.capabilities.isCreativeMode)
			itemstack.shrink(1);

		if(!worldIn.isRemote) {
			BlockPos blockpos = playerIn.getEntityWorld().findNearestStructure("Fortress", playerIn.getPosition(), false);

			if(blockpos != null) {
				EntitySoulPowder entity = new EntitySoulPowder(worldIn, blockpos.getX(), blockpos.getZ());
				Vec3d look = playerIn.getLookVec();
				entity.setPosition(playerIn.posX + look.x * 2, playerIn.posY + 0.25, playerIn.posZ + look.z * 2);
				worldIn.spawnEntity(entity);
				worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_GHAST_DEATH, SoundCategory.PLAYERS, 1F, 1F);
			}
		} else playerIn.swingArm(handIn);

		playerIn.addStat(StatList.getObjectUseStats(this));
		return new ActionResult(EnumActionResult.SUCCESS, itemstack);
	}

}
