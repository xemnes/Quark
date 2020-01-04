package vazkii.quark.api.flag;

import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.GameData;
import vazkii.quark.api.Module;

import java.util.HashMap;
import java.util.Map;

public final class ConfigFlagManager {

	private Map<String, Boolean> flags = new HashMap<>();
	
	public ConfigFlagManager() {
		CraftingHelper.register(new FlagRecipeCondition.Serializer(this, GameData.checkPrefix("flag", false)));
		LootConditionManager.registerCondition(new FlagLootCondition.Serializer(this, GameData.checkPrefix("flag", false)));

		CraftingHelper.register(GameData.checkPrefix("potion", false), PotionIngredient.Serializer.INSTANCE);
		CraftingHelper.register(GameData.checkPrefix("flag", false), new FlagIngredient.Serializer(this));
	}
	
	public void clear() {
		flags.clear();
	}
	
	public void putFlag(Module module, String flag, boolean value) {
		flags.put(flag, value && module.enabled);
	}
	
	public void putEnabledFlag(Module module) {
		flags.put(module.lowercaseName, module.enabled);
	}
	
	public boolean getFlag(String flag) {
		Boolean obj = flags.get(flag);
		return obj != null && obj;
	}
	
}
