package vazkii.quark.base.handler;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.quark.base.Quark;

public class MiscUtil {

	public static final ResourceLocation GENERAL_ICONS = new ResourceLocation(Quark.MOD_ID, "textures/gui/general_icons.png");

	public static final Direction[] HORIZONTALS = new Direction[] {
			Direction.NORTH,
			Direction.SOUTH,
			Direction.WEST,
			Direction.EAST
	};

	public static final String[] VARIANT_WOOD_TYPES = new String[] {
			"spruce",
			"birch",
			"jungle",
			"acacia", 
			"dark_oak"	
	};

	public static final String[] ALL_WOOD_TYPES = new String[] {
			"oak",
			"spruce",
			"birch",
			"jungle",
			"acacia", 
			"dark_oak"
	};

	public static void addToLootTable(LootTable table, LootEntry entry) {
		List<LootPool> pools = ObfuscationReflectionHelper.getPrivateValue(LootTable.class, table, "field_186466_c"); // table.pools;
		if (pools == null)
			return;
		LootPool pool = pools.get(0);
		List<LootEntry> list = ObfuscationReflectionHelper.getPrivateValue(LootPool.class, pool, "field_186453_a"); // pool.lootEntries;
		if (list == null)
			return;
		list.add(entry);
	}

	public static void damageStack(PlayerEntity player, Hand hand, ItemStack stack, int dmg) {
		stack.damageItem(dmg, player, (p) -> p.sendBreakAnimation(hand));
	}
	
	public static <T, V> void editFinalField(Class<T> clazz, String fieldName, Object obj, V value) {
		Field f = ObfuscationReflectionHelper.findField(clazz, fieldName);
		editFinalField(f, obj, value);
	}

	public static <T> void editFinalField(Field f, Object obj, T value) {
		try {
			f.setAccessible(true);
			
			Field modifiers = Field.class.getDeclaredField("modifiers");
			modifiers.setAccessible(true);
			modifiers.setInt(f, f.getModifiers() & ~Modifier.FINAL);
			
			f.set(obj, value);
		} catch(ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void initializeEnchantmentList(Iterable<String> enchantNames, List<Enchantment> enchants) {
		enchants.clear();
		for(String s : enchantNames) {
			ResourceLocation r = new ResourceLocation(s);
			Enchantment e = ForgeRegistries.ENCHANTMENTS.getValue(r);
			if(e != null)
				enchants.add(e);
		}
	}	

}
