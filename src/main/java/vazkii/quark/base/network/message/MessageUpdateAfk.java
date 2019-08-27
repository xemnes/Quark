package vazkii.quark.base.network.message;

import net.minecraftforge.fml.network.NetworkEvent;
import vazkii.arl.network.IMessage;
import vazkii.quark.tweaks.module.ImprovedSleepingModule;

public class MessageUpdateAfk implements IMessage {

	public boolean afk;
	
	public MessageUpdateAfk() { }

	public MessageUpdateAfk(boolean afk) { 
		this.afk = afk;
	}

	@Override
	public boolean receive(NetworkEvent.Context context) {
		context.enqueueWork(() -> ImprovedSleepingModule.updateAfk(context.getSender(), afk));
		return false;
	}

}
