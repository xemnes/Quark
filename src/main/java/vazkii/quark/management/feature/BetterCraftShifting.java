package vazkii.quark.management.feature;

import vazkii.quark.base.module.Feature;
import vazkii.quark.base.module.ModuleLoader;

public class BetterCraftShifting extends Feature {

	public static int getInventoryBoundary(int curr) {
		if(!ModuleLoader.isFeatureEnabled(BetterCraftShifting.class))
			return curr;
		
		return curr == 37 ? 0 : 10;
	}
	
}
