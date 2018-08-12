package vazkii.quark.oddities.feature;

import net.minecraft.block.Block;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import vazkii.quark.base.module.Feature;
import vazkii.quark.oddities.block.BlockPipe;
import vazkii.quark.oddities.client.render.RenderTilePipe;
import vazkii.quark.oddities.tile.TilePipe;

public class Pipes extends Feature {

	public static Block pipe;
	
	public static int pipeSpeed;
	public static int maxPipeItems;
	
	@Override
	public void setupConfig() {
		pipeSpeed = loadPropInt("Pipe Speed", "How long it takes for an item to cross a pipe. Bigger = slower.", 5) * 2;
		maxPipeItems = loadPropInt("Max Pipe Items", "Set to 0 if you don't want pipes to have a max amount of items", 16);
	}
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		pipe = new BlockPipe();
		registerTile(TilePipe.class, "pipe");
	}
	
	@Override
	public void preInitClient(FMLPreInitializationEvent event) {
		ClientRegistry.bindTileEntitySpecialRenderer(TilePipe.class, new RenderTilePipe());
	}
	
}
