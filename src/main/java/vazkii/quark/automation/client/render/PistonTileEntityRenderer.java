package vazkii.quark.automation.client.render;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.PistonTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import vazkii.quark.automation.module.PistonsMoveTileEntitiesModule;
import vazkii.quark.base.handler.ReflectionKeys;
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

			GlStateManager.translated(x + piston.getOffsetX(pTicks), y + piston.getOffsetY(pTicks), z + piston.getOffsetZ(pTicks));

			RenderHelper.enableStandardItemLighting();
			ObfuscationReflectionHelper.setPrivateValue(TileEntity.class, tile, state, ReflectionKeys.TileEntity.CACHED_BLOCK_STATE);
			TileEntityRenderer<TileEntity> tileentityrenderer = TileEntityRendererDispatcher.instance.getRenderer(tile);
			tileentityrenderer.render(tile, 0, 0, 0, pTicks, -1);
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
