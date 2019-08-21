package vazkii.quark.base.network.message;

import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import vazkii.arl.network.IMessage;
import vazkii.quark.vanity.client.emote.EmoteHandler;

public class DoEmoteMessage implements IMessage {

	private static final long serialVersionUID = -7952633556330869633L;
	
	public String emote;
	public UUID playerUUID;
	public int tier;
	
	public DoEmoteMessage() { }
	
	public DoEmoteMessage(String emote, UUID playerUUID, int tier) {
		this.emote = emote;
		this.playerUUID = playerUUID;
		this.tier = tier;
	}
	
	@Override
	public boolean receive(Context context) {
		context.enqueueWork(() -> {
			World world = Minecraft.getInstance().world;
			PlayerEntity player = world.getPlayerByUuid(playerUUID);
			if(player instanceof AbstractClientPlayerEntity)
				EmoteHandler.putEmote((AbstractClientPlayerEntity) player, emote, tier);
		});
		
		return true;
	}

}
