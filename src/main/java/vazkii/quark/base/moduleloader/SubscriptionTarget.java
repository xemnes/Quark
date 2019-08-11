package vazkii.quark.base.moduleloader;

import net.minecraftforge.fml.loading.FMLEnvironment;

public enum SubscriptionTarget {

	YES(true, true), 
	CLIENT_ONLY(true, false), 
	SERVER_ONLY(false, true), 
	NO(false, false);
	
	private SubscriptionTarget(boolean client, boolean server) {
		this.client = client;
		this.server = server;
	}
	
	private boolean client, server;
	
	public boolean shouldSubscribe() {
		return FMLEnvironment.dist.isClient() ? client : server;
	}
	
	
}
