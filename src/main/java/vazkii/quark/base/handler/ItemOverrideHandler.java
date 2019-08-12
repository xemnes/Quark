package vazkii.quark.base.handler;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class ItemOverrideHandler {

	private static Map<Item, String> defaultItemKeys = new HashMap<>();
	private static Map<Block, String> defaultBlockKeys = new HashMap<>();
	
	public static void changeItemLocalizationKey(Item item, String newKey, boolean enabled) {
		if(!enabled) {
			if(defaultItemKeys.containsKey(item))
				changeItemLocalizationKey(item, defaultItemKeys.get(item));
		} else {
			String currKey = ObfuscationReflectionHelper.getPrivateValue(Item.class, item, ReflectionKeys.Item.TRANSLATION_KEY);
			if(!defaultItemKeys.containsKey(item))
				defaultItemKeys.put(item, currKey);
			
			changeItemLocalizationKey(item, newKey);
		}
	}
	
	public static void changeBlockLocalizationKey(Block block, String newKey, boolean enabled) {
		if(!enabled) {
			if(defaultBlockKeys.containsKey(block))
				changeBlockLocalizationKey(block, defaultBlockKeys.get(block));
		} else {
			String currKey = ObfuscationReflectionHelper.getPrivateValue(Block.class, block, ReflectionKeys.Block.TRANSLATION_KEY);
			if(!defaultBlockKeys.containsKey(block))
				defaultBlockKeys.put(block, currKey);
			
			changeBlockLocalizationKey(block, newKey);
		}
	}
	
	private static void changeItemLocalizationKey(Item item, String newKey) {
		ObfuscationReflectionHelper.setPrivateValue(Item.class, item, newKey, ReflectionKeys.Item.TRANSLATION_KEY);
	}
	
	
	private static void changeBlockLocalizationKey(Block block, String newKey) {
		ObfuscationReflectionHelper.setPrivateValue(Block.class, block, newKey, ReflectionKeys.Block.TRANSLATION_KEY);
	}
	
}
