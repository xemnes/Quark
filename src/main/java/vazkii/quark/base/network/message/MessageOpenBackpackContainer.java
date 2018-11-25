package vazkii.quark.base.network.message;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import vazkii.arl.network.NetworkMessage;
import vazkii.quark.oddities.inventory.ContainerBackpack;

public class MessageOpenBackpackContainer extends NetworkMessage<MessageOpenBackpackContainer> {

	@Override
	public IMessage handleMessage(MessageContext context) {
		EntityPlayerMP player = context.getServerHandler().player;
		player.getServer().addScheduledTask(() -> player.openContainer = new ContainerBackpack(player));
		
		return null;
	}
	
}
