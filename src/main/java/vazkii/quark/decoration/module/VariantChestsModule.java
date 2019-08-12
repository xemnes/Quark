package vazkii.quark.decoration.module;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import vazkii.arl.util.RegistryHelper;
import vazkii.quark.base.handler.ItemOverrideHandler;
import vazkii.quark.base.moduleloader.Config;
import vazkii.quark.base.moduleloader.LoadModule;
import vazkii.quark.base.moduleloader.Module;
import vazkii.quark.base.moduleloader.ModuleCategory;
import vazkii.quark.decoration.block.VariantChestBlock;
import vazkii.quark.decoration.block.VariantTrappedChestBlock;
import vazkii.quark.decoration.client.render.VariantChestTileEntityRenderer;
import vazkii.quark.decoration.tile.VariantChestTileEntity;
import vazkii.quark.decoration.tile.VariantTrappedChestTileEntity;

@LoadModule(category = ModuleCategory.DECORATION)
public class VariantChestsModule extends Module {

	@Config public boolean changeNames = true;
	
	public static TileEntityType<VariantChestTileEntity> chestTEType;
	public static TileEntityType<VariantTrappedChestTileEntity> trappedChestTEType;

	@Override
	public void start() {
		Block spruceChest = new VariantChestBlock("spruce", this);
		Block birchChest = new VariantChestBlock("birch", this);
		Block jungleChest = new VariantChestBlock("jungle", this);
		Block acaciaChest = new VariantChestBlock("acacia", this);
		Block darkOakChest = new VariantChestBlock("dark_oak", this);
		
		Block spruceChestTrapped = new VariantTrappedChestBlock("spruce", this);
		Block birchChestTrapped = new VariantTrappedChestBlock("birch", this);
		Block jungleChestTrapped = new VariantTrappedChestBlock("jungle", this);
		Block acaciaChestTrapped = new VariantTrappedChestBlock("acacia", this);
		Block darkOakChestTrapped = new VariantTrappedChestBlock("dark_oak", this);
		
		chestTEType = TileEntityType.Builder.create(VariantChestTileEntity::new, spruceChest, birchChest, jungleChest, acaciaChest, darkOakChest).build(null);
		trappedChestTEType = TileEntityType.Builder.create(VariantTrappedChestTileEntity::new, spruceChestTrapped, birchChestTrapped, jungleChestTrapped, acaciaChestTrapped, darkOakChestTrapped).build(null);

		RegistryHelper.register(chestTEType, "variant_chest");
		RegistryHelper.register(trappedChestTEType, "variant_trapped_chest");
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void clientSetup() {
		ClientRegistry.bindTileEntitySpecialRenderer(VariantChestTileEntity.class, new VariantChestTileEntityRenderer());
	}
	
	@Override
	public void configChanged() {
		System.out.println("enabled is " + enabled);
		ItemOverrideHandler.changeBlockLocalizationKey(Blocks.CHEST, "block.quark.oak_chest", changeNames && enabled);
		ItemOverrideHandler.changeBlockLocalizationKey(Blocks.TRAPPED_CHEST, "block.quark.oak_trapped_chest", changeNames && enabled);
	}
	
}
