/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 *
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 *
 * File Created @ [05/06/2016, 22:57:00 (GMT)]
 */
package vazkii.quark.world.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.arl.item.ItemMod;
import vazkii.quark.base.item.IQuarkItem;

public class ItemSlimeBucket extends ItemMod implements IQuarkItem {

	public static final String[] VARIANTS = new String[] {
			"slime_bucket_normal",
			"slime_bucket_excited"
	};

	public ItemSlimeBucket() {
		super("slime_bucket", VARIANTS);
		setMaxStackSize(1);
		setCreativeTab(CreativeTabs.MISC);
		setContainerItem(Items.BUCKET);
		OreDictionary.registerOre("slimeball", this);
	}

	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
		subItems.add(new ItemStack(itemIn));
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int something, boolean somethingelse) {
		if(!world.isRemote) {
			int x = MathHelper.floor_double(entity.posX);
			int z = MathHelper.floor_double(entity.posZ);
			boolean slime = isSlimeChunk(world, x, z);
			int meta = stack.getItemDamage();
			int newMeta = slime ? 1 : 0;
			if(meta != newMeta)
				stack.setItemDamage(newMeta);
		}
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		double x = pos.getX() + 0.5 + facing.getFrontOffsetX();
		double y = pos.getY() + 0.5 + facing.getFrontOffsetY();
		double z = pos.getZ() + 0.5 + facing.getFrontOffsetZ();

		if(!worldIn.isRemote) {
			EntitySlime slime = new EntitySlime(worldIn);
			slime.setPosition(x, y, z);

			slime.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1.0);
			slime.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3);
			slime.setHealth(slime.getMaxHealth());
			worldIn.spawnEntityInWorld(slime);
			playerIn.swingArm(hand);
		}


		playerIn.setHeldItem(hand, new ItemStack(Items.BUCKET));
		return EnumActionResult.SUCCESS;
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged);
	}

	public static boolean isSlimeChunk(World world, int x, int z) {
		Chunk chunk = world.getChunkFromBlockCoords(new BlockPos(x, 0, z));
		return chunk.getRandomWithSeed(987234911L).nextInt(10) == 0;
	}

}
