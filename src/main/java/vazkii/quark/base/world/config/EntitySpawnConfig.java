package vazkii.quark.base.world.config;

import vazkii.quark.base.module.Config;
import vazkii.quark.base.module.ConfigFlagManager;
import vazkii.quark.base.module.IConfigType;
import vazkii.quark.base.module.Module;

public class EntitySpawnConfig implements IConfigType {

	public final String flag;
	public Module module;

	@Config
	public boolean enabled = true;

	@Config
	@Config.Min(value = 0, exclusive = true)
	public int spawnWeight = 40;

	@Config
	@Config.Min(1)
	public int minGroupSize = 1;

	@Config
	@Config.Min(1)
	public int maxGroupSize = 3;
	
	@Config
	public BiomeTypeConfig biomes;

	public EntitySpawnConfig(String flag, int spawnWeight, int minGroupSize, int maxGroupSize, BiomeTypeConfig biomes) {
		this.flag = flag;
		this.spawnWeight = spawnWeight;
		this.minGroupSize = minGroupSize;
		this.maxGroupSize = maxGroupSize;
		this.biomes = biomes;
	}
	
	public void setModule(Module module) {
		this.module = module;
	}

	@Override
	public void onReload(ConfigFlagManager flagManager) {
		if(module != null)
			flagManager.putFlag(module, flag, enabled);
	} 

}
