package vazkii.quark.base.network.message;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import vazkii.arl.network.IMessage;
import vazkii.quark.tweaks.module.DoubleDoorOpeningModule;

public class DoubleDoorMessage implements IMessage {

	private static final long serialVersionUID = 8024722624953236124L;
	
	public BlockPos pos;
	
	public DoubleDoorMessage() { }
	
	public DoubleDoorMessage(BlockPos pos) {
		this.pos = pos;
	}
	
	@Override
	public boolean receive(Context context) {
		context.enqueueWork(() -> DoubleDoorOpeningModule.openDoor(context.getSender().world, pos));
		return true;
	}

}
	