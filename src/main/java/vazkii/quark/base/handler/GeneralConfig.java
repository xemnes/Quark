package vazkii.quark.base.handler;

import vazkii.quark.base.module.Config;

public class GeneralConfig {
	
	public static final GeneralConfig INSTANCE = new GeneralConfig();
	
	@Config(name = "Use Piston Logic Replacement",
			description = "Quark replaces the Piston logic to allow for its piston features to work. If you're having troubles, try turning this off.")
	public static boolean usePistonLogicRepl = true;
	
	@Config public static int pistonPushLimit = 12;
	
	@Config public static boolean useAntiOverlap = true;
	
	@Config(description = "Quark messes with the Stonecutter to allow any item that can be used in it to be shift clicked in. Set this to false to turn it off.")
	public static boolean hackStonecutterShiftClick = true;
	
	private GeneralConfig() { }

}
