/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 *
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 *
 * File Created @ [18/04/2016, 18:28:17 (GMT)]
 */
package vazkii.quark.world.world;

import java.util.Random;

import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;
import vazkii.quark.world.feature.Biotite;

public class BiotiteGenerator implements IWorldGenerator {

	int clusterCount;
	public static WorldGenMinable generator;

	public BiotiteGenerator(int clusterSize, int clusterCount) {
		this.clusterCount = clusterCount;

		generator = new WorldGenMinable(Biotite.biotite_ore.getDefaultState(), clusterSize, BlockMatcher.forBlock(Blocks.END_STONE));
	}

	@Override
	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		boolean isEnd = world.provider instanceof WorldProviderEnd;

		if(!isEnd || !Biotite.generateNatually)
			return;

		for(int i = 0; i < clusterCount; i++) {
			int x = chunkX * 16 + rand.nextInt(16);
			int y = rand.nextInt(64);
			int z = chunkZ * 16 + rand.nextInt(16);

			generator.generate(world, rand, new BlockPos(x, y, z));
		}
	}

}
