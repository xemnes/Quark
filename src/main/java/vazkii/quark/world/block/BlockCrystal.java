package vazkii.quark.world.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.block.BlockMetaVariants;
import vazkii.quark.base.block.IQuarkBlock;

public class BlockCrystal extends BlockMetaVariants implements IQuarkBlock {

	public BlockCrystal() {
		super("crystal", Material.GLASS, Variants.class);
		setHardness(0.3F);
		setSoundType(SoundType.GLASS);
		setLightLevel(1.0F * 11F / 15F);
		setCreativeTab(CreativeTabs.DECORATIONS);
	}
	
	@Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }
    
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
        Block block = iblockstate.getBlock();

        return block == this ? false : super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }

	public enum Variants implements EnumBase {
		CRYSTAL_WHITE,
		CRYSTAL_RED,
		CRYSTAL_ORANGE,
		CRYSTAL_YELLOW,
		CRYSTAL_GREEN,
		CRYSTAL_BLUE,
		CRYSTAL_INDIGO,
		CRYSTAL_VIOLET
	}

}
