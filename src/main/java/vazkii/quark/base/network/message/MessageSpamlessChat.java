/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 *
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 *
 * File Created @ [02/04/2016, 17:44:30 (GMT)]
 */
package vazkii.quark.base.network.message;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import vazkii.arl.network.NetworkMessage;
import vazkii.arl.util.ClientTicker;

public class MessageSpamlessChat extends NetworkMessage<MessageSpamlessChat> {

	public ITextComponent message;
	public int id;

	public MessageSpamlessChat() { }

	public MessageSpamlessChat(ITextComponent message, int id) {
		this.message = message;
		this.id = id;
	}

	@Override
	public IMessage handleMessage(MessageContext context) {
		ClientTicker.addAction(() -> {
			Minecraft.getMinecraft().ingameGUI.getChatGUI()
					.printChatMessageWithOptionalDeletion(message, id);
		});
		return null;
	}

}
