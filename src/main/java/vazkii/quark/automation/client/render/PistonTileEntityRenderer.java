package vazkii.quark.automation.client.render;

import java.util.Objects;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.PistonTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import vazkii.quark.automation.module.PistonsMoveTileEntitiesModule;
import vazkii.quark.base.Quark;
import vazkii.quark.base.module.ModuleLoader;

public class PistonTileEntityRenderer {

	public static boolean renderPistonBlock(PistonTileEntity piston, float pTicks, MatrixStack matrix, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		if (!ModuleLoader.INSTANCE.isModuleEnabled(PistonsMoveTileEntitiesModule.class) || piston.getProgress(pTicks) > 1.0F)
			return false;

		BlockState state = piston.getPistonState();
		Block block = state.getBlock();
		String id = Objects.toString(block.getRegistryName());
		BlockPos truePos = piston.getPos();

		try {
			TileEntity tile = PistonsMoveTileEntitiesModule.getMovement(piston.getWorld(), truePos);
			
			if(tile == null || PistonsMoveTileEntitiesModule.renderBlacklist.contains(id))
				return false;

			TileEntityRenderer<TileEntity> tileentityrenderer = TileEntityRendererDispatcher.instance.getRenderer(tile);
			if(tileentityrenderer != null) {
				matrix.push();
				tile.setWorldAndPos(piston.getWorld(), piston.getPos());
				tile.validate();

				matrix.translate(piston.getOffsetX(pTicks), piston.getOffsetY(pTicks), piston.getOffsetZ(pTicks));

				tile.cachedBlockState = state;
				tileentityrenderer.render(tile, pTicks, matrix, bufferIn, combinedLightIn, combinedOverlayIn);

				
				matrix.pop();
			}
		} catch(Throwable e) {
			Quark.LOG.warn(id + " can't be rendered for piston TE moving", e);
			PistonsMoveTileEntitiesModule.renderBlacklist.add(id);
			return false;
		}

		return state.getRenderType() != BlockRenderType.MODEL;
	}

}
