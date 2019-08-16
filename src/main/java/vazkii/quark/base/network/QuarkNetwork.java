package vazkii.quark.base.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.NetworkDirection;
import vazkii.arl.network.IMessage;
import vazkii.arl.network.NetworkHandler;
import vazkii.quark.base.Quark;
import vazkii.quark.base.network.message.InventoryTransferMessage;
import vazkii.quark.base.network.message.SortInventoryMessage;

public final class QuarkNetwork {

	private static final int PROTOCOL_VERSION = 1;
	
	private static NetworkHandler network;
	
	public static void setup() {
		network = new NetworkHandler(Quark.MOD_ID, PROTOCOL_VERSION);
		
		network.register(SortInventoryMessage.class, NetworkDirection.PLAY_TO_SERVER);
		network.register(InventoryTransferMessage.class, NetworkDirection.PLAY_TO_SERVER);
	}
	
	public static void sendToPlayer(IMessage msg, ServerPlayerEntity player) {
		network.sendToPlayer(msg, player);
	}
	
	public static void sendToServer(IMessage msg) {
		network.sendToServer(msg);
	}
	
}
