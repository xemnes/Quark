package vazkii.quark.misc.item;

import com.google.common.collect.ImmutableSet;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import vazkii.arl.item.ItemMod;
import vazkii.quark.base.item.IQuarkItem;
import vazkii.quark.base.sounds.QuarkSounds;
import vazkii.quark.misc.feature.Pickarang;
import vazkii.quark.world.entity.EntityPickarang;

import javax.annotation.Nonnull;

public class ItemPickarang extends ItemMod implements IQuarkItem {

	public ItemPickarang() {
		super("pickarang");
		setMaxStackSize(1);
		setCreativeTab(CreativeTabs.TOOLS);
		
		if(Pickarang.durability > -1)
			setMaxDamage(Pickarang.durability);
	}
	
	@Nonnull
	@Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        playerIn.setHeldItem(handIn, ItemStack.EMPTY);
        worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, QuarkSounds.ENTITY_PICKARANG_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if(!worldIn.isRemote)  {
        	int slot = handIn == EnumHand.OFF_HAND ? playerIn.inventory.getSizeInventory() - 1 : playerIn.inventory.currentItem;
        	EntityPickarang entity = new EntityPickarang(worldIn, playerIn);
        	entity.setThrowData(slot, itemstack);
        	entity.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
            worldIn.spawnEntity(entity);
        }

        if(!Pickarang.noCooldown)
        	playerIn.getCooldownTracker().setCooldown(this, 10);
        
        playerIn.addStat(StatList.getObjectUseStats(this));
        return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
    }
	
	@Override
	public boolean isRepairable() {
		return true;
	}
	
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		return repair.getItem() == Items.DIAMOND;
	}
	
	@Override
	public int getItemEnchantability() {
		return Items.DIAMOND_PICKAXE.getItemEnchantability();
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return super.canApplyAtEnchantingTable(stack, enchantment) || ImmutableSet.of(Enchantments.FORTUNE, Enchantments.SILK_TOUCH).contains(enchantment);
	}
	
}
