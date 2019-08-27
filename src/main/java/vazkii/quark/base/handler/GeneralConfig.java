package vazkii.quark.base.handler;

import vazkii.quark.base.module.Config;

public class GeneralConfig {

	public static final GeneralConfig INSTANCE = new GeneralConfig();

	@Config(name = "Enable 'q' Button")
	public static boolean enableQButton = true;

	@Config(name = "'q' Button on the Right")
	public static boolean qButtonOnRight = false;

	@Config
	public static boolean useAntiOverlap = true;

	@Config(name = "Use Piston Logic Replacement",
			description = "Quark replaces the Piston logic to allow for its piston features to work. If you're having troubles, try turning this off.")
	public static boolean usePistonLogicRepl = true;

	@Config
	@Config.Min(value = 0, exclusive = true)
	public static int pistonPushLimit = 12;

	@Config(description = "Quark messes with the Stonecutter to allow any item that can be used in it to be shift clicked in. Set this to false to turn it off.")
	public static boolean hackStonecutterShiftClick = true;

	private GeneralConfig() {
		// NO-OP
	}

}
