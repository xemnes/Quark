package vazkii.quark.api;

/**
 * Implement on a TileEntity to add a callback to when it's moved by a piston.
 */
public interface IPistonCallback {

	void onPistonMovementStarted();
	void onPistonMovementFinished();
	
}
