package vazkii.quark.base.network.message;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import vazkii.arl.network.NetworkMessage;
import vazkii.quark.management.feature.RightClickAddToShulkerBox;

public class MessageAddToShulkerBox extends NetworkMessage<MessageAddToShulkerBox> {

	public int slot;
	public ItemStack stack = ItemStack.EMPTY;
	
	public MessageAddToShulkerBox() { }
	
	public MessageAddToShulkerBox(int slot) { 
		this.slot = slot;
	}
	
	public MessageAddToShulkerBox(int slot, ItemStack stack) { 
		this(slot);
		this.stack = stack;
	}
	
	@Override
	public IMessage handleMessage(MessageContext context) {
		RightClickAddToShulkerBox.addToShulkerBox(context.getServerHandler().player, slot, stack);
		
		return null;
	}

}
