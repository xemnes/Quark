package vazkii.quark.world.gen.structure.processor;

import java.util.Random;

import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;

import net.minecraft.block.SpawnerBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.Template.BlockInfo;
import net.minecraft.world.spawner.AbstractSpawner;
import vazkii.quark.base.Quark;

public class BigDungeonSpawnerProcessor extends StructureProcessor {

    private static final IStructureProcessorType TYPE = Registry.register(Registry.STRUCTURE_PROCESSOR, Quark.MOD_ID + ":big_dungeon_spawner", BigDungeonSpawnerProcessor::new);
	
    public BigDungeonSpawnerProcessor() { 
    	// NO-OP
    }
    
    public BigDungeonSpawnerProcessor(Dynamic<?> dyn) {
    	this();
    }
    
    @Override
    public BlockInfo process(IWorldReader worldReaderIn, BlockPos pos, BlockInfo p_215194_3_, BlockInfo blockInfo, PlacementSettings placementSettingsIn, Template template) {
    	if(blockInfo.state.getBlock() instanceof SpawnerBlock) {
    		Random rand = placementSettingsIn.getRandom(blockInfo.pos);
    		TileEntity tile = TileEntity.create(blockInfo.nbt);
    		
    		if(tile instanceof MobSpawnerTileEntity) {
    			MobSpawnerTileEntity spawner = (MobSpawnerTileEntity) tile;
    			AbstractSpawner logic = spawner.getSpawnerBaseLogic();
    			
    			double val = rand.nextDouble();
    			if(val > 0.95)
    				logic.setEntityType(EntityType.CREEPER);
    			else if(val > 0.5)
    				logic.setEntityType(EntityType.SKELETON);
    			else logic.setEntityType(EntityType.ZOMBIE);
    			
    			CompoundNBT nbt = new CompoundNBT();
    			spawner.write(nbt);
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
