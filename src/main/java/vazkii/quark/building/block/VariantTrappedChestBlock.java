package vazkii.quark.building.block;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.TrappedChestBlock;
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
import vazkii.quark.base.module.Module;
import vazkii.quark.building.tile.VariantTrappedChestTileEntity;

@OnlyIn(value = Dist.CLIENT, _interface = IBlockItemProvider.class)
public class VariantTrappedChestBlock extends TrappedChestBlock implements IBlockItemProvider {

	public final String type;
	private final Module module;
	
	public final ResourceLocation modelNormal, modelDouble;
	
	public VariantTrappedChestBlock(String type, Module module, Block.Properties props) {
		super(props);
		RegistryHelper.registerBlock(this, type + "_trapped_chest");
		RegistryHelper.setCreativeTab(this, ItemGroup.DECORATIONS);
		
		this.type = type;
		this.module = module;
		
		modelNormal = new ResourceLocation(Quark.MOD_ID, "textures/model/chest/" + type + "_trap.png");
		modelDouble = new ResourceLocation(Quark.MOD_ID, "textures/model/chest/" + type + "_trap_double.png");
	}
	
	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
		if(module.enabled || group == ItemGroup.SEARCH)
			super.fillItemGroup(group, items);
	}
	
	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		return new VariantTrappedChestTileEntity();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public BlockItem provideItemBlock(Block block, Item.Properties props) {
		VariantChestBlock.setTEISR(props, modelNormal, modelDouble);
		return new BlockItem(block, props);
	}
	
}
