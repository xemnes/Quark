package vazkii.quark.base.module;

import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraftforge.common.crafting.CraftingHelper;
import vazkii.quark.base.Quark;

import java.util.HashMap;
import java.util.Map;

public final class ConfigFlagManager {

	private Map<String, Boolean> flags = new HashMap<>();
	
	public ConfigFlagManager() {
		CraftingHelper.register(new ResourceLocation(Quark.MOD_ID, "flag"), json -> () -> getFlag(JSONUtils.getString(json, "flag")));
		LootConditionManager.registerCondition(new FlagCondition.Serializer(this, new ResourceLocation(Quark.MOD_ID, "flag")));
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
