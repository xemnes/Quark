package vazkii.quark.oddities.block;

import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import vazkii.arl.block.BlockMetaVariants.EnumBase;
import vazkii.arl.block.BlockModContainer;
import vazkii.quark.base.block.IQuarkBlock;
import vazkii.quark.oddities.tile.TilePipe;
import vazkii.quark.oddities.tile.TilePipe.PipeItem;

public class BlockPipe extends BlockModContainer implements IQuarkBlock {
	
    public static final PropertyEnum<ConnectionType> DOWN = PropertyEnum.create("down", ConnectionType.class);
    public static final PropertyEnum<ConnectionType> UP = PropertyEnum.create("up", ConnectionType.class);
    public static final PropertyEnum<ConnectionType> NORTH = PropertyEnum.create("north", ConnectionType.class);
    public static final PropertyEnum<ConnectionType> SOUTH = PropertyEnum.create("south", ConnectionType.class);
    public static final PropertyEnum<ConnectionType> WEST = PropertyEnum.create("west", ConnectionType.class);
    public static final PropertyEnum<ConnectionType> EAST = PropertyEnum.create("east", ConnectionType.class);
    public static final PropertyBool ENABLED = PropertyBool.create("enabled");
    
    private static final PropertyEnum<ConnectionType>[] CONNECTIONS = new PropertyEnum[] {
    		DOWN, UP, NORTH, SOUTH, WEST, EAST
    };
    
	public BlockPipe() {
		super("pipe", Material.GLASS);
		setHardness(3.0F);
		setResistance(10.0F);
		setSoundType(SoundType.GLASS);
		setCreativeTab(CreativeTabs.REDSTONE);

		setHarvestLevel("pickaxe", 1);
		
        setDefaultState(getDefaultState()
        		.withProperty(DOWN, ConnectionType.NONE).withProperty(UP, ConnectionType.NONE)
        		.withProperty(NORTH, ConnectionType.NONE).withProperty(SOUTH, ConnectionType.NONE)
        		.withProperty(WEST, ConnectionType.NONE).withProperty(EAST, ConnectionType.NONE)
        		.withProperty(ENABLED, true));
	}
	
	@Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        boolean flag = !worldIn.isBlockPowered(pos);

        if(flag != state.getValue(ENABLED))
            worldIn.setBlockState(pos, state.withProperty(ENABLED, Boolean.valueOf(flag)), 2 | 4);
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }
	
	@Override
	public IProperty[] getIgnoredProperties() {
		return new IProperty[] { ENABLED };
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { UP, DOWN, NORTH, SOUTH, WEST, EAST, ENABLED });
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return (state.getValue(ENABLED) ? 0b0 : 1);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(ENABLED, (meta & 0b1) != 1);
	}
	
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		IBlockState actualState = state;
		for(EnumFacing facing : EnumFacing.VALUES) {
			PropertyEnum<ConnectionType> prop = CONNECTIONS[facing.ordinal()];
			ConnectionType type = getConnectionTo(worldIn, pos, facing);
			actualState = actualState.withProperty(prop, type);
		}
		
		return actualState;
	}
	
	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}
	
	@Override
	public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile instanceof TilePipe)
			return ((TilePipe) tile).getComparatorOutput();
		return 0;
	}
	
	@Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if(tileentity instanceof TilePipe)
        	((TilePipe) tileentity).dropAllItems();;

        super.breakBlock(worldIn, pos, state);
    }

	@Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

	@Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
        Block block = iblockstate.getBlock();

        return block == this ? false : super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TilePipe();
	}
	
	private ConnectionType getConnectionTo(IBlockAccess world, BlockPos pos, EnumFacing face) {
		pos = pos.offset(face);
		TileEntity tile = world.getTileEntity(pos);
		if(tile != null) {
			if(tile instanceof TilePipe)
				return ConnectionType.PIPE;
			else if(tile instanceof IInventory || (tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, face.getOpposite()) 
					&& tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, face.getOpposite()) != null))
				return ConnectionType.TERMINAL;
		}
		
		return ConnectionType.NONE;
	}
	
	public static enum ConnectionType implements EnumBase {
		
		NONE, PIPE, TERMINAL
		
	}

}
