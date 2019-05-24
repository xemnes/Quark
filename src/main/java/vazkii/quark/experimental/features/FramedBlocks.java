package vazkii.quark.experimental.features;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.quark.base.lib.LibMisc;
import vazkii.quark.base.module.Feature;
import vazkii.quark.experimental.block.BlockFramed;
import vazkii.quark.experimental.client.model.FramedBlockModel;

public class FramedBlocks extends Feature {

	public static Block frame;
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		frame = new BlockFramed();
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onModelBake(ModelBakeEvent event) {
		applyCustomModel(event, "frame");
	}
	
	private void applyCustomModel(ModelBakeEvent event, String modelName) {
		ModelResourceLocation location = new ModelResourceLocation(new ResourceLocation(LibMisc.MOD_ID, modelName), "normal");
		IModel model = ModelLoaderRegistry.getModelOrLogError(location, "Error loading model for " + location);
		IBakedModel standard = event.getModelRegistry().getObject(location);
		IBakedModel finalModel = new FramedBlockModel(standard, model);
		event.getModelRegistry().putObject(location, finalModel);
	}
	
	@Override
	public boolean requiresMinecraftRestartToEnable() {
		return true;
	}
	
	@Override
	public boolean hasSubscriptions() {
		return true;
	}
	
}
