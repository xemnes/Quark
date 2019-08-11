package vazkii.quark.base;

import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import vazkii.quark.base.proxy.ClientProxy;
import vazkii.quark.base.proxy.CommonProxy;

@Mod(Quark.MOD_ID)
public class Quark {

	public static final String MOD_ID = "quark";
	
	public static Quark instance;
	public static CommonProxy proxy;
	
	public Quark() {
		instance = this;
		
		proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);
		proxy.start();
	}
	
}
