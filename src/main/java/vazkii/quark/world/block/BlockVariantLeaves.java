package vazkii.quark.world.block;

import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import akka.dispatch.sysmsg.Suspend;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.BlockPressurePlate.Sensitivity;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.interf.IBlockColorProvider;
import vazkii.quark.base.block.IQuarkBlock;

public class BlockVariantLeaves extends BlockLeaves implements IQuarkBlock, IBlockColorProvider {

    public static final PropertyEnum<Variant> VARIANT = PropertyEnum.create("variant", Variant.class);
	
	private final String[] variants;
	private final String bareName;
	
	public BlockVariantLeaves() {
		variants = new String[] {
				"swamp_leaves", "sakura_leaves"
		};
		bareName = "variant_leaves";

		setTranslationKey(bareName);
        setDefaultState(blockState.getBaseState().withProperty(VARIANT, Variant.SWAMP_LEAVES).withProperty(CHECK_DECAY, true).withProperty(DECAYABLE, true));
	}
	
	@Override
    public int damageDropped(IBlockState state) {
        return state.getValue(VARIANT).ordinal();
    }

	@Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(this, 1, state.getBlock().getMetaFromState(state) & 2);
    }
    
	@Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        items.add(new ItemStack(this, 1, 0));
        items.add(new ItemStack(this, 1, 1));
    }
	
	@Override
    protected ItemStack getSilkTouchDrop(IBlockState state) {
        return new ItemStack(Item.getItemFromBlock(this), 1, state.getValue(VARIANT).ordinal());
    }
	
	@Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(VARIANT, Variant.values()[meta & 2]).withProperty(DECAYABLE, Boolean.valueOf((meta & 4) == 0)).withProperty(CHECK_DECAY, Boolean.valueOf((meta & 8) > 0));
    }

	@Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i = i |state.getValue(VARIANT).ordinal();

        if(!state.getValue(DECAYABLE))
            i |= 4;

        if(state.getValue(CHECK_DECAY))
            i |= 8;

        return i;
    }
	
	@Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { VARIANT, CHECK_DECAY, DECAYABLE });
    }

	@Override
	public EnumType getWoodType(int meta) {
		return EnumType.OAK;
	}
	
	@Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) { 
        if(!worldIn.isRemote && stack.getItem() == Items.SHEARS) {
            player.addStat(StatList.getBlockStats(this));
            spawnAsEntity(worldIn, pos, getSilkTouchDrop(state));
        }
        else super.harvestBlock(worldIn, player, pos, state, te, stack);
    }

	@Override
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
        return NonNullList.withSize(1, new ItemStack(this, 1, world.getBlockState(pos).getValue(VARIANT).ordinal()));
	}
	
	@Nonnull
	@Override
	public Block setTranslationKey(@Nonnull String name) {
		super.setTranslationKey(name);
		register(name);
		return this;
	}

	@Override
	public String getBareName() {
		return bareName;
	}

	@Override
	public String[] getVariants() {
		return variants;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ItemMeshDefinition getCustomMeshDefinition() {
		return null;
	}

	@Override
	public EnumRarity getBlockRarity(ItemStack stack) {
		return EnumRarity.COMMON;
	}

	@Override
	public IProperty[] getIgnoredProperties() {
		return new IProperty[] { CHECK_DECAY, DECAYABLE };
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
	public IItemColor getItemColor() {
		return (stack, i) -> stack.getMetadata() == 0 ? ColorizerFoliage.getFoliageColorBasic() : 0xFFFFFF;
	}

	@Override
	public IBlockColor getBlockColor() {
		return (state, world, pos, i) -> {
			System.out.println(i);
			if(state.getValue(VARIANT) == Variant.SWAMP_LEAVES) {
				int color = 0xFF0000;//world != null && pos != null ? BiomeColorHelper.getFoliageColorAtPos(world, pos) : ColorizerFoliage.getFoliageColorBasic();
				System.out.println(color);
				return color;
			}
			return 0xFFFFFF;
		};
	}
	
	private static enum Variant implements IStringSerializable {
		
		SWAMP_LEAVES,
		SAKURA_LEAVES;
		
		@Override
		public String getName() {
			return name().toLowerCase(Locale.ROOT);
		}
		
	}

}
