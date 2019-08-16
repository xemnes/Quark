package vazkii.quark.api;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class QuarkCapabilities {

	@CapabilityInject(ICustomSorting.class)
	public static Capability<ICustomSorting> SORTING = null;
	
	@CapabilityInject(ITransferManager.class)
	public static Capability<ITransferManager> TRANSFER = null;
	
}
