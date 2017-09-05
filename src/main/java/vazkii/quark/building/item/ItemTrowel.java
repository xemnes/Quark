package vazkii.quark.building.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.arl.item.ItemMod;
import vazkii.arl.util.ItemNBTHelper;
import vazkii.quark.base.item.IQuarkItem;

public class ItemTrowel extends ItemMod implements IQuarkItem {

	public ItemTrowel(int durability) {
		super("trowel");
		setMaxStackSize(1);
		setMaxDamage(durability);
		setCreativeTab(CreativeTabs.TOOLS);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		List<ItemStack> targets = new ArrayList();
		for(int i = 0; i < InventoryPlayer.getHotbarSize(); i++) {
			ItemStack stack = player.inventory.getStackInSlot(i);
			if(!stack.isEmpty() && stack.getItem() instanceof ItemBlock)
				targets.add(stack);
		}
		
		ItemStack ourStack = player.getHeldItem(hand);
		if(targets.isEmpty())
			return EnumActionResult.PASS;
		
		long seed = ItemNBTHelper.getLong(ourStack, "placingSeed", 0);
		Random rand = new Random(seed);
		ItemNBTHelper.setLong(ourStack, "placingSeed", rand.nextLong());
		
		ItemStack target = targets.get(rand.nextInt(targets.size()));
		EnumActionResult result = placeBlock(target, player, pos, facing, worldIn, hand, hitX, hitY, hitZ);
		System.out.println(result + " " + ourStack + " " + ourStack.isItemStackDamageable() + " " + getMaxDamage(ourStack));
		if(result == EnumActionResult.SUCCESS && ourStack.isItemStackDamageable()) {
			ourStack.damageItem(1, player);
			System.out.println("damage");
		}
		
		return result;
	}
	
	private EnumActionResult placeBlock(ItemStack itemstack, EntityPlayer player, BlockPos pos, EnumFacing facing, World worldIn, EnumHand hand, float hitX, float hitY, float hitZ) {
		IBlockState stateAt = worldIn.getBlockState(pos);
        if(!stateAt.getBlock().isReplaceable(worldIn, pos))
            pos = pos.offset(facing);
		
		if(itemstack.getItem() instanceof ItemBlock) {
			ItemBlock item = (ItemBlock) itemstack.getItem();
			Block block = item.getBlock();
	        if(player.canPlayerEdit(pos, facing, itemstack) && worldIn.mayPlace(block, pos, false, facing, null)) {
	            int i = item.getMetadata(itemstack.getMetadata());
	            IBlockState state = block.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, i, player, hand);
	            
	            if(item.placeBlockAt(itemstack, player, worldIn, pos, facing, hitX, hitY, hitZ, state)) {
	            	state = worldIn.getBlockState(pos);
	                SoundType soundtype = state.getBlock().getSoundType(state, worldIn, pos, player);
	                worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
	                if(!player.capabilities.isCreativeMode)
	                	itemstack.shrink(1);
	            }

	            return EnumActionResult.SUCCESS;
	        }
		}

        return EnumActionResult.FAIL;
	}

}
