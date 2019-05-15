package vazkii.quark.world.item;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.item.ItemMod;
import vazkii.quark.base.item.IQuarkItem;
import vazkii.quark.world.entity.EntityStoneling;

public class ItemDiamondHeart extends ItemMod implements IQuarkItem {

	public ItemDiamondHeart() {
		super("diamond_heart");
		setCreativeTab(CreativeTabs.MISC);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		IBlockState stateAt = worldIn.getBlockState(pos);
		Block block = stateAt.getBlock();
		ItemStack stack = player.getHeldItem(hand);
		
		if(player.canPlayerEdit(pos, facing, stack) && block.getMaterial(stateAt) == Material.ROCK && block.getBlockHardness(stateAt, worldIn, pos) != -1) {
			if(!worldIn.isRemote) {
				NonNullList<ItemStack> drops = NonNullList.create();
				worldIn.setBlockToAir(pos);
				worldIn.playEvent(2001, pos, Block.getStateId(stateAt));
				
				EntityStoneling stoneling = new EntityStoneling(worldIn);
				stoneling.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
				stoneling.setPlayerMade();
				stoneling.rotationYaw = player.rotationYaw + 180F;
				stoneling.onInitialSpawn(worldIn.getDifficultyForLocation(pos), null);
				worldIn.spawnEntity(stoneling);
			}
			
			stack.shrink(1);
			
			return EnumActionResult.SUCCESS;
		}
		
		return EnumActionResult.PASS;
	}

	@Nonnull
	@Override
	public IRarity getForgeRarity(@Nonnull ItemStack stack) {
		return EnumRarity.UNCOMMON;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack) {
		return true;
	}

}
