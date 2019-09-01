package vazkii.quark.base.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.network.NetworkDirection;
import vazkii.arl.network.IMessage;
import vazkii.arl.network.NetworkHandler;
import vazkii.quark.base.Quark;
import vazkii.quark.base.network.message.*;

public final class QuarkNetwork {

	private static final int PROTOCOL_VERSION = 1;
	
	private static NetworkHandler network;
	
	public static void setup() {
		network = new NetworkHandler(Quark.MOD_ID, PROTOCOL_VERSION);
		
		network.register(SortInventoryMessage.class, NetworkDirection.PLAY_TO_SERVER);
		network.register(InventoryTransferMessage.class, NetworkDirection.PLAY_TO_SERVER);
		network.register(DoubleDoorMessage.class, NetworkDirection.PLAY_TO_SERVER);
		network.register(HarvestMessage.class, NetworkDirection.PLAY_TO_SERVER);
		network.register(RequestEmoteMessage.class, NetworkDirection.PLAY_TO_SERVER);
		network.register(MessageUpdateAfk.class, NetworkDirection.PLAY_TO_SERVER);
		network.register(LinkItemMessage.class, NetworkDirection.PLAY_TO_SERVER);

		network.register(DoEmoteMessage.class, NetworkDirection.PLAY_TO_CLIENT);
		network.register(MessageSpamlessChat.class, NetworkDirection.PLAY_TO_CLIENT);
	}
	
	public static void sendToPlayer(IMessage msg, ServerPlayerEntity player) {
		network.sendToPlayer(msg, player);
	}
	
	public static void sendToServer(IMessage msg) {
		network.sendToServer(msg);
	}
	
	public static void sendToPlayers(IMessage msg, Iterable<ServerPlayerEntity> players) {
		for(ServerPlayerEntity player : players)
			network.sendToPlayer(msg, player);
	}
	
	public static void sendToAllPlayers(IMessage msg, MinecraftServer server) {
		sendToPlayers(msg, server.getPlayerList().getPlayers());
	}
	
}
