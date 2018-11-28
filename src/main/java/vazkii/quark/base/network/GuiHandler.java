/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 *
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 *
 * File Created @ [19/03/2016, 00:28:38 (GMT)]
 */
package vazkii.quark.base.network;

import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntityNote;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import vazkii.quark.base.lib.LibGuiIDs;
import vazkii.quark.misc.client.gui.GuiNoteBlock;
import vazkii.quark.oddities.client.gui.GuiBackpackInventory;
import vazkii.quark.oddities.inventory.ContainerBackpack;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch(ID) {
		case LibGuiIDs.BACKPACK: return new ContainerBackpack(player);
		}
		
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch(ID) {
		case LibGuiIDs.SIGN: return new GuiEditSign((TileEntitySign) world.getTileEntity(new BlockPos(x, y, z)));
		case LibGuiIDs.NOTE_BLOCK: return new GuiNoteBlock((TileEntityNote) world.getTileEntity(new BlockPos(x, y, z)));
		case LibGuiIDs.BACKPACK: return new GuiBackpackInventory(player);
		}
		return null;
	}

}
