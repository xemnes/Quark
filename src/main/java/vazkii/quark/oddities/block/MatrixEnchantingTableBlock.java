package vazkii.quark.oddities.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.EnchantingTableBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.EnchantmentContainer;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import vazkii.quark.base.module.ModuleLoader;
import vazkii.quark.oddities.module.MatrixEnchantingModule;
import vazkii.quark.oddities.tile.MatrixEnchantingTableTileEntity;

public class MatrixEnchantingTableBlock extends EnchantingTableBlock {

	public MatrixEnchantingTableBlock() {
		super(Block.Properties.from(Blocks.ENCHANTING_TABLE));
	}
	
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new MatrixEnchantingTableTileEntity();
	}
	
	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult raytrace) {
		if(!(worldIn.getTileEntity(pos) instanceof MatrixEnchantingTableTileEntity))
			worldIn.setTileEntity(pos, createTileEntity(state, worldIn));
		
		if(ModuleLoader.INSTANCE.isModuleEnabled(MatrixEnchantingModule.class)) {
			if(player instanceof ServerPlayerEntity)
				NetworkHooks.openGui((ServerPlayerEntity) player, (MatrixEnchantingTableTileEntity) worldIn.getTileEntity(pos), pos);
		} else {
			if(!worldIn.isRemote) {
				INamedContainerProvider provider = new SimpleNamedContainerProvider((p_220147_2_, p_220147_3_, p_220147_4_) -> {
		            return new EnchantmentContainer(p_220147_2_, p_220147_3_, IWorldPosCallable.of(worldIn, pos));
		         }, ((MatrixEnchantingTableTileEntity) worldIn.getTileEntity(pos)).getDisplayName());
				player.openContainer(provider);
			}
		}
		
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
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		TileEntity tileentity = worldIn.getTileEntity(pos);

		if(tileentity instanceof MatrixEnchantingTableTileEntity) {
			MatrixEnchantingTableTileEntity enchanter = (MatrixEnchantingTableTileEntity) tileentity;
			enchanter.dropItem(0);
			enchanter.dropItem(1);
		}
		
		super.onReplaced(state, worldIn, pos, newState, isMoving);
	}

}
