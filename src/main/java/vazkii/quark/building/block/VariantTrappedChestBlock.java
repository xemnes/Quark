package vazkii.quark.building.block;

import java.util.function.BooleanSupplier;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
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
import net.minecraftforge.fml.ModList;
import vazkii.arl.interf.IBlockItemProvider;
import vazkii.arl.util.RegistryHelper;
import vazkii.quark.base.Quark;
import vazkii.quark.base.block.IQuarkBlock;
import vazkii.quark.base.module.Module;
import vazkii.quark.building.tile.VariantTrappedChestTileEntity;

@OnlyIn(value = Dist.CLIENT, _interface = IBlockItemProvider.class)
public class VariantTrappedChestBlock extends TrappedChestBlock implements IBlockItemProvider, IQuarkBlock {

	public final String type;
	private final Module module;
	private BooleanSupplier enabledSupplier = () -> true;
	
	public final ResourceLocation modelNormal, modelDouble;
	
	public VariantTrappedChestBlock(String type, Module module, Block.Properties props) {
		super(props);
		RegistryHelper.registerBlock(this, type + "_trapped_chest");
		RegistryHelper.setCreativeTab(this, ItemGroup.REDSTONE);
		
		this.type = type;
		this.module = module;
		
		String path = (this instanceof Compat ? "compat/" : "");
		modelNormal = new ResourceLocation(Quark.MOD_ID, "textures/model/chest/" + path + type + "_trap.png");
		modelDouble = new ResourceLocation(Quark.MOD_ID, "textures/model/chest/" + path + type + "_trap_double.png");
	}
	
	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
		if(module.enabled || group == ItemGroup.SEARCH)
			super.fillItemGroup(group, items);
	}
	
	@Override
	public VariantTrappedChestBlock setCondition(BooleanSupplier enabledSupplier) {
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
		return new VariantTrappedChestTileEntity();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public BlockItem provideItemBlock(Block block, Item.Properties props) {
		VariantChestBlock.setTEISR(props, modelNormal, modelDouble);
		return new BlockItem(block, props);
	}
	
	public static class Compat extends VariantTrappedChestBlock {

		public Compat(String type, String mod, Module module, Properties props) {
			super(type, module, props);
			setCondition(() -> ModList.get().isLoaded(mod));
		}
		
	}
	
}
