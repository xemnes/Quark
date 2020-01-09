package vazkii.quark.building.block;

import net.minecraft.item.ItemGroup;
import vazkii.quark.base.block.QuarkGlassBlock;
import vazkii.quark.base.module.Module;

public class FramedGlassBlock extends QuarkGlassBlock {

	public FramedGlassBlock(String regname, Module module, ItemGroup creativeTab, Properties properties) {
		super(regname, module, creativeTab, properties);
	}
//
//	@Nonnull TODO
//	@Override
//	@OnlyIn(Dist.CLIENT)
//	public BlockRenderLayer getRenderLayer() {
//		return BlockRenderLayer.CUTOUT;
//	}

}
