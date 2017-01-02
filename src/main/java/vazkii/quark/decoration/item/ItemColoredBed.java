/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 *
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 *
 * File Created @ [03/07/2016, 22:27:53 (GMT)]
 */
package vazkii.quark.decoration.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.interf.IItemColorProvider;
import vazkii.arl.item.ItemMod;
import vazkii.quark.base.item.IQuarkItem;
import vazkii.quark.decoration.feature.ColoredBeds;

public class ItemColoredBed extends ItemMod implements IItemColorProvider, IQuarkItem {

	private static final String[] VARIANTS = {
			"colored_bed_item_white",
			"colored_bed_item_orange",
			"colored_bed_item_magenta",
			"colored_bed_item_light_blue",
			"colored_bed_item_yellow",
			"colored_bed_item_lime",
			"colored_bed_item_pink",
			"colored_bed_item_gray",
			"colored_bed_item_silver",
			"colored_bed_item_cyan",
			"colored_bed_item_purple",
			"colored_bed_item_blue",
			"colored_bed_item_brown",
			"colored_bed_item_green",
			"colored_bed_item_black"
	};

	public ItemColoredBed() {
		super("colored_bed_item", VARIANTS);
		setMaxStackSize(ColoredBeds.stackSize);
		setCreativeTab(CreativeTabs.DECORATIONS);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getActiveItemStack();
		if(worldIn.isRemote)
			return EnumActionResult.SUCCESS;
		else if(facing != EnumFacing.UP)
			return EnumActionResult.FAIL;
		else {
			IBlockState iblockstate = worldIn.getBlockState(pos);
			Block block = iblockstate.getBlock();
			boolean flag = block.isReplaceable(worldIn, pos);

			if(!flag)
				pos = pos.up();

			int i = MathHelper.floor(player.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
			EnumFacing enumfacing = EnumFacing.getHorizontal(i);
			BlockPos blockpos = pos.offset(enumfacing);

			if(player.canPlayerEdit(pos, facing, stack) && player.canPlayerEdit(blockpos, facing, stack)) {
				boolean flag1 = worldIn.getBlockState(blockpos).getBlock().isReplaceable(worldIn, blockpos);
				boolean flag2 = flag || worldIn.isAirBlock(pos);
				boolean flag3 = flag1 || worldIn.isAirBlock(blockpos);

				if(flag2 && flag3 && worldIn.getBlockState(pos.down()).isFullyOpaque() && worldIn.getBlockState(blockpos.down()).isFullyOpaque()) {
					Block bedBlock = ColoredBeds.colored_bed_blocks[stack.getItemDamage()];
					IBlockState iblockstate1 = bedBlock.getDefaultState().withProperty(BlockBed.OCCUPIED, Boolean.valueOf(false)).withProperty(BlockHorizontal.FACING, enumfacing).withProperty(BlockBed.PART, BlockBed.EnumPartType.FOOT);

					if(worldIn.setBlockState(pos, iblockstate1, 11)) {
						IBlockState iblockstate2 = iblockstate1.withProperty(BlockBed.PART, BlockBed.EnumPartType.HEAD);
						worldIn.setBlockState(blockpos, iblockstate2, 11);
					}

					SoundType soundtype = iblockstate1.getBlock().getSoundType();
					worldIn.playSound((EntityPlayer)null, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
					stack.shrink(1);
					return EnumActionResult.SUCCESS;
				}
				else return EnumActionResult.FAIL;
			} else return EnumActionResult.FAIL;
		}
	}

	@Override
	public String getUniqueModel() {
		return "colored_bed_item";
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IItemColor getItemColor() {
		return new IItemColor() {

			@Override
			public int getColorFromItemstack(ItemStack stack, int tintIndex) {
				return tintIndex == 1 ? ColoredBeds.getColor(stack.getItemDamage()) : 0xFFFFFF;
			}

		};
	}
}
