package vazkii.quark.world.world.underground;

import net.minecraft.block.BlockPrismarine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.quark.base.module.ModuleLoader;
import vazkii.quark.world.feature.UndergroundBiomes;

public class UndergroundBiomePrismarine extends BasicUndergroundBiome {

	int seaLanternChance, waterChance;
	IBlockState lanternState; 
	boolean spawnElderPrismarine;
	
	public UndergroundBiomePrismarine() {
		super(Blocks.PRISMARINE.getDefaultState(), Blocks.PRISMARINE.getDefaultState(), Blocks.PRISMARINE.getDefaultState());
		lanternState = Blocks.SEA_LANTERN.getDefaultState();
	}
	
	public void update() {
		boolean elder = UndergroundBiomes.elderPrismarineEnabled && spawnElderPrismarine;
		
		IBlockState prismarineState = (elder ? UndergroundBiomes.elder_prismarine : Blocks.PRISMARINE).getDefaultState();
		ceilingState = floorState = wallState = prismarineState;
		
		lanternState = (elder ? UndergroundBiomes.elder_sea_lantern : Blocks.SEA_LANTERN).getDefaultState();
	}
	
	@Override
	public void fillWall(World world, BlockPos pos, IBlockState state) {
		super.fillWall(world, pos, state);
		
		if(seaLanternChance > 0 && world.rand.nextInt(seaLanternChance) == 0)
			world.setBlockState(pos, lanternState, 2);
	}
	
	@Override
	public void fillFloor(World world, BlockPos pos, IBlockState state) {
		if(waterChance > 0 && !isBorder(world, pos, state) && world.rand.nextInt(waterChance) == 0)
			world.setBlockState(pos, Blocks.WATER.getDefaultState());
		else super.fillFloor(world, pos, state);
	}
	
	@Override
	public void setupConfig(String category) {
		seaLanternChance = ModuleLoader.config.getInt("Sea Lantern Chance", category, 120, 0, Integer.MAX_VALUE, "The higher, the less sea lanterns will spawn");
		waterChance = ModuleLoader.config.getInt("Water Chance", category, 4, 0, Integer.MAX_VALUE, "The higher, the less water will spawn");
		spawnElderPrismarine = ModuleLoader.config.getBoolean("Spawn Elder Prismarine", category, true, "Set to false to spawn regular prismarine instead of elder prismarine (even if the block is enabled)");
	}
	
	
}
