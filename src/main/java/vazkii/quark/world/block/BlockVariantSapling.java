package vazkii.quark.world.block;

import java.util.Locale;
import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.IGrowable;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.event.terraingen.TerrainGen;
import vazkii.quark.base.block.BlockQuarkBush;
import vazkii.quark.world.world.tree.WorldGenSakuraTree;
import vazkii.quark.world.world.tree.WorldGenSwampTree;

public class BlockVariantSapling extends BlockQuarkBush implements IGrowable {

	public static final PropertyEnum<Variant> VARIANT = PropertyEnum.create("variant", Variant.class);
	public static final PropertyInteger STAGE = BlockSapling.STAGE;

	protected static final AxisAlignedBB SAPLING_AABB = new AxisAlignedBB(0.1, 0, 0.1, 0.9, 0.8, 0.9);

	private static final String[] VARIANTS = new String[] { "swamp_sapling", "sakura_sapling" };

	public BlockVariantSapling() {
		super("variant_sapling", VARIANTS);

		setDefaultState(blockState.getBaseState().withProperty(VARIANT, Variant.SWAMP_SAPLING).withProperty(STAGE, 0));
		setCreativeTab(CreativeTabs.DECORATIONS);
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if (!worldIn.isRemote) {
			super.updateTick(worldIn, pos, state, rand);

			if (!worldIn.isAreaLoaded(pos, 1))
				return; // Forge: prevent loading unloaded chunks when checking neighbor's light

			if (worldIn.getLightFromNeighbors(pos.up()) >= 9 && rand.nextInt(7) == 0)
				grow(worldIn, pos, state, rand);
		}
	}

	public void grow(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if (state.getValue(STAGE) == 0)
			worldIn.setBlockState(pos, state.cycleProperty(STAGE), 4);
		else
			generateTree(worldIn, pos, state, rand);
	}

	public void generateTree(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if (!TerrainGen.saplingGrowTree(worldIn, rand, pos))
			return;

		WorldGenAbstractTree generator = state.getValue(VARIANT) == Variant.SWAMP_SAPLING ? new WorldGenSwampTree(false)
				: new WorldGenSakuraTree(true);

		worldIn.setBlockToAir(pos);
		if (!generator.generate(worldIn, rand, pos))
			worldIn.setBlockState(pos, state, 4);

	}

	@Override
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
		return true;
	}

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
		return worldIn.rand.nextFloat() < 0.45;
	}

	@Override
	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
		grow(worldIn, pos, state, rand);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return SAPLING_AABB;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(VARIANT, Variant.values()[meta & 1]).withProperty(STAGE, (meta & 8) >> 3);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int i = state.getValue(VARIANT).ordinal() & 1;
		i = i | (state.getValue(STAGE) << 3);

		return i;
	}

	@Override
	public int damageDropped(IBlockState state) {
		return state.getValue(VARIANT).ordinal();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { VARIANT, STAGE });
	}

	@Override
	public IProperty getVariantProp() {
		return VARIANT;
	}

	@Override
	public Class getVariantEnum() {
		return Variant.class;
	}

	@Override
	public IProperty[] getIgnoredProperties() {
		return new IProperty[] { STAGE };
	}

	private static enum Variant implements IStringSerializable {

		SWAMP_SAPLING, SAKURA_SAPLING;

		@Override
		public String getName() {
			return name().toLowerCase(Locale.ROOT);
		}

	}

}
