package vazkii.quark.base.module;

import vazkii.quark.api.flag.ConfigFlagManager;

public interface IConfigType {

	default void onReload(ConfigFlagManager flagManager) { }
	
}
