package vazkii.quark.base.handler;

import java.util.List;

import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import vazkii.quark.base.Quark;

public class MiscUtil {

	public static final ResourceLocation GENERAL_ICONS = new ResourceLocation(Quark.MOD_ID, "textures/gui/general_icons.png");
	
	public static final Direction[] HORIZONTALS = new Direction[] {
			Direction.NORTH,
			Direction.SOUTH,
			Direction.WEST,
			Direction.EAST
	};
	
	public static void addToLootTable(LootTable table, LootEntry entry) {
		List<LootPool> pools = ObfuscationReflectionHelper.getPrivateValue(LootTable.class, table, ReflectionKeys.LootTable.POOLS);
		LootPool pool = pools.get(0);
		List<LootEntry> list = ObfuscationReflectionHelper.getPrivateValue(LootPool.class, pool, ReflectionKeys.LootPool.LOOT_ENTRIES);
		System.out.println(list);
		list.add(entry);
	}
	
}
