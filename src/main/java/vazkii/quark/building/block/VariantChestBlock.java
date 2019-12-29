package vazkii.quark.building.block;

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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.arl.interf.IBlockItemProvider;
import vazkii.arl.util.RegistryHelper;
import vazkii.quark.base.Quark;
import vazkii.quark.base.block.IQuarkBlock;
import vazkii.quark.base.module.Module;
import vazkii.quark.building.client.render.VariantChestTileEntityRenderer;
import vazkii.quark.building.tile.VariantChestTileEntity;

import javax.annotation.Nullable;
import java.util.function.BooleanSupplier;

@OnlyIn(value = Dist.CLIENT, _interface = IBlockItemProvider.class)
public class VariantChestBlock extends ChestBlock implements IBlockItemProvider, IQuarkBlock {

	public final String type;
	private final Module module;
	private BooleanSupplier enabledSupplier = () -> true;

	public final ResourceLocation modelNormal, modelDouble;
	
	public VariantChestBlock(String type, Module module, Block.Properties props) {
		super(props);
		RegistryHelper.registerBlock(this, type + "_chest");
		RegistryHelper.setCreativeTab(this, ItemGroup.DECORATIONS);
		
		this.type = type;
		this.module = module;
		
		modelNormal = new ResourceLocation(Quark.MOD_ID, "textures/model/chest/" + type + ".png");
		modelDouble = new ResourceLocation(Quark.MOD_ID, "textures/model/chest/" + type + "_double.png");
	}

	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
		if(isEnabled() || group == ItemGroup.SEARCH)
			super.fillItemGroup(group, items);
	}

	@Override
	public VariantChestBlock setCondition(BooleanSupplier enabledSupplier) {
		this.enabledSupplier = enabledSupplier;
		return this;
	}

	@Override
	public boolean doesConditionApply() {
		return enabledSupplier.getAsBoolean();
	}

	@Nullable
	@Override
	public Module getModule() {
		return module;
	}

	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		return new VariantChestTileEntity();
	}
	
	@OnlyIn(Dist.CLIENT)
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
	@OnlyIn(Dist.CLIENT)
	public BlockItem provideItemBlock(Block block, Item.Properties props) {
		setTEISR(props, modelNormal, modelDouble);
		return new BlockItem(block, props);
	}
	
}
