package vazkii.quark.base.world.generator;

import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.GenerationStage;

import java.util.List;

/**
 * @author WireSegal
 * Created at 9:02 PM on 10/1/19.
 */
public class CombinedGenerator implements IGenerator {

    private List<? extends IGenerator> children;

    public CombinedGenerator(List<? extends IGenerator> children) {
        this.children = children;
    }

    @Override
    public int generate(int seedIncrement, long seed, GenerationStage.Decoration stage, IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, SharedSeedRandom rand, BlockPos pos) {
        for (IGenerator child : children) {
            if (child.canGenerate(worldIn))
                seedIncrement = child.generate(seedIncrement, seed, stage, worldIn, generator, rand, pos);
        }
        return seedIncrement;
    }

    @Override
    public boolean canGenerate(IWorld world) {
        return children.stream().anyMatch((it) -> it.canGenerate(world));
    }
}
