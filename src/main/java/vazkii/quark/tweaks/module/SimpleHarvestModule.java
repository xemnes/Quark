/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 * <p>
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * <p>
 * File Created @ [Jul 05, 2019, 16:56 AM (EST)]
 */
package vazkii.quark.tweaks.module;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropsBlock;
import net.minecraft.command.arguments.BlockStateParser;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import vazkii.quark.base.module.Config;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.base.network.QuarkNetwork;
import vazkii.quark.base.network.message.HarvestMessage;

import java.util.List;
import java.util.Map;

@LoadModule(category = ModuleCategory.TWEAKS, hasSubscriptions = true)
public class SimpleHarvestModule extends Module {

	@Config(description = "Can players harvest crops with empty hand clicks?")
	public static boolean emptyHandHarvest = true;
	@Config(description = "Does harvesting crops with a hoe cost durability?")
	public static boolean harvestingCostsDurability = false;
	@Config(description = "Should Quark look for (nonvanilla) crops, and handle them?")
	public static boolean doHarvestingSearch = true;

	@Config(description = "Which crops can be harvested?\n" +
			"Format is: \"harvestState[,afterHarvest]\", i.e. \"minecraft:wheat[age=7]\" or \"minecraft:cocoa[age=2,facing=north],minecraft:cocoa[age=0,facing=north]\"")
	public static List<String> harvestableBlocks = Lists.newArrayList(
			"minecraft:wheat[age=7]",
			"minecraft:carrots[age=7]",
			"minecraft:potatoes[age=7]",
			"minecraft:beetroots[age=3]",
			"minecraft:nether_wart[age=3]",
			"minecraft:cocoa[age=2,facing=north],minecraft:cocoa[age=0,facing=north]",
			"minecraft:cocoa[age=2,facing=south],minecraft:cocoa[age=0,facing=south]",
			"minecraft:cocoa[age=2,facing=east],minecraft:cocoa[age=0,facing=east]",
			"minecraft:cocoa[age=2,facing=west],minecraft:cocoa[age=0,facing=west]");

	public static final Map<BlockState, BlockState> crops = Maps.newHashMap();


	@Override
	public void configChanged() {
		crops.clear();

		if (doHarvestingSearch) {
			GameRegistry.findRegistry(Block.class).getValues().stream()
					.filter(b -> !isVanilla(b) && b instanceof CropsBlock)
					.forEach(b -> crops.put(b.getDefaultState().with(((CropsBlock) b).getAgeProperty(), ((CropsBlock) b).getMaxAge()), b.getDefaultState()));
		}

		for (String harvestKey : harvestableBlocks) {
			BlockState initial, result;
			String[] split = harvestKey.split(",", 2);
			initial = fromString(split[0]);
			if (split.length > 1)
				result = fromString(split[1]);
			else
				result = initial.getBlock().getDefaultState();

			crops.put(initial, result);
		}
	}

	private boolean isVanilla(IForgeRegistryEntry entry) {
		ResourceLocation loc = entry.getRegistryName();
		if (loc == null)
			return true; // Just in case

		return loc.getNamespace().equals("minecraft");
	}

	private BlockState fromString(String key) {
		try {
			BlockStateParser parser = new BlockStateParser(new StringReader(key), false).parse(false);
			BlockState state = parser.getState();
			return state == null ? Blocks.AIR.getDefaultState() : state;
		} catch (CommandSyntaxException e) {
			return Blocks.AIR.getDefaultState();
		}
	}

	private static void replant(World world, BlockPos pos, BlockState inWorld, PlayerEntity player) {
		ItemStack mainHand = player.getHeldItemMainhand();
		boolean isHoe = !mainHand.isEmpty() && mainHand.getItem() instanceof HoeItem;

		BlockState newBlock = crops.get(inWorld);
		int fortune = HoeHarvestingModule.canFortuneApply(Enchantments.FORTUNE, mainHand) && isHoe ?
				EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, mainHand) : 0;
		fortune--; // Simulate the crop dropping one less seed


		ItemStack copy = mainHand.copy();
		if (copy.isEmpty())
			copy = new ItemStack(Items.STICK);

		Map<Enchantment, Integer> enchMap = EnchantmentHelper.getEnchantments(copy);
		enchMap.put(Enchantments.FORTUNE, fortune);
		EnchantmentHelper.setEnchantments(enchMap, copy);

		if (world instanceof ServerWorld) {
			List<ItemStack> drops = Block.getDrops(inWorld, (ServerWorld) world, pos, world.getTileEntity(pos), player, copy);

			NonNullList<ItemStack> newDrops = NonNullList.from(ItemStack.EMPTY, drops.toArray(new ItemStack[0]));

			ForgeEventFactory.fireBlockHarvesting(newDrops, world, pos, inWorld, fortune, 1.0F, false, player);

			if (!world.isRemote) {
				world.playEvent(2001, pos, Block.getStateId(newBlock));
				world.setBlockState(pos, newBlock);
				for (ItemStack stack : newDrops) {
					ItemEntity entityItem = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
					entityItem.setPickupDelay(10);
					world.addEntity(entityItem);
				}
			}
		}
	}

	@SubscribeEvent
	public void onClick(PlayerInteractEvent.RightClickItem event) {
		if (click(event.getPlayer(), event.getPos())) {
			event.setCanceled(true);
			event.setCancellationResult(ActionResultType.SUCCESS);
		}
	}

	public static boolean click(PlayerEntity player, BlockPos pos) {
		if (player == null)
			return false;

		ItemStack mainHand = player.getHeldItemMainhand();
		boolean isHoe = !mainHand.isEmpty() && mainHand.getItem() instanceof HoeItem;

		if (!emptyHandHarvest && !isHoe)
			return false;

		int range = HoeHarvestingModule.getRange(mainHand);

		int harvests = 0;

		for(int x = 1 - range; x < range; x++) {
			for (int z = 1 - range; z < range; z++) {
				BlockPos shiftPos = pos.add(x, 0, z);

				BlockState worldBlock = player.world.getBlockState(shiftPos);
				if (crops.containsKey(worldBlock)) {
					replant(player.world, shiftPos, worldBlock, player);
					harvests++;
				}
			}
		}

		if (harvests > 0) {
			if (harvestingCostsDurability && isHoe && !player.world.isRemote)
				mainHand.damageItem(1, player, (p) -> p.sendBreakAnimation(Hand.MAIN_HAND));

			if (mainHand.isEmpty() && player.world.isRemote)
				QuarkNetwork.sendToServer(new HarvestMessage(pos));
			return true;
		}

		return false;
	}
}
