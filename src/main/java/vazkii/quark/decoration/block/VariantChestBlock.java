package vazkii.quark.decoration.block;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockReader;
import vazkii.arl.interf.IBlockItemProvider;
import vazkii.arl.util.RegistryHelper;
import vazkii.quark.base.Quark;
import vazkii.quark.base.module.Module;
import vazkii.quark.decoration.client.render.VariantChestTileEntityRenderer;
import vazkii.quark.decoration.tile.VariantChestTileEntity;

public class VariantChestBlock extends ChestBlock implements IBlockItemProvider {

	public final String type;
	private final Module module;
	
	public final ResourceLocation modelNormal, modelDouble;
	
	public VariantChestBlock(String type, Module module) {
		super(Block.Properties.from(Blocks.CHEST));
		RegistryHelper.registerBlock(this, type + "_chest");
		RegistryHelper.setCreativeTab(this, ItemGroup.DECORATIONS);
		
		this.type = type;
		this.module = module;
		
		modelNormal = new ResourceLocation(Quark.MOD_ID, "textures/model/chest/" + type + ".png");
		modelDouble = new ResourceLocation(Quark.MOD_ID, "textures/model/chest/" + type + "_double.png");
	}
	
	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
		if(module.enabled || group == ItemGroup.SEARCH)
			super.fillItemGroup(group, items);
	}
	

	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		return new VariantChestTileEntity();
	}
	
	public static void setTEISR(Item.Properties props, ResourceLocation modelNormal, ResourceLocation modelDouble) {
		props.setTEISR(() -> () -> new ItemStackTileEntityRenderer() {
			private final TileEntity tile = new VariantChestTileEntity();
			
			@Override
			public void renderByItem(ItemStack itemStackIn) {
				VariantChestTileEntityRenderer.forceNormal = modelNormal;
				VariantChestTileEntityRenderer.forceDouble = modelDouble;
				TileEntityRendererDispatcher.instance.renderAsItem(tile);
			}
		});
	}

	@Override
	public BlockItem provideItemBlock(Block block, Item.Properties props) {
		setTEISR(props, modelNormal, modelDouble);
		return new BlockItem(block, props);
	}
	
}
