package vazkii.quark.world.feature;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Biomes;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration.Type;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerCareer;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;
import vazkii.arl.util.ItemNBTHelper;
import vazkii.quark.base.module.Feature;
import vazkii.quark.base.module.ModuleLoader;
import vazkii.quark.experimental.world.BiomeLocator;

public class PathfinderMaps extends Feature {

	public static Multimap<Integer, TradeInfo> trades;

	public static boolean unlockAllAtOnce;
	public static boolean multipleAtFirstUnlock;
	
	@Override
	public void setupConfig() {
		trades = HashMultimap.create();
		
		unlockAllAtOnce = loadPropBool("Unlock All Trades at Once", "By default, when a Cartographer levels up, a random Pathfinder Map from that level is added to their trades."
				+ "\nSet this to true to add all the maps from that level to the trades instead.", false);
		multipleAtFirstUnlock = loadPropBool("Unlock Multiples At Level 2", "By default, when a Cartographer evolves to level 2, two or three Pathfinder Maps are unlocked."
				+ "\nSet this to false to disable this, and make it only unlock one, like in the other levels.", true);
		
		loadTradeInfo(Biomes.ICE_PLAINS, true, 2, 8, 14, 0x7FE4FF, "(Ice Plains)");
		loadTradeInfo(Biomes.EXTREME_HILLS, true, 2, 8, 14, 0x8A8A8A);
		loadTradeInfo(Biomes.ROOFED_FOREST, true, 2, 8, 14, 0x00590A);
		loadTradeInfo(Biomes.DESERT, true, 2, 8, 14, 0xCCB94E);
		loadTradeInfo(Biomes.SAVANNA, true, 2, 8, 14, 0x9BA562);

		loadTradeInfo(Biomes.SWAMPLAND, true, 3, 12, 18, 0x22370F);
		loadTradeInfo(Biomes.REDWOOD_TAIGA, true, 3, 12, 18, 0x5B421F);
		loadTradeInfo(Biomes.MUTATED_FOREST, true, 3, 12, 18, 0xDC7BEA, "(Flower Forest)");
		
		loadTradeInfo(Biomes.JUNGLE, true, 4, 16, 22, 0x22B600);
		loadTradeInfo(Biomes.MESA, true, 4, 16, 22, 0xC67F22);

		loadTradeInfo(Biomes.MUSHROOM_ISLAND, true, 5, 20, 26, 0x4D4273);
		loadTradeInfo(Biomes.MUTATED_ICE_FLATS, true, 5, 20, 26, 0x41D6C9, "(Ice Spikes)");
	}
	
	@SubscribeEvent
	public void onRegisterVillagers(RegistryEvent.Register<VillagerProfession> event) {
		VillagerProfession librarian = event.getRegistry().getValue(new ResourceLocation("minecraft:librarian"));
		VillagerCareer cartographer = librarian.getCareer(1);
		
		for(Integer level : trades.keySet())
			cartographer.addTrade(level, new PathfinderMapTrade(level));
 	}
	
	private void loadTradeInfo(Biome biome, boolean enabled, int level, int minPrice, int maxPrice, int color) {
		loadTradeInfo(biome, enabled, level, minPrice, maxPrice, color, "");
	}
	
	private void loadTradeInfo(Biome biome, boolean enabled, int level, int minPrice, int maxPrice, int color, String comment) {
		String category = configCategory + "." + biome.getRegistryName().getResourcePath();
		if(!comment.isEmpty())
			ModuleLoader.config.getCategory(category).setComment(comment);
		
		TradeInfo info = new TradeInfo(category, biome, enabled, level, minPrice, maxPrice, color);
		if(info.enabled)
			trades.put(info.level, info);
	}

	@Override
	public boolean requiresMinecraftRestartToEnable() {
		return true;
	}
	
	@Override
	public boolean hasSubscriptions() {
		return true;
	}
	
	public static ItemStack createMap(World world, BlockPos pos, Biome biome, int color) {
		BlockPos biomePos = BiomeLocator.spiralOutwardsLookingForBiome(world, biome, pos.getX(), pos.getZ());

		int id = world.getUniqueDataId("map");
		ItemStack stack = new ItemStack(Items.FILLED_MAP, 1, id);
		stack.setTranslatableName("quark.biomeMap." + biome.getRegistryName().getResourcePath());
		NBTTagCompound cmp = ItemNBTHelper.getCompound(stack, "display", false);
		cmp.setInteger("MapColor", color);
		ItemNBTHelper.setCompound(stack, "display", cmp);

		String s = "map_" + id;
		MapData mapdata = new MapData(s);
		world.setData(s, mapdata);
		mapdata.scale = 2;
		mapdata.xCenter = biomePos.getX() + (int) ((Math.random() - 0.5) * 200);
		mapdata.zCenter = biomePos.getZ() + (int) ((Math.random() - 0.5) * 200);
		mapdata.dimension = 0;
		mapdata.trackingPosition = true;
		mapdata.unlimitedTracking = true;

		ItemMap.renderBiomePreviewMap(world, stack);
		MapData.addTargetDecoration(stack, biomePos, "+", Type.TARGET_X);

		return stack;
	}

	private static class PathfinderMapTrade implements EntityVillager.ITradeList {
		
		public final int level;
		
		public PathfinderMapTrade(int level) {
			this.level = level;
		}

		public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random) {
			List<TradeInfo> infos = new ArrayList(trades.get(level));
			if(infos == null || infos.isEmpty())
				return;
			
			if(unlockAllAtOnce)
				for(TradeInfo info : infos)
					unlock(merchant, recipeList, random, info);
			else {
				int amount = (level == 2 && multipleAtFirstUnlock) ? Math.min(infos.size(), 2 + random.nextInt(2)) : 1;
				
				for(int i = 0; i < amount; i++) {
					TradeInfo info = infos.get(random.nextInt(infos.size()));
					unlock(merchant, recipeList, random, info);
					infos.remove(info);
				}
			}
		}
		
		private void unlock(IMerchant merchant, MerchantRecipeList recipeList, Random random, TradeInfo info) {
			int i = random.nextInt(info.maxPrice - info.minPrice) + info.minPrice;
			World world = merchant.getWorld();

			ItemStack itemstack = createMap(merchant.getWorld(), merchant.getPos(), info.biome, info.color); 
			MerchantRecipe recipe = new MerchantRecipe(new ItemStack(Items.EMERALD, i), new ItemStack(Items.COMPASS), itemstack, 0, 1);
			recipeList.add(recipe);
		}
	}

	public static class TradeInfo {
		
		public final boolean enabled;
		public final Biome biome;
		public final int level;
		public final int minPrice;
		public final int maxPrice;
		public final int color;
		
		TradeInfo(String category, Biome biome, boolean enabled, int level, int minPrice, int maxPrice, int color) {
			this.enabled = ModuleLoader.config.getBoolean("Enabled", category, enabled, "");
			this.biome = biome;
			this.level = ModuleLoader.config.getInt("Required Villager Level", category, level, 0, 10, "");
			this.minPrice = ModuleLoader.config.getInt("Minimum Emerald Price", category, minPrice, 1, 64, "");
			this.maxPrice = Math.max(minPrice, ModuleLoader.config.getInt("Maximum Emerald Price", category, maxPrice, 1, 64, ""));
			this.color = color;
		}
	}
	
}
