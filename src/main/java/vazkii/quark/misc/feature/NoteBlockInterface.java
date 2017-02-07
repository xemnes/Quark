package vazkii.quark.misc.feature;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityNote;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import vazkii.quark.base.Quark;
import vazkii.quark.base.lib.LibGuiIDs;
import vazkii.quark.base.lib.LibObfuscation;
import vazkii.quark.base.module.Feature;

public class NoteBlockInterface extends Feature {

	boolean emptyHand;

	@Override
	public void setupConfig() {
		emptyHand = loadPropBool("Requires Empty Hands", "", false);
	}

	@SubscribeEvent
	public void onInteract(PlayerInteractEvent.RightClickBlock event) {
		if(event.getUseBlock() == Result.DENY)
			return;
		
		TileEntity tile = event.getWorld().getTileEntity(event.getPos());
		if(tile instanceof TileEntityNote && (!emptyHand || event.getEntityPlayer().getHeldItemMainhand().isEmpty()) && event.getEntityPlayer().capabilities.allowEdit && !event.getEntity().isSneaking()) {
			TileEntityNote noteBlock = (TileEntityNote) tile;

			event.getEntityPlayer().openGui(Quark.instance, LibGuiIDs.NOTE_BLOCK, event.getWorld(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ());
			event.setCanceled(true);
		}
	}

	@Override
	public boolean hasSubscriptions() {
		return true;
	}
	
}
