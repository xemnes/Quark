/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 * <p>
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * <p>
 * File Created @ [16/07/2016, 21:39:56 (GMT)]
 */
package vazkii.quark.tweaks.feature;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import vazkii.quark.base.module.Feature;
import vazkii.quark.base.module.ModuleLoader;


public class HoeSickle extends Feature {
	public static boolean hoesCanHaveFortune;
	public static String[] hoeRanges;

	@Override
	public void setupConfig() {
		hoesCanHaveFortune = loadPropBool("Hoes Can Have Fortune", "Can hoes have Fortune anviled on?", true);
		hoeRanges = loadPropStringList("Hoe ranges",
				"in all four directions: 1 is 1x1, 2 is 3x3, 3 is 5x5 and so on",
				new String[]{
						"minecraft:diamond_hoe 3",
						"minecraft:iron_hoe 2",
						"minecraft:golden_hoe 2",
						"minecraft:stone_hoe 2",
						"minecraft:wooden_hoe 1"
				}
		);
	}

	public static int getRange(Item item) {
		if (!ModuleLoader.isFeatureEnabled(HoeSickle.class))
			return 1;

		for (String hoeType : hoeRanges) {
			String[] parts = hoeType.split(" ");
			if (ForgeRegistries.ITEMS.containsKey(new ResourceLocation(parts[0])) &&
					(ForgeRegistries.ITEMS.getValue(new ResourceLocation(parts[0])) == item)) {
				return Integer.parseInt(parts[1]);
			}
		}
		return 1;
	}

	public static boolean canFortuneApply(Enchantment enchantment, ItemStack stack) {
		return enchantment == Enchantments.FORTUNE && hoesCanHaveFortune &&
				!stack.isEmpty() && stack.getItem() instanceof ItemHoe;
	}

	@SubscribeEvent
	public void onBlockBroken(BlockEvent.BreakEvent event) {
		World world = event.getWorld();
		EntityPlayer player = event.getPlayer();
		BlockPos basePos = event.getPos();
		ItemStack stack = player.getHeldItemMainhand();
		if (!stack.isEmpty() && canHarvest(world, basePos, event.getState())) {
			int range = getRange(stack.getItem());

			for (int i = 1 - range; i < range; i++)
				for (int k = 1 - range; k < range; k++) {
					if (i == 0 && k == 0)
						continue;

					BlockPos pos = basePos.add(i, 0, k);
					IBlockState state = world.getBlockState(pos);
					if (canHarvest(world, pos, state)) {
						Block block = state.getBlock();
						if (block.canHarvestBlock(world, pos, player))
							block.harvestBlock(world, player, pos, state, world.getTileEntity(pos), stack);
						world.setBlockToAir(pos);
						world.playEvent(2001, pos, Block.getStateId(state));
					}
				}

			stack.damageItem(1, player);
		}
	}

	private boolean canHarvest(World world, BlockPos pos, IBlockState state) {
		Block block = state.getBlock();
		if (block instanceof IPlantable) {
			IPlantable plant = (IPlantable) block;
			EnumPlantType type = plant.getPlantType(world, pos);
			return type != EnumPlantType.Water && type != EnumPlantType.Desert;
		}

		return state.getMaterial() == Material.PLANTS && block.isReplaceable(world, pos);
	}

	@Override
	public boolean hasSubscriptions() {
		return true;
	}

}
