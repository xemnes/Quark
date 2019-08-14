package vazkii.quark.world.gen.underground;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

public class SandstoneUndergroundBiome extends BasicUndergroundBiome {

	public SandstoneUndergroundBiome() {
		super(Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE.getDefaultState());
	}
	
	@Override
	public void fillCeiling(IWorld world, BlockPos pos, BlockState state, Random rand) {
		if(rand.nextDouble() < 0.1)
			world.setBlockState(pos.down(), ceilingState, 2);
		
		super.fillCeiling(world, pos, state, rand);
	}
	
	@Override
	public void fillFloor(IWorld world, BlockPos pos, BlockState state, Random rand) {
		if(rand.nextBoolean()) {
			world.setBlockState(pos, Blocks.SAND.getDefaultState(), 2);
			if(rand.nextDouble() < 0.05)
				world.setBlockState(pos.up(), Blocks.DEAD_BUSH.getDefaultState(), 2);
		} else super.fillFloor(world, pos, state, rand);
	}
	
	@Override
	public void fillWall(IWorld world, BlockPos pos, BlockState state, Random rand) {
		if(rand.nextDouble() < 0.1)
			world.setBlockState(pos, Blocks.CHISELED_SANDSTONE.getDefaultState(), 2);
		else super.fillWall(world, pos, state, rand);
	}
	
//	@Override
//	public boolean hasDungeon() {
//		return true;
//	}
//	
//	@Override
//	public void spawnDungeon(ServerWorld world, BlockPos pos, Direction side) {
//		if(side == null)
//			side = Direction.NORTH;
//		
//		switch(side) {
//		case NORTH:
//			pos = pos.add(3, -7, 6);
//			break;
//		case SOUTH:
//			pos = pos.add(-3, -7, -6);
//			break;
//		case EAST:
//			pos = pos.add(-6, -7, 3);
//			break;
//		case WEST:
//			pos = pos.add(6, -7, -3);
//			break;
//		default: break; 
//		}
//		
//		MinecraftServer server = world.getMinecraftServer();
//		Template template = world.getStructureTemplateManager().getTemplate(server, HUSK_GRAVE_STRUCTURE);
//		PlacementSettings settings = new PlacementSettings();
//		settings.setRotation(rotationFromFacing(side.getOpposite()));
//		
//		template.addBlocksToWorld(world, pos, settings);
//
//		Map<BlockPos, String> dataBlocks = template.getDataBlocks(pos, settings);
//		for(Entry<BlockPos, String> entry : dataBlocks.entrySet()) {
//			BlockPos dataPos = entry.getKey();
//			switch(entry.getValue()) {
//			case "spawner":
//				world.setBlockState(dataPos, Blocks.MOB_SPAWNER.getDefaultState(), 2);
//				TileEntity spawner = world.getTileEntity(dataPos);
//
//				if(spawner instanceof MobSpawnerTileEntity)
//					((MobSpawnerTileEntity) spawner).getSpawnerBaseLogic().setEntityId(EntityList.getKey(ZombieEntity.class));
//				break;
//			case "chest":
//				BlockState chestState = Blocks.CHEST.getDefaultState().withProperty(ChestBlock.FACING, Direction.WEST);
//				world.setBlockState(dataPos, chestState);
//
//				TileEntity chest = world.getTileEntity(dataPos);
//				if(chest instanceof LockableLootTileEntity)
//					((LockableLootTileEntity) chest).setLootTable(LootTables.CHESTS_DESERT_PYRAMID, world.rand.nextLong());
//				break;
//			}
//		}
//	}

}
