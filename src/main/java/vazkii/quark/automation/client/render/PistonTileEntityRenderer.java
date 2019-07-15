package vazkii.quark.automation.client.render;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import vazkii.quark.automation.feature.PistonsMoveTEs;
import vazkii.quark.base.lib.LibObfuscation;

public class PistonTileEntityRenderer {

	public static void renderPistonBlock(BlockPos pos, IBlockState state, BufferBuilder buffer, World world) {
		Minecraft mc = Minecraft.getMinecraft();
		Block block = state.getBlock();
		String id = Block.REGISTRY.getNameForObject(block).toString();

		renderTE: {
			try {
				TileEntity tile = PistonsMoveTEs.getMovement(world, pos);
				if(tile == null || PistonsMoveTEs.renderBlacklist.contains(id))
					break renderTE;

				GlStateManager.pushMatrix();
				tile.setWorld(world);
				tile.validate();

				if(tile instanceof TileEntityChest) {
					TileEntityChest chest = (TileEntityChest) tile;
					chest.adjacentChestXPos = null;
					chest.adjacentChestXNeg = null;
					chest.adjacentChestZPos = null;
					chest.adjacentChestZNeg = null;
				}

				double x = (double) ObfuscationReflectionHelper.getPrivateValue(BufferBuilder.class, buffer, LibObfuscation.X_OFFSET) + pos.getX();
				double y = (double) ObfuscationReflectionHelper.getPrivateValue(BufferBuilder.class, buffer, LibObfuscation.Y_OFFSET) + pos.getY();
				double z = (double) ObfuscationReflectionHelper.getPrivateValue(BufferBuilder.class, buffer, LibObfuscation.Z_OFFSET) + pos.getZ();
				GlStateManager.translate(x, y, z);

				EnumFacing facing = null;

				if(state.getPropertyKeys().contains(BlockHorizontal.FACING))
					facing = state.getValue(BlockHorizontal.FACING);
				else if(state.getPropertyKeys().contains(BlockDirectional.FACING))
					facing = state.getValue(BlockDirectional.FACING);

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

					GlStateManager.translate(0.5, 0.5, 0.5);
					GlStateManager.rotate(rotation, 0, 1, 0);
					GlStateManager.translate(-0.5, -0.5, -0.5);
				}

				RenderHelper.enableStandardItemLighting();
				TileEntityRendererDispatcher.instance.render(tile, 0, 0, 0, 0);
				RenderHelper.disableStandardItemLighting();
				GlStateManager.popMatrix();

			} catch(Throwable e) {
				new RuntimeException(id + " can't be rendered for piston TE moving", e).printStackTrace();
				PistonsMoveTEs.renderBlacklist.add(id);
			}
		}

		mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
	}

}
