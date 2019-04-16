package vazkii.quark.world.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.block.BlockMetaVariants;
import vazkii.quark.base.block.IQuarkBlock;

import javax.annotation.Nonnull;
import java.util.Locale;

public class BlockCrystal extends BlockMetaVariants<BlockCrystal.Variants> implements IQuarkBlock {

	public BlockCrystal() {
		super("crystal", Material.GLASS, Variants.class);
		setHardness(0.3F);
		setSoundType(SoundType.GLASS);
		setLightLevel(1.0F * 11F / 15F);
		setCreativeTab(CreativeTabs.DECORATIONS);
	}
	
	@Override
	@SuppressWarnings("deprecation")
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }
    
    @Nonnull
	@Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings("deprecation")
    public boolean shouldSideBeRendered(IBlockState blockState, @Nonnull IBlockAccess blockAccess, @Nonnull BlockPos pos, EnumFacing side) {
        IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
        Block block = iblockstate.getBlock();

        return block != this && super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }

	public enum Variants implements IStringSerializable {
		CRYSTAL_WHITE,
		CRYSTAL_RED,
		CRYSTAL_ORANGE,
		CRYSTAL_YELLOW,
		CRYSTAL_GREEN,
		CRYSTAL_BLUE,
		CRYSTAL_INDIGO,
		CRYSTAL_VIOLET;

		@Override
		public String getName() {
			return name().toLowerCase(Locale.ROOT);
		}
	}

}
