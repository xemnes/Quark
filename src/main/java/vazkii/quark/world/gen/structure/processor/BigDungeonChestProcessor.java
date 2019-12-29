package vazkii.quark.world.gen.structure.processor;

import java.util.Random;

import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;

import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.Template.BlockInfo;
import vazkii.quark.base.Quark;
import vazkii.quark.world.module.BigDungeonModule;

public class BigDungeonChestProcessor extends StructureProcessor {

    private static final IStructureProcessorType TYPE = Registry.register(Registry.STRUCTURE_PROCESSOR, Quark.MOD_ID + ":big_dungeon_chest", BigDungeonChestProcessor::new);
	
    public BigDungeonChestProcessor() { 
    	// NO-OP
    }
    
    public BigDungeonChestProcessor(Dynamic<?> dyn) {
    	this();
    }
    
    @Override
    public BlockInfo process(IWorldReader worldReaderIn, BlockPos pos, BlockInfo p_215194_3_, BlockInfo blockInfo, PlacementSettings placementSettingsIn, Template template) {
    	if(blockInfo.state.getBlock() instanceof ChestBlock) {
    		Random rand = placementSettingsIn.getRandom(blockInfo.pos);
    		if(rand.nextDouble() > BigDungeonModule.chestChance)
	            return new BlockInfo(blockInfo.pos, Blocks.CAVE_AIR.getDefaultState(), new CompoundNBT());
    		
    		TileEntity tile = TileEntity.create(blockInfo.nbt);
    		if(tile instanceof ChestTileEntity) {
    			ChestTileEntity chest = (ChestTileEntity) tile;
    			chest.setLootTable(null, 0);
    			for(int i = 0; i < chest.getSizeInventory(); i++)
    				chest.setInventorySlotContents(i, ItemStack.EMPTY);
    			
    			chest.setLootTable(new ResourceLocation(BigDungeonModule.lootTable), rand.nextLong());
    			CompoundNBT nbt = new CompoundNBT();
    			chest.write(nbt);
    			return new BlockInfo(blockInfo.pos, blockInfo.state, nbt);
    		}
    	}
    	
    	return blockInfo;
    }
    
	@Override
	protected IStructureProcessorType getType() {
		return TYPE;
	}

	@Override
	protected <T> Dynamic<T> serialize0(DynamicOps<T> ops) {
		return new Dynamic<>(ops);
	}

}
