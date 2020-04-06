package vazkii.quark.oddities.block;

import java.util.function.BooleanSupplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.EnchantingTableBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import vazkii.quark.base.block.IQuarkBlock;
import vazkii.quark.base.module.Module;
import vazkii.quark.oddities.tile.MatrixEnchantingTableTileEntity;

public class MatrixEnchantingTableBlock extends EnchantingTableBlock implements IQuarkBlock {

	private final Module module;
	private BooleanSupplier enabledSupplier = () -> true;
	
	public MatrixEnchantingTableBlock(Module module) {
		super(Block.Properties.from(Blocks.ENCHANTING_TABLE));
		this.module = module;
	}
	
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new MatrixEnchantingTableTileEntity();
	}
	
	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult raytrace) {
		if(!(worldIn.getTileEntity(pos) instanceof MatrixEnchantingTableTileEntity))
			worldIn.setTileEntity(pos, createTileEntity(state, worldIn));
		
		if(player instanceof ServerPlayerEntity)
			NetworkHooks.openGui((ServerPlayerEntity) player, (MatrixEnchantingTableTileEntity) worldIn.getTileEntity(pos), pos);
		
		return ActionResultType.SUCCESS;
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		
		if(stack.hasDisplayName()) {
			TileEntity tileentity = worldIn.getTileEntity(pos);

			if(tileentity instanceof MatrixEnchantingTableTileEntity)
				((MatrixEnchantingTableTileEntity) tileentity).setCustomName(stack.getDisplayName());
		}
	}

	@Override
	public IQuarkBlock setCondition(BooleanSupplier enabledSupplier) {
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

}
