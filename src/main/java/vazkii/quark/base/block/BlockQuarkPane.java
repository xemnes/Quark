/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 *
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 *
 * File Created @ [24/03/2016, 02:17:54 (GMT)]
 */
package vazkii.quark.base.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPane;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.block.BlockMod;

import javax.annotation.Nonnull;
import java.util.List;

public class BlockQuarkPane extends BlockMod implements IQuarkBlock {

	public static final PropertyBool NORTH = PropertyBool.create("north");
	public static final PropertyBool EAST = PropertyBool.create("east");
	public static final PropertyBool SOUTH = PropertyBool.create("south");
	public static final PropertyBool WEST = PropertyBool.create("west");

	protected static final AxisAlignedBB[] BOUNDING_BOXES = new AxisAlignedBB[] {new AxisAlignedBB(0.4375D, 0.0D, 0.4375D, 0.5625D, 1.0D, 0.5625D), new AxisAlignedBB(0.4375D, 0.0D, 0.4375D, 0.5625D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.4375D, 0.5625D, 1.0D, 0.5625D), new AxisAlignedBB(0.0D, 0.0D, 0.4375D, 0.5625D, 1.0D, 1.0D), new AxisAlignedBB(0.4375D, 0.0D, 0.0D, 0.5625D, 1.0D, 0.5625D), new AxisAlignedBB(0.4375D, 0.0D, 0.0D, 0.5625D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5625D, 1.0D, 0.5625D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5625D, 1.0D, 1.0D), new AxisAlignedBB(0.4375D, 0.0D, 0.4375D, 1.0D, 1.0D, 0.5625D), new AxisAlignedBB(0.4375D, 0.0D, 0.4375D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.4375D, 1.0D, 1.0D, 0.5625D), new AxisAlignedBB(0.0D, 0.0D, 0.4375D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.4375D, 0.0D, 0.0D, 1.0D, 1.0D, 0.5625D), new AxisAlignedBB(0.4375D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.5625D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)};

	public BlockQuarkPane(String name, Material materialIn) {
		super(name, materialIn);
		setDefaultState(blockState.getBaseState().withProperty(NORTH, Boolean.FALSE).withProperty(EAST, Boolean.FALSE).withProperty(SOUTH, Boolean.FALSE).withProperty(WEST, Boolean.FALSE));
	}

	// COPYPASTA

	@SuppressWarnings("deprecation")
	@Override
	public void addCollisionBoxToList(IBlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull AxisAlignedBB entityBox, @Nonnull List<AxisAlignedBB> collidingBoxes, Entity entity, boolean isActualState) {
		if (!isActualState)
			state = state.getActualState(worldIn, pos);

		addCollisionBoxToList(pos, entityBox, collidingBoxes, BOUNDING_BOXES[0]);

		if(state.getValue(NORTH))
			addCollisionBoxToList(pos, entityBox, collidingBoxes, BOUNDING_BOXES[getBoundingBoxIndex(EnumFacing.NORTH)]);

		if(state.getValue(SOUTH))
			addCollisionBoxToList(pos, entityBox, collidingBoxes, BOUNDING_BOXES[getBoundingBoxIndex(EnumFacing.SOUTH)]);

		if(state.getValue(EAST))
			addCollisionBoxToList(pos, entityBox, collidingBoxes, BOUNDING_BOXES[getBoundingBoxIndex(EnumFacing.EAST)]);

		if(state.getValue(WEST))
			addCollisionBoxToList(pos, entityBox, collidingBoxes, BOUNDING_BOXES[getBoundingBoxIndex(EnumFacing.WEST)]);
	}


	private static int getBoundingBoxIndex(EnumFacing facing) {
		return 1 << facing.getHorizontalIndex();
	}

