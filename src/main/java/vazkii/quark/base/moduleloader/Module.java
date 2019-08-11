package vazkii.quark.base.moduleloader;

import java.util.List;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;

public class Module {

	public String displayName = "";
	public String lowercaseName = "";
	public String description = "";
	public List<String> antiOverlap = null;
	public SubscriptionTarget subscriptionTarget = SubscriptionTarget.NONE;
	public boolean enabledByDefault = true;
	
	public boolean enabled = false;
	public boolean ignoreAntiOverlap = false;
	
	public void start() {
		// NO-OP
	}
	
	public void configChanged() {
		// NO-OP
	}
	
	public void setup() {
		// NO-OP
	}
	
	public void modulesLoaded() {
		// NO-OP
	}
	
	@OnlyIn(Dist.CLIENT)
	public void clientSetup() {
		// NO-OP
	}
	
	public void loadComplete() {
		// NO-OP
	}
	
	public void pushFlags(ConfigFlagManager manager) {
		// NO-OP
	}
	
	public final void setEnabled(boolean enabled) {
		if(!ignoreAntiOverlap && antiOverlap != null) {
			ModList list = ModList.get();
			for(String s : antiOverlap)
				if(list.isLoaded(s))
					return;
		}
		
		setEnabledAndManageSubscriptions(enabled);
	}
	
	private final void setEnabledAndManageSubscriptions(boolean enabled) {
		boolean wasEnabled = this.enabled;
		this.enabled = enabled;
		
		if(subscriptionTarget.shouldSubscribe() && wasEnabled != enabled) {
			if(enabled)
				MinecraftForge.EVENT_BUS.register(this);
			else MinecraftForge.EVENT_BUS.unregister(this);
		}
	}
	
}
