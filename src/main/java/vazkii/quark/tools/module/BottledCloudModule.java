package vazkii.quark.tools.module;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import vazkii.arl.util.RegistryHelper;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.tools.block.CloudBlock;
import vazkii.quark.tools.client.render.CloudTileEntityRenderer;
import vazkii.quark.tools.item.BottledCloudItem;
import vazkii.quark.tools.tile.CloudTileEntity;

@LoadModule(category = ModuleCategory.TOOLS)
public class BottledCloudModule extends Module {

    public static TileEntityType<CloudTileEntity> tileEntityType;
    public static Block cloud;
    public static Item bottled_cloud;

	@Override
	public void construct() {
		cloud = new CloudBlock(this);
		bottled_cloud = new BottledCloudItem(this);
		
    	tileEntityType = TileEntityType.Builder.create(CloudTileEntity::new, cloud).build(null);
		RegistryHelper.register(tileEntityType, "cloud");
	}
	
	@Override
	public void clientSetup() {
		ClientRegistry.bindTileEntityRenderer(tileEntityType, CloudTileEntityRenderer::new);
	}
	
}
