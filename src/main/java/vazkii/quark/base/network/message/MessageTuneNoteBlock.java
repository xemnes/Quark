package vazkii.quark.base.network.message;

import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntityNote;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import vazkii.arl.network.TileEntityMessage;
import vazkii.quark.base.module.ModuleLoader;
import vazkii.quark.misc.feature.NoteBlockInterface;

public class MessageTuneNoteBlock extends TileEntityMessage<TileEntityNote> {

	public boolean next;
	public byte target;

	public MessageTuneNoteBlock() { }

	public MessageTuneNoteBlock(TileEntityNote note, boolean next, byte target) {
		super(note.getPos());
		this.next = next;
		this.target = target;
	}

	@Override
	public Runnable getAction() {
		return () -> {
			if(!ModuleLoader.isFeatureEnabled(NoteBlockInterface.class))
				return;
			
			byte old = tile.note;
			
			if(next)
				tile.changePitch();
			else {
				tile.note = target;
				if(net.minecraftforge.common.ForgeHooks.onNoteChange(tile, old))
					tile.markDirty();
			}

			tile.triggerNote(tile.getWorld(), pos);
		};
	}

}
