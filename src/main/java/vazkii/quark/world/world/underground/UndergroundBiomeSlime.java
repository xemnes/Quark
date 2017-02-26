package vazkii.quark.world.world.underground;

import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockHardenedClay;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.quark.base.module.ModuleLoader;

public class UndergroundBiomeSlime extends BasicUndergroundBiome {

	int slimeBlockChance;
	
	public UndergroundBiomeSlime() {
		super(Blocks.WATER.getDefaultState(), null, null);
	}
	
	@Override
	public void fillCeiling(World world, BlockPos pos, IBlockState state) {
		IBlockState setState = Blocks.STAINED_HARDENED_CLAY.getDefaultState();
		switch(world.rand.nextInt(7)) {
		case 0: case 1: case 2:
			setState = setState.withProperty(BlockColored.COLOR, EnumDyeColor.GREEN);
			break;
		case 3: case 4: case 5:
			setState = setState.withProperty(BlockColored.COLOR, EnumDyeColor.LIME);
			break;
		case 6:
			setState = setState.withProperty(BlockColored.COLOR, EnumDyeColor.LIGHT_BLUE);
		}
		
		world.setBlockState(pos, setState, 2);
	}
	
	@Override
	public void fillWall(World world, BlockPos pos, IBlockState state) {
		fillCeiling(world, pos, state);
	}

	@Override
	public void fillFloor(World world, BlockPos pos, IBlockState state) {
		world.setBlockState(pos, floorState, 3);
		
		if(world.rand.nextInt(slimeBlockChance) == 0)
			world.setBlockState(pos, Blocks.SLIME_BLOCK.getDefaultState());
	}
	
	@Override
	public void setupConfig(String category) {
		slimeBlockChance = ModuleLoader.config.getInt("Slime Block Chance", category, 12, 0, Integer.MAX_VALUE, "The higher, the less slime blocks will spawn");
	}
	
	
}
