package vazkii.quark.automation.client.render;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.PistonTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import vazkii.quark.automation.module.PistonsMoveTileEntitiesModule;
import vazkii.quark.base.module.ModuleLoader;

public class PistonTileEntityRenderer {

	public static boolean renderPistonBlock(PistonTileEntity piston, double x, double y, double z, float pTicks) {
		if (!ModuleLoader.INSTANCE.isModuleEnabled(PistonsMoveTileEntitiesModule.class) || piston.getProgress(pTicks) > 1.0F)
			return false;

		BlockState state = piston.getPistonState();
		Block block = state.getBlock();
		String id = block.getRegistryName().toString();

		try {
			TileEntity tile = PistonsMoveTileEntitiesModule.getMovement(piston.getWorld(), piston.getPos());
			if(tile == null || PistonsMoveTileEntitiesModule.renderBlacklist.contains(id))
				return false;

			GlStateManager.pushMatrix();
			tile.setWorld(piston.getWorld());
			tile.validate();

//			if(tile instanceof ChestTileEntity) { TODO needed?
//				ChestTileEntity chest = (ChestTileEntity) tile;
//				chest.adjacentChestXPos = null;
//				chest.adjacentChestXNeg = null;
//				chest.adjacentChestZPos = null;
//				chest.adjacentChestZNeg = null;
//			}

			Direction facing = null;

			if(state.getProperties().contains(HorizontalBlock.HORIZONTAL_FACING))
				facing = state.get(HorizontalBlock.HORIZONTAL_FACING);
			else if(state.getProperties().contains(DirectionalBlock.FACING))
				facing = state.get(DirectionalBlock.FACING);

			GlStateManager.translated(x + piston.getOffsetX(pTicks), y + piston.getOffsetY(pTicks), z + piston.getOffsetZ(pTicks));

			if(facing != null) {
				float rotation = 0;
				switch(facing) {
					case NORTH:
						rotation = 180F;
						break;
					case EAST:
						rotation = 90F;
						break;
					case WEST:
						rotation = -90F;
						break;
					default: break;
				}

				GlStateManager.translated(0.5, 0.5, 0.5);
				GlStateManager.rotated(rotation, 0, 1, 0);
				GlStateManager.translated(-0.5, -0.5, -0.5);
			}

			RenderHelper.enableStandardItemLighting();
			TileEntityRendererDispatcher.instance.render(tile, 0, 0, 0, pTicks);
			RenderHelper.disableStandardItemLighting();
			GlStateManager.popMatrix();

		} catch(Throwable e) {
			new RuntimeException(id + " can't be rendered for piston TE moving", e).printStackTrace();
			PistonsMoveTileEntitiesModule.renderBlacklist.add(id);
			return false;
		}

		return state.getRenderType() != BlockRenderType.MODEL;
	}
	
}