	@Nonnull
	@Override
	@SuppressWarnings("deprecation")
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		state = getActualState(state, source, pos);
		return BOUNDING_BOXES[getBoundingBoxIndex(state)];
	}

	private static int getBoundingBoxIndex(IBlockState state) {
		int i = 0;

		if(state.getValue(NORTH))
			i |= getBoundingBoxIndex(EnumFacing.NORTH);

		if(state.getValue(EAST))
			i |= getBoundingBoxIndex(EnumFacing.EAST);

		if(state.getValue(SOUTH))
			i |= getBoundingBoxIndex(EnumFacing.SOUTH);

		if(state.getValue(WEST))
			i |= getBoundingBoxIndex(EnumFacing.WEST);

		return i;
	}

	@Nonnull
	@Override
	@SuppressWarnings("deprecation")
	public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return state.withProperty(NORTH, canPaneConnectTo(worldIn, pos, EnumFacing.NORTH))
				.withProperty(SOUTH, canPaneConnectTo(worldIn, pos, EnumFacing.SOUTH))
				.withProperty(WEST, canPaneConnectTo(worldIn, pos, EnumFacing.WEST))
				.withProperty(EAST, canPaneConnectTo(worldIn, pos, EnumFacing.EAST));
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	public final boolean canPaneConnectToBlock(Block blockIn) {
		return blockIn.getDefaultState().isFullCube() || blockIn == this || blockIn == Blocks.GLASS || blockIn == Blocks.STAINED_GLASS || blockIn == Blocks.STAINED_GLASS_PANE || blockIn instanceof BlockPane;
	}

	@SideOnly(Side.CLIENT)
	@Override
	@SuppressWarnings("deprecation")
	public boolean shouldSideBeRendered(IBlockState blockState, @Nonnull IBlockAccess blockAccess, @Nonnull BlockPos pos, EnumFacing side) {
		return blockAccess.getBlockState(pos.offset(side)).getBlock() != this && super.shouldSideBeRendered(blockState, blockAccess, pos, side);
	}

	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, @Nonnull IBlockState state, EntityPlayer player) {
		return true;
	}

	@Nonnull
	@SideOnly(Side.CLIENT)
	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}

	@Nonnull
	@Override
	@SuppressWarnings("deprecation")
	public IBlockState withRotation(@Nonnull IBlockState state, Rotation rot) {
		switch (rot) {
		case CLOCKWISE_180:
			return state.withProperty(NORTH, state.getValue(SOUTH)).withProperty(EAST, state.getValue(WEST)).withProperty(SOUTH, state.getValue(NORTH)).withProperty(WEST, state.getValue(EAST));
		case COUNTERCLOCKWISE_90:
			return state.withProperty(NORTH, state.getValue(EAST)).withProperty(EAST, state.getValue(SOUTH)).withProperty(SOUTH, state.getValue(WEST)).withProperty(WEST, state.getValue(NORTH));
		case CLOCKWISE_90:
			return state.withProperty(NORTH, state.getValue(WEST)).withProperty(EAST, state.getValue(NORTH)).withProperty(SOUTH, state.getValue(EAST)).withProperty(WEST, state.getValue(SOUTH));
		default:
			return state;
		}
	}

	@Nonnull
	@Override
	@SuppressWarnings("deprecation")
	public IBlockState withMirror(@Nonnull IBlockState state, Mirror mirrorIn) {
		switch (mirrorIn) {
		case LEFT_RIGHT:
			return state.withProperty(NORTH, state.getValue(SOUTH)).withProperty(SOUTH, state.getValue(NORTH));
		case FRONT_BACK:
			return state.withProperty(EAST, state.getValue(WEST)).withProperty(WEST, state.getValue(EAST));
		default:
			return super.withMirror(state, mirrorIn);
		}
	}

	@Nonnull
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, NORTH, EAST, WEST, SOUTH);
	}

	@Override
	public IProperty[] getIgnoredProperties() {
		return new IProperty[] {NORTH, EAST, WEST, SOUTH};
	}

	@Nonnull
	@Override
	@SuppressWarnings("deprecation")
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos blockPos, EnumFacing face) {
		return face != EnumFacing.UP && face != EnumFacing.DOWN ? BlockFaceShape.MIDDLE_POLE_THIN : BlockFaceShape.CENTER_SMALL;
	}
	
	public boolean canPaneConnectTo(IBlockAccess world, BlockPos pos, EnumFacing dir) {
		BlockPos off = pos.offset(dir);
		IBlockState state = world.getBlockState(off);
		return canPaneConnectToBlock(state.getBlock()) || state.isSideSolid(world, off, dir.getOpposite());
	}
	
	

}
